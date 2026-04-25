package dellemuse.serverapp.guidecontent;

import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.NumberFormatter;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.DateRange;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.GuideContentDBService;
import dellemuse.serverapp.serverdb.service.StatDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.nav.toolbar.ToolbarItem;

public class GuideContentReportsPanel extends DBModelPanel<GuideContent> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(GuideContentReportsPanel.class.getName());

	private DateRange selectedRange = DateRange.YESTERDAY;
	private WebMarkupContainer reportContainer;
	private final IModel<Site> siteModel;
	private List<IModel<GuideContent>> other;

	public GuideContentReportsPanel(String id, IModel<GuideContent> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel = siteModel;
		setOutputMarkupId(true);
	}

	public void onDetach() {
		super.onDetach();
		if (siteModel != null) {
			siteModel.detach();
		}

		if (other != null) {
			for (IModel<GuideContent> m : other) {
				m.detach();
			}
		}

	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		// Date range selector
		DropDownChoice<DateRange> rangeSelector = new DropDownChoice<DateRange>("rangeSelector", new PropertyModel<DateRange>(this, "selectedRange"), Arrays.asList(DateRange.values()), new IChoiceRenderer<DateRange>() {

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

	protected List<IModel<GuideContent>> getOtherContentsModel() {
		if (other == null) {
			List<GuideContent> tempOtherContents = new java.util.ArrayList<>();
			Optional<ArtWork> aw = getGuideContentDBService().getArtWork(getModel().getObject());
			if (aw.isPresent()) {
				tempOtherContents = getGuideContentDBService().getByArtWorkId(aw.get().getId());
				final Long currentId = getModel().getObject().getId();
				tempOtherContents.removeIf(gc -> gc.getId().equals(currentId));

				// Initialize ArtExhibitionGuide for each related GuideContent to avoid
				// LazyInitializationException
				for (int i = 0; i < tempOtherContents.size(); i++) {
					GuideContent gc = tempOtherContents.get(i);
					tempOtherContents.set(i, getGuideContentDBService().findWithDeps(gc.getId()).orElse(gc));
				}
			}
			other = tempOtherContents.stream().map(gc -> (IModel<GuideContent>) new ObjectModel<GuideContent>(gc)).collect(Collectors.toList());
		}
		return other;
	}

	private void buildReport() {

		ZoneId zoneId = getSessionUser().get().getZoneId();
		OffsetDateTime from = selectedRange.getFrom(zoneId);
		OffsetDateTime to = selectedRange.getTo(zoneId);

		long totalSessions = 0;
		try {
			totalSessions = getStatDBService().countByGuideContentInRange(getModel().getObject().getId(), from, to);
		} catch (Exception e) {
			logger.error(e);
		}
		Label ts = new Label("totalSessions", NumberFormatter.formatNumber(totalSessions, getSessionUser().get().getLocale()));
		if (totalSessions > 0) {
			ts.add(new AttributeModifier("class", "alert alert-info"));
		} else {
			ts.add(new AttributeModifier("class", "alert alert-neutral"));
		}
		reportContainer.addOrReplace(ts);

		// Per-language breakdown
		Site site = siteModel.getObject();
		List<Language> siteLanguages = site.getLanguages();

		if (siteLanguages == null) {
			siteLanguages = new java.util.ArrayList<>();
		}

		Language masterLanguage = Language.of(site.getMasterLanguage());
		siteLanguages.add(masterLanguage);

		// siteLanguages.sort((a, b) ->
		// a.getLanguageCode().compareToIgnoreCase(b.getLanguageCode()));

		java.util.List<LangRow> langRows = new java.util.ArrayList<>();
		Locale userLocale = getSessionUser().get().getLocale();

		/**
		 * if (masterLanguage != null) { long masterVisits = 0; try { masterVisits =
		 * getStatDBService().countByGuideContentAndLanguageInRange(getModel().getObject().getId(),
		 * masterLanguage.getLanguageCode(), from, to); } catch (Exception e) {
		 * logger.error(e); } langRows.add(new
		 * LangRow(masterLanguage.getLabel(userLocale), masterVisits)); }
		 **/

		for (Language lang : siteLanguages) {
			long langVisits = 0;
			try {
				langVisits = getStatDBService().countByGuideContentAndLanguageInRange(getModel().getObject().getId(), lang.getLanguageCode(), from, to);
			} catch (Exception e) {
				logger.error(e);
			}
			langRows.add(new LangRow(lang.getLabel(userLocale), langVisits));
		}

		langRows.sort((a, b) -> a.getLabel().compareToIgnoreCase(b.getLabel()));

		ListView<LangRow> langListView = new ListView<LangRow>("langList", langRows) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<LangRow> item) {
				LangRow row = item.getModelObject();
				item.add(new Label("langLabel", row.getLabel()));

				Label lv = new Label("langVisits", NumberFormatter.formatNumber(row.getVisits(), userLocale));
				if (row.getVisits() > 0) {
					lv.add(new AttributeModifier("class", "alert alert-info"));
				} else {
					lv.add(new AttributeModifier("class", "alert alert-neutral"));
				}
				item.add(lv);
			}
		};

		reportContainer.addOrReplace(langListView);

		// Other GuideContent from same Artwork
		WebMarkupContainer otherGuidesContainer = new WebMarkupContainer("otherGuidesContainer");

		otherGuidesContainer.setVisible(!getOtherContentsModel().isEmpty());

		otherGuidesContainer.add(new ListView<IModel<GuideContent>>("otherGuidesList", getOtherContentsModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<IModel<GuideContent>> item) {
				IModel<GuideContent> gc = item.getModelObject();

				AjaxLink<GuideContent> link = new AjaxLink<GuideContent>("otherGuideLink", gc) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {

						GuideContentPage p = new GuideContentPage(getModel());
						p.setStartTab(ServerAppConstant.guide_content_reports);
						setResponsePage(p);
					}
				};
				item.add(link);

				link.add(new Label("otherGuideName",

						getObjectTitle(getArtExhibitionGuideDBService().findById(gc.getObject().getArtExhibitionGuide().getId()).get()))

				);
			}
		});

		reportContainer.addOrReplace(otherGuidesContainer);
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

	protected GuideContentDBService getGuideContentDBService() {
		return (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);
	}

	private static class LangRow implements java.io.Serializable {

		private static final long serialVersionUID = 1L;

		private final String label;
		private final long visits;

		public LangRow(String label, long visits) {
			this.label = label;
			this.visits = visits;
		}

		public String getLabel() {
			return label;
		}

		public long getVisits() {
			return visits;
		}
	}
}
