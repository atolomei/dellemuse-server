package dellemuse.serverapp.elevenlabs;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.Optional;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.command.Command;
import dellemuse.serverapp.command.CommandStatus;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;

public class TestElevenLabsCommand extends Command {

	static private Logger logger =	Logger.getLogger(TestElevenLabsCommand.class.getName());
	
	public TestElevenLabsCommand() {
	}

	@Override
	public void execute() {
		try {
			super.setOffsetDateTimeStart(OffsetDateTime.now());
			setStatus(CommandStatus.RUNNING);
		
			String text = "En la revuelta de Niká de 532, convenció a Justiniano de no huir y enfrentar a sus enemigos con su famosa frase -“el púrpura es una buena mortaja”-. Promovió leyes en favor de los derechos de las mujeres: prohibió el tráfico sexual forzado, fortaleció los derechos de las mujeres en el matrimonio y el divorcio, y fundó hogares para mujeres rescatadas de la prostitución, algo sin precedentes en la época.";
			
			Optional<File> ofile = getElevenLabsService().generate(text, "theodora.mp3", LanguageCode.ES, "mariana");
			
			if (ofile.isPresent()) {
				logger.debug(ofile.get().getAbsolutePath());
			}
			
			super.setOffsetDateTimeEnd(OffsetDateTime.now());
			super.setProgress(1.0);
			
		} catch (Exception e) {
			logger.error(e);
		}
		finally {
			setStatus(CommandStatus.TERMINATED);
		}
	}
	
	protected ServerDBSettings getSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);
	}
	
	protected ResourceDBService getResourceDBService() {
		return  (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}
	
	protected ElevenLabsService getElevenLabsService() {
		return  (ElevenLabsService) ServiceLocator.getInstance().getBean(ElevenLabsService.class);
		
		
	}
	
}
 