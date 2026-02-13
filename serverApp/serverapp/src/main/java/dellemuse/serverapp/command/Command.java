package dellemuse.serverapp.command;

import java.io.File;
import java.time.Duration;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.JsonObject;
import dellemuse.model.util.RandomIDGenerator;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.elevenlabs.ClientConstant;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.MusicDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.LockService;

public abstract class Command extends JsonObject {

	private static final RandomIDGenerator rand = new RandomIDGenerator();

	private String id;
	private CommandStatus status;
	private OffsetDateTime startTime;
	private OffsetDateTime endTime;

	public Command() {
	}

	public abstract void execute();

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		if (this.id == null)
			this.id = newId();
		return this.id;
	}

	private static synchronized String newId() {
		return String.valueOf(Long.valueOf(System.nanoTime())) + "-" + rand.randomString(6);
	}

	double progress;

	public void setProgress(double d) {
		this.progress = d;
	}

	public double getProgress() {
		return this.progress;
	}

	public Duration getDuration() {
		if (startTime != null && endTime != null)
			return Duration.between(startTime, endTime);
		return null;
	}

	@JsonIgnore
	private LockService lockService;

	public LockService getLockService() {
		if (lockService == null)
			this.lockService = (LockService) ServiceLocator.getInstance().getBean(LockService.class);

		return this.lockService;
	}

	protected String getAudioCacheWorkDir() {
		return getHomeDirAbsolutePath() + File.separator + "audiocache" + File.separator + "download";
	}

	protected String getHomeDirAbsolutePath() {
		if (isLinux())
			return ClientConstant.linux_home;
		return ClientConstant.windows_home;
	}

	protected boolean isLinux() {
		if (System.getenv("OS") != null && System.getenv("OS").toLowerCase().contains("windows"))
			return false;
		return true;
	}

	/* seconds */
	public double estimatedSecsToEnd() {
		return 0;
	}

	public CommandStatus getStatus() {
		return status;
	}

	public void setStatus(CommandStatus status) {
		this.status = status;
	}

	public void addCallback(CommandLifeCycleCallback commandLifecycleCallback) {
	}

	public String getConcurrentUniqueKey() {
		return null;
	}

	public void setOffsetDateTimeStart(OffsetDateTime date) {
		startTime = date;
	}

	public OffsetDateTime getOffsetDateTimeStart() {
		return startTime;
	}

	public OffsetDateTime getOffsetDateTimeEnd() {
		return endTime;
	}

	public void setOffsetDateTimeEnd(OffsetDateTime endOffseDateTime) {
		this.endTime = endOffseDateTime;
	}

	protected ServerDBSettings getServerDBSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);
	}

	protected ResourceDBService getResourceDBService() {
		return (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}
	
	protected MusicDBService getMusicDBService() {
		return (MusicDBService) ServiceLocator.getInstance().getBean(MusicDBService.class);
	}

	protected ObjectStorageService getObjectStorageService() {
		return (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
	}
	
	protected ServerDBSettings getSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);
	}
}
