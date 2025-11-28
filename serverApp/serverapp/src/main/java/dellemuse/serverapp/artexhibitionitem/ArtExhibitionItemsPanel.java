package dellemuse.serverapp.artexhibitionitem;

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
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.MultipleSelectorPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.form.FormState;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
 
public class ArtExhibitionItemsPanel extends DBModelPanel<ArtExhibition> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionItemsPanel.class.getName());

	private List<IModel<ArtExhibitionItem>> list;
	private List<IModel<ArtWork>> aList;
	
	private IModel<Site> siteModel;
	
	private FormState state = FormState.VIEW;
	
	private MultipleSelectorPanel<ArtWork> multipleSeletor;
	private WebMarkupContainer addContainer;
	private AjaxLink<Void> add;
	private AjaxLink<Void> close;
	private WebMarkupContainer addContainerButtons;
	private ListPanel<ArtExhibitionItem> itemsPanel;

	 
	/**
	 * 
	 * Artwork name
	 * Artwork artists
	 * 
	 * Floor
	 * Room
	 * Read Code
	 * 
	 * Inflo
	 * 
	 * 
	 * 
	 * @param model
	 * @return
	 */
	
	/**
	 * @param id
	 * @param model
	 */
	
	public ArtExhibitionItemsPanel(String id, IModel<ArtExhibition> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel=siteModel;
		setOutputMarkupId (true );
		
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		addItems();
		addSelector();
	
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.list != null)
			this.list.forEach(t -> t.detach());
		
		if (siteModel!=null)
			siteModel.detach();
		
		if (aList!=null)
			this.aList.forEach(t -> t.detach());
	}
	
	protected IModel<String> getObjectInfo(IModel<ArtExhibitionItem> model) {
		if (!model.getObject().isDependencies())
			model.setObject(  super.findArtExhibitionItemWithDeps(model.getObject().getId()).get());
		return Model.of(TextCleaner.clean( getInfo( model.getObject())));
	}
	
	protected IModel<String> getObjectSubtitle(IModel<ArtExhibitionItem> model) {
		return null;
	}

	protected String getObjectImageSrc(IModel<ArtExhibitionItem> model) {
		return super.getImageSrc(model.getObject());
	}
	
	@Override
	public List<ToolbarItem> getToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		return list;
	}
	
	protected Panel getObjectListItemExpandedPanel(IModel<ArtExhibitionItem> model, ListPanelMode mode) {

		model.setObject( super.findArtExhibitionItemWithDeps(model.getObject().getId()).get() );
		
		return new ObjectListItemExpandedPanel<ArtExhibitionItem>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return ArtExhibitionItemsPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return ArtExhibitionItemsPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return ArtExhibitionItemsPanel.this.getObjectImageSrc(getModel());
			}
			
		};
	}

	
	protected void onObjectRemove(IModel<ArtExhibitionItem> model, AjaxRequestTarget target) {
		ArtExhibitionItem item = model.getObject();
		ArtExhibition ex= getModel().getObject();
		
		super.removeItem(ex, item, getUserDBService().findRoot() );
		resetList();
		target.add(this.itemsPanel);
	}
	
	
	protected void onObjectSelect(IModel<ArtWork> model, AjaxRequestTarget target) {
		ArtWork item = model.getObject();
		ArtExhibition ex= getModel().getObject();
		super.addItem(ex, item, getUserDBService().findRoot() );
		resetList();
		target.add(this.itemsPanel);
	}

	private void resetList() {
		this.list = null;
	}

	
	protected List<IModel<ArtWork>> getArtWorks() {

		if (aList!=null)
			return aList;
		
		aList = new ArrayList<IModel<ArtWork>>();
		getSiteArtWorks( this.siteModel.getObject()).forEach( i -> aList.add( new ObjectModel<ArtWork>(i)));;
		return aList;
	}

	protected WebMarkupContainer getMenu(IModel<ArtExhibitionItem> model) {
		NavDropDownMenu<ArtExhibitionItem> menu = new NavDropDownMenu<ArtExhibitionItem>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};
	
		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss(
				"fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionItem> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionItem>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
					
					
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionItem> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionItem>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						ArtExhibitionItemsPanel.this.onObjectRemove(getModel(), target);
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

	
	
	private List<IModel<ArtExhibitionItem>> getItems() {

		if (this.list == null) {
			this.list = new ArrayList<IModel<ArtExhibitionItem>>();
			getArtExhibitionItems(getModel().getObject()).forEach(item -> this.list.add(new ObjectModel<>(item)));
		}
		return this.list;
	}
	
	 
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
				//target.add(addContainer);
				target.add(multipleSeletor); 
			}
			public boolean isVisible() {
				return getState()==FormState.EDIT;
			}
		};
		
		this.addContainerButtons.add(add);
		this.addContainerButtons.add(close);
	 
		
		this.multipleSeletor = new MultipleSelectorPanel<ArtWork> ("multipleSelector", getArtWorks()) {
			
			private static final long serialVersionUID = 1L;

			protected IModel<String> getTitle() {
					return getLabel("artworks");
			}
			
			public boolean isVisible() {
				return getState()==FormState.EDIT;
			}

			@Override
			protected void onClick(IModel<ArtWork> model) {
				// TODO Auto-generated method stub
			}
			
			@Override
			protected void onObjectSelect(IModel<ArtWork> model, AjaxRequestTarget target) {
				ArtExhibitionItemsPanel.this.onObjectSelect(model, target);
			}
			
			@Override
			protected IModel<String> getObjectInfo(IModel<ArtWork> model) {
				String str = TextCleaner.clean(model.getObject().getInfo(), 280);
				return new Model<String>(str);
			}

			@Override
			protected IModel<String> getObjectSubtitle(IModel<ArtWork> model) {
				return Model.of(getArtistStr(model.getObject()));
			}
			
			@Override
			protected boolean isExpander() {
				return true;
			}
	
			@Override
			protected String getObjectImageSrc(IModel<ArtWork> model) {
				if (model.getObject().getPhoto() != null) {
					Resource photo = getResource(model.getObject().getPhoto().getId()).get();
					return getPresignedThumbnailSmall(photo);
				}
				return null;
			}
		};
		
		this.multipleSeletor.setOutputMarkupId(true);
		
		this.addContainerButtons.add(this.multipleSeletor);
	}
	
	


	private void addItems() {

		this.itemsPanel = new ListPanel<ArtExhibitionItem>("items") {

			private static final long serialVersionUID = 1L;

			protected List<IModel<ArtExhibitionItem>> filter(List<IModel<ArtExhibitionItem>> initialList,
					String filter) {
				List<IModel<ArtExhibitionItem>> list = new ArrayList<IModel<ArtExhibitionItem>>();
				final String str = filter.trim().toLowerCase();
				initialList.forEach(s -> {
					if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
						list.add(s);
					}
				});
				return list;
			}
			
			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<ArtExhibitionItem> model, ListPanelMode mode) {
				return ArtExhibitionItemsPanel.this.getObjectListItemExpandedPanel(model, mode);
				
			}

			@Override
			protected Panel getListItemPanel(IModel<ArtExhibitionItem> model) {
				DelleMuseObjectListItemPanel<ArtExhibitionItem> panel = new DelleMuseObjectListItemPanel<ArtExhibitionItem>("row-element",
						model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected String getImageSrc() {
						return ArtExhibitionItemsPanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						setResponsePage(new ArtExhibitionItemPage(getModel(),ArtExhibitionItemsPanel.this.getItems()));
					}

					@Override
					protected IModel<String> getInfo() {
						return ArtExhibitionItemsPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return ArtExhibitionItemsPanel.this.getMenu(getModel());
					}
				
					@Override
					protected String getTitleIcon() {
						if (getModel().getObject().getAudio()!=null)
							return ServerAppConstant.headphoneIcon;
						else
							return null;
					}

				
				};
				return panel;
			}
		
			@Override
			public List<IModel<ArtExhibitionItem>> getItems()  {
				return  ArtExhibitionItemsPanel.this.getItems();
			}
		};
		add(itemsPanel);

		// panel.setTitle(getLabel("exhibitions-permanent"));
		itemsPanel.setListPanelMode(ListPanelMode.TITLE);
		itemsPanel.setLiveSearch(false);
		itemsPanel.setSettings(true);
		itemsPanel.setHasExpander(true);
	}
	
	
	
	public FormState getState() {
		return this.state;
	}
	
	public void setState( FormState state) {
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

}
