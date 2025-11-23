package dellemuse.serverapp.service;

 

 
import java.time.OffsetDateTime;
import java.util.ArrayList;
 
import java.util.List;
 
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.ServiceStatus;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class LockService extends BaseService {

	private static Logger logger = Logger.getLogger(LockService.class.getName());
	
    private static final long minTimeToSleepMillisec = 1000 * 5; // 5 secs
    private static final long maxTimeToSleepMillisec = minTimeToSleepMillisec * 24; // 2 minutes
    private static final long deltaTimeToSleep = maxTimeToSleepMillisec - minTimeToSleepMillisec;

    
  static private Logger startuplogger = Logger.getLogger("StartupLogger");
	  
    /** 2000 lock/sec */
    @JsonProperty("ratePerMillisec")
    private double ratePerMillisec = 2;

    @JsonIgnore
    ReentrantReadWriteLock serverLock = new ReentrantReadWriteLock();

    @JsonIgnore
    private ConcurrentMap<Long, ReentrantReadWriteLock> objectLocks = new ConcurrentHashMap<>(1000);

    @JsonIgnore
    private ConcurrentMap<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>(1000);
    
    
    @JsonIgnore
    private Timer cleaner;

    @JsonIgnore
    private Timer fileCleaner;
	 
    @JsonIgnore
	private final OffsetDateTime created = OffsetDateTime.now();

    
    
    @Autowired
	public LockService(ServerDBSettings settings) {
		super(settings);
	}
 

    public ReadWriteLock getFileLock(String path) {
        return getFileLocks().computeIfAbsent(path, key -> new ReentrantReadWriteLock());
    }
    
    public ReadWriteLock getObjectLock(Long id) {
        return getObjectLocks().computeIfAbsent(id, key -> new ReentrantReadWriteLock());
    }


    public boolean isLocked(Long id) {

	        if (!getObjectLocks().containsKey(id))
	            return false;

	        if (getObjectLocks().get(id).isWriteLocked())
	            return true;

	        if (getObjectLocks().get(id).getReadLockCount() > 0)
	            return true;

	        return false;
	    }


	    public ConcurrentMap<Long, ReentrantReadWriteLock> getObjectLocks() {
	        return objectLocks;
	    }

	    public void setObjectLocks(ConcurrentMap<Long, ReentrantReadWriteLock> objectLocks) {
	        this.objectLocks = objectLocks;
	    }

	    public ConcurrentMap<String, ReentrantReadWriteLock> getFileLocks() {
	        return fileLocks;
	    }

	    public void setFileLocks(ConcurrentMap<String, ReentrantReadWriteLock> objectLocks) {
	        this.fileLocks = objectLocks;
	    }
	    
	    @PostConstruct
	    protected synchronized void onInitialize() {

	        setStatus(ServiceStatus.STARTING);
	        this.ratePerMillisec = 10; // getServerSettings().getLockRateMillisecs();

	        this.cleaner = new Timer() {

	            @Override
	            public long getSleepTimeMillis() {
	                return Math.round(minTimeToSleepMillisec
	                        + deltaTimeToSleep / (1.0 + ((getObjectLocks().size()) / deltaTimeToSleep)));
	            }

	            @Override
	            public void onTimeUp() {

	                if (exit())
	                    return;

	                if (getObjectLocks().size() > 0) {
	                    long maxToPurge = Math.round(getRatePerMillisec() * maxTimeToSleepMillisec)
	                            + (long) (getRatePerMillisec() * 1000.0);
	                    List<Long> list = new ArrayList<Long>();
	                    try {
	                        int counter = 0;
	                        for (Entry<Long, ReentrantReadWriteLock> entry : getObjectLocks().entrySet()) {
	                            if (entry.getValue().writeLock().tryLock()) {
	                                list.add(entry.getKey());
	                                counter++;
	                                if (counter >= maxToPurge) {
	                                    break;
	                                }
	                            }
	                        }
	                        list.forEach(item -> {
	                            ReentrantReadWriteLock lock = getObjectLocks().get(item);
	                            getObjectLocks().remove(item);
	                            lock.writeLock().unlock();
	                        });
	                        list.forEach(item -> getObjectLocks().remove(item));

	                    } finally {
	                    }
	                }
	            }
	        };

	        Thread thread = new Thread(cleaner);
	        thread.setDaemon(true);
	        thread.setName(
	                LockService.class.getSimpleName() + "Object Cleaner-" + Double.valueOf(Math.abs(Math.random() * 1000000)).intValue());
	        thread.start();
	        
	        
	        
	        this.fileCleaner = new Timer() {

	            @Override
	            public long getSleepTimeMillis() {
	                return Math.round(minTimeToSleepMillisec
	                        + deltaTimeToSleep / (1.0 + ((getObjectLocks().size()) / deltaTimeToSleep)));
	            }

	            @Override
	            public void onTimeUp() {

	                if (exit())
	                    return;

	                if (getFileLocks().size() > 0) {
	                    long maxToPurge = Math.round(getRatePerMillisec() * maxTimeToSleepMillisec)
	                            + (long) (getRatePerMillisec() * 1000.0);
	                    List<String> list = new ArrayList<String>();
	                    try {
	                        int counter = 0;
	                        for (Entry<String, ReentrantReadWriteLock> entry : getFileLocks().entrySet()) {
	                            if (entry.getValue().writeLock().tryLock()) {
	                                list.add(entry.getKey());
	                                counter++;
	                                if (counter >= maxToPurge) {
	                                    break;
	                                }
	                            }
	                        }
	                        list.forEach(item -> {
	                            ReentrantReadWriteLock lock = getFileLocks().get(item);
	                            getFileLocks().remove(item);
	                            lock.writeLock().unlock();
	                        });
	                        list.forEach(item -> getFileLocks().remove(item));

	                    } finally {
	                    }
	                }
	            }
	        };

	        Thread f_thread = new Thread(fileCleaner);
	        f_thread.setDaemon(true);
	        f_thread.setName(
	                LockService.class.getSimpleName() + "File Cleaner-" + Double.valueOf(Math.abs(Math.random() * 1000000)).intValue());
	        f_thread.start();
	        
	        setStatus(ServiceStatus.RUNNING);
	        startuplogger.debug("Started -> " + LockService.class.getSimpleName());
	    }

	    @PreDestroy
	    private void preDestroy() {
	        getPoolCleaner().sendExitSignal();
	    }

	    

	    private Timer getPoolCleaner() {
	        return this.cleaner;
	    }

	    private double getRatePerMillisec() {
	        return this.ratePerMillisec;
	    }

	 
	
	@PostConstruct
	protected void onInit() {

		try {
			 
			setStatus(ServiceStatus.RUNNING);
		} catch (Exception e) {
			setStatus(ServiceStatus.STOPPED);
		}
	}


	public OffsetDateTime getOffsetDateTimeCreated() {
		return this.created;
	}

 
	

	 

}
