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
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.MultiLanguageObjectDBservice;
import dellemuse.serverapp.serverdb.service.RecordDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.PersonRecordDBService;
import dellemuse.serverapp.service.language.LanguageCacheEvictEvent;
import dellemuse.serverapp.service.language.LanguageObjectService;

public class EvictLanguageCacheRecordCommand extends Command {

	static private Logger logger = Logger.getLogger(EvictLanguageCacheRecordCommand.class.getName());

	final String parentObjectClassName;
	final Long  parentId;

	 
	
	public EvictLanguageCacheRecordCommand(String objectClassName, Long id) {
		this.parentObjectClassName=objectClassName;
		this.parentId=id;
	}

	@Override
	public void execute() {
		try {
 			 getLanguageObjectService().onApplicationEvent(new LanguageCacheEvictEvent(this.parentObjectClassName, this.parentId));
		} catch (Exception e) {
			logger.error(e, ServerConstant.NOT_THROWN);
		}
	}

	protected LanguageObjectService getLanguageObjectService() {
		return (LanguageObjectService) ServiceLocator.getInstance().getBean(LanguageObjectService.class);
	}
	 
}
