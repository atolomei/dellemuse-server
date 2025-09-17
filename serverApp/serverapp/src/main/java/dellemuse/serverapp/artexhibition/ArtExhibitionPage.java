package dellemuse.serverapp.artexhibition;

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
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.PersonEditor;
import dellemuse.serverapp.page.person.PersonPage;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.user.UserEditor;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
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
import io.odilon.util.Check;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.UIEvent;
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
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/artexhibition/${id}")
public class ArtExhibitionPage extends ObjectPage<ArtExhibition> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionPage.class.getName());

	private IModel<Site> siteModel;
	private ArtExhibitionEditor editor;
	private ArtExhibitionItemsPanel itemsPanel;

	
	@Override
	protected void onEdit(AjaxRequestTarget target) {
		editor.onEdit(target);
	}

	
	static final int PANEL_EDITOR=0;
	static final int PANEL_ITEMS=1;
	
	
	@Override
	protected void addListeners() {
		super.addListeners();
		
		
		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				logger.debug(event.toString());
				//refresh(event.getRequestTarget());
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof SimpleAjaxWicketEvent)
					return true;
				return false;
			}
		});
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<ArtExhibition> edit = new AjaxButtonToolbarItem<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				ArtExhibitionPage.this.togglePanel(PANEL_EDITOR, target);
				ArtExhibitionPage.this.onEdit(target);
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		edit.setAlign(Align.TOP_LEFT);
		list.add(edit);
		
		


		AjaxButtonToolbarItem<ArtExhibition> items = new AjaxButtonToolbarItem<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				ArtExhibitionPage.this.togglePanel(PANEL_ITEMS, target);
				ArtExhibitionPage.this.onEdit(target);
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("artworks");
			}
		};
		items.setAlign(Align.TOP_LEFT);
		list.add(items);
		
		
		list.add(new ArtExhibitionNavDropDownMenuToolbarItem("item", getModel(), Model.of(TextCleaner.truncate(getModel().getObject().getName(), 24)), Align.TOP_RIGHT));
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Model.of(getSiteModel().getObject().getShortName()), Align.TOP_RIGHT ));
		
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
		
		AbstractTab tab_2=new AbstractTab(Model.of("artworks")) {
			 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return  getItemsPanel(panelId);
			}
		};
		
		tabs.add(tab_2);
	
		return tabs;
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	public ArtExhibitionPage() {
		super();
		super.setEdit(true);
	}

	public ArtExhibitionPage(PageParameters parameters) {
		super(parameters);
		super.setEdit(true);
	}

	public ArtExhibitionPage(IModel<ArtExhibition> model) {
		this(model, null);
		super.setEdit(true);
	}
	
	public ArtExhibitionPage(IModel<ArtExhibition> model, List<IModel<ArtExhibition>> list) {
		super( model, list);
		super.setEdit(true);
	}
	
	@Override
	protected Optional<ArtExhibition> getObject(Long id) {
		return getArtExhibition( id );
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String> (getModel().getObject().getName());
	}

	@Override
	protected void addHeaderPanel() {
		
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
				new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname())));

		JumboPageHeaderPanel<ArtExhibition> ph = new JumboPageHeaderPanel<ArtExhibition>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));
		
		
		if (getList()!=null && getList().size()>0) {
			Navigator<ArtExhibition> nav = new Navigator<ArtExhibition>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void navigate(int current) {
					setResponsePage( new ArtExhibitionPage( getList().get(current), getList() ));
				}
			};
			bc.setNavigator(nav);
		}
		
		if (getModel().getObject().getPhoto()!=null)
			ph.setPhotoModel(new ObjectModel<Resource>( getModel().getObject().getPhoto()));
		
		if (getModel().getObject().getSubtitle()!=null)
			ph.setTagline(Model.of(getModel().getObject().getSubtitle()));
		
		ph.setBreadCrumb(bc);
		add(ph);
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<ArtExhibition> model, List<IModel<ArtExhibition>> list) {
	 	return new ArtExhibitionPage(model, list);
	}
	
	@Override
	protected void setUpModel() {
		super.setUpModel();
		
		if (!getModel().getObject().isDependencies()) {
			Optional<ArtExhibition> o_i = getArtExhibitionDBService().findByIdWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<ArtExhibition>(o_i.get()));
		}
		
		setSiteModel(new ObjectModel<Site>( getModel().getObject().getSite() ));

		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findByIdWithDeps(getSiteModel().getObject().getId());
			setSiteModel(new ObjectModel<Site>(o_i.get()));
		}
	}

	//protected Panel getEditor() {
	//	editor = new ArtExhibitionEditor("editor", getModel());
	//	return (editor);
	//}
	
	
	
	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();
	}

	
 
	protected Panel getItemsPanel(String id) {
		if (this.itemsPanel==null)
			this.itemsPanel=new ArtExhibitionItemsPanel(id, getModel());
		return this.itemsPanel;
	}
	
	protected Panel getEditor(String id) {
		if (this.editor==null)
			this.editor = new ArtExhibitionEditor(id, getModel());
		return this.editor;
	}
}
