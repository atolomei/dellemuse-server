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
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
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

public class QRArtExhibitionCodeGenerationCommand extends Command {

	static private Logger logger = Logger.getLogger(QRArtExhibitionCodeGenerationCommand.class.getName());

	@JsonProperty("artExhibitionId")
	private Long artExhibitionId;

	public QRArtExhibitionCodeGenerationCommand(Long aId) {
		this.artExhibitionId = aId;
	}

	@Override
	public void execute() {

		if (!getSettings().isQRCodeGenerate()) {
			logger.debug("QR Code generation is disabled in application.properties | qrcode.generation=disabled");
			return;
		}

		try {

			Optional<ArtExhibition> o = getArtExhibitionDBService().findById(getArtExhibitionId());
 	
			if (o.isPresent()) {
 		
				ArtExhibition aex = o.get();

				getLockService().getObjectLock(aex.getId()).writeLock().lock();
				try {
			
					getArtExhibitionDBService().evict(aex);
	
					o = getArtExhibitionDBService().findWithDeps(getArtExhibitionId());
	
					aex = o.get();
	
					BufferedImage image;
	
					if (aex.getName() == null || aex.getSite() == null) {
						logger.debug("ArtExhibition name or site is null, cannot generate QR code");
						return;
					}
	
					if (aex.getQrcode() == null) {
	
						ObjectStorageService os = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
	
						String url = getSettings().getQRServer() + "/ag/guide/" + aex.getId().toString();
	
						image = genereate(url);
	
						ServerDBSettings settings = getSettings();
						File file = new File(settings.getWorkDir(), "qrartexhibition-" + getResourceDBService().normalizeFileName(aex.getName()) + "-" + aex.getId().toString() + ".png");
	
						try {
							logger.debug("write -> " + file.getAbsolutePath());
							ImageIO.write(image, "PNG", file);
	
							String bucketName = ServerConstant.QR_BUCKET;
							String objectName = "qrartexhibition-png-" + aex.getId().toString();
	
							if (!os.existsObject(bucketName, objectName)) {
								os.getClient().putObject(bucketName, objectName, file);
							}
							aex = getArtExhibitionDBService().addQR(aex, bucketName, objectName, file.getName(), getMimeType(file.getName()), file.length(), getRootUser());
							logger.debug(aex.getQrcode() != null ? aex.getQrcode().getDisplayname() : "nul");
	
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
	
					}
				} finally {
					getLockService().getObjectLock(aex.getId()).writeLock().unlock();
					
				}
			}

		} catch (Exception e) {
			logger.error(e, ServerConstant.NOT_THROWN);
		}
	}

	public Long getArtExhibitionId() {
		return this.artExhibitionId;
	}

	public void setSiteId(Long resourceId) {
		this.artExhibitionId = resourceId;
	}

	

	private BufferedImage genereate(String barcodeText) throws IOException {

		QRCodeWriter barcodeWriter = new QRCodeWriter();

		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.MARGIN, 2); // optional tweak

		BitMatrix bitMatrix;
		try {
			// bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 800,
			// 800);

			bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 800, 800, hints);

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
