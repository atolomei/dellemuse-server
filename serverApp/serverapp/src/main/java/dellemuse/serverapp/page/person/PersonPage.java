package dellemuse.serverapp.page.person;

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
 * site 
 * foto 
 * Info - exhibitions
 * 
 */

@MountPath("/person/${id}")
public class PersonPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(PersonPage.class.getName());

	private StringValue stringValue;

	private IModel<Person> personModel;
	
	
	public  PersonPage() {
		super();
	}		
	
	PersonEditor editor;
	
	public  PersonPage(PageParameters parameters) {
		 super(parameters);
		 stringValue = getPageParameters().get("id");
	 }
	 	
	public PersonPage(IModel<Person> model) {
		setPersonModel( model );
		getPageParameters().add( "id", model.getObject().getId().toString());
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


		if (getPersonModel()==null) {
			if (stringValue!=null) {
				Optional<Person> o_site= getPerson(Long.valueOf(stringValue.toLong()));
				if (o_site.isPresent()) {
					setPersonModel(new ObjectModel<Person>(o_site.get()));
				}
			}
		}

		if (getPersonModel()==null) {
			throw new RuntimeException("no person");
		}
		
		
        BreadCrumb<Void> bc = createBreadCrumb();
        bc.addElement(new HREFBCElement( "/site/list", getLabel("persons")));
        bc.addElement(new BCElement( new Model<String>( getPersonModel().getObject().getDisplayname())));
        
    	PageHeaderPanel<Person> ph = new PageHeaderPanel<Person>("page-header", getPersonModel(), 
    			new Model<String>(getPersonModel().getObject().getDisplayname() ));
		ph.setBreadCrumb(bc);
		add(ph);

        add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));

		
		editor = new PersonEditor("personEditor", getPersonModel());
		add(editor);
		
		

		
		}
    

    
     


	@Override
	public void onDetach() {
	    super.onDetach();
	    
	    if (personModel!=null)
	    	personModel.detach();
	    
	}

	public IModel<Person> getPersonModel() {
		return personModel;
	}

	public void setPersonModel(IModel<Person> siteModel) {
		this.personModel = siteModel;
	}


	
	 

}
