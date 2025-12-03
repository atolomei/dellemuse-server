package dellemuse.serverapp.artwork;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.record.ArtWorkRecord;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import wktui.base.InvisiblePanel;

public class ArtWorkRecordEditor extends DBObjectEditor<ArtWorkRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtWorkRecordEditor.class.getName());

	static private final List<Boolean> b_list = new ArrayList<Boolean>();
	static {

		b_list.add(Boolean.TRUE);
		b_list.add(Boolean.FALSE);
	}

	private TextField<String> nameField;
	private TextAreaField<String> infoField;
	private TextAreaField<String> specField;
	private IModel<ArtWork> artWorkModel;

	/**
	 * @param id
	 * @param model
	 */
	public ArtWorkRecordEditor(String id, IModel<ArtWork> artWorkModel, IModel<ArtWorkRecord> model) {
		super(id, model);
		this.artWorkModel = artWorkModel;
	}

	public IModel<ArtWork> getArtWorkModel() {
		return this.artWorkModel;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		add(new InvisiblePanel("error"));

		if (getModel().getObject().getName() == null)
			getModel().getObject().setName(getArtWorkModel().getObject().getName());

		Form<ArtWorkRecord> form = new Form<ArtWorkRecord>("form");
		add(form);
		setForm(form);

		Label artWorkRecordInfo = new Label("artWorkRecordInfo", getLabel("artwork-record-info", getModel().getObject().getLanguage()));

		form.add(artWorkRecordInfo);

		this.specField = new TextAreaField<String>("spec", new PropertyModel<String>(getModel(), "spec"), getLabel("spec"), 8);
		this.nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		this.infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 20);

		form.add(nameField);
		form.add(specField);
		form.add(infoField);

		EditButtons<ArtWorkRecord> buttons = new EditButtons<ArtWorkRecord>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				ArtWorkRecordEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				ArtWorkRecordEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				ArtWorkRecordEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		form.add(buttons);

		EditButtons<ArtWorkRecord> b_buttons_top = new EditButtons<ArtWorkRecord>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				ArtWorkRecordEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				ArtWorkRecordEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				ArtWorkRecordEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}

			protected String getSaveClass() {
				return "ps-0 btn btn-sm btn-link";
			}

			protected String getCancelClass() {
				return "ps-0 btn btn-sm btn-link";
			}

		};
		getForm().add(b_buttons_top);
	}

	public Optional<Person> getPerson(Long value) {
		return super.getPerson(value);
	}

	public void onCancel(AjaxRequestTarget target) {
		super.cancel(target);

	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
	}

	public void onSave(AjaxRequestTarget target) {
		try {
			getUpdatedParts().forEach(s -> logger.debug(s));
			save(getModelObject(), getSessionUser(), getUpdatedParts());
			getForm().setFormState(FormState.VIEW);
		} catch (Exception e) {

			addOrReplace(new SimpleAlertRow<Void>("error", e));
			logger.error(e);
		}
		target.add(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.artWorkModel != null)
			this.artWorkModel.detach();
	}

	protected void onSubmit() {
		logger.debug("");
		logger.debug("onSubmit");
		logger.debug("");
	}

	private void setArtWorkModel(ObjectModel<ArtWork> m) {
		this.artWorkModel = m;
	}

	private void setUpModel() {
		Optional<ArtWorkRecord> o_i = getArtWorkRecordDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<ArtWorkRecord>(o_i.get()));

		Optional<ArtWork> o_a = getArtWorkDBService().findWithDeps(getModel().getObject().getArtwork().getId());
		setArtWorkModel(new ObjectModel<ArtWork>(o_a.get()));
	}

}
