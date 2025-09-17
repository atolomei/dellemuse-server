package dellemuse.serverapp.page.site;

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
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
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

@MountPath("/artexhibitionitem/${id}")
public class ArtExhibitionItemPage extends ObjectPage<ArtExhibitionItem> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionItemPage.class.getName());

	private IModel<Site> siteModel;

	private Link<ArtExhibitionItem> imageLink;
	private Image image;
	private WebMarkupContainer imageContainer;

	private ArtWorkEditor editor;

	
	protected List<ToolbarItem> getToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Model.of(getSiteModel().getObject().getShortName()), Align.TOP_RIGHT ));
		return list;
	}
	
	@Override
	protected void onEdit(AjaxRequestTarget target) {
		logger.error("not done");
		
	}
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
	protected void addHeaderPanel() {

		
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
				new Model<String>(getSiteModel().getObject().getDisplayname())));
		bc.addElement(new HREFBCElement("/site/artwork/" + getSiteModel().getObject().getId().toString(),
				getLabel("artworks")));

		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname())));

		PageHeaderPanel<ArtExhibitionItem> ph = new PageHeaderPanel<ArtExhibitionItem>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));

		ph.setBreadCrumb(bc);
		
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
		
		add(ph);
	}


	@Override
	protected IRequestablePage getObjectPage(IModel<ArtExhibitionItem> model, List<IModel<ArtExhibitionItem>> list) {
	 	return new ArtExhibitionItemPage( model, list );
	}
	
	protected void setUpModel() {
		super.setUpModel();
		
		/**
		if (!getModel().getObject().isDependencies()) {
			Optional<ArtExhibitionItem> o_i = getArtWorkDBService().findByIdWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<ArtExhibitionItem>(o_i.get()));
		}
		
		
		setSiteModel(new ObjectModel<Site>( getModel().getObject().getSite() ));
		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findByIdWithDeps(getSiteModel().getObject().getId());
			setSiteModel(new ObjectModel<Site>(o_i.get()));
		}
		**/
	}

	protected Panel getEditor(String id) {
		//editor = new ArtWorkEditor("editor", getModel());
		//return (editor);
		return new DummyBlockPanel(id, Model.of("no editor yet"));
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
	}

	@Override
	protected List<ITab> getInternalPanels() {
		// TODO Auto-generated method stub
		return null;
	}

	 
}
