package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.wktui.error.ErrorPanel;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanel;
import wktui.base.InvisiblePanel;


/**
 * 
 * site 
 * foto 
 * Info - exhibitions
 * 
 */

@MountPath("/site/info/${id}")
public class SiteInfoPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(SiteInfoPage.class.getName());

	private StringValue stringValue;
	private IModel<Site> siteModel;
	private SiteEditor editor;
	private Image image;
	private WebMarkupContainer imageContainer;
	private Link<Resource> imageLink;

	public  SiteInfoPage() {
		super();
	}		
	
	
	public  SiteInfoPage(PageParameters parameters) {
		 super(parameters);
		 stringValue = getPageParameters().get("id");
	 }
	 	
	
	public SiteInfoPage(IModel<Site> model) {
		setSiteModel( model );
		getPageParameters().add( "id", model.getObject().getId().toString());
	}

	
	protected void setupModel() {

		if (getSiteModel()==null) {
			if (stringValue!=null) {
				Optional<Site> o_site= getSite(Long.valueOf(stringValue.toLong()));
				if (o_site.isPresent()) {
					setSiteModel(new ObjectModel<Site>(o_site.get()));
				}
			}
		}

		if (getSiteModel()==null) {
			isError = true;
			return;
		}
		
		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findByIdWithDeps(getSiteModel().getObject().getId());
			setSiteModel(new ObjectModel<Site>(o_i.get()));
		}
	}
	
	
	boolean isError = false;
	IModel<String> errorMsg;
	
	
	public IModel<String> getErrorMsg() {
		return errorMsg;
	}
	
	
	public void setErrorMsg(IModel<String> msg) {
	 errorMsg=msg;
	}
	
	public boolean isError() {
		return this.isError;
	}
	/**
	 * 
	 * Institution
	 * Site
	 * Artwork
	 * Person
	 * Exhibition
	 * ExhibitionItem
	 * GuideContent
	 * User
	 * 
	 * 
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();

		setupModel();
		
        add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));

		if (!isError()) {
	        BreadCrumb<Void> bc = createBreadCrumb();
	        bc.addElement(new HREFBCElement( "/site/list", getLabel("sites")));
	        bc.addElement(new HREFBCElement( "/site/"+ getSiteModel().getObject().getId().toString(), 
	        			  new Model<String>(getSiteModel().getObject().getDisplayname())));
	        bc.addElement(new BCElement( getLabel("general-info")));
	        PageHeaderPanel<Site> ph = new PageHeaderPanel<Site>("page-header", getSiteModel(), new Model<String>(getSiteModel().getObject().getDisplayname() ));
			ph.setBreadCrumb(bc);
			add(ph);

			this.editor = new SiteEditor("siteEditor", getSiteModel());
			add(this.editor);
			

		}
		else {
			add( new InvisiblePanel("page-header"));
			add( new ErrorPanel("siteEditor", getErrorMsg()) );
		}
	}
     
    @Override
	public void onDetach() {
	    super.onDetach();
	    
	    if (siteModel!=null)
	    	siteModel.detach();
	    
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}



	/**
	private void addImageAndInfo() {

		this.imageContainer = new WebMarkupContainer("imageContainer");
		this.imageContainer.setVisible(getSiteModel().getObject().getPhoto() != null);
		addOrReplace(this.imageContainer);
		this.imageLink = new Link<Resource>("image-link", null) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				logger.debug("on click");
			}
		};
		
		this.imageContainer.add(this.imageLink);
		
		Label info = new Label("info", TextCleaner.clean(getSiteModel().getObject().getInfo()));
		info.setEscapeModelStrings(false);
		this.imageContainer.add(info);
		
		String presignedThumbnail = null;
		
		if (getSiteModel().getObject().getPhoto()!=null) 
			presignedThumbnail = getPresignedThumbnailSmall(getSiteModel().getObject().getPhoto());
		
		if (presignedThumbnail != null) {
			Url url = Url.parse(presignedThumbnail);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			this.image = new Image("image", resourceReference);
			this.imageLink.addOrReplace(this.image);
		} else {
			this.image = new Image("image", new UrlResourceReference(Url.parse("")));
			this.image.setVisible(false);
			this.imageLink.addOrReplace(image);
		}
	}*/
	 

}
