package dellemuse.serverapp.page.user;

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
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
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

@MountPath("/user/list")
public class UserListPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(UserListPage.class.getName());

	 
	  
	public  UserListPage() {
		super();
	}		
	
	public  UserListPage(PageParameters parameters) {
		 super(parameters);
	 }
	 	
	
	public Iterable<User> getUsers() {
		
		UserDBService service = (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
		return service.findAllSorted();
		
	}
	
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
        BreadCrumb<Void> bc = createBreadCrumb();
        bc.addElement(new BCElement( getLabel("users")));
        
    	PageHeaderPanel<Void> ph = new PageHeaderPanel<Void>("page-header", null, getLabel("users"));
		ph.setBreadCrumb(bc);
		add(ph);
	 	add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));

	       
		list = new ArrayList<IModel<User>>();

		getUsers().forEach(s -> list.add(new ObjectModel<User>(s)));
        
        ListPanel<User> panel = new ListPanel<>("contents", list) {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected Panel getListItemPanel(IModel<User> model, ListPanelMode mode) {

                ObjectListItemPanel<User> panel = new ObjectListItemPanel<>("row-element", model, mode) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                    	setResponsePage( new UserPage(getModel() ));
                    }
                    
                    protected IModel<String> getInfo() {
                    	StringBuilder str  = new StringBuilder();	
                         return new Model<String>(str.toString());
                    }
                    
                    protected IModel<String> getObjectTitle() {
                        return new Model<String>( getModel().getObject().getUsername());
                    }
                    
                };
                return panel;
            }
            
            protected void onClick(IModel<User> model) {
               	setResponsePage(new UserPage(model));
            }
            
            @Override
            public IModel<String> getItemLabel(IModel<User> model) {
                return new Model<String>(model.getObject().getDisplayname());
            }
            
            @Override
            protected List<IModel<User>> filter(List<IModel<User>> initialList, String filter) {
            	
            	List<IModel<User>> list = new ArrayList<IModel<User>>();

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
        
        panel.setTitle(getLabel("users"));
        panel.setListPanelMode(ListPanelMode.TITLE);
        add(panel);
	}
    
	
	List<IModel<User>> list;
	
    @Override
	public void onDetach() {
	    super.onDetach();
	    
	    if (list!=null)
	    	list.forEach(i->i.detach());
	    
	}
	
	 

}
