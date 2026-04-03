package dellemuse.serverapp.page.site;

import java.io.File;
import java.util.List;

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
import dellemuse.serverapp.command.QRSiteCodeGenerationCommand;
import dellemuse.serverapp.command.QRSitePdfGenerationCommand;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.error.ErrorPanel;
import io.wktui.media.InvisibleImage;
import io.wktui.nav.toolbar.ToolbarItem;
import wktui.base.InvisiblePanel;
import wktui.base.LabelPanel;

public class SiteQRCodePanel extends DBModelPanel<Site> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteQRCodePanel.class.getName());

	private WebMarkupContainer qrcodecontainer;

	private IModel<File> qrFileModel;

	private IModel<File> qrPdfFileModel;

	public SiteQRCodePanel(String id, IModel<Site> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	
	private void addPanels() {
		
		Resource qrcode = getModel().getObject().getQrcode();
		Resource qrcodePdf = getModel().getObject().getQRCodePdf();

		try {

			if (qrcode != null) {

				qrFileModel = new dellemuse.serverapp.serverdb.objectstorage.ObjectStorageFileModel(
						qrcode.getBucketName(), qrcode.getObjectName(), qrcode.getName());

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
				link.addOrReplace(f);
				qrcodecontainer.addOrReplace(link);

				 
				if (qrcodePdf != null) {

					qrPdfFileModel = new dellemuse.serverapp.serverdb.objectstorage.ObjectStorageFileModel(
							qrcodePdf.getBucketName(), qrcodePdf.getObjectName(), qrcodePdf.getName());

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
					pdfLink.addOrReplace(pdfName);
					qrcodecontainer.addOrReplace(pdfLink);

				} else {
					qrcodecontainer.addOrReplace(new InvisiblePanel("qr-pdf-link"));
				}

				Label l = new Label("qrcode-text",  getModel().getObject().getQrCodeText());
				qrcodecontainer.addOrReplace(l);
				
				addOrReplace(new InvisiblePanel("noqrcode"));

				
				
			} else {
				qrcodecontainer.addOrReplace(new InvisibleImage("qrcode"));
				qrcodecontainer.addOrReplace(new InvisiblePanel("qrcode-text"));
				qrcodecontainer.addOrReplace(new InvisiblePanel("qr-file-link"));
				qrcodecontainer.addOrReplace(new InvisiblePanel("qr-pdf-link"));
				addOrReplace(new LabelPanel("noqrcode", getLabel("noqrcode")));
			}

		} catch (Exception e) {
			qrcodecontainer.addOrReplace(new InvisibleImage("qrcode"));
			qrcodecontainer.addOrReplace(new InvisiblePanel("qrcode-text"));
			qrcodecontainer.addOrReplace(new InvisiblePanel("qr-file-link"));
			qrcodecontainer.addOrReplace(new InvisiblePanel("qr-pdf-link"));
			addOrReplace(new ErrorPanel("noqrcode", Model.of(e.getClass().getSimpleName() + " | " + e.getMessage())));
			logger.error(e, ServerConstant.NOT_THROWN);
		}
	}
	
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
	
		qrcodecontainer = new WebMarkupContainer("qrcodeContainer");
		add (qrcodecontainer);
		
		qrcodecontainer.add( new InvisiblePanel("error"));
		
	 	AjaxLink<Void> make = new AjaxLink<Void>("make") {
			private static final long serialVersionUID = 1L;
		
			@Override
			public boolean isVisible() {
				return SiteQRCodePanel.this.getModel().getObject().getQRCodePdf()==null || isRoot();
			}
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					QRSitePdfGenerationCommand c = new QRSitePdfGenerationCommand(SiteQRCodePanel.this.getModel().getObject().getId(), true);
					c.execute();
					setUpModel();
					addPanels();
					target.add(SiteQRCodePanel.this);
				} catch (Exception e) {
					logger.error(e, ServerConstant.NOT_THROWN);
					qrcodecontainer.addOrReplace(new ErrorPanel("error", Model.of(e.getClass().getSimpleName() + " | " + e.getMessage())));
					target.add(SiteQRCodePanel.this);
				}
			}
		};
		
		qrcodecontainer.add(make);
		
	
		//qrcodecontainer.setVisible(qrcode != null);
		
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
		Site s = getModel().getObject();
		getModel().setObject(getSiteDBService().findWithDeps(s.getId()).get());
	}

	private IModel<File> getQRFileModel() {
		return qrFileModel;
	}

	private IModel<File> getQRPdfFileModel() {
		return qrPdfFileModel;
	}

}
