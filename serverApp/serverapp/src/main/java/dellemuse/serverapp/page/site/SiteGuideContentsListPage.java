package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;


import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;


/**
 * 
 * 
 * 
 */
@MountPath("/site/contents/${id}")
public class SiteGuideContentsListPage extends ObjectListPage<GuideContent> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteGuideContentsListPage.class.getName());
	 
	private StringValue stringValue;
	private IModel<Site> siteModel;
	private List<ToolbarItem> listToolbar;

	public SiteGuideContentsListPage() {
		super();
	}

	public SiteGuideContentsListPage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
	}
 
	public SiteGuideContentsListPage(IModel<Site> model) {
		super();
		setSiteModel(model);
		getPageParameters().add("id", model.getObject().getId().toString());
	}
 
	@Override
	public void onInitialize() {
		
		if (getSiteModel() == null) {
			if (stringValue != null) {
				Optional<Site> o_site = getSite(Long.valueOf(stringValue.toLong()));
				if (o_site.isPresent()) {
					setSiteModel(new ObjectModel<Site>(o_site.get()));
				}
			}
		}

		if (getSiteModel() == null) {
			throw new RuntimeException("no site");
		}
		super.onInitialize();
	}


	public IRequestablePage getObjectPage(IModel<GuideContent> model) {
			return null;
	}
	
	public Iterable<GuideContent> getObjects() {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		Iterable<ArtExhibitionItem> it= service.getSiteArtExhibitionItems(getSiteModel().getObject().getId());
		return service.getSiteGuideContent( getSiteModel().getObject().getId());
	}

	@Override
	public Iterable<GuideContent> getObjects(ObjectState os1) {
		 return this.getObjects(os1, null);
	}

	@Override
	public Iterable<GuideContent> getObjects(ObjectState os1, ObjectState os2) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		Iterable<ArtExhibitionItem> it= service.getSiteArtExhibitionItems(getSiteModel().getObject().getId());
		return service.getSiteGuideContent( getSiteModel().getObject().getId());
	}
	

	 
	public IModel<String> getObjectInfo(IModel<GuideContent> model) {
		return new Model<String>( model.getObject().getInfo());
	}

	/**
	public IModel<String> getObjectTitle(IModel<GuideContent> model) {
		
		if (model.getObject().getState()==ObjectState.DELETED) 
			return new Model<String>(model.getObject().getDisplayname() + ServerConstant.DELETED_ICON);

		return new Model<String>( model.getObject().getDisplayname() );
	}
**/
	
	public void onClick(IModel<GuideContent> model) {
		
	}

	public IModel<String> getPageTitle() {
		return new Model<String>(getSiteModel().getObject().getDisplayname());
	}
	 
	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();
 	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	@Override
	public IModel<String> getListPanelLabel() {
		return null;
	}

	
	@Override
	protected List<ToolbarItem> getListToolbarItems() {

		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		IModel<String> selected = Model.of(ObjectStateEnumSelector.ALL.getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);

		listToolbar.add(s);

		return listToolbar;
	}
	
	protected IModel<String> getTitleLabel() {
		return getLabel("guide-contents");
	}
	
	protected List<ToolbarItem> getMainToolbarItems() {
		
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>("item") {
			private static final long serialVersionUID = 1L;
			protected void onClick() {
				SiteGuideContentsListPage.this.onCreate();
			}
		};
	
		create.setAlign(Align.TOP_LEFT);
		list.add(create);
		
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(),   Align.TOP_RIGHT ));
		return list;
	}
	
	
	protected void onCreate() {
		throw new RuntimeException("not done");
		
	}

	protected void addHeaderPanel() {
	    BreadCrumb<Void> bc = createBreadCrumb();
		   
	    bc.addElement(new HREFBCElement( "/site/list", getLabel("sites")));
        bc.addElement(new HREFBCElement( "/site/"+ getSiteModel().getObject().getId().toString(), 
        			  new Model<String>(getSiteModel().getObject().getDisplayname())));
        
        bc.addElement(new BCElement( getLabel("guide-contents")));
    
		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, new Model<String>(getSiteModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);

		ph.setTagline(getLabel("guide-contents"));
		add(ph);

	}

	@Override
	protected String getObjectTitleIcon(IModel<GuideContent> model) {
		// TODO Auto-generated method stub
		return null;
	}
 
}
