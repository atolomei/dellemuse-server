package dellemuse.serverapp.page;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.NumberFormatter;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.site.DateRange;
import dellemuse.serverapp.page.site.SiteReportsPage;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.StatDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.nav.toolbar.ToolbarItem;

public class HomeReportsPanel extends DBModelPanel<User> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(HomeReportsPanel.class.getName());

	private static class SiteRow implements IDetachable {

		private static final long serialVersionUID = 1L;

		private IModel<Site> model;
		
		private final String name;
		private final long visits;

		public SiteRow(IModel<Site> model, String name, long visits) {
			this.name = name;
			this.visits = visits;
			this.model=model;
		}

		public String getName() {
			return name;
		}

		public long getVisits() {
			return visits;
		}
		
		public void detach() {
			model.detach();
		}
		
		public IModel<Site> getModel() {
			return this.model;
		}
	}
	
	
	
	private DateRange selectedRange = DateRange.YESTERDAY;
	private WebMarkupContainer reportContainer;
	private final List<IModel<Site>> sites;

	
	
	public HomeReportsPanel(String id, IModel<User> model, List<IModel<Site>> sites) {
		super(id, model);
		this.sites = sites;
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		DropDownChoice<DateRange> rangeSelector = new DropDownChoice<DateRange>(
				"rangeSelector",
				new PropertyModel<DateRange>(this, "selectedRange"),
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

		rangeSelector.add(new AjaxFormComponentUpdatingBehavior("change") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				buildReport();
				target.add(reportContainer);
			}
		});
		add(rangeSelector);

		reportContainer = new WebMarkupContainer("reportContainer");
		reportContainer.setOutputMarkupId(true);
		add(reportContainer);

		buildReport();
	}


	@Override
	public List<ToolbarItem> getToolbarItems() {
		return null;
	}

	public DateRange getSelectedRange() {
		return selectedRange;
	}

	public void setSelectedRange(DateRange selectedRange) {
		this.selectedRange = selectedRange;
	}

	protected StatDBService getStatDBService() {
		return (StatDBService) ServiceLocator.getInstance().getBean(StatDBService.class);
	}
	
	private void buildReport() {

		java.util.List<SiteRow> rows = new java.util.ArrayList<>();

		ZoneId zoneId = getSessionUser().get().getZoneId();
		OffsetDateTime from = selectedRange.getFrom(zoneId);
		OffsetDateTime to = selectedRange.getTo(zoneId);

		for (IModel<Site> siteModel : sites) {
			Site site = siteModel.getObject();

			long visits = 0;
			try {
				visits = getStatDBService().countBySiteInRange(site.getId(), from, to);
			} catch (Exception e) {
				logger.error(e);
			}

			String name = super.getObjectTitle(site).getObject();
			rows.add(new SiteRow(siteModel, name, visits));
		}

		ListView<SiteRow> siteListView = new ListView<SiteRow>("siteList", rows) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<SiteRow> item) {

				SiteRow row = item.getModelObject();
				
				Label la = new Label("siteName", row.getName());
				la.setEscapeModelStrings(false);

				Link<Site> li =new Link<Site>("siteLink", row.getModel()) {

					@Override
					public void onClick() {
						SiteReportsPage page = new SiteReportsPage(row.getModel());
						setResponsePage(page);
					}
					
				};
				
				Label lv = new Label("siteVisits", NumberFormatter.formatNumber(row.getVisits(), getSessionUser().get().getLocale()));
				if (row.getVisits() > 0) {
					lv.add(new AttributeModifier("class", "alert alert-info"));
				} else {
					lv.add(new AttributeModifier("class", "alert alert-neutral"));
				}
		
				li.add(la);
				item.add(lv);
				item.add(li);
			}
		};
		reportContainer.addOrReplace(siteListView);
	}

}
