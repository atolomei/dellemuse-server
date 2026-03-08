package dellemuse.serverapp.page.site;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artwork.ArtWorkEditor;
import dellemuse.serverapp.editor.DBSiteObjectEditor;
 
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
 
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.event.MenuAjaxEvent;
 
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.BooleanField;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.MultipleSelectField;
import io.wktui.form.field.StaticTextField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.form.field.ZoneIdField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class SitePublicPortalEditor extends DBSiteObjectEditor<Site> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SitePublicPortalEditor.class.getName());

 	private ChoiceField<Boolean> portalPublicEnabledField;
	private StaticTextField<String> urlField;
	

	/**
	 * @param id
	 * @param model
	 */
	public SitePublicPortalEditor(String id, IModel<Site> model) {
		super(id, model);
	}


	@Override
	public void onInitialize() {
		super.onInitialize();

		Optional<Site> o_i = getSiteDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<Site>(o_i.get()));

			
		add(new Label("site-public-portal", getLabel("site-public-portal", getModel().getObject().getMasterLanguage())));

		add(new InvisiblePanel("error"));
 	
		Form<Site> form = new Form<Site>("siteForm", getModel());
		add(form);
		setForm(form);

		Site site = getModel().getObject();
	 	getModel().setObject(site);

		List<Institution> list = new ArrayList<Institution>();
		getInstitutions().forEach(x -> list.add(x));
		
		
		//urlField = new StaticTextField<String>("url", Model.of( getPublicUrl()), getLabel("url"));
		//form.add(urlField);
	 	
		
		Link<Void> link = new Link<Void>("link") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RedirectPage(getPublicUrl()));
			}
		};
		form.add(link);
		
		Label linkLabel = new Label("portal", getPublicUrl());
		link.add(linkLabel);
		
		
		portalPublicEnabledField = new ChoiceField<Boolean>("publicPortalEnabled",new PropertyModel<Boolean>(getModel(), "publicPortalEnabled"), getLabel("publicPortalEnabled")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<Boolean>> getChoices() {
				return new ListModel<Boolean>(b_list);
			}

			@Override
			protected String getDisplayValue(Boolean value) {
				if (value == null)
					return null;
				if (value.booleanValue())
					return getLabel("yes").getObject();
				return getLabel("no").getObject();
			}
		};
		form.add(portalPublicEnabledField);
		
			
		EditButtons<Site> buttons = new EditButtons<Site>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				SitePublicPortalEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				SitePublicPortalEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				SitePublicPortalEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				
				if (!hasWritePermission())
					return false;
				
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		form.add(buttons);

		EditButtons<Site> b_buttons_top = new EditButtons<Site>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				SitePublicPortalEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				SitePublicPortalEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				SitePublicPortalEditor.this.onSave(target);
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


	public String getPublicUrl() {
		return getServerUrl() + "/"+ ServerConstant.AG + "/" + getModel().getObject().getId();
	}

	

	public IModel<Site> getSiteModel() {
		return getModel();
	}
	
	@Override
	public List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<Person> create = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.site_portal_action_edit, target));
			}

			public boolean isVisible() {
				return hasWritePermission();
			}
			
			public boolean isEnabled() {
				return hasWritePermission();
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


	@Override
	public void onDetach() {
		super.onDetach();

		 
	}

	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
		 
	}

	protected void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		SitePublicPortalEditor.this.addOrReplace( new InvisiblePanel("error"));
		target.add(this);

		 
	}
 
	
	protected void onSave(AjaxRequestTarget target) {

		logger.debug("onSave");
		logger.debug("updated parts:");
		getUpdatedParts().forEach(s -> logger.debug(s));
		logger.debug("saving...");

		if ((getUpdatedParts()!=null) && (getUpdatedParts().size()>0)) {
			try {
	
				 
							
				save(getModelObject(), getSessionUser().get(), getUpdatedParts());
				 
				getForm().setFormState(FormState.VIEW);
				getForm().updateReload();
				fireScanAll(new ObjectUpdateEvent(target));
	
			} catch (Exception e) {
	
				addOrReplace(new SimpleAlertRow<Void>("error", e));
				logger.error(e);
	
			}
		}
		target.add(this);
	}

	 

	protected void onSubmit() {

		logger.debug("");
		logger.debug("onSubmit");
		logger.debug(getForm().isSubmitted());
 
		logger.debug("done");
		logger.debug("");
	}

	 
}
