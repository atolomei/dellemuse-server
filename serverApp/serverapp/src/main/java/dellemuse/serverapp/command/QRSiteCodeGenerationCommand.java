package dellemuse.serverapp.command;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.FSUtil;
import dellemuse.serverapp.DelleMuseServerDBVersion;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class QRSiteCodeGenerationCommand extends Command {

	static private Logger logger = Logger.getLogger(QRSiteCodeGenerationCommand.class.getName());

	@JsonProperty("siteId")
	private Long siteId;

	public QRSiteCodeGenerationCommand(Long aId) {
		this.siteId = aId;
	}

	@Override
	public void execute() {

		if (!getSettings().isQRCodeGenerate()) {
			logger.debug("QR Code generation is disabled in application.properties | qrcode.generation=disabled");
			return;
		}

		try {

			Optional<Site> o = getSiteDBService().findById(getSiteId());

			if (o.isPresent()) {

				Site site = o.get();

				getSiteDBService().evict(site);

				o = getSiteDBService().findWithDeps(getSiteId());

				site = o.get();

				BufferedImage image;

				if (site.getName() == null) {
					logger.debug("Site name is null, cannot generate QR code");
					return;
				}

				if (site.getQrcode() == null) {

					getLockService().getObjectLock(site.getId()).writeLock().lock();
					try {
						ObjectStorageService os = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);

						String url = getSettings().getQRServer() + "/ag/" + site.getId().toString();

						image = genereate(url);

						ServerDBSettings settings = getSettings();
						File file = new File(settings.getWorkDir(), "qrsite-" + getResourceDBService().normalizeFileName(site.getName()) + "-" + site.getId().toString() + ".png");

						try {
							logger.debug("write -> " + file.getAbsolutePath());
							ImageIO.write(image, "PNG", file);

							String bucketName = ServerConstant.QR_BUCKET;
							String objectName = "qrsite-png-" + site.getId().toString();

							if (os.existsObject(bucketName, objectName)) {
								os.getClient().deleteObject(bucketName, objectName);
							}
							os.getClient().putObject(bucketName, objectName, file);
							
							site = getSiteDBService().addQR(site, url, bucketName, objectName, file.getName(), getMimeType(file.getName()), file.length(), getRootUser());
							logger.debug(site.getQrcode() != null ? site.getQrcode().getDisplayname() : "nul");

						} catch (IOException e) {
							logger.error(e, ServerConstant.NOT_THROWN);
						}

						/**
						 * try {
						 * 
						 * 
						 * File outputDir = new File(settings.getWorkDir()); File pdf= generatePdf(site,
						 * image, outputDir);
						 * 
						 * if (pdf.exists()) { String bucketName = ServerConstant.QR_BUCKET; String
						 * objectName = "qrsite-pdf-"+site.getId().toString();
						 * 
						 * if (!os.existsObject(bucketName, objectName)) {
						 * os.getClient().putObject(bucketName, objectName, file); } site=
						 * getSiteDBService().addQRPdf(site, bucketName, objectName, pdf.getName(),
						 * getMimeType(pdf.getName()), pdf.length(), getRootUser());
						 * 
						 * }
						 * 
						 * 
						 * 
						 * } catch (IOException e) { logger.error(e, ServerConstant.NOT_THROWN); }
						 **/
					} finally {
						getLockService().getObjectLock(site.getId()).writeLock().unlock();
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

	public Long getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Long resourceId) {
		this.siteId = resourceId;
	}

	protected ServerDBSettings getSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);
	}

	protected ResourceDBService getResourceDBService() {
		return (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}

	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}
 

	 

	private File generatePdf3(Site site, BufferedImage qrImage, File outputDir) throws IOException {

		if (qrImage == null) {
			throw new RuntimeException("QR image is null");
		}

		String filename = "qrsite-" + getResourceDBService().normalizeFileName(site.getName()) + "-" + site.getId() + ".pdf";

		File pdfFile = new File(outputDir, filename);

		try (PDDocument document = new PDDocument()) {

			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			PDImageXObject pdImage = LosslessFactory.createFromImage(document, qrImage);

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
				contentStream.drawImage(pdImage, 100, 400, 200, 200);
			} catch (Exception e) {
				logger.error(e);
				throw e;
			}

			document.save(pdfFile);
		}

		logger.debug("PDF size: " + pdfFile.length());

		return pdfFile;
	}

	private File generatePdf(Site site, BufferedImage qrImage, File outputDir) throws IOException {

		String filename = "qrsite-" + getResourceDBService().normalizeFileName(site.getName()) + "-" + site.getId() + ".pdf";

		File pdfFile = new File(outputDir, filename);

		try (PDDocument document = new PDDocument()) {

			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			// Convert image safely (REQUIRED in 3.x)
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(qrImage, "PNG", baos);

			PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, baos.toByteArray(), "qr");

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true)) {

				float pageWidth = page.getMediaBox().getWidth();
				float pageHeight = page.getMediaBox().getHeight();

				float qrSize = 200f;

				float x = (pageWidth - qrSize) / 2;
				float y = (pageHeight - qrSize) / 2;

				contentStream.drawImage(pdImage, x, y, qrSize, qrSize);
			}

			document.save(pdfFile);
		}
		return pdfFile;
	}

	private File generatePdf2(Site site, BufferedImage qrImage, File outputDir) throws IOException {

		String filename = "qrsite-" + getResourceDBService().normalizeFileName(site.getName()) + "-" + site.getId() + ".pdf";

		File pdfFile = new File(outputDir, filename);

		try (PDDocument document = new PDDocument()) {

			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			PDImageXObject pdImage = LosslessFactory.createFromImage(document, qrImage);

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

				float pageWidth = page.getMediaBox().getWidth();
				float pageHeight = page.getMediaBox().getHeight();

				float qrSize = 200f;

				float x = (pageWidth - qrSize) / 2;
				float y = (pageHeight / 2) - (qrSize / 2);

				// QR
				contentStream.drawImage(pdImage, x, y, qrSize, qrSize);

				// ---- Title ----
				if (site.getName() != null) {

					contentStream.beginText();

					PDType0Font font = PDType0Font.load(document, new File(getSettings().getFontsDir(), "NotoSans-Regular.ttf"));
					contentStream.setFont(font, 18);
					contentStream.newLineAtOffset(50, y + qrSize + 40);
					contentStream.showText(safeText(site.getName()));
					contentStream.endText();
				}

				// ---- Instruction ----
				contentStream.beginText();

				PDType0Font font = PDType0Font.load(document, new File(getSettings().getFontsDir(), "NotoSans-Regular.ttf"));
				contentStream.setFont(font, 12);
				contentStream.newLineAtOffset(50, y - 40);
				contentStream.showText("Scan to listen");
				contentStream.endText();
			}

			document.save(pdfFile);
		}

		return pdfFile;
	}

	private String safeText(String input) {
		if (input == null)
			return "";
		return input.replaceAll("[^\\x00-\\xFF]", "?");
	}

}
