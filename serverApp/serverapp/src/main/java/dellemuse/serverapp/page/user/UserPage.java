package dellemuse.serverapp.page.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
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
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanel;


/**
 * 
 * 
 */

@MountPath("/user/${id}")
public class UserPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(UserPage.class.getName());

	private StringValue stringValue;

	private IModel<User> userModel;
	
	private UserEditor editor;
	
	
	public  UserPage() {
		super();
	}		
	
	
	public  UserPage(PageParameters parameters) {
		 super(parameters);
		 stringValue = getPageParameters().get("id");
	 }
	 	
	public UserPage(IModel<User> model) {
		setUserModel( model );
		getPageParameters().add( "id", model.getObject().getId().toString());
	}

	/**
	 * 
	
	 * Institution
	 * Site
	 * Artwork
	 * LIST(S)  Exhibition
	 * LIST(ex) ExhibitionItem
	 * Guide(S)
	 * GuideContent(G)
	 *
	 * Person
	 * Roles
	 * User
	 * 
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();


		if (getUserModel()==null) {
			if (stringValue!=null) {
				Optional<User> o_user = getUser(Long.valueOf(stringValue.toLong()));
				if (o_user.isPresent()) {
					setUserModel(new ObjectModel<User>(o_user.get()));
				}
			}
		}

		if (getUserModel()==null) {
			throw new RuntimeException("no user");
		}
		
		
        BreadCrumb<Void> bc = createBreadCrumb();
        bc.addElement(new HREFBCElement( "/user/list", getLabel("users")));
        bc.addElement(new BCElement( new Model<String>( getUserModel().getObject().getUsername())));
        
    	PageHeaderPanel<User> ph = new PageHeaderPanel<User>("page-header", getUserModel(), 
    			new Model<String>(getUserModel().getObject().getDisplayname() ));
		ph.setBreadCrumb(bc);
		add(ph);

        add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));
		
		editor = new UserEditor("userEditor", getUserModel());
		add(editor);
		
		}
    

	@Override
	public void onDetach() {
	    super.onDetach();
	    
	    if (userModel!=null)
	    	userModel.detach();
	    
	}

	public IModel<User> getUserModel() {
		return userModel;
	}

	public void setUserModel(IModel<User> siteModel) {
		this.userModel = siteModel;
	}


	
	 

}
