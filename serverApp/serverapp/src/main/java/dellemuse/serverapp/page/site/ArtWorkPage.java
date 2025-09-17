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
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.PersonPage;
import dellemuse.serverapp.page.user.UserEditor;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
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

@MountPath("/artwork/${id}")
public class ArtWorkPage extends ObjectPage<ArtWork> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtWorkPage.class.getName());

	private IModel<Site> siteModel;

	private ArtWorkMainPanel editor;

	
	@Override
	protected void onEdit(AjaxRequestTarget target) {
		editor.getEditor().onEdit(target);
	}

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
	protected Optional<ArtWork> getObject(Long id) {
		return getArtWork( id );
	}


	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String> (getModel().getObject().getName());
	}
	

	static final int PANEL_EDITOR = 0;
	static final int PANEL_AUDIT = 1;
	
	@Override
	protected List<ToolbarItem> getToolbarItems() {
	
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Align.TOP_RIGHT));
		
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
		
		
		

		AjaxButtonToolbarItem<Person> audit = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				ArtWorkPage.this.togglePanel(PANEL_AUDIT, target);
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
			Optional<ArtWork> o_i = getArtWorkDBService().findByIdWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<ArtWork>(o_i.get()));
		}
		
		setSiteModel(new ObjectModel<Site>( getModel().getObject().getSite() ));
		
		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findByIdWithDeps(getSiteModel().getObject().getId());
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
 **/
	
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

	private boolean isAudio() {
		return getModel().getObject().getAudio() != null;
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
