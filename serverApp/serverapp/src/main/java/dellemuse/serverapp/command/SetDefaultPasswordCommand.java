package dellemuse.serverapp.command;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

public class SetDefaultPasswordCommand extends Command {

	static private Logger logger = Logger.getLogger(SetDefaultPasswordCommand.class.getName());

	public SetDefaultPasswordCommand() {

	}

	@Override
	public void execute() {

		try {
			getUserDBService().findAll().forEach(user -> {
				if (user.getPassword() == null) {
					String hash = new BCryptPasswordEncoder().encode("dellemuse");
					user.setPassword(hash);
					logger.debug("Saving user -> " + user.getDisplayname() + " | p: " + user.getPassword());
					getUserDBService().save(user);
				}
			});

		} catch (Exception e) {
			logger.error(e, ServerConstant.NOT_THROWN);
		}
	}

	protected ArtWorkDBService getArtWorkDBService() {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		return service;
	}

	private UserDBService getUserDBService() {
		return ((UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class));
	}

	private User getRootUser() {
		return ((UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class)).findRoot();
	}

	protected ServerDBSettings getSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);
	}

	protected ResourceDBService getResourceDBService() {
		return (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}

}
