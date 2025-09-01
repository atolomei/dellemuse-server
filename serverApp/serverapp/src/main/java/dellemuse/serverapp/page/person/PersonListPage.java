package dellemuse.serverapp.page.person;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Site;
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
import dellemuse.model.GuideContentModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;


/**
 * 
 * site 
 * foto 
 * Info - exhibitions
 * 
 */

@MountPath("/person/list")
public class PersonListPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(PersonListPage.class.getName());

	private StringValue stringValue;
	List<IModel<Person>> list;
	  
	public  PersonListPage() {
		super();
	}		
	
	public  PersonListPage(PageParameters parameters) {
		 super(parameters);
	 }
	 	
	
	public Iterable<Person> getPersons() {
		
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		return service.findAllSorted();
		
	}
	
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
        BreadCrumb<Void> bc = createBreadCrumb();
        bc.addElement(new BCElement( getLabel("persons")));
        
    	PageHeaderPanel<Void> ph = new PageHeaderPanel<Void>("page-header", null, getLabel("persons"));
		ph.setBreadCrumb(bc);
		add(ph);
	 	add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));

	       
		 list = new ArrayList<IModel<Person>>();

		getPersons().forEach(s -> list.add(new ObjectModel<Person>(s)));
        
        ListPanel<Person> panel = new ListPanel<>("contents", list) {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected Panel getListItemPanel(IModel<Person> model, ListPanelMode mode) {

                ObjectListItemPanel<Person> panel = new ObjectListItemPanel<>("row-element", model, mode) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                    	setResponsePage( new PersonPage(getModel() ));
                    }
                    
                    protected IModel<String> getInfo() {
 
                    	StringBuilder str  = new StringBuilder();	
                    	if (model.getObject().getEmail()!=null)
                    		str.append( (str.length()>0?", ":"") +  "email:" + model.getObject().getEmail() );
                    
                    	if (model.getObject().getPhone()!=null)
                    		str.append( (str.length()>0?", ":"") + "phone: " + model.getObject().getPhone() );
                    	
                     	if (model.getObject().getUser()!=null)
                    		str.append( (str.length()>0?", ":"") + "user: " + model.getObject().getUser().getUsername() );
           
                    	// String str = TextCleaner.clean(model.getObject().getInfo(), 280);
                        return new Model<String>(str.toString());
                    }
                    
                    protected IModel<String> getObjectTitle() {
                        return new Model<String>( getModel().getObject().getLastFirstname());
                    }
                    
                };
                return panel;
            }
            
            protected void onClick(IModel<Person> model) {
               	setResponsePage( new PersonPage(model));
            }
            
            @Override
            public IModel<String> getItemLabel(IModel<Person> model) {
                return new Model<String>(model.getObject().getDisplayname());
            }
            
            @Override
            protected List<IModel<Person>> filter(List<IModel<Person>> initialList, String filter) {
            	
            	List<IModel<Person>> list = new ArrayList<IModel<Person>>();

            	final String str = filter.trim().toLowerCase();

            	initialList.forEach(
                		s -> {
                if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
                	 list.add(s);
                	}
               }
               );
               return list; 
            }
        };
        
        panel.setTitle(getLabel("list"));
        panel.setListPanelMode(ListPanelMode.TITLE);
        add(panel);
	}
    
    @Override
	public void onDetach() {
	    super.onDetach();
	    
	    if (list!=null)
	    	list.forEach(i->i.detach());
    }
	
	 

}
