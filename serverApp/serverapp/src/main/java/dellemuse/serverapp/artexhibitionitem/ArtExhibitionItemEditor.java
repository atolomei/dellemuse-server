package dellemuse.serverapp.artexhibitionitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.event.MenuAjaxEvent;

import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

/**
 * 
 * alter table artexhibition add column spec text;
 * 
 */
public class ArtExhibitionItemEditor extends DBObjectEditor<ArtExhibitionItem> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionItemEditor.class.getName());
	
	private TextField<String> nameField;
	private TextField<String> orderField;
	private TextField<String> readCodeField;
	private TextField<String> qrCodeField;
 
	private TextField<String> floorStrField;
	private TextField<String> roomStrField;
	
 	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;
	private IModel<ArtExhibitionItem> artExhibitionItemModel;
	
	/**
	 * @param id
	 * @param model
	 */
	public ArtExhibitionItemEditor(String id, 	IModel<ArtExhibitionItem> model, 
												IModel<ArtExhibition> artExhibitionModel, 
												IModel<Site> siteModel) {
		super(id, model);
		this.artExhibitionModel=artExhibitionModel;
		this.siteModel=siteModel;
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		Form<ArtExhibitionItem> form = new Form<ArtExhibitionItem>("form");
		add(form);
		setForm(form);
	
		/**objectStateField = new ChoiceField<ObjectState>("state", new PropertyModel<ObjectState>(getModel(), "state"), getLabel("state")) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public IModel<List<ObjectState>> getChoices() {
				return new ListModel<ObjectState> (getStates());
			}
			
			@Override
			protected String getDisplayValue(ObjectState value) {
				if (value==null)
					return null;
				return value.getLabel( getLocale());
			}
		};
		*/
		
		
		this.nameField 		= new TextField<String>("name", 	new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		this.floorStrField 	= new TextField<String>("floor", 	new PropertyModel<String>(getModel(), "floorStr"), getLabel("floor"));
		this.roomStrField 	= new TextField<String>("room", 	new PropertyModel<String>(getModel(), "roomStr"), getLabel("room"));
		this.orderField	 	= new TextField<String>("order", 	new PropertyModel<String>(getModel(), "exhibitionOrder"), getLabel("order"));
		this.readCodeField 	= new TextField<String>("readcode", new PropertyModel<String>(getModel(), "readCode"), getLabel("readcode"));
		this.qrCodeField 	= new TextField<String>("qrcode", 	new PropertyModel<String>(getModel(), "qRCode"), getLabel("qrcode"));
		
		form.add(nameField);
		//form.add(objectStateField);
		form.add(floorStrField);
		form.add(roomStrField);
		form.add(orderField);
		form.add(readCodeField);
		form.add(qrCodeField);
		
		EditButtons<ArtExhibitionItem> buttons = new EditButtons<ArtExhibitionItem>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				ArtExhibitionItemEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				ArtExhibitionItemEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				ArtExhibitionItemEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		form.add(buttons);
	 
		EditButtons<ArtExhibitionItem> b_buttons_top = new EditButtons<ArtExhibitionItem>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				 ArtExhibitionItemEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				ArtExhibitionItemEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				ArtExhibitionItemEditor.this.onSave(target);
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
		
		// add(new ArtExhibitionItemsGuidesPanel("items", getModel(), getSiteModel()));
	}
	
	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
		// getForm().setFormState(FormState.VIEW);
		// target.add(getForm());
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		// getForm().setFormState(FormState.EDIT);
		// target.add(getForm());
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		
	List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		AjaxButtonToolbarItem<ArtExhibition> create = new AjaxButtonToolbarItem<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
 				fire(new MenuAjaxEvent(ServerAppConstant.action_exhibition_item_info_edit, target));
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(create);

		return list;
	}

	public IModel<ArtExhibitionItem> getArtExhibitionItemModel() {
		return artExhibitionItemModel;
	}

	public void setArtExhibitionItemModel(IModel<ArtExhibitionItem> artExhibitionItemModel) {
		this.artExhibitionItemModel = artExhibitionItemModel;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
 		
		if (siteModel!=null)
			siteModel.detach();
		
		if (artExhibitionModel!=null)
			artExhibitionModel.detach();
		
		if (artExhibitionItemModel!=null)
			artExhibitionItemModel.detach();
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	public IModel<ArtExhibition> getArtExhibitionModel() {
		return artExhibitionModel;
	}

	public void setArtExhibitionModel(IModel<ArtExhibition> siteModel) {
		this.artExhibitionModel = siteModel;
	}
	
	protected void onSubmit() {
		logger.debug("");
		logger.debug("onSubmit");
		logger.debug("");
	}

	protected void onSave(AjaxRequestTarget target) {
		logger.debug("onSave");
		logger.debug("updated parts:");
		getUpdatedParts().forEach(s -> logger.debug(s));
		logger.debug("saving...");
		
		save(getModelObject(), getSessionUser(), getUpdatedParts());
		
		getForm().setFormState(FormState.VIEW);
		getForm().updateReload();
		fire (new ObjectUpdateEvent(target));
	
		target.add(this);
	}

	

	private void setUpModel() {

		Optional<ArtExhibitionItem> o_i = getArtExhibitionItemDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<ArtExhibitionItem>(o_i.get()));
		
		Optional<ArtExhibition> o_a = getArtExhibitionDBService().findWithDeps(getArtExhibitionModel().getObject().getId());
		setArtExhibitionModel(new ObjectModel<>(o_a.get()));
		
		Optional<Site> o_s = getSiteDBService().findWithDeps(getArtExhibitionModel().getObject().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_s.get()));
	}
	
}
