package dellemuse.serverapp.command;

public interface CommandLifeCycleCallback {

    public void start();
    public void stop();
    public void end();
}
