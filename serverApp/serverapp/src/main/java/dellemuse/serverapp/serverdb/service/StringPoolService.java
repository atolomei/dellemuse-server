package dellemuse.serverapp.serverdb.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.serverdb.model.PooledString;

/**
 * Service that pools strings in the database using their hashCode as id.
 * <p>
 * Given a string, it computes the hashCode, checks if it already exists in the
 * database, and saves it only if it does not. Returns the id (hashCode) so
 * callers can store just the id instead of the full string.
 * </p>
 * <p>
 * An in-memory {@link ConcurrentHashMap} acts as a write-through cache so that
 * repeated strings never hit the database more than once.
 * </p>
 */
@Service
public class StringPoolService {

	static private Logger logger = Logger.getLogger(StringPoolService.class.getName());

	private final PooledStringDBService pooledStringDBService;

	/** Cache of ids that are known to exist in the database. */
	private final ConcurrentHashMap<Long, Boolean> knownIds = new ConcurrentHashMap<>();

	public StringPoolService(PooledStringDBService pooledStringDBService) {
		this.pooledStringDBService = pooledStringDBService;
	}

	/**
	 * Pools the given string.
	 * <p>
	 * Computes the id as {@code (long) value.hashCode()}, persists the string
	 * if it has not been persisted before, and returns the id.
	 * </p>
	 *
	 * @param value the string to pool (must not be {@code null})
	 * @return the id (hashCode) of the pooled string
	 */
	public long pool(String value) {
		if (value == null)
			throw new IllegalArgumentException("value must not be null");

		long id = (long) value.hashCode();

		if (knownIds.containsKey(id))
			return id;

		try {
			if (!pooledStringDBService.existsById(id)) {
				PooledString ps = new PooledString(id, value);
				pooledStringDBService.save(ps);
				logger.debug("Pooled new string id -> " + id);
			}
			knownIds.put(id, Boolean.TRUE);
		} catch (Exception e) {
			// Another thread/node may have inserted in parallel — that is fine
			logger.debug("PooledString already exists or error for id -> " + id);
			knownIds.put(id, Boolean.TRUE);
		}

		return id;
	}

	protected PooledStringDBService getPooledStringDBService() {
		return this.pooledStringDBService;
	}
}
