package dellemuse.serverapp.artexhibitionitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import dellemuse.serverapp.artexhibition.ArtExhibitionEXTNavDropDownMenuToolbarItem;
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.artexhibitionguide.ArtExhibitionGuidePage;
import dellemuse.serverapp.artexhibitionsection.ArtExhibitionSectionPage;
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
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.page.user.UserEditor;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionItemRecord;
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

@MountPath("/artexhibitionitem/${id}")
public class ArtExhibitionItemPage extends MultiLanguageObjectPage<ArtExhibitionItem, ArtExhibitionItemRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionItemPage.class.getName());

	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;
	private IModel<ArtWork> artWorkModel;

	private Link<ArtExhibitionItem> imageLink;
	private Image image;
	private WebMarkupContainer imageContainer;

	private ArtExhibitionItemEditor editor;
	private JumboPageHeaderPanel<ArtExhibitionItem> header;
	private List<ToolbarItem> list;
	
	
	protected Optional<ArtExhibitionItemRecord> loadTranslationRecord(String lang) {
		return getArtExhibitionItemRecordDBService().findByArtExhibitionItem(getModel().getObject(), lang);
	}
	
	protected ArtExhibitionItemRecord createTranslationRecord(String lang) {
		return getArtExhibitionItemRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}
	
	protected boolean isAudioVisible() {
		return false;
	}
	
	@Override
	protected boolean isInfoVisible() {
		return false;
	}
	
	protected List<ToolbarItem> getToolbarItems() {
		
		
		if (list!=null)
			return list;
		
		list = new ArrayList<ToolbarItem>();
		
		
	String name = null;
		
		//if (getArtExhibitionModel().getObject().getShortname()!=null)
		//	name=TextCleaner.truncate(getArtExhibitionModel().getObject().getShortname(), 24);
		//else
		//	name=TextCleaner.truncate(getArtExhibitionModel().getObject().getName(), 24);
				

		name=TextCleaner.truncate(getModel().getObject().getName(), 24);

	list.add(new ArtExhibitionItemNavDropDownMenuToolbarItem("item", getModel(), 
				getLabel("art-exhibition-item", name), Align.TOP_RIGHT));
		
		
	String ae_name = TextCleaner.truncate( this.getArtExhibitionModel().getObject().getName(), 24);
		
	
	ArtExhibitionEXTNavDropDownMenuToolbarItem ae = new ArtExhibitionEXTNavDropDownMenuToolbarItem("item", getArtExhibitionModel(), getLabel("art-exhibition", ae_name), Align.TOP_RIGHT);
		ae.add( new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-none d-md-none d-lg-block d-xl-block d-xxl-block text-md-center"));
		list.add(ae);
		
		// site
		SiteNavDropDownMenuToolbarItem site = new SiteNavDropDownMenuToolbarItem("item", getSiteModel(),  Align.TOP_RIGHT);
		site.add( new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-none d-md-none d-lg-block d-xl-block d-xxl-block text-md-center"));
		list.add(site);

 		
		return list;
	}
	

	@Override
	protected void onEdit(AjaxRequestTarget target) {
		editor.onEdit(target);
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				logger.debug(event.toString());

				if (event.getName().equals(ServerAppConstant.action_exhibition_item_info_edit)) {
					ArtExhibitionItemPage.this.onEdit(event.getTarget());
				}
 
				if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					ArtExhibitionItemPage.this.getMetaEditor().onEdit(event.getTarget());
				}
				
				else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
					ArtExhibitionItemPage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
				}

				
				
				else if (event.getName().equals(ServerAppConstant.artexhibition_item_info)) {
					ArtExhibitionItemPage.this.togglePanel(ServerAppConstant.artexhibition_item_info, event.getTarget());
				}
				
				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					ArtExhibitionItemPage.this.togglePanel(event.getName(), event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					ArtExhibitionItemPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.object_audit)) {
					ArtExhibitionItemPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
				}
			
			
				//else if (event.getName().equals(ServerAppConstant.audit)) {
				//	ArtExhibitionItemPage.this.togglePanel(ServerAppConstant.audit, event.getTarget());
				//}
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
					setResponsePage(new SitePage( getSiteModel()));
				}
				
				if (event.getName().equals(ServerAppConstant.exhibition_info)) {
					setResponsePage(new ArtExhibitionPage( getArtExhibitionModel()));
				}

				if (event.getName().equals(ServerAppConstant.exhibition_items)) {
					ArtExhibitionPage page = new ArtExhibitionPage( getArtExhibitionModel());
					page.setStartTab(ServerAppConstant.exhibition_items);
					setResponsePage(page);
				}

				if (event.getName().equals(ServerAppConstant.exhibition_guides)) {
					ArtExhibitionPage page = new ArtExhibitionPage( getArtExhibitionModel());
					page.setStartTab(ServerAppConstant.exhibition_guides);
					setResponsePage(page);
				}
				
				if (event.getName().equals(ServerAppConstant.object_audit)) {
					ArtExhibitionPage page = new ArtExhibitionPage( getArtExhibitionModel());
					page.setStartTab(ServerAppConstant.object_audit);
					setResponsePage(page);
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
	
	public ArtExhibitionItemPage() {
		super();
	}

	public ArtExhibitionItemPage(PageParameters parameters) {
		super(parameters);
	}

	public ArtExhibitionItemPage(IModel<ArtExhibitionItem> model) {
		this(model, null);
	}
	
	public ArtExhibitionItemPage(IModel<ArtExhibitionItem> model, List<IModel<ArtExhibitionItem>> list) {
		super( model, list);
	}
	
	 
	
	@Override
	protected Optional<ArtExhibitionItem> getObject(Long id) {
		return getArtExhibitionItem( id );
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
		
		bc.addElement(new HREFBCElement("/artexhibition/" + getArtExhibitionModel().getObject().getId().toString(),  
				Model.of( getArtExhibitionModel().getObject().getDisplayname() + " (E)")));

		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname()  + " (Obra)" )));

		header = new JumboPageHeaderPanel<ArtExhibitionItem>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));
		
		header.setContext(getLabel("artwork-in-exhibition"));
		
		if (getList()!=null && getList().size()>0) {
			Navigator<ArtExhibitionItem> nav = new Navigator<ArtExhibitionItem>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void navigate(int current) {
					setResponsePage( new ArtExhibitionItemPage( getList().get(current), getList() ));
				}
			};
			bc.setNavigator(nav);
		}
		
		header.add( new org.apache.wicket.AttributeModifier("class", "row mt-0 mb-0 text-center imgReduced"));
	 	
		if (getArtWorkModel().getObject().getPhoto()!=null) 
			header.setPhotoModel(new ObjectModel<Resource>( getArtWorkModel().getObject().getPhoto()));
		 	
		//if (getModel().getObject().getSubtitle()!=null)
		//	header.setTagline(Model.of(getModel().getObject().getSubtitle()));
		//else  if (getArtExhibitionModel().getObject().getSubtitle()!=null)
	
		//header.setTagline(Model.of(getArtExhibitionModel().getObject().getSubtitle()));
		
		header.setTagline(Model.of(getArtistStr(getArtWorkModel().getObject())));
		
		header.setBreadCrumb(bc);
		add(header);
 	 	
		if (getList()!=null && getList().size()>0) {
			Navigator<ArtExhibitionItem> nav = new Navigator<ArtExhibitionItem>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void navigate(int current) {
					setResponsePage( new ArtExhibitionItemPage( getList().get(current), getList() ));
				}
			};
			bc.setNavigator(nav);
		}
		
		return header;
	}


	@Override
	protected IRequestablePage getObjectPage(IModel<ArtExhibitionItem> model, List<IModel<ArtExhibitionItem>> list) {
	 	return new ArtExhibitionItemPage( model, list );
	}
	
	protected void setUpModel() {
		super.setUpModel();
		
		if (!getModel().getObject().isDependencies()) {
			Optional<ArtExhibitionItem> o_i = getArtExhibitionItemDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<ArtExhibitionItem>(o_i.get()));
		}
		
		Optional<ArtExhibition> o_a = getArtExhibitionDBService().findWithDeps(getModel().getObject().getArtExhibition().getId());
		setArtExhibitionModel(new ObjectModel<ArtExhibition>(o_a.get()));
		
	
		Optional<ArtWork> o_aw = getArtWorkDBService().findWithDeps(getModel().getObject().getArtWork().getId());
		setArtWorkModel(new ObjectModel<ArtWork>(o_aw.get()));
	
		
		Optional<Site> o_i = getSiteDBService().findWithDeps(getArtExhibitionModel().getObject().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_i.get()));
		
	}
 	
	
	
	private void setArtWorkModel(IModel<ArtWork> m) {
		this.artWorkModel=m;
	}
	
	
	private IModel<ArtWork> getArtWorkModel() {
		return this.artWorkModel;
	}
	

	protected Panel getEditor(String id) {
		if (editor==null)
			editor = new ArtExhibitionItemEditor(id, getModel(), getArtExhibitionModel(), getSiteModel());
		return (editor);
	}
	
	public IModel<ArtExhibition> getArtExhibitionModel() {
		return artExhibitionModel;
	}

	public void setArtExhibitionModel(IModel<ArtExhibition> artExhibitionModel) {
		this.artExhibitionModel = artExhibitionModel;
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
		
		if (this.artExhibitionModel!=null)
			this.artExhibitionModel.detach();
		
		if (this.artWorkModel!=null)
			this.artWorkModel.detach();
		
	}
	
	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		
		NamedTab tab_1=new NamedTab(Model.of("editor"), ServerAppConstant.artexhibition_item_info) {
		 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);
		
		/**
		NamedTab tab_2=new NamedTab(Model.of("audit"), ServerAppConstant.object_audit) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new DummyBlockPanel(panelId,getLabel("audit"));
			}
		};
		tabs.add(tab_2);
	**/
		if (getStartTab()==null)
			setStartTab( ServerAppConstant.artexhibition_item_info );
		
		
		return tabs;
	}


	 
}
