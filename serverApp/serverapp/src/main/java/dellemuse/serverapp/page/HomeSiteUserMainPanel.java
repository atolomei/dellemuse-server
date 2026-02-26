package dellemuse.serverapp.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.model.TextCleaner;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

public class HomeSiteUserMainPanel extends DBModelPanel<User> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(HomeSiteUserMainPanel.class.getName());

	private ListPanel<Site> panel;
	private List<IModel<Site>> list;

	public HomeSiteUserMainPanel(String id, IModel<User> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		addSites();
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (list != null)
			list.forEach(s -> s.detach());
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return null;
	}

	protected IModel<String> getObjectTitle(IModel<Site> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	private List<IModel<Site>> getList() {
		return list;
	}

	private void loadList() {
		list = new ArrayList<IModel<Site>>();
		 getUserDBService().getUserAuthorizedSites(getSessionUser().get()).forEach(i -> list.add(new ObjectModel<Site>(i)));
	}

	public Iterable<Site> getSites(ObjectState o1, ObjectState o2) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.findAllSorted(o1, o2);
	}

	private void addSites() {

		loadList();

		this.panel = new ListPanel<>("siteList") {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<String> getItemLabel(IModel<Site> model) {
				return HomeSiteUserMainPanel.this.getObjectTitle(model);
			}

			@Override
			public List<IModel<Site>> getItems() {
				return HomeSiteUserMainPanel.this.getList();
			}

			@Override
			protected Panel getListItemPanel(IModel<Site> model, ListPanelMode mode) {

				DelleMuseObjectListItemPanel<Site> panel = new DelleMuseObjectListItemPanel<>("row-element", model, mode) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						HomeSiteUserMainPanel.this.onClick(getModel());
					}

					protected IModel<String> getInfo() {
						return HomeSiteUserMainPanel.this.getObjectInfo(getModel());
					}

					protected IModel<String> getObjectTitle() {
						return HomeSiteUserMainPanel.this.getObjectTitle(getModel());
					}

					protected IModel<String> getObjectSubtitle() {
						if (getMode() == ListPanelMode.TITLE)
							return null;
						return HomeSiteUserMainPanel.this.getObjectSubtitle(getModel());
					}

					@Override
					protected String getImageSrc() {
						return HomeSiteUserMainPanel.this.getObjectImageSrc(getModel());
					}

				};
				return panel;
			}

			protected void onClick(IModel<Site> model) {
				HomeSiteUserMainPanel.this.onClick(model);
			}

		};

		this.panel.setHasExpander(false);
		add(this.panel);
	}

	protected void setList(List<IModel<Site>> list) {
		this.list = list;
	}

	protected void onClick(IModel<Site> model) {
		setResponsePage(new SitePage(model, getList()));
	}

	protected String getObjectImageSrc(IModel<Site> model) {
		if (model.getObject().getPhoto() != null) {
			Resource photo = getResource(model.getObject().getPhoto().getId()).get();
			return getPresignedThumbnailSmall(photo);
		}
		return null;
	}

	protected IModel<String> getObjectSubtitle(IModel<Site> model) {
		return new Model<String>(model.getObject().getSubtitle());
	}

	protected IModel<String> getObjectInfo(IModel<Site> model) {
		return new Model<String>(TextCleaner.clean(model.getObject().getInfo(), 280));
	}

}
