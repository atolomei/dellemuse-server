package dellemuse.serverapp.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import dellemuse.model.util.NumberFormatter;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
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
 * 
 * @param <T>
 */
public class DBObjectEditor<T> extends DBModelPanel<T> implements Editor<T> {

	private static final long serialVersionUID = 1L;

	private Form<T> form;
//	private IModel<T> model;
	private boolean readonly = false;
	private List<String> updatedParts = new ArrayList<String>();

	public DBObjectEditor(String id, IModel<T> model) {
		super(id, model);
		super.setOutputMarkupId(true);
	}

	@Override
	public boolean isReadOnly() {
		return readonly;
	}

	public void cancel(AjaxRequestTarget target) {

		getForm().setFormState(FormState.VIEW);

		getForm().visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				// if (focus==null) {
				// target.focusComponent(field.getInput());
				// focus = field;
				// }
				field.editOff();
			}
		});

		/**
		 * getForm().visitChildren(ObjectEditorPanel.class, new
		 * IVisitor<ObjectEditorPanel<?>, Void>() {
		 * 
		 * @Override public void component(ObjectEditorPanel<?> panel, IVisit<Void>
		 *           visit) { panel.cancel(); } }); getForm().visitChildren(Field.class,
		 *           new IVisitor<Field<?>, Void>() {
		 * @Override public void component(Field<?> field, IVisit<Void> visit) {
		 *           field.cancel(); } });
		 **/
		target.add(this);
	}

	public void edit(final AjaxRequestTarget target) {
		getForm().setFormState(FormState.EDIT);
		getForm().visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				// if (focus==null) {
				// target.focusComponent(field.getInput());
				// focus = field;
				// }
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
				// if (focus==null) {
				// target.focusComponent(field.getInput());
				// focus = field;
				// }
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

	public void save(ArtExhibition a) {
		ArtExhibitionDBService service = (ArtExhibitionDBService) ServiceLocator.getInstance()
				.getBean(ArtExhibitionDBService.class);
		service.save(a);
	}
	
	public void save(Institution inst) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance()
				.getBean(InstitutionDBService.class);
		service.save(inst);
	}

	public void save(Site site) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		service.save(site);
	}

	public void save(ArtWork artwork) {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		service.save(artwork);
	}

	protected String getMimeType(String clientFileName) {
		return super.getResourceDBService().getMimeType(clientFileName);
	}

}
