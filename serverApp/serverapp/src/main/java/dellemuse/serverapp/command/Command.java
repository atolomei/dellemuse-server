package dellemuse.serverapp.command;


import java.time.Duration;
import java.time.OffsetDateTime;

import dellemuse.model.JsonObject;
import dellemuse.model.util.RandomIDGenerator;

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
		if (this.id==null)
			this.id=newId();
		return this.id;
	}

	private static synchronized String newId() {
		return String.valueOf(Long.valueOf(System.nanoTime()))+"-"+rand.randomString(6);
	}

	double progress;
	
	public void setProgress(double d) {
		this.progress=d;
	}
		
	public double getProgress() {
		return this.progress;
	}

	public Duration getDuration() {
		if (startTime!=null && endTime!=null)
			return Duration.between(startTime, endTime);
		return null;
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
		startTime=date;		
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
}
