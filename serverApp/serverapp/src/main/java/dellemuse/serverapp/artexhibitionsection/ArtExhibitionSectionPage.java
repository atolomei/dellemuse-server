package dellemuse.serverapp.artexhibitionsection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibition.ArtExhibitionEXTNavDropDownMenuToolbarItem;
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.editor.ObjectMarkAsDeleteEvent;
import dellemuse.serverapp.editor.ObjectRestoreEvent;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.MultiLanguageObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionSectionRecord;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
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
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/ArtExhibitionSection/${id}")
public class ArtExhibitionSectionPage extends MultiLanguageObjectPage<ArtExhibitionSection, ArtExhibitionSectionRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionSectionPage.class.getName());

	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;
	private IModel<ArtWork> artWorkModel;
	private List<ToolbarItem> list;

	 
	JumboPageHeaderPanel<ArtExhibitionSection> header;

	protected List<Language> getSupportedLanguages() {
		return  getSiteModel().getObject().getLanguages();
	}

	
	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		
		if (ouser.isEmpty())
			return false;
	
		
		User user = ouser.get();  
		
		if (user.isRoot()) 
			return true;
		
		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		{
		Set<RoleGeneral> set = user.getRolesGeneral();
			if (set!=null) {
					boolean isAccess=set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT) ));
					if (isAccess)
						return true;
			}
		}
		
		
		{
			final Long sid = getSiteModel().getObject().getId();
			
			Set<RoleSite> set = user.getRolesSite();
			if (set!=null) {
				boolean isAccess=set.stream().anyMatch((p -> p.getSite().getId().equals(sid) && (p.getKey().equals(RoleSite.ADMIN) || p.getKey().equals(RoleSite.EDITOR))));
				if (isAccess)
					return true;
			}
		}		
		
		{
			final Long iid = getSiteModel().getObject().getInstitution().getId();
			Set<RoleInstitution> set = user.getRolesInstitution();
			if (set!=null) {
				boolean isAccess=set.stream().anyMatch((p -> p.getInstitution().getId().equals(iid) && (p.getKey().equals(RoleInstitution.ADMIN) )));
				if (isAccess)
					return true;
			}
		}

		return false;
	} 
	public ArtExhibitionSectionPage() {
		super();
	}

	public ArtExhibitionSectionPage(PageParameters parameters) {
		super(parameters);
	}

	public ArtExhibitionSectionPage(IModel<ArtExhibitionSection> model) {
		this(model, null);
	}

	public ArtExhibitionSectionPage(IModel<ArtExhibitionSection> model, List<IModel<ArtExhibitionSection>> list) {
		super(model, list);
	}
	
	protected Optional<ArtExhibitionSectionRecord> loadTranslationRecord(String lang) {
		return getArtExhibitionSectionDBService().findByArtExhibitionSection(getModel().getObject(), lang);
	}

	protected ArtExhibitionSectionRecord createTranslationRecord(String lang) {
		return getArtExhibitionSectionRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}

	@Override
	protected boolean isAudioVisible() {
		return true;
	}
	
	
	@Override
	protected Class<?> getTranslationClass() {
		return ArtExhibitionSectionRecord.class;
	}

	protected List<ToolbarItem> getToolbarItems() {

		if (list != null)
			return list;

		list = new ArrayList<ToolbarItem>();

		String name = null;

		if (getArtExhibitionModel().getObject().getShortname() != null)
			name = TextCleaner.truncate(getArtExhibitionModel().getObject().getShortname(), 24);
		else
			name = TextCleaner.truncate(getArtExhibitionModel().getObject().getName(), 24);

		list.add(new ArtExhibitionEXTNavDropDownMenuToolbarItem("item", getArtExhibitionModel(), getLabel("art-exhibition", name), Align.TOP_RIGHT));

		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Model.of(getSiteModel().getObject().getShortName()), Align.TOP_RIGHT));

		return list;
	}

	 

	protected void onDelete(AjaxRequestTarget target) {
		getArtExhibitionSectionDBService().markAsDeleted( getModel().getObject(), getSessionUser().get() );
		fireScanAll(new ObjectMarkAsDeleteEvent(target));
	}
	
	protected void onRestore(AjaxRequestTarget target) {
		getArtExhibitionSectionDBService().restore( getModel().getObject(), getSessionUser().get() );
		fireScanAll(new ObjectRestoreEvent(target));
	}
	
	
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());

				if (event.getName().equals(ServerAppConstant.action_exhibition_item_info_edit)) {
					//ArtExhibitionSectionPage.this.onEdit(event.getTarget());
				}

				if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					ArtExhibitionSectionPage.this.getMetaEditor().onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
					ArtExhibitionSectionPage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
				}

				else if (event.getName().equals(ServerAppConstant.exhibition_item_info)) {
					ArtExhibitionSectionPage.this.togglePanel(ServerAppConstant.exhibition_item_info, event.getTarget());
				}

				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					ArtExhibitionSectionPage.this.togglePanel(event.getName(), event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					ArtExhibitionSectionPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
					
				} else if (event.getName().equals(ServerAppConstant.object_audit)) {
					ArtExhibitionSectionPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
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

				if (event.getName().equals(ServerAppConstant.site_action_home)) {
					setResponsePage(new SitePage(getSiteModel()));
				}

				if (event.getName().equals(ServerAppConstant.exhibition_info)) {
					setResponsePage(new ArtExhibitionPage(getArtExhibitionModel()));
				}

				if (event.getName().equals(ServerAppConstant.exhibition_items)) {
					ArtExhibitionPage page = new ArtExhibitionPage(getArtExhibitionModel());
					page.setStartTab(ServerAppConstant.exhibition_items);
					setResponsePage(page);
				}

				if (event.getName().equals(ServerAppConstant.exhibition_guides)) {
					ArtExhibitionPage page = new ArtExhibitionPage(getArtExhibitionModel());
					page.setStartTab(ServerAppConstant.exhibition_guides);
					setResponsePage(page);
				}

				if (event.getName().equals(ServerAppConstant.object_audit)) {
					ArtExhibitionPage page = new ArtExhibitionPage(getArtExhibitionModel());
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



	@Override
	protected Optional<ArtExhibitionSection> getObject(Long id) {
		return getArtExhibitionSection(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String>(getModel().getObject().getName());
	}

	@Override
	protected Panel createHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();

		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));

		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(), new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new HREFBCElement("/site/exhibitions/" + getSiteModel().getObject().getId().toString(), getLabel("exhibitions")));

		bc.addElement(new HREFBCElement("/artexhibition/" + getArtExhibitionModel().getObject().getId().toString(), Model.of(getArtExhibitionModel().getObject().getDisplayname() + " (E)")));

		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname() + " (Obra)")));

		header = new JumboPageHeaderPanel<ArtExhibitionSection>("page-header", getModel(), new Model<String>(getModel().getObject().getDisplayname()));

		header.setContext(getLabel("artwork-in-exhibition"));

		if (getList() != null && getList().size() > 0) {
			Navigator<ArtExhibitionSection> nav = new Navigator<ArtExhibitionSection>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new ArtExhibitionSectionPage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}

		header.add(new org.apache.wicket.AttributeModifier("class", "row mt-0 mb-0 text-center imgReduced"));

		// if (getArtWorkModel().getObject().getPhoto()!=null)
		// header.setPhotoModel(new ObjectModel<Resource>(
		// getArtWorkModel().getObject().getPhoto()));

		// if (getModel().getObject().getSubtitle()!=null)
		// header.setTagline(Model.of(getModel().getObject().getSubtitle()));
		// else if (getArtExhibitionModel().getObject().getSubtitle()!=null)

		// header.setTagline(Model.of(getArtExhibitionModel().getObject().getSubtitle()));

		// header.setTagline(Model.of(getArtistStr(getArtWorkModel().getObject())));

		header.setBreadCrumb(bc);
		add(header);

		if (getList() != null && getList().size() > 0) {
			Navigator<ArtExhibitionSection> nav = new Navigator<ArtExhibitionSection>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new ArtExhibitionSectionPage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}

		return (header);
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<ArtExhibitionSection> model, List<IModel<ArtExhibitionSection>> list) {
		return new ArtExhibitionSectionPage(model, list);
	}

	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<ArtExhibitionSection> o_i = getArtExhibitionSectionDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<ArtExhibitionSection>(o_i.get()));
		}

		Optional<ArtExhibition> o_a = getArtExhibitionDBService().findWithDeps(getModel().getObject().getArtExhibition().getId());
		setArtExhibitionModel(new ObjectModel<ArtExhibition>(o_a.get()));

		Optional<Site> o_i = getSiteDBService().findWithDeps(getArtExhibitionModel().getObject().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_i.get()));
	}

	protected Panel getEditor(String id) {
		// if (editor==null)
		// editor = new ArtExhibitionSectionEditor(id, getModel(),
		// getArtExhibitionModel(), getSiteModel());
		// return (editor);
		return new DummyBlockPanel(id, Model.of("not done!"));

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

		if (this.artExhibitionModel != null)
			this.artExhibitionModel.detach();

		if (this.artWorkModel != null)
			this.artWorkModel.detach();

	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.exhibition_item_info) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);

		setStartTab(ServerAppConstant.exhibition_item_info);

		return tabs;
	}

}
