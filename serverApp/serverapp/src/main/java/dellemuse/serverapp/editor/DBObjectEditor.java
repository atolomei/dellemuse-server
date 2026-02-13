package dellemuse.serverapp.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import dellemuse.model.util.NumberFormatter;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AudioStudio;

import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Music;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.Voice;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionItemRecord;
import dellemuse.serverapp.serverdb.model.record.ArtWorkRecord;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import dellemuse.serverapp.serverdb.model.record.SiteRecord;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionItemDBService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.AudioStudioDBService;

import dellemuse.serverapp.serverdb.service.GuideContentDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.MusicDBService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.VoiceDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionItemRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtWorkRecordDBService;
import dellemuse.serverapp.serverdb.service.record.InstitutionRecordDBService;
import dellemuse.serverapp.serverdb.service.record.SiteRecordDBService;
import io.wktui.editor.Editor;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.field.Field;

/**
 * 
 * reset edit [new value] ------------------------- remove [id] add [id]
 * 
 * op.
 * 
 * edit -> reset -> relationship ->
 * 
 * id value
 * 
 * @param <T>
 */
public class DBObjectEditor<T> extends DBModelPanel<T> implements Editor<T> {

	private static final long serialVersionUID = 1L;

	static public final List<Boolean> b_list = new ArrayList<Boolean>();
	static {
		b_list.add(Boolean.TRUE);
		b_list.add(Boolean.FALSE);
	}

	static public final List<ObjectState> b_state = new ArrayList<ObjectState>();
	static {

		b_state.add(ObjectState.EDITION);
		b_state.add(ObjectState.DELETED);
		b_state.add(ObjectState.PUBLISHED);
	}

	private Form<T> form;
	private boolean readonly = false;
	private List<String> updatedParts = new ArrayList<String>();

	
	public boolean hasWritePermission() {
		return true;
	}
	
	
	public DBObjectEditor(String id, IModel<T> model) {
		super(id, model);
		super.setOutputMarkupId(true);
	}

	
	protected Locale getUserLocale() {
		return getSessionUser().get().getLocale();
	}

	protected List<Language> getLanguages() {
		return getLanguageService().getLanguagesSorted( getLocale() );
	}
	
	@Override
	public boolean isReadOnly() {
		return readonly;
	}

	public List<ObjectState> getStates() {
		return b_state;
	}

	public void cancel(AjaxRequestTarget target) {

		getForm().setFormState(FormState.VIEW);

		getForm().visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				field.editOff();
			}
		});
		target.add(this);
	}

	public void edit() {
		getForm().setFormState(FormState.EDIT);
		getForm().visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				field.editOn();

			}
		});
	}

	public void edit(final AjaxRequestTarget target) {
		getForm().setFormState(FormState.EDIT);
		getForm().visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				field.editOn();

			}
		});
		target.add(this);
	}

	public void updateModel() {
		getForm().visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				field.updateModel();
			}
		});

	}

	public void submit(final AjaxRequestTarget target) {
		getForm().setFormState(FormState.VIEW);
		getForm().visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				field.editOff();
			}
		});
		target.add(this);
	}

	public boolean isEditionEnabled() {
		return getForm().getFormState() == FormState.EDIT;
	}

	public Form<T> getForm() {
		return this.form;
	}

	public void setForm(Form<T> form) {
		this.form = form;
	}

	public void reset() {
		updatedParts.clear();
	}

	public void setReadOnly(boolean re) {
		this.readonly = re;
	}

	public T getModelObject() {
		return getModel().getObject();
	}

	public void setUpdatedPart(String updatedPart) {

		if (updatedPart == null)
			return;

		if (updatedPart.length() == 0)
			return;

		if (!updatedParts.contains(updatedPart))
			updatedParts.add(updatedPart);
	}

	public List<String> getUpdatedParts() {
		return updatedParts;
	}

	@Override
	public void onDetach() {

		if (getModel() != null)
			getModel().detach();

		if (getForm() != null)
			getForm().detach();

		super.onDetach();
	}

	public void update(AjaxRequestTarget target) {
	}

	public String formatFileSize(long size) {
		return NumberFormatter.formatFileSize(size);
	}

	public void save(AudioStudio a) {
		AudioStudioDBService service = (AudioStudioDBService) ServiceLocator.getInstance().getBean(AudioStudioDBService.class);
		service.save(a);
	}

	public void save(GuideContent a) {
		GuideContentDBService service = (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);
		service.save(a);
	}

	public void save(ArtExhibitionItem a) {
		ArtExhibitionItemDBService service = (ArtExhibitionItemDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class);
		service.save(a);
	}

	public void save(ArtExhibitionItemRecord ir) {
		ArtExhibitionItemRecordDBService service = (ArtExhibitionItemRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemRecordDBService.class);
		service.save(ir);
	}

	public void save(ArtExhibitionGuide a) {
		ArtExhibitionGuideDBService service = (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
		service.save(a);
	}

	public void save(ArtExhibition a, User user) {
		ArtExhibitionDBService service = (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
		service.save(a, user);
	}

	public void save(ArtWork modelObject, User user, List<String> updatedParts) {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		service.save(modelObject, user, updatedParts);
	}


	public void save( Voice modelObject, User user, List<String> updatedParts) {
		VoiceDBService service = (VoiceDBService) ServiceLocator.getInstance().getBean(VoiceDBService.class);
		service.save(modelObject, user, updatedParts);
	}
	
	
	public void save( Music modelObject, User user, List<String> updatedParts) {
		MusicDBService service = (MusicDBService) ServiceLocator.getInstance().getBean(MusicDBService.class);
		service.save(modelObject, user, updatedParts);
	}
	
	
	public void save(ArtExhibitionItem modelObject, User user, List<String> updatedParts) {
		ArtExhibitionItemDBService service = (ArtExhibitionItemDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class);
		service.save(modelObject, user, updatedParts);
	}

	public void save(ArtExhibition modelObject, User user, List<String> updatedParts) {
		ArtExhibitionDBService service = (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
		service.save(modelObject, user, updatedParts);
	}

	public void save(ArtExhibitionGuide modelObject, User user, List<String> updatedParts) {
		ArtExhibitionGuideDBService service = (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
		service.save(modelObject, user, updatedParts);
	}

	public void save(GuideContent modelObject, User user, List<String> updatedParts) {
		GuideContentDBService service = (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);
		service.save(modelObject, user, updatedParts);
	}

	public void save(Institution inst, User user, List<String> updatedParts) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
		service.save(inst, user, updatedParts);
	}

	public void save(Person person, User user, List<String> updatedParts) {
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		service.save(person, user, updatedParts);
	}

	public void save(User u, User user, List<String> updatedParts) {
		UserDBService service = (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
		service.save(u, user, updatedParts);
	}

	public void save(InstitutionRecord ir) {
		InstitutionRecordDBService service = (InstitutionRecordDBService) ServiceLocator.getInstance().getBean(InstitutionRecordDBService.class);
		service.save(ir);
	}

	public void save(Site site, User user, List<String> updatedParts) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		service.save(site, user, updatedParts);
	}

	public void save(SiteRecord ir) {
		SiteRecordDBService service = (SiteRecordDBService) ServiceLocator.getInstance().getBean(SiteRecordDBService.class);
		service.save(ir);
	}

	public void save(ArtWork artwork) {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		service.save(artwork);
	}

	public void save(ArtWorkRecord o) {
		ArtWorkRecordDBService service = (ArtWorkRecordDBService) ServiceLocator.getInstance().getBean(ArtWorkRecordDBService.class);
		service.save(o);
	}

	/** ------------------------ **/

	protected String getMimeType(String clientFileName) {
		return super.getResourceDBService().getMimeType(clientFileName);
	}

	protected String normalizeFileName(String name) {
		String str = name.replaceAll("[^\\x00-\\x7F]|[\\s]+", "-").toLowerCase().trim();
		str=str.replace("'", "");
		str = str.replace(".", "");
		if (str.length() < 100)
			return str;
		return str.substring(0, 100);
	}

	protected IModel<String> getLabel(String key) {
		return new StringResourceModel(key, this, null);
	}

	protected String getLabelString(String key) {
		return getLabel(key).getObject();
	}

	protected String getLabelString(String key, String... parameter) {
		return getLabel(key, parameter).getObject();
	}

	protected IModel<String> getLabel(String key, String... parameter) {
		StringResourceModel model = new StringResourceModel(key, this, null);
		model.setParameters((Object[]) parameter);
		return model;
	}

}
