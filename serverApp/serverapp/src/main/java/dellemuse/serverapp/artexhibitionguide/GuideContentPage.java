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
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
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
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/guidecontent/${id}")
public class GuideContentPage extends ObjectPage<GuideContent> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(GuideContentPage.class.getName());

	private IModel<ArtExhibitionGuide> artExhibitionGuideModel;
	private IModel<ArtExhibition> artExhibitionModel;
	private IModel<ArtExhibitionItem> artExhibitionItemModel;
	private IModel<Site> siteModel;
	private IModel<ArtWork> artWorkModel;
	private  GuideContentEditor editor;
	
	
	private JumboPageHeaderPanel<GuideContent> header;

	
	
	
	public GuideContentPage() {
		super();
	}

	public GuideContentPage(PageParameters parameters) {
		super(parameters);
	}

	public GuideContentPage(IModel<GuideContent> model) {
		this(model, null);
	}
	
	public GuideContentPage(IModel<GuideContent> model, List<IModel<GuideContent>> list) {
		super( model, list);
	}
	
	
	protected WebMarkupContainer getEditor(String id ) {
		if (this.editor==null)
			this.editor = new GuideContentEditor(id,   getModel(), getArtExhibitionGuideModel(), getArtExhibitionModel(), getSiteModel());
		return this.editor;
	}
	
	
	protected void setUpModel() {
		super.setUpModel();
		
		if (!getModel().getObject().isDependencies()) {
			Optional<GuideContent> o_i = getGuideContentDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<GuideContent>(o_i.get()));
		}
		
		ArtExhibitionItem item = getModel().getObject().getArtExhibitionItem();
		Optional<ArtExhibitionItem> o_i = getArtExhibitionItemDBService().findWithDeps(item.getId());
		this.setArtExhibitionItemModel( new ObjectModel<ArtExhibitionItem>(o_i.get()));
		
		ArtWork aw = getArtWorkDBService().findWithDeps(o_i.get().getArtWork().getId()).get();
		setArtWorkModel(new ObjectModel<ArtWork>(aw));
		
		ArtExhibitionGuide guide = getModel().getObject().getArtExhibitionGuide();
		Optional<ArtExhibitionGuide> o_g = getArtExhibitionGuideDBService().findWithDeps(guide.getId());
		this.setArtExhibitionGuideModel( new ObjectModel<ArtExhibitionGuide>(o_g.get()));
		
		ArtExhibition  a = item.getArtExhibition();
		
		Optional<ArtExhibition> o_a = getArtExhibitionDBService().findWithDeps(a.getId());
		this.setArtExhibitionModel(new ObjectModel<ArtExhibition>(o_a.get()));
		
		Optional<Site> o_s = getSiteDBService().findWithDeps( getArtExhibitionModel().getObject().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_s.get()));
	
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
			
				logger.debug(event.toString());

				/** Action  */

				if (event.getName().equals(ServerAppConstant.action_guide_content_edit)) {
					GuideContentPage.this.onEdit(event.getTarget());
				}
			
				/** Panels */
				else if (event.getName().equals(ServerAppConstant.guide_content_info)) {
					 GuideContentPage.this.togglePanel(ServerAppConstant.guide_content_info, event.getTarget());
					 GuideContentPage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(GuideContentPage.this.getHeader());
				}

				else if (event.getName().equals(ServerAppConstant.audit)) {
					 GuideContentPage.this.togglePanel(ServerAppConstant.audit, event.getTarget());
					 GuideContentPage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(GuideContentPage.this.getHeader());
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof SimpleAjaxWicketEvent)
					return true;
				return false;
			}
		});

		add(new io.wktui.event.WicketEventListener<SimpleWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleWicketEvent event) {
				if (event.getName().equals(ServerAppConstant.guide_content_info)) {
					setResponsePage( new SitePage( getSiteModel()));
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
		editor.onEdit(target);
	}
	
	@Override
	protected List<ToolbarItem> getToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		list.add(new GuideContentNavDropDownMenuToolbarItem(	"item", 
																	getArtExhibitionGuideModel(), 
																	getLabel("audio-guide", TextCleaner.truncate(getArtExhibitionGuideModel().getObject().getName(), 24)),
																	Align.TOP_RIGHT));
		
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(),  Align.TOP_RIGHT ));
		
		/**
		AjaxButtonToolbarItem<ArtExhibition> edit = new AjaxButtonToolbarItem<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				GuideContentPage.this.togglePanel(0, target);
				GuideContentPage.this.onEdit(target);
			}
		};
		edit.setAlign(Align.TOP_LEFT);
		list.add(edit);
		 **/
		
		return list;
	}


	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = new ArrayList<INamedTab>();
		
		NamedTab tab_1=new NamedTab(Model.of("editor"), ServerAppConstant.guide_content_info) {
		 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);
		
		NamedTab tab_2=new NamedTab(Model.of("audit"), ServerAppConstant.audit) {
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
	protected Optional<GuideContent> getObject(Long id) {
		return getGuideContent( id );
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
		bc.addElement(new HREFBCElement("/artexhibition/" + getArtExhibitionModel().getObject().getId().toString(),  Model.of( getArtExhibitionModel().getObject().getDisplayname()  + " (E)" )));
		bc.addElement(new HREFBCElement("/guide/" + getArtExhibitionGuideModel().getObject().getId().toString(),  Model.of( getArtExhibitionGuideModel().getObject().getDisplayname()  + " (A)")));
		
		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname())));

		header = new JumboPageHeaderPanel<GuideContent>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));
		
		header.add( new org.apache.wicket.AttributeModifier("class", "row mt-0 mb-0 text-center imgReduced"));
		
		header.setContext(getLabel("guide-content"));
		
		if (getList()!=null && getList().size()>0) {
			Navigator<GuideContent> nav = new Navigator<GuideContent>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void navigate(int current) {
					setResponsePage( new  GuideContentPage( getList().get(current), getList() ));
				}
			};
			bc.setNavigator(nav);
		}
		
		if (getArtWorkModel().getObject().getPhoto()!=null) 
			this.header.setPhotoModel(new ObjectModel<Resource>( getArtWorkModel().getObject().getPhoto()));
		
		header.setBreadCrumb(bc);

		if (getList()!=null && getList().size()>0) {
			Navigator<GuideContent> nav = new Navigator<GuideContent>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void navigate(int current) {
					setResponsePage( new GuideContentPage( getList().get(current), getList() ));
				}
			};
			bc.setNavigator(nav);
		}
		
		
		
		
		add(header);
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<GuideContent> model, List<IModel<GuideContent>> list) {
	 	return new GuideContentPage( model, list );
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
	
		if (artExhibitionGuideModel!=null) 
			artExhibitionGuideModel.detach();
		
		if ( artExhibitionModel!=null)
			artExhibitionModel.detach();
		
		if ( artExhibitionItemModel!=null)
			artExhibitionItemModel.detach();
		
		if (artWorkModel!=null)
			artWorkModel.detach();
		
	}
	
	/**
	private String getInfoGral() {
		return TextCleaner.clean(getModel().getObject().getSpec() + " <br/>" + " id: "
				+ getModel().getObject().getId().toString());
	}

	private boolean isInfoGral() {
		return  getModel() != null && 
				getModel().getObject() != null && 
				getModel().getObject().getId() != null || 
				getModel().getObject().getSpec() != null;
	}
 */
	
	/**
	protected String getImageSrc(IModel<GuideContent> model) {

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
**/
	
	private JumboPageHeaderPanel<?> getHeader() {
		return header;
	}
	
	private boolean isAudio() {
		return getModel().getObject().getAudio() != null;
	}

	public IModel<ArtExhibitionGuide> getArtExhibitionGuideModel() {
		return artExhibitionGuideModel;
	}

	public IModel<ArtExhibition> getArtExhibitionModel() {
		return artExhibitionModel;
	}

	public IModel<ArtExhibitionItem> getArtExhibitionItemModel() {
		return artExhibitionItemModel;
	}

	public void setArtExhibitionGuideModel(IModel<ArtExhibitionGuide> artExhibitionGuideModel) {
		this.artExhibitionGuideModel = artExhibitionGuideModel;
	}

	public void setArtExhibitionModel(IModel<ArtExhibition> artExhibitionModel) {
		this.artExhibitionModel = artExhibitionModel;
	}

	public void setArtExhibitionItemModel(IModel<ArtExhibitionItem> artExhibitionItemModel) {
		this.artExhibitionItemModel = artExhibitionItemModel;
	}

	public IModel<ArtWork> getArtWorkModel() {
		return artWorkModel;
	}

	public void setArtWorkModel(IModel<ArtWork> artWorkModel) {
		this.artWorkModel = artWorkModel;
	}
 

	/**
	protected List<Person> getArtists(ArtWork aw) {
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		List<Person> list = new ArrayList<Person>(); 
		for (Person person: aw.getArtists()) {
			list.add(service.findById(person.getId()).get());
		}
		return list;
	}
	**/
	
}
