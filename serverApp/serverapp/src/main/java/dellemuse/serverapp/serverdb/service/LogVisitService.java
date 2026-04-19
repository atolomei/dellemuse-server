package dellemuse.serverapp.serverdb.service;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.stat.Stat;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Asynchronous service for logging page visits.
 * <p>
 * Stats are placed into an unbounded in-memory queue and persisted by a
 * background worker thread. This decouples the HTTP request thread from the
 * database write, accepting the small risk of losing queued events if the JVM
 * crashes.
 * </p>
 * <p>
 * The queue grows dynamically — no events are ever dropped. A warning is logged
 * every {@link #WARN_THRESHOLD} entries so backpressure issues can be detected.
 * </p>
 */
@Service
public class LogVisitService extends BaseService {

	static private Logger logger = Logger.getLogger(LogVisitService.class.getName());

	private static final int WARN_THRESHOLD = 10000;
	private static final long POLL_SLEEP_MS = 1000;

	private final StatDBService statDBService;
	private final StringPoolService stringPoolService;
	private final ConcurrentLinkedQueue<Stat> queue = new ConcurrentLinkedQueue<>();
	private final AtomicInteger size = new AtomicInteger(0);

	private volatile boolean running = false;
	private Thread workerThread;

	public LogVisitService(ServerDBSettings settings, StatDBService statDBService, StringPoolService stringPoolService) {
		super(settings);
		this.statDBService = statDBService;
		this.stringPoolService = stringPoolService;
	}

	/**
	 * Enqueues a {@link Stat} for asynchronous persistence.
	 * The queue is unbounded so events are never dropped.
	 *
	 * @param stat the visit stat to save (must not be {@code null})
	 */
	public void logVisit(Stat stat) {
		if (stat == null)
			return;
		queue.offer(stat);
		int currentSize = size.incrementAndGet();
		if (currentSize > 0 && currentSize % WARN_THRESHOLD == 0) {
			logger.warn("LogVisitService queue size -> " + currentSize);
		}
	}

	@PostConstruct
	protected void start() {
		running = true;
		workerThread = new Thread(this::processQueue, "log-visit-worker");
		workerThread.setDaemon(true);
		workerThread.start();
		logger.info("LogVisitService worker thread started");
	}

	@PreDestroy
	protected void stop() {
		running = false;
		workerThread.interrupt();
		drainQueue();
		logger.info("LogVisitService worker thread stopped");
	}

	private void processQueue() {
		while (running) {
			try {
				Stat stat = queue.poll();
				if (stat != null) {
					size.decrementAndGet();
					saveStat(stat);
				} else {
					Thread.sleep(POLL_SLEEP_MS);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				if (!running)
					break;
			} catch (Exception e) {
				logger.error(e, "Error in LogVisitService worker thread");
			}
		}
		drainQueue();
	}

	private void drainQueue() {
		Stat stat;
		while ((stat = queue.poll()) != null) {
			try {
				saveStat(stat);
			} catch (Exception e) {
				logger.error(e, "Error draining LogVisitService queue");
			}
		}
	}

	private void saveStat(Stat stat) {
		try {
			// Pool the userAgent string in the background thread
			String userAgent = stat.getUserAgent();
			if (userAgent != null && !userAgent.isEmpty()) {
				long pooledId = stringPoolService.pool(userAgent);
				stat.setUserAgent(String.valueOf(pooledId));
			}

			// Enrich stat: GuideContent → ArtExhibitionGuide → Site
			if (stat.getGuideContent() != null) {
				// Get ArtWork from GuideContent
				Optional<ArtWork> a = getGuideContentDBService().getArtWork(stat.getGuideContent());
				if (a.isPresent()) {
					stat.setArtWork(a.get());
				}
				// Get ArtExhibitionGuide from GuideContent
				if (stat.getArtExhibitionGuide() == null) {
					GuideContent managed = getGuideContentDBService().findWithDeps(stat.getGuideContent().getId()).orElse(null);
					if (managed != null && managed.getArtExhibitionGuide() != null) {
						stat.setArtExhibitionGuide(managed.getArtExhibitionGuide());
					}
				}
			}

			// Enrich stat: ArtExhibitionGuide → Site
			if (stat.getArtExhibitionGuide() != null && stat.getSite() == null) {
				Optional<ArtExhibitionGuide> oGuide = getArtExhibitionGuideDBService().findWithDeps(stat.getArtExhibitionGuide().getId());
				if (oGuide.isPresent()) {
					ArtExhibition ex = oGuide.get().getArtExhibition();
					if (ex != null && ex.getSite() != null) {
						stat.setSite(ex.getSite());
					}
				}
			}

			statDBService.save(stat);
			
		} catch (Exception e) {
			logger.error(e, "Failed to save stat -> " + stat.toString());
		}
	}

	protected StatDBService getStatDBService() {
		return this.statDBService;
	}

	protected StringPoolService getStringPoolService() {
		return this.stringPoolService;
	}

	protected ArtExhibitionGuideDBService getArtExhibitionGuideDBService() {
		return (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
	}
}