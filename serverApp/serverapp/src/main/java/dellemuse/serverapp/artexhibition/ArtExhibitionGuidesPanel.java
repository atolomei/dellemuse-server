package dellemuse.serverapp.artexhibition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
 
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.artexhibitionguide.ArtExhibitionGuidePage;
import dellemuse.serverapp.artexhibitionguide.GuideContentPage;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.form.FormState;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

public class ArtExhibitionGuidesPanel extends DBModelPanel<ArtExhibition> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ArtExhibitionGuidesPanel.class.getName());

	private List<IModel<ArtExhibitionGuide>> list;
	private FormState state = FormState.VIEW;
	
	
	WebMarkupContainer itemsContainer;
	ListPanel<ArtExhibitionGuide> panel;



	/**
	 * 
	 * 
	 * 
	 * @param id
	 * @param model
	 */
	
	public ArtExhibitionGuidesPanel(String id, IModel<ArtExhibition> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		itemsContainer =new WebMarkupContainer("itemsContainer");
		itemsContainer.setOutputMarkupId(true);
		add(itemsContainer);
		
		addItems();
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.list != null)
			this.list.forEach(t -> t.detach());
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<ArtExhibitionGuide> create = new AjaxButtonToolbarItem<ArtExhibitionGuide>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new SimpleAjaxWicketEvent(ServerAppConstant.action_exhibition_guide_create, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("create");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(create);

		return list;
	}

	
	
	public FormState getState() {
		return this.state;
	}
	
	public void setState(FormState state) {
		this.state=state;
	}
	
	protected void onCancel(AjaxRequestTarget target) {
		setState(FormState.VIEW);
		target.add(this);
	}

	public void onEdit(AjaxRequestTarget target) {
		setState(FormState.EDIT);
		target.add(this);
	}
	
	
	public void onGuideCreate(AjaxRequestTarget target) {
		
		ArtExhibitionGuideDBService service = (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean( ArtExhibitionGuideDBService.class);

		String name = getModel().getObject().getName();

		if (getItems().size()<0)
			name = name + "-"+String.valueOf(getItems().size());

		service.create(name, getModel().getObject(), getRootUser());
	
		resetItems();
		target.add(this.itemsContainer);
	}
	
	
	
	
	protected WebMarkupContainer getMenu(IModel<ArtExhibitionGuide> model) {
		NavDropDownMenu<ArtExhibitionGuide> menu = new NavDropDownMenu<ArtExhibitionGuide>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};
	
		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss(
				"fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionGuide>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("edit");
					}
					
					 
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionGuide>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("delete");
					}

					 
					
					 
				};
			}
		});
		return menu;
	}

	private List<IModel<ArtExhibitionGuide>> getItems() {

		if (this.list == null) {
			this.list = new ArrayList<IModel<ArtExhibitionGuide>>();
			getArtExhibitionIGuides(getModel().getObject()).forEach(item -> this.list.add(new ObjectModel<>(item)));
		}
		return this.list;
	}

	private void resetItems() {
		this.list=null;
	}
	
	
	protected IModel<String> getObjectInfo(IModel<ArtExhibitionGuide> model) {
		
		
		return Model.of( getInfo( model.getObject() ));
	}
	
	
	protected String getObjectImageSrc(IModel<ArtExhibitionGuide> model) {
		return super.getImageSrc(model.getObject());
	}
	
	protected IModel<String> getObjectSubtitle(IModel<ArtExhibitionGuide> model) {
		return null;
	}
	
	


	protected Panel getObjectListItemExpandedPanel(IModel<ArtExhibitionGuide> model, ListPanelMode mode) {

		model.setObject( super.findArtExhibitionGuideWithDeps(model.getObject().getId()).get() );
		
		return new ObjectListItemExpandedPanel<ArtExhibitionGuide>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return ArtExhibitionGuidesPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return ArtExhibitionGuidesPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return ArtExhibitionGuidesPanel.this.getObjectImageSrc(getModel());
			}
		};
	}
	
	private void addItems() {

		 this.panel = new ListPanel<ArtExhibitionGuide>("items") {
			
			private static final long serialVersionUID = 1L;

			public List<IModel<ArtExhibitionGuide>> getItems()  {
				return  ArtExhibitionGuidesPanel.this.getItems();
			}
			
			@Override
			public Integer getTotalItems()  {
				return  Integer.valueOf(ArtExhibitionGuidesPanel.this.getItems().size() );
			}
			
			@Override
			protected List<IModel<ArtExhibitionGuide>> filter(List<IModel<ArtExhibitionGuide>> initialList,
					String filter) {
				List<IModel<ArtExhibitionGuide>> list = new ArrayList<IModel<ArtExhibitionGuide>>();
				final String str = filter.trim().toLowerCase();
				initialList.forEach(s -> {
					if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
						list.add(s);
					}
				});
				return list;
			}


			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<ArtExhibitionGuide> model, ListPanelMode mode) {
				return ArtExhibitionGuidesPanel.this.getObjectListItemExpandedPanel(model, mode);
				
			}
			@Override
			protected Panel getListItemPanel(IModel<ArtExhibitionGuide> model) {
				
				ObjectListItemPanel<ArtExhibitionGuide> panel = new ObjectListItemPanel<ArtExhibitionGuide>("row-element",
						model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected String getImageSrc() {
						return ArtExhibitionGuidesPanel.this.getObjectImageSrc( getModel() );
					}

					@Override
					public void onClick() {
						setResponsePage(new ArtExhibitionGuidePage(getModel(), getList()));
					}

					@Override
					protected IModel<String> getInfo() {
						return ArtExhibitionGuidesPanel.this.getObjectInfo( getModel() );
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return ArtExhibitionGuidesPanel.this.getMenu(getModel());
					}
				};
				return panel;
			}
		};
		
		itemsContainer.add(panel);

		// panel.setTitle(getLabel("exhibitions-permanent"));
		panel.setListPanelMode(ListPanelMode.TITLE);
		panel.setLiveSearch(false);
		panel.setSettings(true);
		panel.setHasExpander(true);

	}

	

}
