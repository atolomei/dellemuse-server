package dellemuse.serverapp.artwork;

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
import dellemuse.serverapp.command.QRCodeArtWorkGenerationCommand;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Resource;
import io.odilon.client.error.ODClientException;
import io.wktui.error.ErrorPanel;
import io.wktui.media.InvisibleImage;
import io.wktui.nav.toolbar.ToolbarItem;
import wktui.base.InvisiblePanel;
import wktui.base.LabelPanel;

public class ArtWorkQRCodePanel extends DBModelPanel<ArtWork> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtWorkQRCodePanel.class.getName());

	private WebMarkupContainer qrcodecontainer;

	private IModel<File> qrFileModel;
	private IModel<File> qrPdfFileModel;
	private IModel<File> audioNumberPngFileModel;

	public ArtWorkQRCodePanel(String id, IModel<ArtWork> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		qrcodecontainer = new WebMarkupContainer("qrcodeContainer");
		addOrReplace(qrcodecontainer);

		qrcodecontainer.add(new InvisiblePanel("error"));

		// ----------------
		//
		// generate QR Code
		//
		AjaxLink<Void> makeQRCode = new AjaxLink<Void>("make-qrcode") {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return ArtWorkQRCodePanel.this.getModel().getObject().getQRCode() == null;
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					QRCodeArtWorkGenerationCommand cmd = new QRCodeArtWorkGenerationCommand(
							ArtWorkQRCodePanel.this.getModel().getObject().getId(), true);
					cmd.execute();
					setUpModel();
					addPanels();
					target.add(ArtWorkQRCodePanel.this);
				} catch (Exception e) {
					logger.error(e, ServerConstant.NOT_THROWN);
					qrcodecontainer.addOrReplace(new ErrorPanel("error", Model.of(e.getClass().getSimpleName() + " | " + e.getMessage())));
					target.add(ArtWorkQRCodePanel.this);
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
				return ArtWorkQRCodePanel.this.getModel().getObject().getQRCodePdf() == null || isRoot();
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					addPdf();
					setUpModel();
					addPanels();
					target.add(ArtWorkQRCodePanel.this);
				} catch (Exception e) {
					logger.error(e, ServerConstant.NOT_THROWN);
					qrcodecontainer.addOrReplace(new ErrorPanel("error", Model.of(e.getClass().getSimpleName() + " | " + e.getMessage())));
					target.add(ArtWorkQRCodePanel.this);
				}
			}
		};

		qrcodecontainer.add(make);

		// ----------------
		//
		// make audio number png
		//
		AjaxLink<Void> makeAudioNumberPng = new AjaxLink<Void>("make-audio-number-png") {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return ArtWorkQRCodePanel.this.getModel().getObject().getAudioNumberPng() == null || isRoot();
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					addAudioNumberPng();
					setUpModel();
					addPanels();
					target.add(ArtWorkQRCodePanel.this);
				} catch (Exception e) {
					logger.error(e, ServerConstant.NOT_THROWN);
					qrcodecontainer.addOrReplace(new ErrorPanel("error", Model.of(e.getClass().getSimpleName() + " | " + e.getMessage())));
					target.add(ArtWorkQRCodePanel.this);
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
		ArtWork aw = getModel().getObject();
		getModel().setObject(getArtWorkDBService().findWithDeps(aw.getId()).get());
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

		Resource qrcode = getModel().getObject().getQRCode();
		Resource qrcodePdf = getModel().getObject().getQRCodePdf();

		try {

			if (qrcode != null) {

				qrFileModel = new dellemuse.serverapp.serverdb.objectstorage.ObjectStorageFileModel(qrcode.getBucketName(), qrcode.getObjectName(), qrcode.getName());

				String presignedThumbnail = super.getPresignedThumbnail(qrcode, ThumbnailSize.LARGE);
				Url url = Url.parse(presignedThumbnail);
				UrlResourceReference resourceReference = new UrlResourceReference(url);

				Link<Void> qrImageLink = new Link<Void>("qr-image-link") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						File file = getQRFileModel().getObject();
						FileResourceStream stream = new FileResourceStream(file);
						ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(stream, file.getName());
						handler.setContentDisposition(ContentDisposition.INLINE);
						getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
					}
				};

				Image image = new Image("qrcode", resourceReference);
				qrImageLink.add(image);
				qrcodecontainer.addOrReplace(qrImageLink);

				Label l = new Label("qrcode-text", getModel().getObject().getQrCodeText());
				qrcodecontainer.addOrReplace(l);

				// PDF link
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

					Link<Void> audioImageLink = new Link<Void>("audio-number-png-link") {

						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							File file = getAudioNumberPngFileModel().getObject();
							FileResourceStream stream = new FileResourceStream(file);
							ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(stream, file.getName());
							handler.setContentDisposition(ContentDisposition.INLINE);
							getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
						}
					};

					Image audioImage = new Image("audio-number-png-img", audioRef);
					audioImageLink.add(audioImage);
					qrcodecontainer.addOrReplace(audioImageLink);

				} else {
					qrcodecontainer.addOrReplace(new InvisiblePanel("audio-number-png-link"));
				}


			} else {
				qrcodecontainer.addOrReplace(new InvisiblePanel("qr-image-link"));
				qrcodecontainer.addOrReplace(new InvisibleImage("qrcode-text"));
				qrcodecontainer.addOrReplace(new InvisiblePanel("qr-pdf-link"));
				qrcodecontainer.addOrReplace(new InvisiblePanel("audio-number-png-link"));
			}

		} catch (Exception e) {
			qrcodecontainer.addOrReplace(new InvisiblePanel("qr-image-link"));
			qrcodecontainer.addOrReplace(new InvisibleImage("qrcode-text"));
			qrcodecontainer.addOrReplace(new InvisiblePanel("qr-pdf-link"));
			qrcodecontainer.addOrReplace(new InvisiblePanel("audio-number-png-link"));

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
			logger.error(e, ServerConstant.NOT_THROWN);
			throw new RuntimeException("Failed to download QR code image from object storage: " + e.getMessage(), e);
		}

		try {
			image = ImageIO.read(qrimage);
		} catch (IOException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			throw new RuntimeException("Failed to read QR code image file: " + e.getMessage(), e);
		}

		File outputDir = new File(getServerDBSettings().getWorkDir());
		File pdf;

		try {
			pdf = generatePdf(getModel().getObject(), image, outputDir);
		} catch (IOException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
		}

		logger.debug("Generated PDF: " + pdf.getAbsolutePath() + " (size: " + pdf.length() + " bytes)");

		if (pdf.exists()) {
			String bucketName = ServerConstant.QR_BUCKET;
			String objectName = "qr-artwork-pdf-" + getModel().getObject().getId().toString();

			try {
				if (getObjectStorageService().existsObject(bucketName, objectName)) {
					getObjectStorageService().getClient().deleteObject(bucketName, objectName);
				}

				getObjectStorageService().getClient().putObject(bucketName, objectName, pdf);
				getArtWorkDBService().addQRPdf(getModel().getObject(), bucketName, objectName, pdf.getName(), "application/pdf", pdf.length(), getSessionUser().get());

			} catch (IOException | ODClientException e) {
				logger.error(e, ServerConstant.NOT_THROWN);
				throw new RuntimeException("Failed to upload PDF to object storage: " + e.getMessage(), e);
			}
		}
	}

	private void addAudioNumberPng() {

		ArtWork aw = getModel().getObject();

		if (aw.getAudioId() == null) {
			getArtWorkDBService().generateAudioId(aw, getSessionUser().get());
			setUpModel();
			aw = getModel().getObject();
		}

		File outputDir = new File(getServerDBSettings().getWorkDir());
		File png;

		try {
			png = generateAudioNumberPng(aw, outputDir);
		} catch (IOException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			throw new RuntimeException("Failed to generate audio number PNG: " + e.getClass().getSimpleName() + " | " + e.getMessage(), e);
		}

		logger.debug("Generated audio number PNG: " + png.getAbsolutePath() + " (size: " + png.length() + " bytes)");

		if (png.exists()) {
			String bucketName = ServerConstant.QR_BUCKET;
			String objectName = "artwork-audio-number-png-" + aw.getId().toString();

			try {
				if (getObjectStorageService().existsObject(bucketName, objectName)) {
					getObjectStorageService().getClient().deleteObject(bucketName, objectName);
				}

				getObjectStorageService().getClient().putObject(bucketName, objectName, png);
				getArtWorkDBService().addAudioNumberPng(aw, bucketName, objectName, png.getName(), "image/png", png.length(), getSessionUser().get());

			} catch (IOException | ODClientException e) {
				logger.error(e, ServerConstant.NOT_THROWN);
				throw new RuntimeException("Failed to upload audio number PNG to object storage: " + e.getClass().getSimpleName() + " | " + e.getMessage(), e);
			}
		}
	}

	private File generatePdf(ArtWork ex, BufferedImage qrImage, File outputDir) throws IOException {

		String filename = "qr-artwork-" + io.wktui.model.TextCleaner.truncate(getResourceDBService().normalizeFileName(ex.getName()), 24) + "-" + ex.getId() + ".pdf";

		File pdfFile = new File(outputDir, filename);

		try (PDDocument document = new PDDocument()) {

			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(qrImage, "PNG", baos);
			PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, baos.toByteArray(), "qr");

			File headphonesFile = new File("img" + File.separator + "headphones.png");
			PDImageXObject headphonesImage = PDImageXObject.createFromFileByContent(headphonesFile, document);

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true)) {

				float pageWidth = page.getMediaBox().getWidth();
				float pageHeight = page.getMediaBox().getHeight();

				float qrSize = 200f;

				float qrX = (pageWidth - qrSize) / 2;
				float qrY = pageHeight - 150 - qrSize;
				contentStream.drawImage(pdImage, qrX, qrY, qrSize, qrSize);

				float headphonesSize = 44f;
				String audioIdText = ex.getAudioId() != null ? ex.getAudioId().toString() : "";

				PDType0Font fontBold = PDType0Font.load(document, new File(getServerDBSettings().getFontsDir() + File.separator + "montserrat", "Montserrat-Bold.ttf"));
				float audioIdFontSize = 32f;
				float audioIdWidth = fontBold.getStringWidth(audioIdText) / 1000 * audioIdFontSize;

				float gap = 10f;
				float totalWidth = headphonesSize + gap + audioIdWidth;
				float startX = (pageWidth - totalWidth) / 2;
				float iconY = qrY - headphonesSize - 30;

				contentStream.drawImage(headphonesImage, startX, iconY, headphonesSize, headphonesSize);

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

	private File generateAudioNumberPng(ArtWork aw, File outputDir) throws IOException {

		String filename = "artwork-audio-number-" + io.wktui.model.TextCleaner.truncate(getResourceDBService().normalizeFileName(aw.getName()), 24) + "-" + aw.getId() + ".png";
		File pngFile = new File(outputDir, filename);

		File headphonesFile = new File("img" + File.separator + "headphones-duotone-solid-full.svg");

		String audioIdText = aw.getAudioId() != null ? aw.getAudioId().toString() : "";

		File fontFile = new File(getServerDBSettings().getFontsDir() + File.separator + "montserrat", "Montserrat-Bold.ttf");
		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.BOLD, 48f);
		} catch (java.awt.FontFormatException e) {
			throw new IOException("Failed to load Montserrat font: " + e.getMessage(), e);
		}

		BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D tempG2d = tempImage.createGraphics();
		tempG2d.setFont(font);
		FontMetrics fm = tempG2d.getFontMetrics();
		int textWidth = fm.stringWidth(audioIdText);
		int textHeight = fm.getHeight();
		tempG2d.dispose();

		int iconSize = 64;
		int gap = 16;
		int padding = 20;

		BufferedImage headphonesIcon = loadSvgAsImage(headphonesFile, iconSize, iconSize);

		int totalWidth = padding + iconSize + gap + textWidth + padding;
		int totalHeight = padding + Math.max(iconSize, textHeight) + padding;

		BufferedImage image = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		g2d.setComposite(java.awt.AlphaComposite.Clear);
		g2d.fillRect(0, 0, totalWidth, totalHeight);
		g2d.setComposite(java.awt.AlphaComposite.SrcOver);

		int iconY = (totalHeight - iconSize) / 2;
		g2d.drawImage(headphonesIcon, padding, iconY, iconSize, iconSize, null);

		g2d.setColor(Color.BLACK);
		g2d.setFont(font);
		int textX = padding + iconSize + gap;
		int textY = (totalHeight - textHeight) / 2 + fm.getAscent();
		g2d.drawString(audioIdText, textX, textY);

		g2d.dispose();

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
