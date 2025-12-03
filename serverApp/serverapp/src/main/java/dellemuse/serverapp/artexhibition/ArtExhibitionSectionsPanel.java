package dellemuse.serverapp.artexhibition;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
 
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.MultipleSelectorPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.ArtExhibitionSectionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.UIEvent;
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
import io.wktui.struct.list.ListPanelWicketEvent;

public class ArtExhibitionSectionsPanel extends DBModelPanel<ArtExhibition> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	 
	static private Logger logger = Logger.getLogger(ArtExhibitionSectionsPanel.class.getName());

	private List<IModel<ArtExhibitionSection>> list;
	
	private IModel<Site> siteModel;
	
	private FormState state = FormState.VIEW;
	
	 
	private WebMarkupContainer addContainer;
	private AjaxLink<Void> add;
	private AjaxLink<Void> close;
	private WebMarkupContainer addContainerButtons;
	private ListPanel<ArtExhibitionSection> sectionsPanel;

	
	
	
	public ArtExhibitionSectionsPanel(String id, IModel<ArtExhibition> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel=siteModel;
		setOutputMarkupId (true );
		
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		addSections();
		// addSelector();
	
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.list != null)
			this.list.forEach(t -> t.detach());
		
		if (siteModel!=null)
			siteModel.detach();
		
		 
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
	
	protected IModel<String> getObjectInfo(IModel<ArtExhibitionSection> model) {
		if (!model.getObject().isDependencies())
			model.setObject(  super.findArtExhibitionSectionWithDeps(model.getObject().getId()).get());
		return Model.of(TextCleaner.clean( getInfo( model.getObject())));
	}
	
	protected IModel<String> getObjectSubtitle(IModel<ArtExhibitionSection> model) {
		return null;
	}

	protected String getObjectImageSrc(IModel<ArtExhibitionSection> model) {
		return super.getImageSrc(model.getObject());
	}
	
	@Override
	public List<ToolbarItem> getToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		return list;
	}
	
	protected Panel getObjectListItemExpandedPanel(IModel<ArtExhibitionSection> model, ListPanelMode mode) {

		model.setObject( super.findArtExhibitionSectionWithDeps(model.getObject().getId()).get() );
		
		return new ObjectListItemExpandedPanel<ArtExhibitionSection>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return ArtExhibitionSectionsPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return ArtExhibitionSectionsPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return ArtExhibitionSectionsPanel.this.getObjectImageSrc(getModel());
			}
		};
	}

	
	protected void onObjectRemove(IModel<ArtExhibitionSection> model, AjaxRequestTarget target) {
		ArtExhibitionSection item = model.getObject();
		ArtExhibition ex= getModel().getObject();
		
		super.removeSection(ex, item, getUserDBService().findRoot() );
		resetList();
		target.add(this.sectionsPanel);
	}
	
	
	protected void onObjectSelect(IModel<ArtWork> model, AjaxRequestTarget target) {
		ArtWork item = model.getObject();
		ArtExhibition ex= getModel().getObject();
		super.addItem(ex, item, getUserDBService().findRoot() );
		resetList();
		target.add(this.sectionsPanel);
	}

	protected WebMarkupContainer getMenu(IModel<ArtExhibitionSection> model) {
		NavDropDownMenu<ArtExhibitionSection> menu = new NavDropDownMenu<ArtExhibitionSection>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};
	
		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss(
				"fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionSection>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionSection> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionSection>(id) {

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

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionSection>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionSection> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionSection>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						ArtExhibitionSectionsPanel.this.onObjectRemove(getModel(), target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("remove");
					}
				};
			}
		});
		return menu;
	}

	
	
	 /**
	private void addSelector() {
		
		this.addContainerButtons = new WebMarkupContainer("addContainerButtons");
		this.addContainerButtons.setOutputMarkupId(true);

		add(this.addContainerButtons);
		
		this.add = new AjaxLink<Void>("add") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				setState( FormState.EDIT);
				target.add(addContainerButtons);
				//target.add(addContainer);
				
			}
			
			public boolean isVisible() {
				return getState()==FormState.VIEW;
			}
		};
		
		this.close = new AjaxLink<Void>("close") {

			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				setState( FormState.VIEW);
				target.add(addContainerButtons);
			}
			public boolean isVisible() {
				return getState()==FormState.EDIT;
			}
		};
		
		this.addContainerButtons.add(add);
		this.addContainerButtons.add(close);
	}
	**/
	
	private void resetList() {
		this.list = null;
	}
	 

	private void addSections() {

		this.sectionsPanel = new ListPanel<ArtExhibitionSection>("items") {

			private static final long serialVersionUID = 1L;

			protected List<IModel<ArtExhibitionSection>> filter(List<IModel<ArtExhibitionSection>> initialList,
					String filter) {
				List<IModel<ArtExhibitionSection>> list = new ArrayList<IModel<ArtExhibitionSection>>();
				final String str = filter.trim().toLowerCase();
				initialList.forEach(s -> {
					if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
						list.add(s);
					}
				});
				return list;
			}
			
			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<ArtExhibitionSection> model, ListPanelMode mode) {
				return ArtExhibitionSectionsPanel.this.getObjectListItemExpandedPanel(model, mode);
				
			}

			@Override
			protected Panel getListItemPanel(IModel<ArtExhibitionSection> model) {
				DelleMuseObjectListItemPanel<ArtExhibitionSection> panel = new DelleMuseObjectListItemPanel<ArtExhibitionSection>("row-element",
						model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected String getImageSrc() {
						return ArtExhibitionSectionsPanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						//setResponsePage(new ArtExhibitionSectionPage(getModel(),ArtExhibitionSectionsPanel.this.getItems()));
					}

					@Override
					protected IModel<String> getInfo() {
						return ArtExhibitionSectionsPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return ArtExhibitionSectionsPanel.this.getMenu(getModel());
					}
				};
				return panel;
			}
		
			@Override
			public List<IModel<ArtExhibitionSection>> getItems()  {
				return  ArtExhibitionSectionsPanel.this.getItems();
			}
			
			//@Override
			//protected void setItems(List<IModel<ArtExhibitionSection>> list) {
			//	ArtExhibitionSectionsPanel.this.setItems(list);
			//}
			
		};
		add(sectionsPanel);

	 	sectionsPanel.setListPanelMode(ListPanelMode.TITLE);
		sectionsPanel.setLiveSearch(false);
		sectionsPanel.setSettings(true);
		sectionsPanel.setHasExpander(true);
	}
	
	private void setItems(List<IModel<ArtExhibitionSection>> list) {
		 this.list = list;
	}

	private List<IModel<ArtExhibitionSection>> getItems() {
		if (this.list == null) {
			this.list = new ArrayList<IModel<ArtExhibitionSection>>();
			getArtExhibitionSections(getModel().getObject()).forEach(item -> this.list.add(new ObjectModel<>(item)));
		}
		return this.list;
	}

	
	
	

}
