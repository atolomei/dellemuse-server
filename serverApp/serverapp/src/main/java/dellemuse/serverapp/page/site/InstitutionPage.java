package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.PersonPage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
 
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.DummyBlockPanel;
import wktui.base.InvisiblePanel;

/**
 * site foto Info - exhibitions
 */

@MountPath("/institution/${id}")
public class InstitutionPage extends ObjectPage<Institution> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionPage.class.getName());

	private Image image;
	private WebMarkupContainer imageContainer;
	private Link<Resource> imageLink;
	
	 
	
	public InstitutionPage() {
		super();
	}

	public InstitutionPage(PageParameters parameters) {
		super(parameters);
		super.setEdit(true);
	}

	public InstitutionPage(IModel<Institution> model ) {
		 this( model, null);
	}
	
	public InstitutionPage(IModel<Institution> model, List<IModel<Institution>> list) {
		super( model, list);
		super.setEdit(true);

	}
	
	static final int PANEL_EDITOR = 0;
	static final int PANEL_AUDIT = 1;
	@Override
	protected List<ToolbarItem> getToolbarItems() {
	
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		
		AjaxButtonToolbarItem<Void> edit = new AjaxButtonToolbarItem<Void>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				InstitutionPage.this.togglePanel(PANEL_EDITOR, target);
				InstitutionPage.this.onEdit(target);
			}
			
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		}; 
		edit.setAlign(Align.TOP_LEFT);

		list.add(edit);
		
		
		

		AjaxButtonToolbarItem<Person> audit = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				InstitutionPage.this.togglePanel(PANEL_AUDIT, target);
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("audit");
			}
		};
		audit.setAlign(Align.TOP_RIGHT);
		list.add(audit);
		
		
		
		
		
		
		return list;
		
	}
	
	
	
	
	@Override
	protected List<ITab> getInternalPanels() {
		
		List<ITab> tabs = new ArrayList<ITab>();
		
		AbstractTab tab_1=new AbstractTab(Model.of("editor")) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);
		
		AbstractTab tab_2=new AbstractTab(Model.of("audit")) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new DummyBlockPanel(panelId,getLabel("audit"));
			}
		};
		tabs.add(tab_2);
	
		return tabs;
	}

	
	
	@Override
	protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/institution/list", getLabel("institutions")));
		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname())));
		
		
		if (getList()!=null && getList().size()>0) {
			Navigator<Institution> nav = new Navigator<Institution>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void navigate(int current) {
					setResponsePage( new InstitutionPage( getList().get(current), getList() ));
				}
			};
			bc.setNavigator(nav);
		}
		
		JumboPageHeaderPanel<Institution> ph = new JumboPageHeaderPanel<Institution>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));
		
		if (getModel().getObject().getSubtitle()!=null)
			ph.setTagline( Model.of( getModel().getObject().getSubtitle()));
	
		if (getModel().getObject().getPhoto()!=null)
			ph.setPhotoModel(new ObjectModel<Resource>( getModel().getObject().getPhoto()));
	
		
		ph.setBreadCrumb(bc);
		
		
		add(ph);
		
		
	}
	
	@Override
	protected IRequestablePage getObjectPage(IModel<Institution> model, List<IModel<Institution>> list) {
	 	return new InstitutionPage( model, list );
	}
	
	
	protected void setUpModel() {
			super.setUpModel();
		
			if (!getModel().getObject().isDependencies()) {
				Optional<Institution> o_i = getInstitutionDBService().findByIdWithDeps(getModel().getObject().getId());
				setModel( new ObjectModel<Institution>(o_i.get()));
			}
		
	}
	
	InstitutionMainPanel editor;
	
	protected Panel getEditor(String id) {
		if (editor==null)
			editor = new InstitutionMainPanel(id, getModel());
		return editor;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	protected Optional<Institution> getObject(Long id) {
		return getInstitution(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return getLabel("institution");
	}

 

	@Override
	protected void onEdit(AjaxRequestTarget target) {
		editor.getInstitutionEditor().edit( target ); 
		
	}

	
}
