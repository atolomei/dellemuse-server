package dellemuse.serverapp.command;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

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
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.DelleMuseServerDBVersion;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Resource;
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

public class QRSitePdfGenerationCommand extends Command {

	static private Logger logger = Logger.getLogger(QRSitePdfGenerationCommand.class.getName());

	@JsonProperty("siteId")
	private Long siteId;

	boolean force = false;

	public QRSitePdfGenerationCommand(Long aId) {
		this.siteId = aId;
	}

	public QRSitePdfGenerationCommand(Long aId, boolean force) {
		this.siteId = aId;
		this.force = force;
	}

	@Override
	public void execute() {

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
					logger.error("Site QR code null, cannot generate QR pdf");
					return;

				}

				if (force || site.getQRCodePdf() == null) {

					ObjectStorageService os = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);

					getLockService().getObjectLock(site.getId()).writeLock().lock();

					try {

						try {

							Resource qrcode = site.getQrcode();

							String bu = qrcode.getBucketName();
							String ob = qrcode.getObjectName();

							File qrimage = new File(getSettings().getWorkDir(), qrcode.getId().toString() + ".png");

							os.getClient().getObject(bu, ob, qrimage.getAbsolutePath());

							image = ImageIO.read(qrimage);

							File outputDir = new File(getSettings().getWorkDir());
							File pdf = generatePdf(site, image, outputDir);

							logger.debug("Generated PDF: " + pdf.getAbsolutePath() + " (size: " + pdf.length() + " bytes)");

							if (pdf.exists()) {
								String bucketName = ServerConstant.QR_BUCKET;
								String objectName = "qrsite-pdf-" + site.getId().toString();

								if (os.existsObject(bucketName, objectName)) {
									os.getClient().deleteObject(bucketName, objectName);
								}

								os.getClient().putObject(bucketName, objectName, pdf);

								site = getSiteDBService().addQRPdf(site, bucketName, objectName, pdf.getName(), getMimeType(pdf.getName()), pdf.length(), getRootUser());

							}

						} catch (IOException e) {
							logger.error(e, ServerConstant.NOT_THROWN);
						}

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

	private String getLabel(String key, String lang) {
		ResourceBundle resources = ResourceBundle.getBundle(getClass().getName(), Locale.forLanguageTag(normalizeForResourceBundle(lang)));
		return resources.getString(key);
	}

	protected String normalizeForResourceBundle(String language) {
		
		if (language.startsWith("pt")) {
			return "pt";
		}
		if (language.startsWith("en")) {
			return "en";
		}

		if (language.startsWith("es")) {
			return "es";
		}

		return language;
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

				// PDType0Font fontRegular = PDType0Font.load(document, new
				// File(getSettings().getFontsDir(), "NotoSans-Regular.ttf"));

				PDType0Font fontRegularBold = PDType0Font.load(document, new File(getSettings().getFontsDir() + File.separator + "montserrat", "Montserrat-Bold.ttf"));

				PDType0Font fontRegular = PDType0Font.load(document, new File(getSettings().getFontsDir() + File.separator + "montserrat", "Montserrat-Regular.ttf"));

				String audioGuidesStr = site.getMasterLanguage();

				// ---- Title: "Museo Nacional de Bellas Artes" ----
				float titleFontSize = 21f;
				String titleText = site.getName();
				float titleWidth = fontRegular.getStringWidth(titleText) / 1000 * titleFontSize;
				float titleX = (pageWidth - titleWidth) / 2;
				float titleY = pageHeight - 150;

				contentStream.beginText();
				contentStream.setFont(fontRegular, titleFontSize);
				contentStream.newLineAtOffset(titleX, titleY);
				contentStream.showText(titleText);
				contentStream.endText();

				// ---- Subtitle: "Audioguías" ----
				float subtitleFontSize = 15f;
				String subtitleText = safeText(getLabel("audio-guides", site.getMasterLanguage()));
				float subtitleWidth = fontRegular.getStringWidth(subtitleText) / 1000 * subtitleFontSize;
				float subtitleX = (pageWidth - subtitleWidth) / 2;
				float subtitleY = titleY - 30;

				// contentStream.setNonStrokingColor(0.4f, 0.4f, 0.4f); // #666666
				contentStream.beginText();
				contentStream.setFont(fontRegular, subtitleFontSize);
				contentStream.newLineAtOffset(subtitleX, subtitleY);
				contentStream.showText(subtitleText);
				contentStream.endText();
				float y = subtitleY - qrSize - 40;

				if (!site.getMasterLanguage().startsWith("en")) {

					// ---- Subtitle: "Audioguías" ----
					String subtitleTextEn = safeText(getLabel("audio-guides", "en"));
					float subtitleYEn = subtitleY - 23;

					float subtitleWidthEn = fontRegular.getStringWidth(subtitleTextEn) / 1000 * subtitleFontSize;
					float subtitleXEn = (pageWidth - subtitleWidthEn) / 2;
					// contentStream.setNonStrokingColor(0.4f, 0.4f, 0.4f); // #666666
					contentStream.beginText();
					contentStream.setFont(fontRegular, subtitleFontSize);
					contentStream.newLineAtOffset(subtitleXEn, subtitleYEn);
					contentStream.showText(subtitleTextEn);

					contentStream.endText();
					y = subtitleYEn - qrSize - 40;

				}

				// ---- QR Image (centered below subtitle) ----
				float x = (pageWidth - qrSize) / 2;

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
