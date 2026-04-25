package dellemuse.serverapp.page;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.help.WelcomePanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.DateRange;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;import dellemuse.serverapp.serverdb.service.DelleMuseAuditDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.DTFormatter;
import dellemuse.serverapp.service.DateTimeService;
import io.wktui.model.TextCleaner;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

public class HomeAdminMainPanel extends DBModelPanel<User> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(HomeAdminMainPanel.class.getName());

	private ListPanel<Site> panel;
	private List<IModel<Site>> list;

	private ListPanel<SigninRow> signinListPanel;
	private List<SigninRow> signinRows;

	private DateRange selectedSigninRange = DateRange.TODAY;
	private WebMarkupContainer signinContainer;

	public HomeAdminMainPanel(String id, IModel<User> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		if (getSessionUser().isPresent()) {
			if (getSessionUser().get().isShowWelcome())
				add(new WelcomePanel("welcomePanel", new ObjectModel<User>(getSessionUser().get())));
			else
				add(new InvisiblePanel("welcomePanel"));
		} else
			add(new InvisiblePanel("welcomePanel"));

		addSites();
		add(new HomeAdminCandidatesPanel("candidatesPanel", getModel()));
		addRecentSignins();
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

	// -------------------------------------------------------------------------
	// Sites
	// -------------------------------------------------------------------------

	protected IModel<String> getObjectTitle(IModel<Site> model) {
		return getObjectTitle(model.getObject());
	}

	private List<IModel<Site>> getList() {
		return list;
	}

	private void loadList() {
		list = new ArrayList<IModel<Site>>();
		getSites(ObjectState.EDITION, ObjectState.PUBLISHED).forEach(i -> list.add(new ObjectModel<Site>(i)));
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
				return HomeAdminMainPanel.this.getObjectTitle(model);
			}

			@Override
			public List<IModel<Site>> getItems() {
				return HomeAdminMainPanel.this.getList();
			}

			@Override
			protected Panel getListItemPanel(IModel<Site> model, ListPanelMode mode) {

				DelleMuseObjectListItemPanel<Site> panel = new DelleMuseObjectListItemPanel<>("row-element", model, mode) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						HomeAdminMainPanel.this.onClick(getModel());
					}

					protected IModel<String> getInfo() {
						return HomeAdminMainPanel.this.getObjectInfo(getModel());
					}

					protected IModel<String> getObjectTitle() {
						return HomeAdminMainPanel.this.getObjectTitle(getModel());
					}

					protected IModel<String> getObjectSubtitle() {
						if (getMode() == ListPanelMode.TITLE)
							return null;
						return HomeAdminMainPanel.this.getObjectSubtitle(getModel());
					}

					@Override
					protected String getImageSrc() {
						return HomeAdminMainPanel.this.getObjectImageSrc(getModel());
					}
				};
				return panel;
			}

			protected void onClick(IModel<Site> model) {
				HomeAdminMainPanel.this.onClick(model);
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

	// -------------------------------------------------------------------------
	// Recent Sign-ins
	// -------------------------------------------------------------------------

	public DateRange getSelectedSigninRange() {
		return selectedSigninRange;
	}

	public void setSelectedSigninRange(DateRange v) {
		this.selectedSigninRange = v;
	}

	private void addRecentSignins() {

		DropDownChoice<DateRange> rangeSelector = new DropDownChoice<DateRange>(
				"signinRangeSelector",
				new PropertyModel<DateRange>(this, "selectedSigninRange"),
				Arrays.asList(DateRange.values()),
				new IChoiceRenderer<DateRange>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getDisplayValue(DateRange object) {
						return getString(object.getKey());
					}

					@Override
					public String getIdValue(DateRange object, int index) {
						return object.name();
					}

					@Override
					public DateRange getObject(String id, IModel<? extends List<? extends DateRange>> choices) {
						return DateRange.valueOf(id);
					}
				});

		signinContainer = new WebMarkupContainer("signinContainer");
		signinContainer.setOutputMarkupId(true);
		add(signinContainer);

		rangeSelector.add(new AjaxFormComponentUpdatingBehavior("change") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				buildSigninList();
				target.add(signinContainer);
			}
		});
		add(rangeSelector);

		buildSigninList();
	}

	private void buildSigninList() {

		ZoneId zoneId = getSessionUser().get().getZoneId();
		OffsetDateTime from = selectedSigninRange.getFrom(zoneId);
		OffsetDateTime to   = selectedSigninRange.getTo(zoneId);

		List<DelleMuseAudit> rawSignins = getAuditService().getRecentSignins(from, to);

		// Keep only the most recent sign-in per user (query is already desc-ordered)
		Map<Long, DelleMuseAudit> latestByUser = new LinkedHashMap<>();
		for (DelleMuseAudit a : rawSignins) {
			if (a.getUser() != null)
				latestByUser.putIfAbsent(a.getUser().getId(), a);
		}

		DateTimeService dts = getDateTimeService();
		signinRows = new ArrayList<>();
		for (DelleMuseAudit a : latestByUser.values()) {
			String name = a.getUser().getDisplayname();
			String ts   = dts.format(a.getLastModified(), getSessionUser().get().getZoneId().getId(), getSessionUser().get().getLocale());
			signinRows.add(new SigninRow(name, ts));
		}

		signinListPanel = new ListPanel<SigninRow>("signinList") {
			private static final long serialVersionUID = 1L;

			@Override
			public IModel<String> getItemLabel(IModel<SigninRow> model) {
				SigninRow row = model.getObject();
				return Model.of("<span class=\"float-start\">"+row.getName()+"</span><span class=\"float-end\">"+row.getTimestamp()+"</span>");
			}

			@Override
			public List<IModel<SigninRow>> getItems() {
				List<IModel<SigninRow>> models = new ArrayList<>();
				for (SigninRow row : signinRows)
					models.add(Model.of(row));
				return models;
			}
		};
		signinListPanel.setHasExpander(false);
		signinListPanel.setLiveSearch(false);
		signinListPanel.setSettings(false);
		signinContainer.addOrReplace(signinListPanel);
	}

	protected DelleMuseAuditDBService getAuditService() {
		return (DelleMuseAuditDBService) ServiceLocator.getInstance().getBean(DelleMuseAuditDBService.class);
	}

	protected DateTimeService getDateTimeService() {
		return (DateTimeService) ServiceLocator.getInstance().getBean(DateTimeService.class);
	}

	// -------------------------------------------------------------------------
	// Inner helper
	// -------------------------------------------------------------------------

	private static class SigninRow implements java.io.Serializable {
		private static final long serialVersionUID = 1L;
		private final String name;
		private final String timestamp;

		SigninRow(String name, String timestamp) {
			this.name = name;
			this.timestamp = timestamp;
		}

		public String getName()      { return name; }
		public String getTimestamp() { return timestamp; }
	}
}