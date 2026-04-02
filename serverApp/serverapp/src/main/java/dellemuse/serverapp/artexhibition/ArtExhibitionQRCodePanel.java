package dellemuse.serverapp.artexhibition;

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

	public ArtExhibitionQRCodePanel(String id, IModel<ArtExhibition> model) {
		super(id, model);
		setOutputMarkupId(true);
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

				addOrReplace(new InvisiblePanel("noqrcode"));

			} else {
				qrcodecontainer.addOrReplace(new InvisibleImage("qrcode"));
				qrcodecontainer.addOrReplace(new InvisibleImage("qrcode-text"));
				qrcodecontainer.addOrReplace(new InvisiblePanel("qr-file-link"));
				qrcodecontainer.addOrReplace(new InvisiblePanel("qr-pdf-link"));
				addOrReplace(new LabelPanel("noqrcode", getLabel("noqrcode")));
			}

		} catch (Exception e) {
			qrcodecontainer.addOrReplace(new InvisibleImage("qrcode"));
			qrcodecontainer.addOrReplace(new InvisibleImage("qrcode-text"));
			qrcodecontainer.addOrReplace(new InvisiblePanel("qr-file-link"));
			qrcodecontainer.addOrReplace(new InvisiblePanel("qr-pdf-link"));
			addOrReplace(new ErrorPanel("noqrcode", Model.of(e.getClass().getSimpleName() + " | " + e.getMessage())));

			logger.error(e, ServerConstant.NOT_THROWN);
		}

	}

	private String safeText(String input) {
		if (input == null)
			return "";
		return input.replaceAll("[^\\x00-\\xFF]", "?");
	}
	
	
	private File generatePdf(ArtExhibition ex, BufferedImage qrImage, File outputDir) throws IOException {

		String filename = "qrsite-" + getResourceDBService().normalizeFileName(ex.getName()) + "-" + ex.getId() + ".pdf";

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
				float headphonesSize = 42f;
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
	
	

	private void addPdf() {

		BufferedImage image;

		Resource qrcode = getResourceDBService().findById( getModel().getObject().getQrcode().getId()).get();

		
		String bu = qrcode.getBucketName();
		String ob = qrcode.getObjectName();

		File qrimage = new File(getServerDBSettings().getWorkDir(), qrcode.getId().toString() + ".png");

		try {

			getObjectStorageService().getClient().getObject(bu, ob, qrimage.getAbsolutePath());

		} catch (ODClientException | IOException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			return;
		}

		try {
			
			image = ImageIO.read(qrimage);
			
		} catch (IOException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			return;
		}

		File outputDir = new File(getServerDBSettings().getWorkDir());
		File pdf;
		
		try {
			
			pdf = generatePdf(getModel().getObject(), image, outputDir);
			
		} catch (IOException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			return;
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
				return;
			}

		}

	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		qrcodecontainer = new WebMarkupContainer("qrcodeContainer");
		add(qrcodecontainer);

		qrcodecontainer.add( new InvisiblePanel("error"));
		
		AjaxLink<Void> make = new AjaxLink<Void>("make") {
			private static final long serialVersionUID = 1L;

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

		addPanels();

	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (qrFileModel != null)
			qrFileModel.detach();

		if (qrPdfFileModel != null)
			qrPdfFileModel.detach();
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

}
