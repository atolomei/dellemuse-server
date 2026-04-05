package dellemuse.serverapp.artexhibition;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.resource.FileResourceStream;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.command.QRArtExhibitionCodeGenerationCommand;
import dellemuse.serverapp.command.QRSitePdfGenerationCommand;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.site.SiteQRCodePanel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.odilon.client.error.ODClientException;
import io.wktui.error.ErrorPanel;
import io.wktui.media.InvisibleImage;
import io.wktui.nav.toolbar.ToolbarItem;
import wktui.base.InvisiblePanel;
import wktui.base.LabelPanel;

public class ArtExhibitionQRCodePanel extends DBModelPanel<ArtExhibition> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionQRCodePanel.class.getName());

	private WebMarkupContainer qrcodecontainer;

	private IModel<File> qrFileModel;

	private IModel<File> qrPdfFileModel;

	private IModel<File> audioNumberPngFileModel;

	public ArtExhibitionQRCodePanel(String id, IModel<ArtExhibition> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		qrcodecontainer = new WebMarkupContainer("qrcodeContainer");
		add(qrcodecontainer);

		qrcodecontainer.add(new InvisiblePanel("error"));

		// ----------------
		//
		// generate QR Code
		//
		AjaxLink<Void> makeQRCode = new AjaxLink<Void>("make-qrcode") {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return ArtExhibitionQRCodePanel.this.getModel().getObject().getQrcode() == null;
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					QRArtExhibitionCodeGenerationCommand cmd = new QRArtExhibitionCodeGenerationCommand(ArtExhibitionQRCodePanel.this.getModel().getObject().getId(), true);
					cmd.execute();
					setUpModel();
					addPanels();
					target.add(ArtExhibitionQRCodePanel.this);
				} catch (Exception e) {
					logger.error(e, ServerConstant.NOT_THROWN);
					qrcodecontainer.addOrReplace(new ErrorPanel("error", Model.of(e.getClass().getSimpleName() + " | " + e.getMessage())));
					target.add(ArtExhibitionQRCodePanel.this);
				}
			}
		};

		qrcodecontainer.add(makeQRCode);

		// ----------------
		//
		// make pdf
		//
		AjaxLink<Void> make = new AjaxLink<Void>("make") {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return ArtExhibitionQRCodePanel.this.getModel().getObject().getQRCodePdf() == null || isRoot();
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {

					addPdf();
					setUpModel();
					addPanels();

					target.add(ArtExhibitionQRCodePanel.this);
				} catch (Exception e) {
					logger.error(e, ServerConstant.NOT_THROWN);
					qrcodecontainer.addOrReplace(new ErrorPanel("error", Model.of(e.getClass().getSimpleName() + " | " + e.getMessage())));
					target.add(ArtExhibitionQRCodePanel.this);
				}
			}
		};

		qrcodecontainer.add(make);

		
		// ----------------
		//
		// make  audio number png
		//
		AjaxLink<Void> makeAudioNumberPng = new AjaxLink<Void>("make-audio-number-png") {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return ArtExhibitionQRCodePanel.this.getModel().getObject().getAudioNumberPng() == null || isRoot();
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					
					addAudioNumberPng();
					setUpModel();
					addPanels();
					
					target.add(ArtExhibitionQRCodePanel.this);
			
				} catch (Exception e) {
					logger.error(e, ServerConstant.NOT_THROWN);
					qrcodecontainer.addOrReplace(new ErrorPanel("error", Model.of(e.getClass().getSimpleName() + " | " + e.getMessage())));
					target.add(ArtExhibitionQRCodePanel.this);
				}
			}
		};

		qrcodecontainer.add(makeAudioNumberPng);

		addPanels();

	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (qrFileModel != null)
			qrFileModel.detach();

		if (qrPdfFileModel != null)
			qrPdfFileModel.detach();

		if (audioNumberPngFileModel != null)
			audioNumberPngFileModel.detach();
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return null;
	}

	private void setUpModel() {
		ArtExhibition ae = getModel().getObject();
		getModel().setObject(getArtExhibitionDBService().findWithDeps(ae.getId()).get());
	}

	private IModel<File> getQRFileModel() {
		return qrFileModel;
	}

	private IModel<File> getQRPdfFileModel() {
		return qrPdfFileModel;
	}

	private IModel<File> getAudioNumberPngFileModel() {
		return audioNumberPngFileModel;
	}

	private void addPanels() {

		Resource qrcode = getModel().getObject().getQrcode();
		Resource qrcodePdf = getModel().getObject().getQRCodePdf();

		try {

			if (qrcode != null) {

				qrFileModel = new dellemuse.serverapp.serverdb.objectstorage.ObjectStorageFileModel(qrcode.getBucketName(), qrcode.getObjectName(), qrcode.getName());

				String presignedThumbnail = super.getPresignedThumbnail(qrcode, ThumbnailSize.LARGE);
				Url url = Url.parse(presignedThumbnail);
				UrlResourceReference resourceReference = new UrlResourceReference(url);

				Image image = new Image("qrcode", resourceReference);
				qrcodecontainer.addOrReplace(image);

				DownloadLink link = new DownloadLink("qr-file-link", getQRFileModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public File getModelObject() {
						return getQRFileModel().getObject();
					}
				};

				Label f = new Label("qr-file-name", qrcode.getName());
				link.add(f);
				qrcodecontainer.addOrReplace(link);
				Label l = new Label("qrcode-text", getModel().getObject().getQrCodeText());
				qrcodecontainer.addOrReplace(l);

				if (qrcodePdf != null) {

					qrPdfFileModel = new dellemuse.serverapp.serverdb.objectstorage.ObjectStorageFileModel(qrcodePdf.getBucketName(), qrcodePdf.getObjectName(), qrcodePdf.getName());

					Link<Void> pdfLink = new Link<Void>("qr-pdf-link") {

						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							File file = getQRPdfFileModel().getObject();
							FileResourceStream stream = new FileResourceStream(file);
							ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(stream, file.getName());
							handler.setContentDisposition(ContentDisposition.INLINE);
							getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
						}
					};

					Label pdfName = new Label("qr-pdf-name", qrcodePdf.getName());
					pdfLink.add(pdfName);
					qrcodecontainer.addOrReplace(pdfLink);

				} else {
					qrcodecontainer.addOrReplace(new InvisiblePanel("qr-pdf-link"));
				}

				// Audio Number PNG
				Resource audioNumberPng = getModel().getObject().getAudioNumberPng();
				
				if (audioNumberPng != null) {

					audioNumberPngFileModel = new dellemuse.serverapp.serverdb.objectstorage.ObjectStorageFileModel(
							audioNumberPng.getBucketName(), audioNumberPng.getObjectName(), audioNumberPng.getName());

					String presignedAudioPng = super.getPresignedThumbnail(audioNumberPng, ThumbnailSize.LARGE);
					Url audioUrl = Url.parse(presignedAudioPng);
					UrlResourceReference audioRef = new UrlResourceReference(audioUrl);

					Image audioImage = new Image("audio-number-png-img", audioRef);
					qrcodecontainer.addOrReplace(audioImage);

					DownloadLink audioLink = new DownloadLink("audio-number-png-link", getAudioNumberPngFileModel()) {

						private static final long serialVersionUID = 1L;

						@Override
						public File getModelObject() {
							return getAudioNumberPngFileModel().getObject();
						}
					};

					Label audioName = new Label("audio-number-png-name", audioNumberPng.getName());
					audioLink.add(audioName);
					qrcodecontainer.addOrReplace(audioLink);

				} else {
					qrcodecontainer.addOrReplace(new InvisibleImage("audio-number-png-img"));
					qrcodecontainer.addOrReplace(new InvisiblePanel("audio-number-png-link"));
				}

				addOrReplace(new InvisiblePanel("noqrcode"));

			} else {
				qrcodecontainer.addOrReplace(new InvisibleImage("qrcode"));
				qrcodecontainer.addOrReplace(new InvisibleImage("qrcode-text"));
				qrcodecontainer.addOrReplace(new InvisiblePanel("qr-file-link"));
				qrcodecontainer.addOrReplace(new InvisiblePanel("qr-pdf-link"));
				qrcodecontainer.addOrReplace(new InvisibleImage("audio-number-png-img"));
				qrcodecontainer.addOrReplace(new InvisiblePanel("audio-number-png-link"));
				addOrReplace(new LabelPanel("noqrcode", getLabel("noqrcode")));
			}

		} catch (Exception e) {
			qrcodecontainer.addOrReplace(new InvisibleImage("qrcode"));
			qrcodecontainer.addOrReplace(new InvisibleImage("qrcode-text"));
			qrcodecontainer.addOrReplace(new InvisiblePanel("qr-file-link"));
			qrcodecontainer.addOrReplace(new InvisiblePanel("qr-pdf-link"));
			qrcodecontainer.addOrReplace(new InvisibleImage("audio-number-png-img"));
			qrcodecontainer.addOrReplace(new InvisiblePanel("audio-number-png-link"));
			addOrReplace(new ErrorPanel("noqrcode", Model.of(e.getClass().getSimpleName() + " | " + e.getMessage())));

			logger.error(e, ServerConstant.NOT_THROWN);
		}

	}

	private void addPdf() {

		BufferedImage image;

		Resource qrcode = getResourceDBService().findById(getModel().getObject().getQrcode().getId()).get();

		String bu = qrcode.getBucketName();
		String ob = qrcode.getObjectName();

		File qrimage = new File(getServerDBSettings().getWorkDir(), qrcode.getId().toString() + ".png");

		try {

			getObjectStorageService().getClient().getObject(bu, ob, qrimage.getAbsolutePath());

		} catch (ODClientException | IOException e) {
			throw new RuntimeException("Failed to download QR code image from object storage: " + e.getClass().getSimpleName() + " | " + e.getMessage(), e);
		}

		try {

			image = ImageIO.read(qrimage);

		} catch (IOException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			throw new RuntimeException("Failed to read QR code image file: " + e.getClass().getSimpleName() + " | " + e.getMessage(), e);
		}

		File outputDir = new File(getServerDBSettings().getWorkDir());
		File pdf;

		try {

			pdf = generatePdf(getModel().getObject(), image, outputDir);

		} catch (IOException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			throw new RuntimeException("Failed to generate PDF: " + e.getClass().getSimpleName() + " | " + e.getMessage(), e);
		}

		logger.debug("Generated PDF: " + pdf.getAbsolutePath() + " (size: " + pdf.length() + " bytes)");

		if (pdf.exists()) {
			String bucketName = ServerConstant.QR_BUCKET;
			String objectName = "qr-artexhibition-pdf-" + getModel().getObject().getId().toString();

			try {
				if (getObjectStorageService().existsObject(bucketName, objectName)) {
					getObjectStorageService().getClient().deleteObject(bucketName, objectName);
				}

				getObjectStorageService().getClient().putObject(bucketName, objectName, pdf);
				getArtExhibitionDBService().addQRPdf(getModel().getObject(), bucketName, objectName, pdf.getName(), "application/pdf", pdf.length(), getSessionUser().get());

			} catch (IOException | ODClientException e) {
				logger.error(e, ServerConstant.NOT_THROWN);
				throw new RuntimeException("Failed to upload PDF to object storage or update database: " + e.getClass().getSimpleName() + " | " + e.getMessage(), e);
			}

		}

	}

	private File generatePdf(ArtExhibition ex, BufferedImage qrImage, File outputDir) throws IOException {

		String filename = "qr-artexhibition-" + io.wktui.model.TextCleaner.truncate( getResourceDBService().normalizeFileName(ex.getName()), 24) + "-" + ex.getId() + ".pdf";

		File pdfFile = new File(outputDir, filename);

		try (PDDocument document = new PDDocument()) {

			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			// Convert QR image safely (REQUIRED in PDFBox 3.x)
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(qrImage, "PNG", baos);
			PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, baos.toByteArray(), "qr");

			// Load headphones image
			File headphonesFile = new File("img" + File.separator + "headphones.png");
			PDImageXObject headphonesImage = PDImageXObject.createFromFileByContent(headphonesFile, document);

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true)) {

				float pageWidth = page.getMediaBox().getWidth();
				float pageHeight = page.getMediaBox().getHeight();

				float qrSize = 200f;

				// ---- QR Code image (centered horizontally, upper area) ----
				float qrX = (pageWidth - qrSize) / 2;
				float qrY = pageHeight - 150 - qrSize;
				contentStream.drawImage(pdImage, qrX, qrY, qrSize, qrSize);

				// ---- Headphones image + Audio ID (centered below QR code) ----
				float headphonesSize = 44f;
				String audioIdText = ex.getAudioId() != null ? ex.getAudioId().toString() : "";

				PDType0Font fontBold = PDType0Font.load(document, new File(getServerDBSettings().getFontsDir() + File.separator + "montserrat", "Montserrat-Bold.ttf"));
				float audioIdFontSize = 32f;
				float audioIdWidth = fontBold.getStringWidth(audioIdText) / 1000 * audioIdFontSize;

				// Total width of headphones icon + gap + audio ID text
				float gap = 10f;
				float totalWidth = headphonesSize + gap + audioIdWidth;
				float startX = (pageWidth - totalWidth) / 2;
				float iconY = qrY - headphonesSize - 30;

				// Draw headphones icon
				contentStream.drawImage(headphonesImage, startX, iconY, headphonesSize, headphonesSize);

				// Draw audio ID text (vertically centered with the icon)
				float textX = startX + headphonesSize + gap;
				float textY = iconY + (headphonesSize - audioIdFontSize) / 2 + 4;

				contentStream.beginText();
				contentStream.setFont(fontBold, audioIdFontSize);
				contentStream.newLineAtOffset(textX, textY);
				contentStream.showText(audioIdText);
				contentStream.endText();
			}

			document.save(pdfFile);
		}

		return pdfFile;
	}

	
	/**
	 * 
	 * 
	 * 
	 * 
	 */
	private void addAudioNumberPng() {

		ArtExhibition ex = getModel().getObject();
		
		if (ex.getAudioId() == null) {
			getArtExhibitionDBService().generateAudioId(ex, getSessionUser().get());
			setUpModel();
			ex = getModel().getObject();
		}

		File outputDir = new File(getServerDBSettings().getWorkDir());
		File png;

		try {
			png = generateAudioNumberPng(ex, outputDir);
			
		} catch (IOException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			throw new RuntimeException("Failed to generate audio number PNG: " + e.getClass().getSimpleName() + " | " + e.getMessage(), e);
		}

		logger.debug("Generated audio number PNG: " + png.getAbsolutePath() + " (size: " + png.length() + " bytes)");

		if (png.exists()) {
			String bucketName = ServerConstant.QR_BUCKET;
			String objectName = "artexhibition-audio-number-png-" + ex.getId().toString();

			try {
				if (getObjectStorageService().existsObject(bucketName, objectName)) {
					getObjectStorageService().getClient().deleteObject(bucketName, objectName);
				}

				getObjectStorageService().getClient().putObject(bucketName, objectName, png);
				getArtExhibitionDBService().addAudioNumberPng(ex, bucketName, objectName, png.getName(), "image/png", png.length(), getSessionUser().get());

			} catch (IOException | ODClientException e) {
				logger.error(e, ServerConstant.NOT_THROWN);
				throw new RuntimeException("Failed to upload audio number PNG to object storage: " + e.getClass().getSimpleName() + " | " + e.getMessage(), e);
			}
		}
	}

	
	/**
	 * 
	 * Generate audio id
	 * 
	 * 
	 * @param ex
	 * @param outputDir
	 * @return
	 * @throws IOException
	 */
	private File generateAudioNumberPng(ArtExhibition ex, File outputDir) throws IOException {

		String filename = "artexhibition-audio-number-" +  io.wktui.model.TextCleaner.truncate( getResourceDBService().normalizeFileName(ex.getName()), 24) + "-" + ex.getId() + ".png";
		File pngFile = new File(outputDir, filename);

		// Load headphones icon from SVG (rasterized at target size for crisp rendering)
		File headphonesFile = new File("img" + File.separator + "headphones-duotone-solid-full.svg");

		String audioIdText = ex.getAudioId() != null ? ex.getAudioId().toString() : "";

		// Setup font (Montserrat Bold, same as PDF generation)
		File fontFile = new File(getServerDBSettings().getFontsDir() + File.separator + "montserrat", "Montserrat-Bold.ttf");
		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.BOLD, 48f);
		} catch (java.awt.FontFormatException e) {
			throw new IOException("Failed to load Montserrat font: " + e.getMessage(), e);
		}
	
		
		// Measure text to calculate image dimensions
		BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D tempG2d = tempImage.createGraphics();
		tempG2d.setFont(font);
		FontMetrics fm = tempG2d.getFontMetrics();
		int textWidth = fm.stringWidth(audioIdText);
		int textHeight = fm.getHeight();
		tempG2d.dispose();

		// Icon dimensions
		int iconSize = 64;
		int gap = 16;
		int padding = 20;

		// Rasterize SVG at exact icon size
		BufferedImage headphonesIcon = loadSvgAsImage(headphonesFile, iconSize, iconSize);

		// Calculate total image size
		int totalWidth = padding + iconSize + gap + textWidth + padding;
		int totalHeight = padding + Math.max(iconSize, textHeight) + padding;

		// Create the image
		BufferedImage image = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();

		// Enable anti-aliasing
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		// Transparent background
		g2d.setComposite(java.awt.AlphaComposite.Clear);
		g2d.fillRect(0, 0, totalWidth, totalHeight);
		g2d.setComposite(java.awt.AlphaComposite.SrcOver);

		// Draw headphones icon (vertically centered)
		int iconY = (totalHeight - iconSize) / 2;
		g2d.drawImage(headphonesIcon, padding, iconY, iconSize, iconSize, null);

		// Draw audio ID text (vertically centered)
		g2d.setColor(Color.BLACK);
		g2d.setFont(font);
		int textX = padding + iconSize + gap;
		int textY = (totalHeight - textHeight) / 2 + fm.getAscent();
		g2d.drawString(audioIdText, textX, textY);

		g2d.dispose();

		// Write PNG
		ImageIO.write(image, "PNG", pngFile);

		return pngFile;
	}

	private BufferedImage loadSvgAsImage(File svgFile, int width, int height) throws IOException {
		try {
			org.apache.batik.transcoder.image.PNGTranscoder transcoder = new org.apache.batik.transcoder.image.PNGTranscoder();
			transcoder.addTranscodingHint(org.apache.batik.transcoder.SVGAbstractTranscoder.KEY_WIDTH, (float) width);
			transcoder.addTranscodingHint(org.apache.batik.transcoder.SVGAbstractTranscoder.KEY_HEIGHT, (float) height);

			org.apache.batik.transcoder.TranscoderInput input = new org.apache.batik.transcoder.TranscoderInput(svgFile.toURI().toString());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			org.apache.batik.transcoder.TranscoderOutput output = new org.apache.batik.transcoder.TranscoderOutput(baos);
			transcoder.transcode(input, output);

			return ImageIO.read(new java.io.ByteArrayInputStream(baos.toByteArray()));
		} catch (org.apache.batik.transcoder.TranscoderException e) {
			throw new IOException("Failed to transcode SVG: " + e.getMessage(), e);
		}
	}

}
