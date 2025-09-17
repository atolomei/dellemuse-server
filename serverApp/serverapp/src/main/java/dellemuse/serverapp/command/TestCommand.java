package dellemuse.serverapp.command;

import java.time.OffsetDateTime;

import dellemuse.model.logging.Logger;

public class TestCommand extends Command {

	static private Logger logger =	Logger.getLogger(TestCommand.class.getName());
	
	public TestCommand() {
	}

	@Override
	public void execute() {
		try {
			
			super.setOffsetDateTimeStart(OffsetDateTime.now());
			setStatus(CommandStatus.RUNNING);
			Thread.sleep(1000);
			super.setOffsetDateTimeEnd(OffsetDateTime.now());
			
		} catch (Exception e) {
			logger.error(e);
		}
		finally {
			
			super. setProgress(1.0);
			
			setStatus(CommandStatus.TERMINATED);
			logger.debug(this.toString());

		}
	}
}
 