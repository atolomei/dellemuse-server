package dellemuse.serverapp.artexhibitionguide;

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
import dellemuse.serverapp.artexhibition.ArtExhibitionEditor;
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
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.page.site.SiteInfoPage;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.page.user.UserEditor;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Person;
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
 * site foto Info - exhibitions
 * alter table artexhibition alter column fromdate drop not null;
ALTER TABLE
dellemuse=# alter table artexhibition alter column todate drop not null;
 * 
 */

@MountPath("/guide/${id}")
public class ArtExhibitionGuidePage extends ObjectPage<ArtExhibitionGuide> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionGuidePage.class.getName());

	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;
	 
	private ArtExhibitionGuideContentsPanel guideContents;
	private JumboPageHeaderPanel<ArtExhibitionGuide> header;

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

				// action
				
				if (event.getName().equals(ServerAppConstant.action_guide_edit_info)) {
					ArtExhibitionGuidePage.this.onEdit(event.getTarget());
				}
				
				// panels 
				
				else if (event.getName().equals(ServerAppConstant.guide_info)) {
					ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.guide_info, event.getTarget());
					ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionGuidePage.this.getHeader());
				}

				else if (event.getName().equals(ServerAppConstant.guide_contents)) {
					ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.guide_contents, event.getTarget());
					ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionGuidePage.this.getHeader());
				}

				
				else if (event.getName().equals(ServerAppConstant.audit)) {
					ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.audit, event.getTarget());
					ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionGuidePage.this.getHeader());
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
				if (event.getName().equals(ServerAppConstant.action_site_home)) {
					setResponsePage( new SitePage(getSiteModel(), null));
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

	protected void onGuideCreate(AjaxRequestTarget target) { 
	}
 	
	
	@Override
	protected List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		list.add(new ArtExhibitionGuideNavDropDownMenuToolbarItem("item", getModel(), 
				getLabel("audio-guide",TextCleaner.truncate(getModel().getObject().getName(), 24)), Align.TOP_RIGHT));
	
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Model.of(getSiteModel().getObject().getShortName()), Align.TOP_RIGHT ));
		
		return list;
	}
	
	ArtExhibitionGuideEditor editor;
	
	protected WebMarkupContainer getArtExhibitionGuideEditor(String id) {
		if (editor==null)
			editor = new ArtExhibitionGuideEditor(id, getModel(), getSiteModel());
		return editor;
	}

	protected WebMarkupContainer getGuideContentsPanel(String id) {
		if (guideContents==null)
			guideContents = new ArtExhibitionGuideContentsPanel(id, getModel(), getSiteModel());
		return guideContents;
	}
	
	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = new ArrayList<INamedTab>();
		
		NamedTab tab_1=new NamedTab(Model.of("editor"), ServerAppConstant.guide_info) {
		 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getArtExhibitionGuideEditor(panelId);
			}
		};
		tabs.add(tab_1);
		

		NamedTab tab_2=new NamedTab(Model.of("contents"), ServerAppConstant.guide_contents) {
			 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getGuideContentsPanel(panelId);
			}
		};
		tabs.add(tab_2);

		
		
		NamedTab tab_3=new NamedTab(Model.of("audit"), ServerAppConstant.audit) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new DummyBlockPanel(panelId,getLabel("audit"));
			}
		};
		tabs.add(tab_3);
		
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

		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname()  + " (A)" )));

		header = new JumboPageHeaderPanel<ArtExhibitionGuide>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));
		
		header.setContext(getLabel("exhibition-guide"));
		
		header.add( new org.apache.wicket.AttributeModifier("class", "row mt-0 mb-0 text-center imgReduced"));
		
		
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
			
		header.setBreadCrumb(bc);
		add(header);
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

	private JumboPageHeaderPanel<?> getHeader() {
		return header;
	}

 
}
