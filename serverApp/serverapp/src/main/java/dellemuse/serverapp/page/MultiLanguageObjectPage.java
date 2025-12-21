package dellemuse.serverapp.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.audit.panel.AuditPanel;
import dellemuse.serverapp.editor.ObjectRecordEditor;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageObjectService;
import wktui.base.INamedTab;
import wktui.base.NamedTab;

/**
 * site foto Info - exhibitions
 */

public abstract class MultiLanguageObjectPage<T extends MultiLanguageObject, R extends TranslationRecord> extends ObjectPage<T> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(MultiLanguageObjectPage.class.getName());

	private Map<String, ObjectRecordEditor<T, R>> recordEditors = new HashMap<String, ObjectRecordEditor<T, R>>();

	private Map<String, IModel<R>> recordModels = new HashMap<String, IModel<R>>();

	private Map<String, AuditPanel<R>> auditPanels = new HashMap<String, AuditPanel<R>>();

	protected abstract Optional<R> loadTranslationRecord(String lang);

	protected abstract R createTranslationRecord(String lang);

	IModel<String> displayName;
	IModel<String> subtitle;

	public MultiLanguageObjectPage() {
		super();
	}

	public MultiLanguageObjectPage(PageParameters parameters) {
		super(parameters);
	}

	public MultiLanguageObjectPage(IModel<T> model) {
		super(model);
	}

	public MultiLanguageObjectPage(IModel<T> model, List<IModel<T>> list) {
		super(model, list);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		recordModels.forEach((k, v) -> v.detach());

	}

	@Override
	protected Panel createSearchPanel() {
		return null;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	/**
	public IModel<String> getObjectTitle(Site o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectDisplayName(o, getLocale()));
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + ServerConstant.DELETED_ICON);
		return Model.of(str.toString());
	}***/
/**
	public IModel<String> getObjectTitle(ArtExhibitionGuide o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectDisplayName(o, getLocale()));
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + ServerConstant.DELETED_ICON);
		return Model.of(str.toString());
	}
	**/
/**
	public IModel<String> getObjectTitle(GuideContent o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectDisplayName(o, getLocale()));
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + ServerConstant.DELETED_ICON);
		return Model.of(str.toString());
	}
**/
	public IModel<String> getObjectSubtitle(MultiLanguageObject o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectSubtitle(o, getLocale()));
		return Model.of(str.toString());
	}

	public IModel<String> getObjectTitle(MultiLanguageObject o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectDisplayName(o, getLocale()));
		return Model.of(str.toString());
	}
	
	public LanguageObjectService getLanguageObjectService() {
		return (LanguageObjectService) ServiceLocator.getInstance().getBean(LanguageObjectService.class);
	}

	@Override
	protected List<INamedTab> createInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		List<Language> list = getLanguageService().getLanguages();

		for (Language la : list) {

			if (!la.getLanguageCode().equals(getModel().getObject().getMasterLanguage())) {

				NamedTab tab = new NamedTab(Model.of(la.getLanguageCode()), ServerAppConstant.object_translation_record_info + "-" + la.getLanguageCode(), la.getLanguageCode()) {

					private static final long serialVersionUID = 1L;

					@Override
					public WebMarkupContainer getPanel(String panelId) {
						return getTranslateRecordEditor(panelId, getMoreInfo());
					}
				};
				tabs.add(tab);
			}
		}

		NamedTab audit = new NamedTab(Model.of("audit"), ServerAppConstant.object_audit) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getAuditPanel(panelId);
			}
		};
		tabs.add(audit);

		for (Language la : list) {

			if (!la.getLanguageCode().equals(getModel().getObject().getMasterLanguage())) {

				NamedTab tab = new NamedTab(Model.of(la.getLanguageCode()), ServerAppConstant.object_audit + "-" + la.getLanguageCode(), la.getLanguageCode()) {

					private static final long serialVersionUID = 1L;

					@Override
					public WebMarkupContainer getPanel(String panelId) {
						return getAuditPanel(panelId, getMoreInfo());
					}
				};
				tabs.add(tab);
			}
		}
		return tabs;
	}

	protected Panel getAuditPanel(String id) {
		return new AuditPanel<T>(id, getModel());
	}

	protected Panel getAuditPanel(String id, String lang) {

		if (this.auditPanels.containsKey(lang))
			return this.auditPanels.get(lang);

		R ar = null;

		Optional<R> a = loadTranslationRecord(lang);

		if (a.isEmpty()) {
			ar = createTranslationRecord(lang);
		} else
			ar = a.get();

		IModel<R> translationRecordModel = new ObjectModel<R>(ar);
		AuditPanel<R> e = new AuditPanel<R>(id, translationRecordModel);

		e.setTitle(getLabel("audit-lang", lang));

		this.auditPanels.put(lang, e);
		return e;
	}

	protected Map<String, ObjectRecordEditor<T, R>> getRecordEditors() {
		return this.recordEditors;
	}

	protected void onEditRecord(AjaxRequestTarget target, String lang) {
		getRecordEditors().get(lang).edit(target);
	}

	protected abstract Class<?> getTranslationClass();

	protected IModel<R> getTranslationRecordModel(String lang) {
		if (recordModels.containsKey(lang))
			return recordModels.get(lang);

		R ar = null;
		Optional<R> a = loadTranslationRecord(lang);
		if (a.isEmpty()) {
			ar = createTranslationRecord(lang);
		} else
			ar = a.get();

		IModel<R> translationRecordModel = new ObjectModel<R>(ar);
		recordModels.put(lang, translationRecordModel);

		return recordModels.get(lang);
	}

	protected Panel getTranslateRecordEditor(String id, String lang) {

		if (this.recordEditors.containsKey(lang))
			return this.recordEditors.get(lang);

		/**
		 * R ar = getTranslationRecordModel(lang);
		 * 
		 * Optional<R> a = loadTranslationRecord(lang); if (a.isEmpty()) { ar =
		 * createTranslationRecord(lang); } else ar = a.get();
		 **/

		IModel<R> translationRecordModel = getTranslationRecordModel(lang);
		ObjectRecordEditor<T, R> e = new ObjectRecordEditor<T, R>(id, getModel(), translationRecordModel);

		e.setIntroVisible(isIntroVisible());
		e.setSpecVisible(isSpecVisible());
		e.setOpensVisible(isOpensVisible());
		e.setAudioVisible(isAudioVisible());
		e.setInfoVisible(isInfoVisible());

		this.recordEditors.put(lang, e);
		return e;
	}

	protected boolean isInfoVisible() {
		return true;
	}

	protected boolean isOpensVisible() {
		return false;
	}

	protected boolean isAudioVisible() {
		return false;
	}

	protected boolean isIntroVisible() {
		return false;
	};

	protected boolean isSpecVisible() {
		return false;
	};

}
