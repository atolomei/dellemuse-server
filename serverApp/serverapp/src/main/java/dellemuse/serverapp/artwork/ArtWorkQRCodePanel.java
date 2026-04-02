package dellemuse.serverapp.artwork;

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
import dellemuse.serverapp.command.QRCodeArtWorkGenerationCommand;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Resource;
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

	public ArtWorkQRCodePanel(String id, IModel<ArtWork> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		
		add(new InvisiblePanel("error"));
		
		qrcodecontainer = new WebMarkupContainer("qrcodeContainer");
		add(qrcodecontainer);
	
		
		AjaxLink<Void> qrGenerateLink = new AjaxLink<Void>("qr-generate-link") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					
					QRCodeArtWorkGenerationCommand c= new QRCodeArtWorkGenerationCommand(ArtWorkQRCodePanel.this.getModel().getObject().getId(), true );
					c.execute();
				
					logger.debug("artwork with id " + ArtWorkQRCodePanel.this.getModel().getObject().getId() + " qr code generation command executed");
					
					
					getLockService().getObjectLock(ArtWorkQRCodePanel.this.getModel().getObject().getId()).writeLock().lock();
					try {
						logger.debug("reloading artwork with id " + ArtWorkQRCodePanel.this.getModel().getObject().getId());
						setUpModel();
						addQRCodePanel();
						addQRCodePdfPanel();
						
					} finally {
						getLockService().getObjectLock(ArtWorkQRCodePanel.this.getModel().getObject().getId()).writeLock().unlock();
					}
				
					target.add(ArtWorkQRCodePanel.this);
				
				} catch (Exception e) {
					logger.error(e, ServerConstant.NOT_THROWN);
					ArtWorkQRCodePanel.this.addOrReplace(new ErrorPanel("error", e));
				}
				
			}
			
			@Override
			public boolean isVisible() {
				return true;
				//return  ArtWorkQRCodePanel.this.getModel().getObject().getQRCode()==null;
			}
			
		};
		
		add(qrGenerateLink);
		
		addQRCodePanel();
		addQRCodePdfPanel();
		
		
		
	}
/**
 * 
 * 	// PDF download link
				if (qrcodePdf != null) {

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
				
 * @return
 */
	private void addQRCodePanel() {
		
			qrcodecontainer = new WebMarkupContainer("qrcodeContainer");
			addOrReplace(qrcodecontainer);

			Resource qrcode = getModel().getObject().getQRCode();
			
			    if (qrcode != null) {
			       
					String presignedThumbnail = getPresignedThumbnail(qrcode, ThumbnailSize.LARGE) ;
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

					Label l = new Label("qrcode-text",  getModel().getObject().getQrCodeText());
					qrcodecontainer.addOrReplace(l);
					
					addOrReplace(new InvisiblePanel("noqrcode"));
	
			    }
			    
			    else {
			    	qrcodecontainer.addOrReplace(new InvisibleImage("qrcode"));
			    	qrcodecontainer.addOrReplace(new InvisiblePanel("qr-file-link"));
			    	qrcodecontainer.addOrReplace(new InvisibleImage("qrcode-text"));

			    	addOrReplace(new LabelPanel("noqrcode", getLabel("noqrcode")));
			}
	}
	
	private void addQRCodePdfPanel() {
		 
	    Resource qrcodepdf = getModel().getObject().getQRCodePdf();
	
	    if (qrcodepdf != null) {
	       
	    

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

			Label f = new Label("qr-pdf-name", qrcodepdf.getName());
			pdfLink.add(f);
			qrcodecontainer.addOrReplace(pdfLink);

			
			addOrReplace(new InvisiblePanel("noqrcode"));
	    }
	    
	    else {
	    	qrcodecontainer.addOrReplace(new InvisiblePanel("qr-pdf-link"));
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

		ArtWork aw = getModel().getObject();
		getModel().setObject(getArtWorkDBService().findWithDeps(aw.getId()).get());


		Resource qrcode = getModel().getObject().getQRCode();
		Resource qrcodePdf = getModel().getObject().getQRCodePdf();
		
		if (qrcode != null) {
		qrFileModel = new dellemuse.serverapp.serverdb.objectstorage.ObjectStorageFileModel(
				qrcode.getBucketName(), qrcode.getObjectName(), qrcode.getName());
		}
		
		if (qrcodePdf != null) {
		qrPdfFileModel = new dellemuse.serverapp.serverdb.objectstorage.ObjectStorageFileModel(
				qrcodePdf.getBucketName(), qrcodePdf.getObjectName(), qrcodePdf.getName());
		}
		
		
	}

	private IModel<File> getQRFileModel() {
		return qrFileModel;
	}

	private IModel<File> getQRPdfFileModel() {
		return qrPdfFileModel;
	}

}
