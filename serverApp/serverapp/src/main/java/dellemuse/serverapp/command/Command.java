package dellemuse.serverapp.command;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import dellemuse.model.JsonObject;
import dellemuse.model.util.FSUtil;
import dellemuse.model.util.RandomIDGenerator;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.elevenlabs.ClientConstant;
import dellemuse.serverapp.email.EmailService;
import dellemuse.serverapp.email.EmailTemplateService;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.CandidateDBService;
import dellemuse.serverapp.serverdb.service.MusicDBService;
import dellemuse.serverapp.serverdb.service.PersistentTokenDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.LockService;
import dellemuse.serverapp.service.ResourceThumbnailService;
import dellemuse.serverapp.service.SecurityService;

public abstract class Command extends JsonObject {

	private static final RandomIDGenerator rand = new RandomIDGenerator();

	private String id;
	private CommandStatus status;
	private OffsetDateTime startTime;
	private OffsetDateTime endTime;

	public Command() {
	}

	public abstract void execute();

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		if (this.id == null)
			this.id = newId();
		return this.id;
	}

	private static synchronized String newId() {
		return String.valueOf(Long.valueOf(System.nanoTime())) + "-" + rand.randomString(6);
	}

	double progress;

	public void setProgress(double d) {
		this.progress = d;
	}

	public double getProgress() {
		return this.progress;
	}

	public Duration getDuration() {
		if (startTime != null && endTime != null)
			return Duration.between(startTime, endTime);
		return null;
	}

	@JsonIgnore
	private LockService lockService;

	public LockService getLockService() {
		if (lockService == null)
			this.lockService = (LockService) ServiceLocator.getInstance().getBean(LockService.class);

		return this.lockService;
	}

	
	protected String getMimeType(String fileName) {

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
	protected BufferedImage genereate(String barcodeText) throws IOException {

		QRCodeWriter barcodeWriter = new QRCodeWriter();

		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.MARGIN, 2); // optional tweak

		BitMatrix bitMatrix;
		try {
			 

			bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 800, 800, hints);

			BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
			return image;

		} catch (WriterException e) {
			throw new IOException(e);
		}
	}
	
	
	protected String getAudioCacheWorkDir() {
		return getHomeDirAbsolutePath() + File.separator + "audiocache" + File.separator + "download";
	}

	protected String getHomeDirAbsolutePath() {
		if (isLinux())
			return ClientConstant.linux_home;
		return ClientConstant.windows_home;
	}

	protected boolean isLinux() {
		if (System.getenv("OS") != null && System.getenv("OS").toLowerCase().contains("windows"))
			return false;
		return true;
	}

	/* seconds */
	public double estimatedSecsToEnd() {
		return 0;
	}

	public CommandStatus getStatus() {
		return status;
	}

	public void setStatus(CommandStatus status) {
		this.status = status;
	}

	public void addCallback(CommandLifeCycleCallback commandLifecycleCallback) {
	}

	public String getConcurrentUniqueKey() {
		return null;
	}

	public void setOffsetDateTimeStart(OffsetDateTime date) {
		startTime = date;
	}

	public OffsetDateTime getOffsetDateTimeStart() {
		return startTime;
	}

	public OffsetDateTime getOffsetDateTimeEnd() {
		return endTime;
	}

	public void setOffsetDateTimeEnd(OffsetDateTime endOffseDateTime) {
		this.endTime = endOffseDateTime;
	}

	 

	protected ResourceThumbnailService getResourceThumbnailService() {
		return (ResourceThumbnailService) ServiceLocator.getInstance().getBean(ResourceThumbnailService.class);
	}

	protected ArtExhibitionDBService getArtExhibitionDBService() {
		return (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
	}

	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}

	protected ArtWorkDBService getArtWorkDBService() {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		return service;
	}
	
	protected ServerDBSettings getServerDBSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);
	}

	protected EmailService getEmailService() {
		return (EmailService) ServiceLocator.getInstance().getBean(EmailService.class);
	}

	protected ResourceDBService getResourceDBService() {
		return (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}

	protected SecurityService getSecurityService() {
		return (SecurityService) ServiceLocator.getInstance().getBean(SecurityService.class);
	}

	protected MusicDBService getMusicDBService() {
		return (MusicDBService) ServiceLocator.getInstance().getBean(MusicDBService.class);
	}

	protected CandidateDBService getCandidateDBService() {
		return (CandidateDBService) ServiceLocator.getInstance().getBean(CandidateDBService.class);
	}

	protected PersistentTokenDBService getPersistentTokenDBServiceDBService() {
		return (PersistentTokenDBService) ServiceLocator.getInstance().getBean(PersistentTokenDBService.class);
	}

	

	protected EmailTemplateService getEmailTemplateService() {
		return (EmailTemplateService) ServiceLocator.getInstance().getBean(EmailTemplateService.class);
	}
	
	protected ObjectStorageService getObjectStorageService() {
		return (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
	}

	protected ServerDBSettings getSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);
	}

	protected User getRootUser() {
		return ((UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class)).findRoot();
	}

}
