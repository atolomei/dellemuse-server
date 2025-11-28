package dellemuse.serverapp.page;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.component.IRequestablePage;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import io.wktui.form.button.SubmitButton;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.LabelAjaxLinkPanel;

public abstract class MultipleSelectorPanel<T extends DelleMuseObject> extends DBModelPanel<T> {

	static private Logger logger = Logger.getLogger(MultipleSelectorPanel.class.getName());

	private static final long serialVersionUID = 1L;

	private List<IModel<T>> list;

	private WebMarkupContainer titleContainer;
	private WebMarkupContainer itemsContainer;
	private ListPanel<T> listPanel;
	private IModel<String> title;

	protected abstract IModel<String> getObjectSubtitle(IModel<T> model);

	protected abstract String getObjectImageSrc(IModel<T> model);

	public MultipleSelectorPanel(String id, List<IModel<T>> list) {
		this(id, list, null);
	}

	public MultipleSelectorPanel(String id, List<IModel<T>> list, IModel<String> title) {
		super(id, null);
		this.list = list;
		this.title = title;
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		itemsContainer = new WebMarkupContainer("itemsContainer");
		itemsContainer.setOutputMarkupId(true);
		add(itemsContainer);

		titleContainer = new WebMarkupContainer("titleContainer");
		titleContainer.setOutputMarkupId(true);
		titleContainer.setVisible(getTitle() != null);
		add(titleContainer);

		titleContainer.add(new Label("title", getTitle()));

		renderPanel();

	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.list != null)
			this.list.forEach(i -> i.detach());
	}

	protected IModel<String> getTitle() {
		return title;
	}

	protected Panel getObjectListItemExpandedPanel(IModel<T> model, ListPanelMode mode) {

		return new ObjectListItemExpandedPanel<T>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return MultipleSelectorPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return MultipleSelectorPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return MultipleSelectorPanel.this.getObjectImageSrc(getModel());
			}
		};
	}

	private void renderPanel() {

		this.listPanel = new ListPanel<T>("items", getList()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getListItemExpandedPanel(IModel<T> model, ListPanelMode mode) {
				return MultipleSelectorPanel.this.getObjectListItemExpandedPanel(model, mode);
			}

			@Override
			protected Panel getListItemPanel(IModel<T> model, ListPanelMode mode) {

				DelleMuseObjectListItemPanel<T> panel = new DelleMuseObjectListItemPanel<T>("row-element", model, mode) {

					private static final long serialVersionUID = 1L;

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return MultipleSelectorPanel.this.getObjectMenu(getModel());
					}

					@Override
					public void onClick() {
						MultipleSelectorPanel.this.onClick(getModel());
					}

					protected IModel<String> getInfo() {
						return MultipleSelectorPanel.this.getObjectInfo(getModel());
					}

					protected IModel<String> getObjectTitle() {
						return MultipleSelectorPanel.this.getObjectTitle(getModel());
					}

					protected IModel<String> getObjectSubtitle() {
						if (getMode() == ListPanelMode.TITLE)
							return null;
						return MultipleSelectorPanel.this.getObjectSubtitle(getModel());
					}

					@Override
					protected String getImageSrc() {
						return MultipleSelectorPanel.this.getObjectImageSrc(getModel());
					}

				};
				return panel;
			}
		};

		this.listPanel.setHasExpander(isExpander());
		this.listPanel.setSettings(true);
		this.listPanel.setLiveSearch(true);

		this.itemsContainer.addOrReplace(this.listPanel);

	}

	protected boolean isExpander() {
		return true;
	}

	protected IModel<String> getObjectTitle(IModel<T> model) {
		return Model.of(model.getObject().getDisplayname());
	}

	protected abstract IModel<String> getObjectInfo(IModel<T> model);

	protected abstract void onClick(IModel<T> model);

	protected abstract void onObjectSelect(IModel<T> model, AjaxRequestTarget target);

	protected WebMarkupContainer getObjectMenu(IModel<T> model) {
		LabelAjaxLinkPanel<T> b = new LabelAjaxLinkPanel<T>("menu", null, model, "fa-regular fa-plus") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onObjectSelect(getModel(), target);
			}
		};

		b.setLinkCss("btn btn-sm btn-outline-primary");
		return b;

	};

	public List<IModel<T>> getList() {
		return list;
	}

	public void setList(List<IModel<T>> list) {
		this.list = list;
	}

}
