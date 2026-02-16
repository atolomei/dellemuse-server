package dellemuse.serverapp.artist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;

import org.apache.wicket.model.PropertyModel;

import org.apache.wicket.model.util.ListModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artwork.ArtWorkEditor;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SiteInfoEditor;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.ObjectType;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.MultipleSelectField;
import io.wktui.form.field.StaticTextField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class ArtistEditor extends DBObjectEditor<Artist> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoEditor.class.getName());

	private ChoiceField<ObjectState> objectStateField;
	private TextField<String> nicknameField;
	private TextAreaField<String> infoField;
	private MultipleSelectField<Site> mSiteField;

	private List<IModel<Site>> selected;
	private List<IModel<Site>> choices;

	/**
	 * @param id
	 * @param model
	 */
	public ArtistEditor(String id, IModel<Artist> model) {
		super(id, model);
	}
	
	 

	private void setUpModel() {
		
		getModel().setObject(getArtistDBService().findWithDeps(getModel().getObject().getId()).get());
		
		selected = new ArrayList<IModel<Site>>();
		choices = new ArrayList<IModel<Site>>();

		
		
		Set<Site> set = getModel().getObject().getArtistSites();
		if (set != null && set.size() > 0) {
		 	set.forEach(i -> selected.add(new ObjectModel<Site>(i)));

		 	
		 	selected.sort( new Comparator<IModel<Site>> () {
				@Override
				public int compare(IModel<Site> o1, IModel<Site>  o2) {
					return o1.getObject().getName().compareToIgnoreCase(o2.getObject().getName());
				}
		 	});
		
		}
		getSiteDBService().findAllSorted(ObjectState.EDITION, ObjectState.PUBLISHED).forEach(a -> choices.add(new ObjectModel<Site>(a)));
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		add(new InvisiblePanel("error"));

		Form<Artist> form = new Form<Artist>("artistForm", getModel());
		form.setOutputMarkupId(true);

		add(form);
		setForm(form);

		form.setFormState(FormState.VIEW);
		
		
		form.add(new StaticTextField<String>("name", new PropertyModel<String>(getModel(), "firstLastname"), getLabel("name") ));

		mSiteField = new MultipleSelectField<Site>("sites", getSelected(), getLabel("sites"), getChoices()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getObjectTitle(IModel<Site> model) {
				return ArtistEditor.this.getObjectTitle(model.getObject());
			}

			@Override
			protected IModel<String> getObjectSubtitle(IModel<Site> model) {
				return ArtistEditor.this.getObjectSubtitle(model.getObject());
			}

			@Override
			protected void onObjectRemove(IModel<Site> model, AjaxRequestTarget target) {
				ArtistEditor.this.getSelected().remove(model);
				target.add(getForm());
			}

			@Override
			protected void onObjectSelect(IModel<Site> model, AjaxRequestTarget target) {
				ArtistEditor.this.getSelected().add(model);
				target.add(getForm());
			}

		};
		form.add(mSiteField);
		

		objectStateField = new ChoiceField<ObjectState>("state", new PropertyModel<ObjectState>(getModel(), "state"), getLabel("state")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<ObjectState>> getChoices() {
				return new ListModel<ObjectState>(getStates());
			}

			@Override
			protected String getDisplayValue(ObjectState value) {
				if (value == null)
					return null;
				return value.getLabel(getSessionUser().get().getLocale());
			}

			@Override
			protected String getIdValue(ObjectState value) {
				return String.valueOf(value.getId());
			}
		};

		form.add(objectStateField);

		infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 10);
		nicknameField = new TextField<String>("nickname", new PropertyModel<String>(getModel(), "nickname"), getLabel("nickname"));

		form.add(nicknameField);
		form.add(infoField);

		EditButtons<Artist> buttons = new EditButtons<Artist>("buttons", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				ArtistEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				ArtistEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				ArtistEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {

				if (!hasWritePermission())
					return false;

				return getForm().getFormState() == FormState.EDIT;
			}
		};

		getForm().add(buttons);

		EditButtons<Artist> b_buttons_top = new EditButtons<Artist>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				ArtistEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				ArtistEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				ArtistEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {

				if (!hasWritePermission())
					return false;

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

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.selected != null)
			this.selected.forEach(i -> i.detach());

		if (this.choices != null)
			this.choices.forEach(i -> i.detach());

	}

	public List<IModel<Site>> getSelected() {
		return selected;
	}

	public void setSelected(List<IModel<Site>> selected) {
		this.selected = selected;
	}

	public List<IModel<Site>> getChoices() {
		return choices;
	}

	public void setChoices(List<IModel<Site>> choices) {
		this.choices = choices;
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<Artist> create = new AjaxButtonToolbarItem<Artist>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_artist_edit, target));
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

	public void onCancel(AjaxRequestTarget target) {
		getForm().setFormState(FormState.VIEW);
		target.add(getForm());
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		target.add(getForm());
	}

	public void onSave(AjaxRequestTarget target) {

		try {


			if (getUpdatedParts() == null || getUpdatedParts().size() == 0) {
				target.add(this);
				return;
			}
			
			if (this.getSelected() != null) {
				Set<Site> set = new HashSet<Site>();
				getSelected().forEach(i-> set.add(i.getObject()));
				getModel().getObject().setArtistSites(set);
			}
			else {
				getModel().getObject().setArtistSites(null);
			}
			
			save(getModelObject(), getSessionUser().get(), getUpdatedParts());

			getForm().setFormState(FormState.VIEW);
			getForm().updateReload();
			fireScanAll(new ObjectUpdateEvent(target));

		} catch (Exception e) {

			addOrReplace(new SimpleAlertRow<Void>("error", e));
			logger.error(e);

		}

		target.add(this);

	}

}
