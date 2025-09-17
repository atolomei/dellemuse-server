package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
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

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.ref.RefPersonModel;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.wktui.error.ErrorPanel;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.DummyBlockPanel;
import wktui.base.InvisiblePanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/site/floors/${id}")
public class SiteFloorsPage extends ObjectPage<Site> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteFloorsPage.class.getName());

	private Panel editor;
	 
	public SiteFloorsPage() {
		super();
	}

	public SiteFloorsPage(PageParameters parameters) {
		super(parameters);
	}

	public SiteFloorsPage(IModel<Site> model) {
		super(model);
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
  	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
 


	protected Panel getEditor(String id) {
		if (this.editor == null)
		//	this.editor = new SiteEditor("editor", getModel());
			this.editor=new ErrorPanel(id, Model.of("not done"));
			return this.editor;
	}
	
	@Override
	protected IRequestablePage getObjectPage(IModel<Site> iModel, List<IModel<Site>> list2) {
		return new SitePage(iModel, list2);
	}
	
	@Override
	protected void addHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(	new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(	new HREFBCElement("/site/" + getModel().getObject().getId().toString(),
						new Model<String>(getModel().getObject().getDisplayname())));

		bc.addElement(new BCElement(new Model<String>("floors")));
		JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);
		
		if (getModel().getObject().getPhoto() != null)
			ph.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));

		
		add(ph);
	}
	
	@Override
	protected Optional<Site> getObject(Long id) {
		return getSite(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return getLabel("floors");
	}
	
	
  

	@Override
	protected void onEdit(AjaxRequestTarget target) {
		logger.error("not done");
		//editor.onEdit(target);
	}
	
	@Override
	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findByIdWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<Site>(o_i.get()));
		}
	}
	
	
	static final int PANEL_EDITOR = 0;
	static final int PANEL_AUDIT = 1;

	protected List<ToolbarItem> getToolbarItems() {
		
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		
		
		AjaxButtonToolbarItem<User> create = new AjaxButtonToolbarItem<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				SiteFloorsPage.this.togglePanel(PANEL_EDITOR, target);
				SiteFloorsPage.this.onEdit(target);
			}
		};
		create.setAlign(Align.TOP_LEFT);

			

		AjaxButtonToolbarItem<Person> audit = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				SiteFloorsPage.this.togglePanel(PANEL_AUDIT, target);
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("audit");
			}
		};
		audit.setAlign(Align.TOP_RIGHT);
		list.add(audit);
		
		list.add(new SiteNavDropDownMenuToolbarItem("item", getModel(), Model.of(getModel().getObject().getShortName()), Align.TOP_RIGHT ));
	
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

	
}
