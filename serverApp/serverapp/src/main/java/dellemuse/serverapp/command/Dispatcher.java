package dellemuse.serverapp.command;

public interface Dispatcher {
	
	public void execute(Runnable runnable);

	/**
	public int getPriority();
 	public void execute(Runnable batch);
	public int getPoolSize();
	public int getMaximumPoolSize();
	public String getInfo();
	public void shutDownNow();
	public String getStatus();
	public void restart();
	public void restart(boolean force);
	**/
	
}
