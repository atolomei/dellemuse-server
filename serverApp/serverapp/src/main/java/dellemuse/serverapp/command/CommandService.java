package dellemuse.serverapp.command;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.ServiceStatus;
import dellemuse.serverapp.serverdb.service.base.BaseService;

import jakarta.annotation.PostConstruct;

@Service
public class CommandService extends BaseService {

	private static Logger logger = Logger.getLogger(CommandService.class.getName());

	private static Set<String> runningSet = new HashSet<>();

	static private final long TTL = 60 * 60 * 24 * 5; // 5 days (in seconds)
	static private final int MAX_SIZE = 32000;

	private Map<Serializable, Command> commands = new ConcurrentHashMap<Serializable, Command>(16, 0.9f, 1);

	private final OffsetDateTime created = OffsetDateTime.now();

	public OffsetDateTime getOffsetDateTimeCreated() {
		return this.created;
	}

	private String id = this.getClass().getSimpleName();

	private int poolSize = 1;
	private OffsetDateTime lastCleanUp;
	private Dispatcher dispatcher;

	// private List<Command> commandsTerminated = Collections.synchronizedList(new
	// ArrayList<Command>());
	private int maxSize = MAX_SIZE;

	private ReadWriteLock com_lock = new ReentrantReadWriteLock();

	public CommandService(ServerDBSettings settings) {
		super(settings);
	}

	@PostConstruct
	protected void onInit() {

		try {
			this.poolSize = getPoolSize();
			this.dispatcher = new ThreadPoolDispatcher(getId(), 1, this.poolSize);
			setStatus(ServiceStatus.RUNNING);
		} catch (Exception e) {
			setStatus(ServiceStatus.STOPPED);
		}
	}

	public String getId() {
		return id;
	}

	public int getPoolSize() {
		return getSettings().getDispatcherPoolSize();
	}

	public void run(Command command) {

		final String concurrentUniqueKey = command.getConcurrentUniqueKey();

		boolean exists = false;

		if (concurrentUniqueKey != null) {

			synchronized (runningSet) {
				exists = !runningSet.add(concurrentUniqueKey);
			}
			if (!exists) {

				command.addCallback(new AbstractCommandLifecycleCallback() {
					@Override
					public void stop() {
						unregister();
					}

					@Override
					public void end() {
						unregister();
					}

					private void unregister() {
						runningSet.remove(concurrentUniqueKey);
					}
				});
			}
		}

		if (!exists) {

			getDispatcher().execute(new CommandExecutor(command, getDispatcher()));

		} else {
			logger.error("Command with key '" + concurrentUniqueKey + "' is already being executed.");
			throw new ConcurrentModificationException(
					"Command with key '" + concurrentUniqueKey + "' is already being executed.");
		}
	}

	public void register(Command command) {
		this.com_lock.writeLock().lock();
		try {
			getCommands().put(command.getId(), command);
		} finally {
			this.com_lock.writeLock().unlock();
		}
	}

	public Map<Serializable, Command> getCommands() {
		return this.commands;
	}

 
	/**
	 * Client has finished using the Command
	 *
	 * @param commandId
	 */
	public void remove(Serializable id) {
		this.com_lock.writeLock().lock();
		try {
			getCommands().remove(id);
		} finally {
			this.com_lock.writeLock().unlock();
		}
	}

	/**
	 * The Command has finished its execution
	 *
	 * @param commandId
	 */
	public void executed(Command command) {

		/**
		 * try { this.com_lock.writeLock().lock();
		 * this.commands_terminated.add(command);
		 * 
		 * if (getCommands().size() >= this.max_size) { if
		 * (commands_terminated.size()>0) getCommands().remove((Long)
		 * commands_terminated.get(0).getId()); } } finally {
		 * this.com_lock.writeLock().unlock(); }
		 **/
	}

	private Dispatcher getDispatcher() {
		return this.dispatcher;
	}

}
