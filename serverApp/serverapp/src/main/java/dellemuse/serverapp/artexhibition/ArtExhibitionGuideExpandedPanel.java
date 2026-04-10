package dellemuse.serverapp.artexhibition;

import java.util.List;
import java.util.Optional;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ExternalImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.IExpandedPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import io.wktui.media.InvisibleImage;
import io.wktui.nav.toolbar.ToolbarItem;

public class ArtExhibitionGuideExpandedPanel extends DBModelPanel<ArtExhibitionGuide>  implements IExpandedPanel  {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionGuideExpandedPanel.class.getName());

	java.util.List<AudioLanguageInfo> audioInfos = new java.util.ArrayList<>();

	public ArtExhibitionGuideExpandedPanel(String id, IModel<ArtExhibitionGuide> model) {
		super(id, model);
		setOutputMarkupId(true);
		logger.debug("Creating expanded panel for guide -> " + model.getObject().getName());
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		ArtExhibitionGuide guide = getModel().getObject();

		// Thumbnail
		String imgSrc = getImageSrc(guide);

		logger.debug("Thumbnail for guide -> " + guide.getName() + ": " + (imgSrc != null ? imgSrc : "no image"));
		
		if (imgSrc != null) {
			add(new ExternalImage("thumbnail", imgSrc));
		} else {
			add(new InvisibleImage("thumbnail"));
		}

		// Name
		String name = getLanguageObjectService().getObjectDisplayName(guide, getLocale());
		add(new Label("guideName", (name != null ? name : "")));

		// Subtitle
		String subtitle = guide.getSubtitle();
		add( (new Label("subtitle", (subtitle != null && !subtitle.isEmpty()) ? subtitle : "")).setVisible((subtitle != null && !subtitle.isEmpty())));

		// Type
		String type = guide.isAccessible() ? getString("type-accessible") : getString("type-general");
		add(new Label("guideType", type));

		
		add(new Label("audioid",  (guide.getArtExhibitionAudioId()!=null? guide.getArtExhibitionAudioId().toString() :"")));
		
		// Audio list per language
		buildAudioList(guide);
	}

	private void buildAudioList(ArtExhibitionGuide guide) {

		// Master language audio (intro audio)
		String masterLang = guide.getMasterLanguage();
		Resource masterAudio = guide.getAudio();

		if (masterAudio != null) {
			audioInfos.add(new AudioLanguageInfo(masterLang, new ObjectModel<Resource>(masterAudio)));
		} else {
			audioInfos.add(new AudioLanguageInfo(masterLang, null));
		}

		// Other languages
		ArtExhibition ae = guide.getArtExhibition();
		if (ae != null) {
			ArtExhibition aeWithDeps = findArtExhibitionWithDeps(ae.getId()).orElse(null);
			if (aeWithDeps != null) {
				Site site = aeWithDeps.getSite();
				if (site != null) {
					for (Language la : site.getLanguages()) {
						String langCode = la.getLanguageCode();
						if (!langCode.equals(masterLang)) {
							Optional<ArtExhibitionGuideRecord> o = getArtExhibitionGuideRecordDBService().findByArtExhibitionGuide(guide, langCode);
							if (o.isPresent()) {
								ArtExhibitionGuideRecord r = getArtExhibitionGuideRecordDBService().findWithDeps(o.get().getId()).get();
								audioInfos.add(new AudioLanguageInfo(langCode, (r.getAudio() != null ? new ObjectModel<Resource>(r.getAudio()) : null)));
							} else {
								audioInfos.add(new AudioLanguageInfo(langCode, null));
							}
						}
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
					String presignedUrl = ArtExhibitionGuideExpandedPanel.this.getPresignedUrl(info.audioModel.getObject());

					ExternalLink audioLink = new ExternalLink("audioLink", presignedUrl, info.audioModel.getObject().getName());
					audioLink.add(new org.apache.wicket.AttributeModifier("target", "_blank"));
					listItem.add(audioLink);

					String meta = ArtExhibitionGuideExpandedPanel.this.getAudioMeta(info.audioModel.getObject());
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
		audioInfos.forEach(i -> i.detach());
	}

	 

	/**
	 * Helper class to hold audio info per language
	 */
	private static class AudioLanguageInfo implements IDetachable {
		private static final long serialVersionUID = 1L;
		final String languageCode;
		final IModel<Resource> audioModel;

		AudioLanguageInfo(String languageCode, IModel<Resource> audioModel) {
			this.languageCode = languageCode;
			this.audioModel = audioModel;
		}

		@Override
		public void detach() {
			if (this.audioModel != null)
				this.audioModel.detach();
		}
	}
}
