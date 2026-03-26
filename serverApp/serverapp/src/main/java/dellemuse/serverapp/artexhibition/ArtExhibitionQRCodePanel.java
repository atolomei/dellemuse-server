package dellemuse.serverapp.artexhibition;

import java.io.File;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Resource;
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

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		qrcodecontainer = new WebMarkupContainer("qrcodeContainer");
		add(qrcodecontainer);

		Resource qrcode = getModel().getObject().getQrcode();
		Resource qrcodePdf = getModel().getObject().getQRCodePdf();

		qrcodecontainer.setVisible(qrcode != null);

		try {

			if (qrcode != null) {

				qrFileModel = new dellemuse.serverapp.serverdb.objectstorage.ObjectStorageFileModel(
						qrcode.getBucketName(), qrcode.getObjectName(), qrcode.getName());

				String presignedThumbnail = super.getPresignedThumbnail(qrcode, ThumbnailSize.LARGE);
				Url url = Url.parse(presignedThumbnail);
				UrlResourceReference resourceReference = new UrlResourceReference(url);

				Image image = new Image("qrcode", resourceReference);
				qrcodecontainer.add(image);

				DownloadLink link = new DownloadLink("qr-file-link", getQRFileModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public File getModelObject() {
						return getQRFileModel().getObject();
					}
				};

				Label f = new Label("qr-file-name", qrcode.getName());
				link.add(f);
				qrcodecontainer.add(link);
				Label l = new Label("qrcode-text",  getModel().getObject().getQrCodeText());
				qrcodecontainer.addOrReplace(l);
				
				
				// PDF download link
				if (qrcodePdf != null) {

					qrPdfFileModel = new dellemuse.serverapp.serverdb.objectstorage.ObjectStorageFileModel(
							qrcodePdf.getBucketName(), qrcodePdf.getObjectName(), qrcodePdf.getName());

					DownloadLink pdfLink = new DownloadLink("qr-pdf-link", getQRPdfFileModel()) {

						private static final long serialVersionUID = 1L;

						@Override
						public File getModelObject() {
							return getQRPdfFileModel().getObject();
						}
					};

					Label pdfName = new Label("qr-pdf-name", qrcodePdf.getName());
					pdfLink.add(pdfName);
					qrcodecontainer.add(pdfLink);

				} else {
					qrcodecontainer.add(new InvisiblePanel("qr-pdf-link"));
				}

				add(new InvisiblePanel("noqrcode"));

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
