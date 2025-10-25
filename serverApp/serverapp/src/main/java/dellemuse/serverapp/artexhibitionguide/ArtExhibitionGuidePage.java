package dellemuse.serverapp.artexhibitionguide;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artwork.ArtWorkPage;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.MultiLanguageObjectPage;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionItemRecord;
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

@MountPath("/guide/${id}")
public class ArtExhibitionGuidePage extends MultiLanguageObjectPage<ArtExhibitionGuide, ArtExhibitionGuideRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionGuidePage.class.getName());

	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;
	 
	private ArtExhibitionGuideContentsPanel guideContents;
	private JumboPageHeaderPanel<ArtExhibitionGuide> header;
	private ArtExhibitionGuideEditor editor;
	

	protected Optional< ArtExhibitionGuideRecord> loadTranslationRecord(String lang) {
		return getArtExhibitionGuideRecordDBService().findByArtExhibitionGuide(getModel().getObject(), lang);
	}
	
	protected  ArtExhibitionGuideRecord createTranslationRecord(String lang) {
		return getArtExhibitionGuideRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}
	
	
	
	public ArtExhibitionGuidePage() {
		super();
	}

	public ArtExhibitionGuidePage(PageParameters parameters) {
		super(parameters);
	}

	public ArtExhibitionGuidePage(IModel<ArtExhibitionGuide> model) {
		this(model, null);
	}
	
	public ArtExhibitionGuidePage(IModel<ArtExhibitionGuide> model, List<IModel<ArtExhibitionGuide>> list) {
		super( model, list);
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				
				logger.debug(event.toString());

				// action -----------
				
				if (event.getName().equals(ServerAppConstant.action_guide_edit_info)) {
					ArtExhibitionGuidePage.this.onEdit(event.getTarget());
				}
				
				// panels  -----------
				
				else if (event.getName().equals(ServerAppConstant.artexhibitionguide_info)) {
					ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.artexhibitionguide_info, event.getTarget());
					ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionGuidePage.this.getHeader());
				}

				else if (event.getName().equals(ServerAppConstant.artexhibitionguide_contents)) {
					ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.artexhibitionguide_contents, event.getTarget());
					ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionGuidePage.this.getHeader());
				}
				
				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
					ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionGuidePage.this.getHeader());
				}
				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					ArtExhibitionGuidePage.this.togglePanel(event.getName(), event.getTarget());
				}
				
				else if (event.getName().equals(ServerAppConstant.artexhibitionguide_audit)) {
					ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.site_audit, event.getTarget());
					ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionGuidePage.this.getHeader());
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
					setResponsePage(new SitePage(getSiteModel(), null));
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
	protected void onEdit(AjaxRequestTarget target) {
		this.editor.onEdit(target);
	}

	protected void onGuideCreate(AjaxRequestTarget target) { 
	}
 	
	
	@Override
	protected List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		list.add(new ArtExhibitionGuideNavDropDownMenuToolbarItem("item", getModel(), 
				getLabel("audio-guide",TextCleaner.truncate(getModel().getObject().getName(), 24)), Align.TOP_RIGHT));
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Model.of(getSiteModel().getObject().getShortName()), Align.TOP_RIGHT));
		return list;
	}
	
	protected WebMarkupContainer getArtExhibitionGuideEditor(String id) {
		if (this.editor==null)
			this.editor = new ArtExhibitionGuideEditor(id, getModel(), getSiteModel());
		return this.editor;
	}

	protected WebMarkupContainer getGuideContentsPanel(String id) {
		if (this.guideContents==null)
			guideContents = new ArtExhibitionGuideContentsPanel(id, getModel(), getSiteModel());
		return guideContents;
	}
	
	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();
		
		NamedTab tab_1=new NamedTab(Model.of("editor"), ServerAppConstant.artexhibitionguide_info) {
		 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getArtExhibitionGuideEditor(panelId);
			}
		};
		tabs.add(tab_1);
		

		NamedTab tab_2=new NamedTab(Model.of("contents"), ServerAppConstant.artexhibitionguide_contents) {
			 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getGuideContentsPanel(panelId);
			}
		};
		tabs.add(tab_2);

		
		
		NamedTab tab_3=new NamedTab(Model.of("audit"), ServerAppConstant.artexhibitionguide_audit) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new DummyBlockPanel(panelId,getLabel("audit"));
			}
		};
		tabs.add(tab_3);
		
		
		setStartTab( ServerAppConstant.artexhibitionguide_info );
		
		return tabs;
	}
	
	
	 
	
	@Override
	protected Optional<ArtExhibitionGuide> getObject(Long id) {
		return super.getArtExhibitionGuide(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String> (getModel().getObject().getName());
	}
	
	@Override
	protected void addHeaderPanel() {
		
		BreadCrumb<Void> bc = createBreadCrumb();
	
		bc.addElement( new HREFBCElement("/site/list", getLabel("sites")));

		bc.addElement( new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
					   new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new HREFBCElement("/site/exhibitions/" + getSiteModel().getObject().getId().toString(), getLabel("exhibitions")));
		
		bc.addElement(new HREFBCElement("/artexhibition/" + getArtExhibitionModel().getObject().getId().toString(),  
				Model.of( getArtExhibitionModel().getObject().getDisplayname() + " (E)")));

		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname()  + " (AG)" )));

		this.header = new JumboPageHeaderPanel<ArtExhibitionGuide>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));
		
		this.header.setContext(getLabel("exhibition-guide"));
		
		this.header.add( new org.apache.wicket.AttributeModifier("class", "row mt-0 mb-0 text-center imgReduced"));
		
		
		if (getList()!=null && getList().size()>0) {
			Navigator<ArtExhibitionGuide> nav = new Navigator<ArtExhibitionGuide>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void navigate(int current) {
					setResponsePage( new ArtExhibitionGuidePage( getList().get(current), getList() ));
				}
			};
			bc.setNavigator(nav);
		}
		
		if (getModel().getObject().getPhoto()!=null) 
			header.setPhotoModel(new ObjectModel<Resource>( getModel().getObject().getPhoto()));
		else  if (getArtExhibitionModel().getObject().getPhoto()!=null)
			header.setPhotoModel(new ObjectModel<Resource>( getArtExhibitionModel().getObject().getPhoto()));
			
		if (getModel().getObject().getSubtitle()!=null)
			header.setTagline(Model.of(getModel().getObject().getSubtitle()));
		else  if (getArtExhibitionModel().getObject().getSubtitle()!=null)
			header.setTagline(Model.of(getArtExhibitionModel().getObject().getSubtitle()));
			
		this.header.setBreadCrumb(bc);
		add(this.header);
	}
 
	public IModel<Site> getSiteModel() {
		return this.siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.siteModel != null)
			this.siteModel.detach();
	}
	@Override
	protected IRequestablePage getObjectPage(IModel<ArtExhibitionGuide> model, List<IModel<ArtExhibitionGuide>> list) {
	 	return new ArtExhibitionGuidePage(model, list);
	}
	
	@Override
	protected void setUpModel() {
		super.setUpModel();
		
		if (!getModel().getObject().isDependencies()) {
			Optional<ArtExhibitionGuide> o_i = getArtExhibitionGuideDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<ArtExhibitionGuide>(o_i.get()));
		}
		
		Optional<ArtExhibition> o_i = getArtExhibitionDBService().findWithDeps(getModel().getObject().getArtExhibition().getId());
		seArtExhibitionModel(new ObjectModel<ArtExhibition>(o_i.get()));
		
		Optional<Site> o_s = getSiteDBService().findWithDeps(o_i.get().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_s.get()));

	}
	
	
	protected void seArtExhibitionModel(IModel<ArtExhibition> model) {
		this.artExhibitionModel=model;
		
	}
	
	protected IModel<ArtExhibition> getArtExhibitionModel() {
		return this.artExhibitionModel;
	}
	private JumboPageHeaderPanel<?> getHeader() {
		return this.header;
	}

 
}
