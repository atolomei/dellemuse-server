package dellemuse.serverapp.page.site;

import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.NumberFormatter;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.StatDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.error.ErrorPanel;
import io.wktui.nav.toolbar.ToolbarItem;

public class SiteReportsPanel extends DBModelPanel<Site> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteReportsPanel.class.getName());

	private DateRange selectedRange = DateRange.YESTERDAY;
	private WebMarkupContainer reportContainer;

	public SiteReportsPanel(String id, IModel<Site> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		try {

			// Date range selector
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

			// Report container (refreshed via AJAX)
			reportContainer = new WebMarkupContainer("reportContainer");
			reportContainer.setOutputMarkupId(true);
			add(reportContainer);

			buildReport();

		} catch (Exception e) {
			logger.error(e);
			add(new ErrorPanel("error", e));
		}
	}

	private void buildReport() {

		Site site = getModel().getObject();
		ZoneId zoneId = getSessionUser().get().getZoneId();
		OffsetDateTime from = selectedRange.getFrom(zoneId);
		OffsetDateTime to = selectedRange.getTo(zoneId);

		// Site visits
		long siteVisits = 0;
		try {
			siteVisits = getStatDBService().countBySiteInRange(site.getId(), from, to);
		} catch (Exception e) {
			logger.error(e);
		}
		Label siteVisitsLabel = new Label("siteVisits", NumberFormatter.formatNumber(siteVisits, getSessionUser().get().getLocale()));
		
		if (siteVisits > 0) {
			siteVisitsLabel.add(new AttributeModifier("class", "alert alert-info"));
		} else {
			siteVisitsLabel.add(new AttributeModifier("class", "alert alert-neutral"));
		}
		reportContainer.addOrReplace(siteVisitsLabel);

		// Art Exhibition Guides
		List<ArtExhibition> exhibitions = getSiteDBService().getArtExhibitions(site);

		// Collect all guides across all exhibitions
		java.util.List<GuideReportRow> rows = new java.util.ArrayList<>();

		for (ArtExhibition ex : exhibitions) {
			List<ArtExhibitionGuide> guides = getArtExhibitionDBService().getArtExhibitionGuides(ex, ObjectState.PUBLISHED, ObjectState.EDITION);
			for (ArtExhibitionGuide guide : guides) {

				long totalSessions = 0;
				try {
					totalSessions = getStatDBService().countTotalByArtExhibitionGuideInRange(guide.getId(), from, to);
				} catch (Exception e) {
					totalSessions = -1;
					logger.error(e);
				}

				String guideName = getObjectTitle(guide).getObject();
				rows.add(new GuideReportRow(guideName, totalSessions));
			}
		}

		ListView<GuideReportRow> guideListView = new ListView<GuideReportRow>("guideList", rows) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<GuideReportRow> item) {
				GuideReportRow row = item.getModelObject();
				item.add( (new Label("guideName", row.getName())).setEscapeModelStrings(false));

				Label ts = new Label("guideTotalSessions", NumberFormatter.formatNumber( row.getTotalSessions(), getSessionUser().get().getLocale()));
				if (row.getTotalSessions() > 0) {
					ts.add(new AttributeModifier("class", "alert alert-info"));
				} else {
					ts.add(new AttributeModifier("class", "alert alert-neutral"));
				}
				item.add(ts);
			}
		};
		reportContainer.addOrReplace(guideListView);
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

	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}

	protected ArtExhibitionDBService getArtExhibitionDBService() {
		return (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
	}

	/**
	 * Inner class to hold report data for each ArtExhibitionGuide.
	 */
	private static class GuideReportRow implements java.io.Serializable {

		private static final long serialVersionUID = 1L;

		private final String name;
		private final long totalSessions;

		public GuideReportRow(String name, long totalSessions) {
			this.name = name;
			this.totalSessions = totalSessions;
		}

		public String getName() {
			return name;
		}

		public long getTotalSessions() {
			return totalSessions;
		}
	}
}
