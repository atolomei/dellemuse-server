package dellemuse.serverapp.institution;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.event.MenuAjaxEvent;

import io.wktui.model.TextCleaner;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

public class InstitutionMainPanel extends DBModelPanel<Institution> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionMainPanel.class.getName());

	private InstitutionEditor editor;
	private List<IModel<Site>> siteList;
	private Link<Site> addSite;

	public InstitutionMainPanel(String id, IModel<Institution> model) {
		super(id, model);
	}

	public InstitutionEditor getInstitutionEditor() {
		return editor;
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<Institution> create = new AjaxButtonToolbarItem<Institution>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_institution_edit, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(create);
		return list;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		this.editor = new InstitutionEditor("institutionEditor", getModel());
		add(this.editor);

		this.siteList = new ArrayList<IModel<Site>>();
		getSites(getModel().getObject()).forEach(s -> siteList.add(new ObjectModel<Site>(s)));

		ListPanel<Site> panel = new ListPanel<>("sites", getSiteList()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getListItemPanel(IModel<Site> model, ListPanelMode mode) {

				DelleMuseObjectListItemPanel<Site> panel = new DelleMuseObjectListItemPanel<>("row-element", model, mode) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new SitePage(getModel(), getSiteList()));
					}

					protected IModel<String> getInfo() {
						String str = TextCleaner.clean(model.getObject().getInfo(), 280);
						return new Model<String>(str);
					}

					@Override
					protected String getImageSrc() {
						try {
							if (getModel().getObject().getPhoto() != null) {
								Resource photo = getResource(model.getObject().getPhoto().getId()).get();
								return getPresignedThumbnail(photo, ThumbnailSize.SMALL);
							}
						} catch (Exception e) {
							logger.error(e, ServerConstant.NOT_THROWN);
						}
						return null;
					}

					@Override
					protected String getTitleLinkCss() {
						return "title-link-highlight";
					}
				};
				return panel;
			}

			@Override
			public IModel<String> getItemLabel(IModel<Site> model) {
				return new Model<String>(model.getObject().getDisplayname());
			}

			@Override
			protected void onClick(IModel<Site> model) {
				setResponsePage(new SitePage(model, getSiteList()));
			}

			@Override
			protected List<IModel<Site>> filter(List<IModel<Site>> initialList, String filter) {
				List<IModel<Site>> list = new ArrayList<IModel<Site>>();
				final String str = filter.trim().toLowerCase();
				initialList.forEach(s -> {
					if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
						list.add(s);
					}
				});
				return list;
			}
		};
		add(panel);

		panel.setListPanelMode(ListPanelMode.TITLE);
		panel.setLiveSearch(false);
		panel.setSettings(true);

		this.addSite = new Link<Site>("addSite", null) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				InstitutionMainPanel.this.addSite();
			}
		};

		add(this.addSite);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.siteList != null)
			this.siteList.forEach(i -> i.detach());
	}

	protected List<IModel<Site>> getSiteList() {
		return this.siteList;
	}

	protected void addSite() {
		
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		Site site;
		
		if (getSiteList().size() == 0)
			site = service.create(getModel().getObject(), getUserDBService().findRoot());
		else
			site = service.create(getModel().getObject().getName() + " " + String.valueOf(getSiteList().size()), getUserDBService().findRoot());
		
		IModel<Site> m = new ObjectModel<Site>(site);
		getSiteList().add(m);
		setResponsePage(new SitePage(m, getSiteList()));
	}
}
