package dellemuse.serverapp.audiostudio;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import io.wktui.form.Form;
import io.wktui.form.field.StaticTextField;
import io.wktui.form.field.TextAreaField;
 
/**
 *
 * 
 */
public class InfoAudioStudioEditor extends BaseAudioStudioEditor {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(InfoAudioStudioEditor.class.getName());

	private StaticTextField<String> nameField;
	private StaticTextField<String> typeField;
	private StaticTextField<String> languageField;
	private TextAreaField<String> infoField;
	private Form<AudioStudio> form;
		
	 
	public InfoAudioStudioEditor(String id, IModel<AudioStudio> model ) {
		super(id, model );
	}
 
	 
	@Override
	public void onInitialize() {
		super.onInitialize();
		setup();
	}
	
	private void setup() {
 
		getModel().getObject().setName(super.getParentName());
		getModel().getObject().setInfo(super.getParentInfo());
		
		form = new Form<AudioStudio>("form");

		add(form);
		setForm(form);

		nameField = new StaticTextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		typeField = new StaticTextField<String>("type", getParentType(), getLabel("type"));
		languageField = new StaticTextField<String>("language", new PropertyModel<String>(getModel(), "language"), getLabel("language"));
		infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 12) {
			private static final long serialVersionUID = 1L;
			public boolean isEnabled() {
				return false;
			};
		};
		form.add(infoField);
		form.add(nameField);
		form.add(typeField);
		form.add(languageField);
	}
}
