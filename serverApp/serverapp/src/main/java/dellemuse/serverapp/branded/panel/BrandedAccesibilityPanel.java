package dellemuse.serverapp.branded.panel;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.serverapp.page.model.ObjectModelPanel;
import dellemuse.serverapp.serverdb.model.AccesibilityMode;

import dellemuse.serverapp.serverdb.model.User;


public class BrandedAccesibilityPanel extends ObjectModelPanel<User> {

	private static final long serialVersionUID = 1L;
	
	private String srcUrl;
	private DropDownChoice<AccesibilityMode> selector;
	private List<AccesibilityMode> modes;
	private AccesibilityMode mode;
	
	private AjaxLink<Void> link;
	
	public BrandedAccesibilityPanel(String id) {
		this(id, null,  null);
	}
	
	
	public BrandedAccesibilityPanel(String id, IModel<User> model) {
		super(id, model);
		this.setOutputMarkupId(true);
	}
	
	
	public BrandedAccesibilityPanel(String id,  IModel<User> userModel, String srcUrl) {
		super(id, userModel);
		this.srcUrl=srcUrl;
		this.setOutputMarkupId(true);
		
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		
		setMode(AccesibilityMode.GENERAL);
		
		link =new AjaxLink<Void>("link") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				if (getMode()==AccesibilityMode.GENERAL)
					setMode(AccesibilityMode.ACCESIBLE);
				else
					setMode(AccesibilityMode.GENERAL);
				target.add(BrandedAccesibilityPanel.this);
			}
			
		};
		add(link);
		
		
		this.modes = AccesibilityMode.getModes();
	
		this.selector = new DropDownChoice<AccesibilityMode>("modes", getModes()) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			protected String getNullValidDisplayValue() {
				return "null";
			}

			@Override
			public boolean isNullValid() {
				return false;
			}

			@Override
			public boolean isRequired() {
				return true;
			}

			@Override
			protected void onComponentTag(final ComponentTag tag) {
				// IValueMap attributes = tag.getAttributes();
				// if (autofocus())
				// attributes.putIfAbsent("autofocus", "");
				super.onComponentTag(tag);
			}
		};

		selector.setModel(new PropertyModel<AccesibilityMode>(this, "mode"));

		selector.setChoiceRenderer(new ChoiceRenderer<AccesibilityMode>() {

			private static final long serialVersionUID = 1L;

			public String getIdValue(AccesibilityMode value, int index) {
				return BrandedAccesibilityPanel.this.getIdValue(value);
			};

			public String getDisplayValue(AccesibilityMode value) {
				return BrandedAccesibilityPanel.this.getDisplayValue(value);
			};
		});
 
		//add(selector);
	}

	protected String getDisplayValue(AccesibilityMode value) {
		return value.getLabel(getLocale());
	}

	protected String getIdValue(AccesibilityMode value) {
		return String.valueOf( value.getId());
	}
	
	public String getSrcUrl() {
		return srcUrl;
	}

	public DropDownChoice<AccesibilityMode> getSelector() {
		return selector;
	}

	public List<AccesibilityMode> getModes() {
		return modes;
	}

	public AccesibilityMode getMode() {
		return mode;
	}

	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}

	public void setSelector(DropDownChoice<AccesibilityMode> selector) {
		this.selector = selector;
	}

	public void setModes(List<AccesibilityMode> modes) {
		this.modes = modes;
	}

	public void setMode(AccesibilityMode mode) {
		this.mode = mode;
	}
}
