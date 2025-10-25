package dellemuse.serverapp.serverdb.objectstorage;

import java.io.File;
import java.io.IOException;

import org.apache.wicket.model.IModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.odilon.client.error.ODClientException;

public class ObjectStorageFileModel implements IModel<File> {
	 
	static private Logger logger = Logger.getLogger(ObjectStorageFileModel.class.getName());
		
	private static final long serialVersionUID = 1L;
	String bucketName;
	String objectName;
    String fileName;

	public ObjectStorageFileModel(	String bucketName,  String objectName, String fileName) {
		this.bucketName=bucketName;
		this.objectName=objectName;
		this.fileName=fileName;
	}

	public void detach() {
		 
	}
	
	@Override
	public File getObject() {
	
		// bucketName+"-"+objectName+"-"
		
		File file = new File (getSettings().getObjectStorageTempDir(), this.fileName);
		
		if (file.exists())
			return file;
		
		try {
			
			getObjectStorageService().getClient().getObject( bucketName, objectName, file.getAbsolutePath());
			logger.debug(file.getAbsolutePath() + " .-> " + file.exists());
		
			return file;
	
		} catch (ODClientException | IOException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			return null;
		}
	}

	protected ObjectStorageService getObjectStorageService() {
		return (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
	}
	
	protected ServerDBSettings getSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);
	}
	
}
