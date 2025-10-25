package dellemuse.serverapp.command;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

public class QRCodeGenerationCommand extends Command {

	static private Logger logger = Logger.getLogger(QRCodeGenerationCommand.class.getName());

	@JsonProperty("artworkId")
	private Long artworkId;

	public QRCodeGenerationCommand(Long aId) {
		this.artworkId = aId;
	}

	@Override
	public void execute() {

		if (!getSettings().isQRCodeGenerate()) {
			logger.debug("QR Code generation is disabled in application.properties | qrcode.generation=disabled");
			return;
		}
		
		
		try {

			Optional<ArtWork> o = getArtWorkDBService().findById(getArtWorkId());

			if (o.isPresent()) {

				ArtWork aw = o.get();
			
				getArtWorkDBService().evict(aw);
				
				o = getArtWorkDBService().findWithDeps(getArtWorkId());
		
				aw=o.get();
				
				if ((aw.getQRCode()==null) && (aw.getName()!=null) && (aw.getArtists() != null)) {
				 
					String url = getSettings().getQRServer()  + aw.getId().toString();

					BufferedImage image = genereate(url);

					ServerDBSettings settings = getSettings();
					File file = new File(settings.getWorkDir(), "qr-" + getResourceDBService().normalizeFileName(aw.getName()) +"-" + aw.getId().toString() + ".png");

					try {
							logger.debug("write -> " + file.getAbsolutePath());
							ImageIO.write(image, "PNG", file);
						
							ArtWorkDBService dbs = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
							ObjectStorageService os = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class); 
						  
							String bucketName = ServerConstant.QR_BUCKET; 
							String objectName = "qr-"+aw.getId().toString(); 
						  
							if (!os.existsObject(bucketName, objectName)) {
								os.getClient().putObject(bucketName, objectName, file); 
								aw=dbs.addQR(aw, bucketName, objectName, file.getName(), getMimeType(file.getName()),  file.length(), getRootUser());
							}
						 	logger.debug(aw.getQRCode()!=null? aw.getQRCode().getDisplayname() : "nul");
						  
						  
					} catch (IOException e) {
						logger.error(e, ServerConstant.NOT_THROWN);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e, ServerConstant.NOT_THROWN);
		}
	}
	
	  protected ArtWorkDBService getArtWorkDBService() {
	    	ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
	    	return service;
	    }
	  
	  


	private User getRootUser() {
		return ((UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class)).findRoot();
	}

	public Long getArtWorkId() {
		return this.artworkId;
	}

	public void setArtWorkIdId(Long resourceId) {
		this.artworkId = resourceId;
	}

	protected ServerDBSettings getSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);
	}
	
	protected ResourceDBService getResourceDBService() {
		return  (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}

	
	private BufferedImage genereate(String barcodeText) throws IOException {

		QRCodeWriter barcodeWriter = new QRCodeWriter();

		BitMatrix bitMatrix;
		try {
			bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
			BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
			return image;

		} catch (WriterException e) {
			throw new IOException(e);
		}
	}

	private String getMimeType(String fileName) {

        if (FSUtil.isImage(fileName)) {
            String str = FilenameUtils.getExtension(fileName);

            if (str.equals("jpg"))
                return "image/jpeg";

            if (str.equals("jpeg"))
                return "image/jpeg";

            return "image/" + str;
        }

        if (FSUtil.isPdf(fileName))
            return "application/pdf";

        if (FSUtil.isVideo(fileName))
            return "video/" + FilenameUtils.getExtension(fileName);

        if (FSUtil.isAudio(fileName))
            return "audio/" + FilenameUtils.getExtension(fileName);

        return "";
    }
}
