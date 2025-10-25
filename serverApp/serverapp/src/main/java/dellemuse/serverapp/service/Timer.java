package dellemuse.serverapp.service;

import java.util.concurrent.atomic.AtomicBoolean;

import dellemuse.serverapp.page.person.ServerAppConstant;
import io.odilon.log.Logger;
import io.odilon.util.Check;

public abstract class Timer implements Runnable {

	static public Logger logger = Logger.getLogger(Timer.class.getName());
	
	private AtomicBoolean exit = new AtomicBoolean(false);
	private Thread thread;

	public Timer() {
	}

	public abstract void onTimeUp();

	public boolean exit() {
		return this.exit.get();
	}

	public void sendExitSignal() {
		this.exit.set(true);
		if (this.thread != null)
			this.thread.interrupt();
	}

	public long getSleepTimeMillis() {
		return ServerAppConstant.DEFAULT_SLEEP_TIME;
	}

	@Override
	public void run() {
		Check.checkTrue(getSleepTimeMillis() > 100,
				"sleep time must be > 100 milisecs -> " + String.valueOf(getSleepTimeMillis()));
		this.thread = Thread.currentThread();
		synchronized (this) {
			while (!exit()) {
				try {
					Thread.sleep(getSleepTimeMillis());
					onTimeUp();
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
