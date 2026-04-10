package dellemuse.serverapp.artexhibitionguide;

import java.util.List;
import java.util.Optional;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ExternalImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.IExpandedPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import io.wktui.error.ErrorPanel;
import io.wktui.media.InvisibleImage;
import io.wktui.nav.toolbar.ToolbarItem;
import wktui.base.InvisiblePanel;

public class GuideContentExpandedPanel extends DBModelPanel<GuideContent> implements IExpandedPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(GuideContentExpandedPanel.class.getName());

	private IModel<Site> siteModel;

	public GuideContentExpandedPanel(String id, IModel<GuideContent> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel = siteModel;
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		try {

			add(new InvisiblePanel("error"));

			GuideContent gc = getModel().getObject();

			// Load ArtExhibitionItem -> ArtWork
			ArtExhibitionItem item = getArtExhibitionItemDBService().findWithDeps(gc.getArtExhibitionItem().getId()).get();
			ArtWork aw = item.getArtWork();

			// Thumbnail
			String imgSrc = getImageSrc(item);
			if (imgSrc != null) {
				add(new ExternalImage("thumbnail", imgSrc));
			} else {
				add(new InvisibleImage("thumbnail"));
			}

			// Name
			String name = getLanguageObjectService().getObjectDisplayName(gc, getLocale());
			add(new Label("artworkName", name != null ? name : "-"));

			// Artists
			String artists = (aw != null) ? getArtistStr(aw) : null;
			add(new Label("artists", (artists != null && !artists.isEmpty()) ? artists : "-"));

			// Audio Number
			Long audioNumber = gc.getArtWorkAudioId();
			add(new Label("audioNumber", audioNumber != null ? audioNumber.toString() : "-"));

			// Audio list per language
			buildAudioList(gc);
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("error", e));
			addOrReplace(new InvisibleImage("thumbnail"));
			addOrReplace(new Label("artworkName", "-"));
			addOrReplace(new Label("artists", "-"));
			addOrReplace(new Label("audioNumber", "-"));
			addOrReplace(new InvisiblePanel("audioList"));

		}

	}

	java.util.List<AudioLanguageInfo> audioInfos = new java.util.ArrayList<>();

	private void buildAudioList(GuideContent gc) {

		// Master language audio
		String masterLang = gc.getMasterLanguage();
		Resource masterAudio = gc.getAudio();

		if (masterAudio != null) {
			audioInfos.add(new AudioLanguageInfo(masterLang, new ObjectModel<Resource>(masterAudio)));
		} else {
			audioInfos.add(new AudioLanguageInfo(masterLang, null));
		}

		// Other languages
		if (siteModel != null && siteModel.getObject() != null) {
			for (Language la : siteModel.getObject().getLanguages()) {
				String langCode = la.getLanguageCode();
				if (!langCode.equals(masterLang)) {
					Optional<GuideContentRecord> o = getGuideContentRecordDBService().findByGuideContent(gc, langCode);
					if (o.isPresent()) {
						GuideContentRecord r = getGuideContentRecordDBService().findWithDeps(o.get().getId()).get();
						audioInfos.add(new AudioLanguageInfo(langCode, (r.getAudio() != null ? new ObjectModel<Resource>(r.getAudio()) : null)));
					} else {
						audioInfos.add(new AudioLanguageInfo(langCode, null));
					}
				}
			}
		}

		ListView<AudioLanguageInfo> audioList = new ListView<AudioLanguageInfo>("audioList", audioInfos) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<AudioLanguageInfo> listItem) {

				AudioLanguageInfo info = listItem.getModelObject();

				listItem.add(new Label("language", info.languageCode != null ? info.languageCode.toUpperCase() : "-"));

				if (info.audioModel != null) {
					String presignedUrl = GuideContentExpandedPanel.this.getPresignedUrl(info.audioModel.getObject());

					ExternalLink audioLink = new ExternalLink("audioLink", presignedUrl, info.audioModel.getObject().getName());
					audioLink.add(new org.apache.wicket.AttributeModifier("target", "_blank"));
					listItem.add(audioLink);

					String meta = GuideContentExpandedPanel.this.getAudioMeta(info.audioModel.getObject());
					Label metaLabel = new Label("audioMeta", meta);
					metaLabel.setEscapeModelStrings(false);
					listItem.add(metaLabel);
				} else {
					listItem.add(new ExternalLink("audioLink", "#", getString("no-audio")));
					listItem.add(new Label("audioMeta", ""));
				}
			}
		};

		add(audioList);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();

		audioInfos.forEach(i -> i.detach());

	}

	/**
	 * Helper class to hold audio info per language
	 */
	private static class AudioLanguageInfo implements IDetachable {

		private static final long serialVersionUID = 1L;
		final String languageCode;
		final IModel<Resource> audioModel;

		AudioLanguageInfo(String languageCode, IModel<Resource> audio) {
			this.languageCode = languageCode;
			this.audioModel = audio;
		}

		@Override
		public void detach() {
			if (this.audioModel != null)
				this.audioModel.detach();
		}
	}

}
