package dellemuse.serverapp.page.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
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
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.ArtWorkPage;
import dellemuse.serverapp.page.site.InstitutionPage;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.odilon.util.Check;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import wktui.base.DummyBlockPanel;
import wktui.base.InvisiblePanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/person/${id}")
public class PersonPage extends ObjectPage<Person> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(PersonPage.class.getName());

	private PersonEditor editor;

	
	

	
	public PersonPage() {
		super();
		super.setEdit(true);
	}

	public PersonPage(PageParameters parameters) {
		super(parameters);
		super.setEdit(true);

	}

	public PersonPage(IModel<Person> model) {
		super(model);
		super.setEdit(true);

	}

	public PersonPage(IModel<Person> model, List<IModel<Person>> list) {
		super(model, list);
		super.setEdit(true);

	}

	/**
	 *  Institution Site Artwork Person 
	 *  Exhibition ExhibitionItem GuideContent User
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	@Override
	protected Optional<Person> getObject(Long id) {
		return getPerson(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String>(getModel().getObject().getDisplayName());
	}

	@Override
	protected void addHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/person/list", getLabel("persons")));
		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname())));

		if (getList() != null && getList().size() > 0) {
			Navigator<Person> nav = new Navigator<Person>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new PersonPage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}
		JumboPageHeaderPanel<Person> ph = new JumboPageHeaderPanel<Person>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);

		if (getList() != null && getList().size() > 0) {
			Navigator<Person> nav = new Navigator<Person>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new PersonPage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}

		add(ph);

	}
	
	protected Panel getEditor(String id) {
		if (this.editor==null)
			this.editor = new PersonEditor(id, getModel());
		return this.editor;
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<Person> model, List<IModel<Person>> list) {
		return new PersonPage(model, list);
	}

	@Override
	protected void onEdit(AjaxRequestTarget target) {
		editor.onEdit(target);
	}
	
	
	static final int PANEL_EDITOR = 0;
	static final int PANEL_AUDIT = 1;
	
	@Override
	protected List<ToolbarItem> getToolbarItems() {
		
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		AjaxButtonToolbarItem<Person> create = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				PersonPage.this.togglePanel(PANEL_EDITOR, target);
				PersonPage.this.onEdit(target);
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		create.setAlign(Align.TOP_RIGHT);
		list.add(create);
		
		
		AjaxButtonToolbarItem<Person> audit = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				PersonPage.this.togglePanel(PANEL_AUDIT, target);
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
		
		AbstractTab tab_2=new AbstractTab(Model.of("info")) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new DummyBlockPanel(panelId,getLabel("audit"));
			}
		};
		tabs.add(tab_2);
	
		return tabs;
	}
	

	//@Override
	//protected Panel getEditor() {
	//	return new InvisiblePanel("editor");
	//}

}
