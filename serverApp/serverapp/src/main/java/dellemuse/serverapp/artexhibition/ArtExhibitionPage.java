package dellemuse.serverapp.artexhibition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import dellemuse.serverapp.artexhibitionguide.ArtExhibitionGuidePage;
import dellemuse.serverapp.artexhibitionitem.ArtExhibitionItemPage;
import dellemuse.serverapp.artexhibitionitem.ArtExhibitionItemsPanel;
import dellemuse.serverapp.artwork.ArtWorkPage;
import dellemuse.serverapp.editor.ObjectRecordEditor;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.MultiLanguageObjectPage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectWithDepModel;
import dellemuse.serverapp.page.person.PersonEditor;
import dellemuse.serverapp.page.person.PersonPage;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.page.site.SiteInfoPage;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.page.user.UserEditor;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
import dellemuse.serverapp.serverdb.model.record.ArtWorkRecord;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
 
import wktui.base.DummyBlockPanel;
import wktui.base.INamedTab;
import wktui.base.NamedTab;

/**
 * site foto Info - exhibitions
 * alter table artexhibition alter column fromdate drop not null;
ALTER TABLE
dellemuse=# alter table artexhibition alter column todate drop not null;
 * 
 */

@MountPath("/artexhibition/${id}")
public class ArtExhibitionPage extends  MultiLanguageObjectPage<ArtExhibition, ArtExhibitionRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionPage.class.getName());

	private IModel<Site> siteModel;
	private ArtExhibitionEditor editor;
	private ArtExhibitionItemsPanel items;
	private ArtExhibitionGuidesPanel guides = null;
	private ArtExhibitionSectionsPanel sections = null;
	
	private List<ToolbarItem> list;
	
	private JumboPageHeaderPanel<ArtExhibition> header;
 
	
	protected Optional<ArtExhibitionRecord> loadTranslationRecord(String lang) {
		return getArtExhibitionRecordDBService().findByArtExhibition(getModel().getObject(), lang);
	}

	
	protected ArtExhibitionRecord createTranslationRecord(String lang) {
		return getArtExhibitionRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}
	
	@Override
	protected boolean isOpensVisible() {
		return true;
	}

	@Override
	protected boolean isIntroVisible() {
		return true;
	}
	
	protected boolean isAudioVisible() {
		return false;
	}
	
	 
	
	public ArtExhibitionPage() {
		super();
	}

	public ArtExhibitionPage(PageParameters parameters) {
		super(parameters);
	}

	public ArtExhibitionPage(IModel<ArtExhibition> model) {
		this(model, null);
	}
	
	public ArtExhibitionPage(IModel<ArtExhibition> model, List<IModel<ArtExhibition>> list) {
		super( model, list);
	}

	@Override
	protected void onEdit(AjaxRequestTarget target) {
		editor.onEdit(target);
	}

	protected void onGuideCreate(AjaxRequestTarget target) {
		guides.onGuideCreate(target);
	}

	protected void onItemsEdit(AjaxRequestTarget target) {
		items.onEdit(target);
	}
	
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				logger.debug(event.toString());

				// action
				
				if (event.getName().equals(ServerAppConstant.action_exhibition_info_edit)) {
					ArtExhibitionPage.this.onEdit(event.getTarget());
				}
			

				if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					ArtExhibitionPage.this.getMetaEditor().onEdit(event.getTarget());
				}
				
				
				if (event.getName().equals(ServerAppConstant.action_exhibition_items_edit)) {
					ArtExhibitionPage.this.onItemsEdit(event.getTarget()); 
				}
				
				else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
					ArtExhibitionPage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
				}

				if (event.getName().equals(ServerAppConstant.action_exhibition_guide_create)) {
					ArtExhibitionPage.this.onGuideCreate(event.getTarget());
				}
				
				
				// panels --------------------------------------
				//
				//
				//
				else if (event.getName().equals(ServerAppConstant.exhibition_info)) {
					ArtExhibitionPage.this.togglePanel(ServerAppConstant.exhibition_info, event.getTarget());
					ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionPage.this.getHeader());
				}
				
				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					ArtExhibitionPage.this.togglePanel(event.getName(), event.getTarget());
				}
		
				else if (event.getName().equals(ServerAppConstant.exhibition_items)) {
					ArtExhibitionPage.this.togglePanel(ServerAppConstant.exhibition_items, event.getTarget());
					ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionPage.this.getHeader());
				}
				
				else if (event.getName().equals(ServerAppConstant.exhibition_sections)) {
					ArtExhibitionPage.this.togglePanel(ServerAppConstant.exhibition_sections, event.getTarget());
					ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionPage.this.getHeader());
				}
				
				
				else if (event.getName().equals(ServerAppConstant.exhibition_guides)) {
					ArtExhibitionPage.this.togglePanel(ServerAppConstant.exhibition_guides, event.getTarget());
					ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionPage.this.getHeader());
				}
				
				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					ArtExhibitionPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
					ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionPage.this.getHeader());
				}
				
				else if (event.getName().equals(ServerAppConstant.object_audit)) {
					ArtExhibitionPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
					ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionPage.this.getHeader());
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
					setResponsePage( new SitePage( getSiteModel(), null));
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

 	
	@Override
	protected List<ToolbarItem> getToolbarItems() {

		
		if (list!=null)
			return list;
		
		list = new ArrayList<ToolbarItem>();
	

		String name = null;
		
		if (getModel().getObject().getShortname()!=null)
			name=TextCleaner.truncate(getModel().getObject().getShortname(), 24);
		else
			name=TextCleaner.truncate(getModel().getObject().getName(), 24);
				
		list.add(new ArtExhibitionNavDropDownMenuToolbarItem("item", getModel(), 
				getLabel("art-exhibition", name), Align.TOP_RIGHT));
		
		// site
				SiteNavDropDownMenuToolbarItem site = new SiteNavDropDownMenuToolbarItem("item", getSiteModel(),  Align.TOP_RIGHT);
				site.add( new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-none d-md-block d-lg-block d-xl-block d-xxl-block text-md-center"));
				list.add(site);
		
		return list;
	}
	
	
	/**
	 * 
	 * 
	 */
	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();
		
		NamedTab tab_1=new NamedTab(Model.of("editor"), ServerAppConstant.exhibition_info) {
		 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);
		
 		
		NamedTab tab_3=new NamedTab(Model.of("items"), ServerAppConstant.exhibition_items) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getArtExhibitionItemsPanel(panelId);
			}
		};
		tabs.add(tab_3);
	
		
		NamedTab tab_4=new NamedTab(Model.of("guides"), ServerAppConstant.exhibition_guides) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getArtExhibitionGuidesPanel(panelId);
			}
		};
		tabs.add(tab_4);
		

		NamedTab tab_5=new NamedTab(Model.of("sections"), ServerAppConstant.exhibition_sections) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getArtExhibitionSectionsPanel(panelId);
			}
		};
		tabs.add(tab_5);
		 
		
		if (getStartTab()==null)
			setStartTab(ServerAppConstant.exhibition_info );
 
		return tabs;
	}
	
	/**
	 * @param panelId
	 * @return
	 */
	
	protected WebMarkupContainer getArtExhibitionGuidesPanel(String panelId) {
		if (guides==null)
			guides = new ArtExhibitionGuidesPanel(panelId, getModel());
		return guides;
	}

	protected WebMarkupContainer getArtExhibitionSectionsPanel(String panelId) {
		if (sections==null)
			sections = new ArtExhibitionSectionsPanel(panelId, getModel(), getSiteModel());
		return sections;
	}

	protected ArtExhibitionItemsPanel getArtExhibitionItemsPanel(String id) {
		if (items==null)
			items = new ArtExhibitionItemsPanel(id, getModel(), getSiteModel());
		return items;
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
	protected Panel createHeaderPanel() {
  
		
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement( new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement( new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
					   new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new HREFBCElement("/site/exhibitions/" + getSiteModel().getObject().getId().toString(), getLabel("exhibitions")));
		
		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname())));

		header = new JumboPageHeaderPanel<ArtExhibition>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));
		
		header.setContext(getLabel("artexhibition"));
		
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
			header.setPhotoModel(new ObjectModel<Resource>( getModel().getObject().getPhoto()));
		
		if (getModel().getObject().getSubtitle()!=null)
			header.setTagline(Model.of(getModel().getObject().getSubtitle()));
		
		header.setBreadCrumb(bc);
		return header;
		
		
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<ArtExhibition> model, List<IModel<ArtExhibition>> list) {
	 	return new ArtExhibitionPage(model, list);
	}
	
	@Override
	protected void setUpModel() {
		super.setUpModel();
		
		if (!getModel().getObject().isDependencies()) {
			Optional<ArtExhibition> o_i = getArtExhibitionDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectWithDepModel<ArtExhibition>(o_i.get()));
		}
		
		setSiteModel(new ObjectModel<Site>( getModel().getObject().getSite() ));

		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findWithDeps(getSiteModel().getObject().getId());
			setSiteModel(new ObjectWithDepModel<Site>(o_i.get()));
		}
	}
	
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
		if (this.items==null)
			this.items=new ArtExhibitionItemsPanel(id, getModel(), getSiteModel());
		return this.items;
	}
	
	protected Panel getEditor(String id) {
		if (this.editor==null)
			this.editor = new ArtExhibitionEditor(id, getModel());
		return this.editor;
	}
	
	private JumboPageHeaderPanel<?> getHeader() {
		return header;
	}
		
	
}
