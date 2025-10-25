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
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.artexhibition.ArtExhibitionItemPage;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.MultiLanguageObjectPage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.PersonPage;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionItemRecord;
import dellemuse.serverapp.serverdb.model.record.SiteRecord;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import wktui.base.DummyBlockPanel;
import wktui.base.INamedTab;
import wktui.base.InvisiblePanel;
import wktui.base.NamedTab;

/**
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/site/info/${id}")
public class SiteInfoPage extends MultiLanguageObjectPage<Site, SiteRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoPage.class.getName());

	private SiteInfoEditor editor;

	
	@Override
	protected Optional<SiteRecord> loadTranslationRecord(String lang) {
		return getSiteRecordDBService().findBySite(getModel().getObject(), lang);
	}
	
	@Override
	protected SiteRecord createTranslationRecord(String lang) {
		return getSiteRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}
	
	
	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				logger.debug(event.toString());

				if (event.getName().equals(ServerAppConstant.action_site_edit)) {
					SiteInfoPage.this.onEdit(event.getTarget());
				}
			
				else if (event.getName().equals(ServerAppConstant.site_info)) {
					SiteInfoPage.this.togglePanel(ServerAppConstant.site_info, event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					SiteInfoPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
				}
			
				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					SiteInfoPage.this.togglePanel(event.getName(), event.getTarget());
				}
			
				else if (event.getName().equals(ServerAppConstant.site_audit)) {
					SiteInfoPage.this.togglePanel(ServerAppConstant.site_audit, event.getTarget());
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof MenuAjaxEvent)
					return true;
				return false;
			}
		});

	
		add(new io.wktui.event.WicketEventListener<SimpleWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleWicketEvent event) {
				if (event.getName().equals(ServerAppConstant.action_site_home)) {
					setResponsePage( new SitePage( getModel(), getList()));
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof SimpleWicketEvent)
					return true;
				return false;
			}
		});
	
	
	
	
	
	}
	public SiteInfoPage() {
		super();
	}

	public SiteInfoPage(PageParameters parameters) {
		super(parameters);
	}

	public SiteInfoPage(IModel<Site> model) {
		super(model);
	}

	@Override
	protected Optional<Site> getObject(Long id) {
		return getSite(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return getLabel("info");
	}

	

 
	
	@Override
	protected void addHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getModel().getObject().getId().toString(),
				new Model<String>(getModel().getObject().getDisplayname())));
		bc.addElement(new BCElement(getLabel("general-info")));
		JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);

		ph.setContext(getLabel("site"));

		if (getModel().getObject().getSubtitle()!=null)
			ph.setTagline( Model.of( getModel().getObject().getSubtitle()));
		
		if (getModel().getObject().getPhoto() != null)
			ph.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));

		add(ph);
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<Site> iModel, List<IModel<Site>> list2) {
		return new SitePage(iModel, list2);
	}

	protected Panel getEditor(String id) {
		if (this.editor == null)
			this.editor = new SiteInfoEditor(id, getModel());
		return this.editor;
	}

 

	protected List<ToolbarItem> getToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
	/**
		AjaxButtonToolbarItem<Site> create = new AjaxButtonToolbarItem<Site>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				SiteInfoPage.this.togglePanel(PANEL_EDITOR, target);
				SiteInfoPage.this.onEdit(target);
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		
		create.setAlign(Align.TOP_LEFT);
		list.add(create);

		

		AjaxButtonToolbarItem<Person> audit = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				SiteInfoPage.this.togglePanel(PANEL_AUDIT, target);
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("audit");
			}
		};
		audit.setAlign(Align.TOP_RIGHT);
		list.add(audit);
		*/
		
		
		
		
		list.add( new SiteInfoNavDropDownMenuToolbarItem("item", getModel(),  Align.TOP_RIGHT ));
		list.add(new SiteNavDropDownMenuToolbarItem("item", getModel(),  Align.TOP_RIGHT ));
		
		return list;
	}
	

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();
		
		NamedTab tab_1=new NamedTab(Model.of("editor"), ServerAppConstant.site_info) {
		 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);
		
		NamedTab tab_2=new NamedTab(Model.of("audit"), ServerAppConstant.site_audit) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new DummyBlockPanel(panelId,getLabel("audit"));
			}
		};
		tabs.add(tab_2);
	
		super.setStartTab(ServerAppConstant.site_info);
		return tabs;
	}

	
	@Override
	protected void onEdit(AjaxRequestTarget target) {
		this.editor.onEdit(target);

	}

	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<Site>(o_i.get()));
		}
	}

	/**
	 * Institution Site Artwork Person Exhibition ExhibitionItem GuideContent User
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
 

}
