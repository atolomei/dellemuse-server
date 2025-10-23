package dellemuse.serverapp.artexhibitionguide;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
 
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.artexhibition.ArtExhibitionItemPage;
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
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.ArtExhibitionItemDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
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

public class ArtExhibitionGuideContentsPanel extends DBModelPanel<ArtExhibitionGuide> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	 
	static private Logger logger = Logger.getLogger(ArtExhibitionGuideContentsPanel.class.getName());

	private List<IModel<GuideContent>> list;

	private List<IModel<ArtExhibitionItem>> aList;
	
	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;
	
	
	private FormState state = FormState.VIEW;
	
	private MultipleSelectorPanel<ArtExhibitionItem> multipleSeletor;
	private WebMarkupContainer addContainer;
	private AjaxLink<Void> add;
	private AjaxLink<Void> close;
	private WebMarkupContainer addContainerButtons;
	private ListPanel<GuideContent> itemsPanel;

	 
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
	
	public ArtExhibitionGuideContentsPanel(String id, IModel<ArtExhibitionGuide> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel=siteModel;
		setOutputMarkupId (true );
		
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		ArtExhibition ae = getModel().getObject().getArtExhibition();
		setArtExhibitionModel(new ObjectModel<ArtExhibition>(ae));

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
		
		if ( artExhibitionModel!=null)
			 artExhibitionModel.detach();
			 
		if (aList!=null)
			this.aList.forEach(t -> t.detach());
	}
	



	@Override
	public List<ToolbarItem> getToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		return list;
	}
	
	
	protected IModel<String> getObjectInfo(IModel<GuideContent> model) {
		if (!model.getObject().isDependencies()) {
			model.setObject(  super.findGuideContentWithDeps(model.getObject().getId()).get());
		}
		return Model.of(TextCleaner.clean(getInfo( model.getObject())));
	}
	
	protected IModel<String> getObjectSubtitle(IModel<GuideContent> model) {
		if (model.getObject().getSubtitle()!=null)
			return Model.of( model.getObject().getSubtitle());
		return null;
	}

	protected String getObjectImageSrc(IModel<GuideContent> model) {
		return super.getImageSrc(model.getObject());
	}
	
	
	
	protected Panel getObjectListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {

		
		GuideContent gc = super.findGuideContentWithDeps(model.getObject().getId()).get();
		
		
		model.setObject(gc);
		
		return new ObjectListItemExpandedPanel<GuideContent>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return ArtExhibitionGuideContentsPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return ArtExhibitionGuideContentsPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return ArtExhibitionGuideContentsPanel.this.getObjectImageSrc(getModel());
			}
			
			@Override
			protected String getIcon() {
				return isAudio( getModel() ) ? "fa-solid fa-headphones iconOver" :null;
			}
			
		};
	}

	
	protected void onObjectRemove(IModel<GuideContent> model, AjaxRequestTarget target) {
		GuideContent item = model.getObject();
		ArtExhibitionGuide ex= getModel().getObject();
		
		getGuideContentDBService().delete(item);
		
		//super.removeItem(ex, item, getUserDBService().findRoot() );
		
		resetList();
		target.add(this.itemsPanel);
	}
	
	
	protected void onObjectSelect(IModel<ArtExhibitionItem> model, AjaxRequestTarget target) {

		ArtExhibitionItem item = model.getObject();
		ArtExhibition ex= getArtExhibitionModel().getObject();

		super.addItem(getModel().getObject(), item, getUserDBService().findRoot() );
		resetList();
		target.add(this.itemsPanel);
	}

	private void resetList() {
		this.list = null;
	}

	
	protected List<IModel<ArtExhibitionItem>> getArtExhibitionItems() {

		if (aList!=null)
			return aList;
		
		aList = new ArrayList<IModel<ArtExhibitionItem>>();
		super.getArtExhibitionItems(this.getArtExhibitionModel().getObject()).forEach( i -> aList.add( new ObjectModel<ArtExhibitionItem>(i)));;
		return aList;
	}

	protected WebMarkupContainer getMenu(IModel<GuideContent> model) {
		NavDropDownMenu<GuideContent> menu = new NavDropDownMenu<GuideContent>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};
	
		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss(
				"fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<GuideContent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<GuideContent> getItem(String id) {

				return new AjaxLinkMenuItem<GuideContent>(id) {

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

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<GuideContent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<GuideContent> getItem(String id) {

				return new AjaxLinkMenuItem<GuideContent>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						ArtExhibitionGuideContentsPanel.this.onObjectRemove(getModel(), target);
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

	
	
	private List<IModel<GuideContent>> getItems() {

		if (this.list == null) {
			this.list = new ArrayList<IModel<GuideContent>>();
			super.getGuideContents(getModel().getObject()).forEach(item -> this.list.add(new ObjectModel<GuideContent>(item)));
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
	 
		
		this.multipleSeletor = new MultipleSelectorPanel<ArtExhibitionItem> ("multipleSelector", getArtExhibitionItems()) {
			
			private static final long serialVersionUID = 1L;

			protected IModel<String> getTitle() {
					return getLabel("exhibition-artworks");
			}
			
			public boolean isVisible() {
				return getState()==FormState.EDIT;
			}

			@Override
			protected void onClick(IModel<ArtExhibitionItem> model) {
				// TODO Auto-generated method stub
			}
			
			@Override
			protected void onObjectSelect(IModel<ArtExhibitionItem> model, AjaxRequestTarget target) {
				ArtExhibitionGuideContentsPanel.this.onObjectSelect(model, target);
			}
			
			@Override
			protected IModel<String> getObjectInfo(IModel<ArtExhibitionItem> model) {
				String str = TextCleaner.clean(model.getObject().getInfo(), 280);
				return new Model<String>(str);
			}

			@Override
			protected IModel<String> getObjectSubtitle(IModel<ArtExhibitionItem> model) {
				return Model.of(getArtistStr(model.getObject()));
			}
			
			@Override
			protected boolean isExpander() {
				return true;
			}
	
			@Override
			protected String getObjectImageSrc(IModel<ArtExhibitionItem> model) {
				return super.getImageSrc(model.getObject());
			}
		};
		
		this.multipleSeletor.setOutputMarkupId(true);
		
		this.addContainerButtons.add(this.multipleSeletor);
	}
	
	
	private void addItems() {

		this.itemsPanel = new ListPanel<GuideContent>("items") {

			private static final long serialVersionUID = 1L;

			protected List<IModel<GuideContent>> filter(List<IModel<GuideContent>> initialList,
					String filter) {
				List<IModel<GuideContent>> list = new ArrayList<IModel<GuideContent>>();
				final String str = filter.trim().toLowerCase();
				initialList.forEach(s -> {
					if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
						list.add(s);
					}
				});
				return list;
			}
			
			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {
				return ArtExhibitionGuideContentsPanel.this.getObjectListItemExpandedPanel(model, mode);
				
			}

			@Override
			protected Panel getListItemPanel(IModel<GuideContent> model) {
				
				ObjectListItemPanel<GuideContent> panel = new ObjectListItemPanel<GuideContent>("row-element", model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected String getImageSrc() {
						return ArtExhibitionGuideContentsPanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						setResponsePage(new GuideContentPage(getModel(), ArtExhibitionGuideContentsPanel.this.getItems()));
					}

					@Override
					protected IModel<String> getInfo() {
						return ArtExhibitionGuideContentsPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return ArtExhibitionGuideContentsPanel.this.getMenu(getModel());
					}
					
					@Override
					protected String getIcon() {
						return isAudio( getModel() ) ? "fa-solid fa-headphones iconOver" :null;
					}

					
				};
				return panel;
			}
		
			@Override
			public List<IModel<GuideContent>> getItems()  {
				return  ArtExhibitionGuideContentsPanel.this.getItems();
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

	public IModel<ArtExhibition> getArtExhibitionModel() {
		return artExhibitionModel;
	}

	public void setArtExhibitionModel(IModel<ArtExhibition> artExhibitionModel) {
		this.artExhibitionModel = artExhibitionModel;
	}

	protected boolean isAudio(IModel<GuideContent> model) {
		return model.getObject().getAudio()!=null;
	}

}
