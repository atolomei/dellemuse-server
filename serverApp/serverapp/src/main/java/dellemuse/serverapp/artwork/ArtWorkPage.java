package dellemuse.serverapp.artwork;

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


import dellemuse.model.logging.Logger;
import dellemuse.model.ref.RefPersonModel;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.artexhibition.ArtExhibitionNavDropDownMenuToolbarItem;
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.editor.ObjectMetaEditor;
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
import dellemuse.serverapp.page.person.PersonPage;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.page.user.UserEditor;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.record.ArtWorkRecord;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.odilon.util.Check;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
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
import wktui.base.INamedTab;
import wktui.base.InvisiblePanel;
import wktui.base.NamedTab;

/**
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/artwork/${id}")
public class ArtWorkPage extends  MultiLanguageObjectPage<ArtWork, ArtWorkRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtWorkPage.class.getName());

	private IModel<Site> siteModel;

	private ArtWorkMainPanel editor;

	private Map<String, Panel> recordEditors = new HashMap<String, Panel>();
	
	//private ObjectMetaEditor<ArtWork> metaEditor;


	public ArtWorkPage() {
		super();
	}

	public ArtWorkPage(PageParameters parameters) {
		super(parameters);
	}

	public ArtWorkPage(IModel<ArtWork> model) {
		this(model, null);
	}
	
	public ArtWorkPage(IModel<ArtWork> model, List<IModel<ArtWork>> list) {
		super( model, list);
	}
	
	@Override
	protected void onEdit(AjaxRequestTarget target) {
		editor.onEdit(target);
	}

	protected void onEditMeta(AjaxRequestTarget target) {
		getMetaEditor().onEdit(target);
	}
	
	
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				
				logger.debug(event.toString());

				/**	
				if (event.getName().equals(ServerAppConstant.action_site_edit)) {
					ArtWorkPage.this.onEdit(event.getTarget());
				}
			
				else if (event.getName().equals(ServerAppConstant.site_info)) {
					ArtWorkPage.this.togglePanel(ServerAppConstant.site_info, event.getTarget());
				}
			
				if (event.getName().equals(ServerAppConstant.audit)) {
					ArtWorkPage.this.togglePanel(ServerAppConstant.audit, event.getTarget());
				}
				**/
				
				if (event.getName().equals(ServerAppConstant.artwork_audit)) {
					ArtWorkPage.this.togglePanel(ServerAppConstant.artwork_audit, event.getTarget());
				}
				
				// edit --------------------------------------
				//
				//
				//
				if (event.getName().equals(ServerAppConstant.action_artwork_edit_info)) {
					ArtWorkPage.this.onEdit(event.getTarget());
				}
				
				else if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					ArtWorkPage.this.onEditMeta(event.getTarget());
				}
	
				
				// panels --------------------------------------
				//
				//
				//
				else if (event.getName().equals(ServerAppConstant.artwork_info)) {
					ArtWorkPage.this.togglePanel(ServerAppConstant.artwork_info, event.getTarget());
					// ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					// event.getTarget().add(ArtWorkPage.this.getHeader());
				}
				else if (event.getName().startsWith(ServerAppConstant.artworkrecord_info)) {
					ArtWorkPage.this.togglePanel(event.getName(), event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.artwork_meta)) {
					ArtWorkPage.this.togglePanel(ServerAppConstant.artwork_meta, event.getTarget());
					// ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					// event.getTarget().add(ArtWorkPage.this.getHeader());
				}
				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					ArtWorkPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
					// ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					// event.getTarget().add(ArtWorkPage.this.getHeader());
				}
				
				else if (event.getName().equals(ServerAppConstant.artwork_audit)) {
					ArtWorkPage.this.togglePanel(ServerAppConstant.artwork_audit, event.getTarget());
					// ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					// event.getTarget().add(ArtWorkPage.this.getHeader());
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
	protected Optional<ArtWork> getObject(Long id) {
		return getArtWork( id );
	}


	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String> (getModel().getObject().getName());
	}
	
	@Override
	protected List<ToolbarItem> getToolbarItems() {
	
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		String name=getModel().getObject().getName();
		
		list.add(new ArtWorkNavDropDownMenuToolbarItem("item", getModel(), getLabel("artwork-menu-title", TextCleaner.truncate(name, 32)), Align.TOP_RIGHT));
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Align.TOP_RIGHT));
		
		/**
		AjaxButtonToolbarItem<Void> edit = new AjaxButtonToolbarItem<Void>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				ArtWorkPage.this.togglePanel(PANEL_EDITOR, target);
				ArtWorkPage.this.onEdit(target);
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		edit.setAlign(Align.TOP_LEFT);
		
		list.add(edit);		
		
		**/
		
		
		return list;
	
	}
	
	
	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();
		
		//List<INamedTab> tabs = new ArrayList<INamedTab>();
		
		NamedTab tab_1=new NamedTab(Model.of("editor"), ServerAppConstant.artwork_info) {
		 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return ArtWorkPage.this.getEditor(panelId);
			}
		};
		tabs.add(tab_1);
		
		
		
		List<Language> list = getLanguageService().getLanguages();
		
		for ( Language la:list ) {
			
			if (!la.getLanguageCode().equals( getModel().getObject().getMasterLanguage()) ) {
				
				NamedTab tab=new NamedTab(Model.of(la.getLanguageCode()), ServerAppConstant.artworkrecord_info+"-"+la.getLanguageCode(), la.getLanguageCode()) {
					 
					private static final long serialVersionUID = 1L;
	
					@Override
					public WebMarkupContainer getPanel(String panelId) {
						return getArtWorkRecordEditor(panelId, getMoreInfo());
					}
				};
				tabs.add(tab);
			}
		}
		
		

		NamedTab tab_2=new NamedTab(Model.of("metainfo"), ServerAppConstant.artwork_meta) {
			 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getMetaEditor(panelId);
			}
		};
		tabs.add(tab_2);
		

		NamedTab tab_3=new NamedTab(Model.of("artwork-audit"), ServerAppConstant.artwork_audit) {
			 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new DummyBlockPanel(panelId,getLabel("artwork-audit"));
			}
		};
		tabs.add(tab_3);

		setStartTab( ServerAppConstant.artwork_info );


		
		
		 
	
		return tabs;
	}

	
	
	protected Panel getArtWorkRecordEditor(String id, String lang) {
		
		if (recordEditors.containsKey(lang))
			return recordEditors.get(lang);
		
		
		ArtWorkRecord ar = null;
		
		Optional<ArtWorkRecord> a = getArtWorkRecordDBService().findByArtWork(getModel().getObject(), lang);
		
		if (a.isEmpty()) {
			ar = getArtWorkRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
		}
		else
			ar=a.get();
				
	 	
		IModel<ArtWorkRecord> arm =new ObjectModel<ArtWorkRecord>(ar);
		ObjectRecordEditor<ArtWorkRecord, ArtWork> e = new ObjectRecordEditor<ArtWorkRecord, ArtWork>(id, arm, getModel(), getLabel("information"));
		
		recordEditors.put(lang, e);
		return e;
		
		
	//ArtWorkRecordEditor e = new ArtWorkRecordEditor(id, getModel(), arm);
		//DummyBlockPanel d=new DummyBlockPanel(id, Model.of(ar.toString()));
		//recordEditors.put(lang, d);
		//return d;
	
	}


	protected Panel getEditor(String id) {
		if (this.editor==null)
			editor = new ArtWorkMainPanel(id, getModel());
		return (editor);
	}
	
	@Override
	protected void addHeaderPanel() {
		
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
				new Model<String>(getSiteModel().getObject().getDisplayname())));
		bc.addElement(new HREFBCElement("/site/artwork/" + getSiteModel().getObject().getId().toString(),
				getLabel("artworks")));

		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname())));

		JumboPageHeaderPanel<ArtWork> ph = new JumboPageHeaderPanel<ArtWork>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));

		ph.setImageLinkCss("jumbo-img jumbo-lg mb-2 mb-lg-0 border bg-body-tertiary");
		
		
		if (getModel().getObject().getArtists()!=null) 
			ph.setTagline(Model.of(getArtistStr(getModel().getObject())));
		
		if (getModel().getObject().getPhoto()!=null)
			ph.setPhotoModel(new ObjectModel<Resource>( getModel().getObject().getPhoto()));
	
		if (getList()!=null && getList().size()>0) {
			Navigator<ArtWork> nav = new Navigator<ArtWork>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void navigate(int current) {
					setResponsePage( new ArtWorkPage( getList().get(current), getList() ));
				}
			};
			bc.setNavigator(nav);
		}
		
		ph.setContext(getLabel("artwork"));
		
		ph.setBreadCrumb(bc);
		add(ph);
	}


	@Override
	protected IRequestablePage getObjectPage(IModel<ArtWork> model, List<IModel<ArtWork>> list) {
	 	return new ArtWorkPage( model, list );
	}
	
	protected void setUpModel() {
		super.setUpModel();
		
		if (!getModel().getObject().isDependencies()) {
			Optional<ArtWork> o_i = getArtWorkDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<ArtWork>(o_i.get()));
		}
		
		setSiteModel(new ObjectModel<Site>( getModel().getObject().getSite() ));
		
		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findWithDeps(getSiteModel().getObject().getId());
			setSiteModel(new ObjectModel<Site>(o_i.get()));
		}
	}

	
	@Override
	public void onInitialize() {
		super.onInitialize();
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
		
		editor.detach();
		
	}
	 
	protected String getImageSrc(IModel<ArtWork> model) {

		try {
			if (getModel().getObject().getPhoto() == null)
				return null;
			ResourceThumbnailService ths = (ResourceThumbnailService) ServiceLocator.getInstance()
					.getBean(ResourceThumbnailService.class);
			return ths.getPresignedThumbnailUrl(getModel().getObject().getPhoto(), ThumbnailSize.MEDIUM);

		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}
 
 
 
	
}
