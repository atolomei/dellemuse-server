package dellemuse.serverapp.institution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.NumberField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;

public class InstitutionRecordEditor extends DBObjectEditor<InstitutionRecord>  {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionRecordEditor.class.getName());

	static private final List<Boolean> b_list = new ArrayList<Boolean>();
	static {
		
		 b_list.add(Boolean.TRUE );
		 b_list.add(Boolean.FALSE);
	}

	private TextField<String> 				nameField;
	private TextAreaField<String> 			infoField;
	private TextAreaField<String> 			specField;

	IModel<Institution> institutionModel;
	
	
	/**
	 * @param id
	 * @param model
	 */
	public InstitutionRecordEditor(String id, IModel<Institution> artWorkModel, IModel<InstitutionRecord> model) {
		super(id, model);
		this.institutionModel=artWorkModel;
	}

	
	public IModel<Institution> getArtWorkModel() {
		return this.institutionModel;
	}
	
	
	private void setUpModel() {
		Optional<InstitutionRecord> o_i = getInstitutionRecordDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<InstitutionRecord>(o_i.get()));
		
		Optional<Institution> o_a = getInstitutionDBService().findWithDeps(getModel().getObject().getInstitution().getId());
		setInstitutionModel(new ObjectModel<Institution>(o_a.get()));
	}
	

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		
		 if (getModel().getObject().getName()==null)
			 getModel().getObject().setName( getArtWorkModel().getObject().getName());
		 
		
		 Form<InstitutionRecord> form = new Form<InstitutionRecord>("form");
			add(form);
			setForm(form);
		 
			
		Label InstitutionRecordInfo = new Label("InstitutionRecordInfo", getLabel("artwork-record-info", getModel().getObject().getLanguage()));
		
		form.add(InstitutionRecordInfo);
		
		 
		
	 	this.specField  = new TextAreaField<String>				("spec", new PropertyModel<String>(getModel(), "spec"), getLabel("spec"), 8		);
		this.nameField  = new TextField<String>					("name", new PropertyModel<String>(getModel(), "name"), getLabel("name")		);
		this.infoField  = new TextAreaField<String>				("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 20	);
	
		form.add(nameField);
		form.add(specField);
		form.add(infoField);
		
		
		EditButtons<InstitutionRecord> buttons = new EditButtons<InstitutionRecord>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				InstitutionRecordEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				InstitutionRecordEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				InstitutionRecordEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		form.add(buttons);

		 
	
		EditButtons<InstitutionRecord> b_buttons_top = new EditButtons<InstitutionRecord>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				InstitutionRecordEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				InstitutionRecordEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				InstitutionRecordEditor.this.onSave(target);
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
		// getForm().setFormState(FormState.VIEW);
		// target.add(getForm());
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		// getForm().setFormState(FormState.EDIT);
		// target.add(getForm());
	}

	public void onSave(AjaxRequestTarget target) {
		logger.debug("onSave");
		logger.debug("updated parts:");
		getUpdatedParts().forEach(s -> logger.debug(s));
		logger.debug("saving...");
	
		save(getModelObject());
	 
		getForm().setFormState(FormState.VIEW);
		logger.debug("done");
		target.add(this);
	
		// ---
		// TODO AT
		// fir e( ne w (target));
		// --
	}

	


	@Override
	public void onDetach() {
		super.onDetach();

		if (this.institutionModel!=null)
			this.institutionModel.detach();
	
	}
 
	protected void onSubmit() {
		logger.debug("");
		logger.debug("onSubmit");
		logger.debug("");
	}

 	private void setInstitutionModel(ObjectModel<Institution> m) {
		this.institutionModel=m;
	}

 

}
