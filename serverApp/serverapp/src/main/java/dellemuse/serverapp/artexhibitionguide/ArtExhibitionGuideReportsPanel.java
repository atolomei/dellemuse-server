package dellemuse.serverapp.artexhibitionguide;

import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.site.DateRange;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverapp.serverdb.service.GuideContentDBService;
import dellemuse.serverapp.serverdb.service.StatDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.nav.toolbar.ToolbarItem;

public class ArtExhibitionGuideReportsPanel extends DBModelPanel<ArtExhibitionGuide> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionGuideReportsPanel.class.getName());

	private DateRange selectedRange = DateRange.YESTERDAY;
	private WebMarkupContainer reportContainer;
	private final IModel<Site> siteModel;

	public ArtExhibitionGuideReportsPanel(String id, IModel<ArtExhibitionGuide> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel = siteModel;
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

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
	}

	private void buildReport() {

		ArtExhibitionGuide guide = getModel().getObject();
		ZoneId zoneId = getSessionUser().get().getZoneId();
		OffsetDateTime from = selectedRange.getFrom(zoneId);
		OffsetDateTime to = selectedRange.getTo(zoneId);
		NumberFormat nf = NumberFormat.getInstance(Locale.US);

		// Total sessions = guide page visits + guide content visits
		long guidePageSessions = 0;
		long contentSessions = 0;
		try {
			guidePageSessions = getStatDBService().countByArtExhibitionGuideInRange(guide.getId(), from, to);
			contentSessions = getStatDBService().countGuideContentsByArtExhibitionGuideInRange(guide.getId(), from, to);
		} catch (Exception e) {
			logger.error(e);
		}
		long totalSessions = guidePageSessions + contentSessions;
		
		Label ts= new Label("totalSessions", nf.format(totalSessions));
		
		if (totalSessions > 0) {
			ts.add(new AttributeModifier("class", "alert alert-info"));
		}
		else {
			ts.add(new AttributeModifier("class", "alert alert-neutral"));
		}
		
		reportContainer.addOrReplace(ts);

		// Per guide content breakdown
		java.util.List<ContentRow> rows = new java.util.ArrayList<>();
		try {
			// Load guide with dependencies to access guide contents
			Optional<ArtExhibitionGuide> oGuide = getArtExhibitionGuideDBService().findWithDeps(guide.getId());
			if (oGuide.isPresent()) {
				List<GuideContent> contents = oGuide.get().getGuideContents();
				if (contents != null) {
					int count = 0;
					for (GuideContent gc : contents) {
						if (count >= 200)
							break;
						long sessions = getStatDBService().countByGuideContentInRange(gc.getId(), from, to);
						String artWorkName = "";
						try {
							Optional<ArtWork> oAw = getGuideContentDBService().getArtWork(gc);
							if (oAw.isPresent()) {
								ArtWork a = getArtWorkDBService().findById(oAw.get().getId()).get();
								artWorkName = a.getName();
							}
						} catch (Exception e) {
							logger.error(e);
						}
						rows.add(new ContentRow(artWorkName, sessions));
						count++;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		// Sort by sessions descending
		//rows.sort((a, b) -> Long.compare(b.getSessions(), a.getSessions()));


		rows.sort((a, b) -> a.getArtWorkName().compareToIgnoreCase(b.getArtWorkName()));

		
		ListView<ContentRow> contentListView = new ListView<ContentRow>("contentList", rows) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ContentRow> item) {
				ContentRow row = item.getModelObject();
				item.add(new Label("artWorkName", row.getArtWorkName()));

				Label la=new Label("contentSessions", nf.format(row.getSessions()));
				
				if (row.getSessions() > 0) {
					la.add(new AttributeModifier("class", "alert alert-info"));
				}
				else {
					la.add(new AttributeModifier("class", "alert alert-neutral"));
				}
				item.add(la);
			}
		};
		reportContainer.addOrReplace(contentListView);
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

	protected ArtExhibitionGuideDBService getArtExhibitionGuideDBService() {
		return (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
	}

	protected GuideContentDBService getGuideContentDBService() {
		return (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);
	}

	/**
	 * Inner class to hold report data for each GuideContent.
	 */
	private static class ContentRow implements java.io.Serializable {

		private static final long serialVersionUID = 1L;

		private final String artWorkName;
		private final long sessions;

		public ContentRow(String artWorkName, long sessions) {
			this.artWorkName = artWorkName;
			this.sessions = sessions;
		}

		public String getArtWorkName() {
			return artWorkName;
		}

		public long getSessions() {
			return sessions;
		}
	}
}
