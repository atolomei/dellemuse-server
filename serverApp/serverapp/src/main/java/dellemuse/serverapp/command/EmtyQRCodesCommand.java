package dellemuse.serverapp.command;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.FSUtil;
import dellemuse.serverapp.DelleMuseServerDBVersion;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.odilon.client.error.ODClientException;
import io.odilon.model.ObjectMetadata;
import io.odilon.model.list.Item;
import io.odilon.model.list.ResultSet;

public class EmtyQRCodesCommand extends Command {

	static private Logger logger = Logger.getLogger(EmtyQRCodesCommand.class.getName());


	public EmtyQRCodesCommand()  {
	}

	@Override
	public void execute() {

		try {
				ObjectStorageService os = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class); 
				String bucketName = ServerConstant.QR_BUCKET; 

				ResultSet<Item<ObjectMetadata>> r = os.listObjects(bucketName);
				
				List<ObjectMetadata> list = new ArrayList<ObjectMetadata>();

				while (r.hasNext()) {
					
					Item<ObjectMetadata> item = r.next();
					if (item.isOk())
						list.add(item.getObject());
				}
							
				list.forEach( 
							v ->  {
							try {
								
								logger.debug( "deleting -> " + bucketName +"-" + v.getObjectName() );

								os.getClient().deleteObject(bucketName, v.getObjectName());
							
							} catch (ODClientException e) {
								logger.error(e, ServerConstant.NOT_THROWN);
							}
						}
				);

		} catch (Exception e) {
			logger.error(e, ServerConstant.NOT_THROWN);
		}
	}
	
	  protected ArtWorkDBService getArtWorkDBService() {
	    	ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
	    	return service;
	  }
	  
	protected User getRootUser() {
		return ((UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class)).findRoot();
	}

 	protected ServerDBSettings getSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);
	}
	
	protected ResourceDBService getResourceDBService() {
		return  (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}

	 
}
