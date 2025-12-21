package dellemuse.serverapp.artexhibitionitem;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
 
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibition.ArtExhibitionGuidesPanel;
import dellemuse.serverapp.artexhibitionguide.ArtExhibitionGuideContentsPanel;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.model.TextCleaner;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

public class ArtExhibitionItemsGuidesPanel extends DBModelPanel<ArtExhibitionItem> implements InternalPanel {

	private static final long serialVersionUID = 1L;
	 
	static private Logger logger = Logger.getLogger(ArtExhibitionItemsGuidesPanel.class.getName());

	private List<IModel<GuideContent>> list;
	private IModel<Site> siteModel;
	private ListPanel<GuideContent> itemsPanel;
	private WebMarkupContainer noAudios;

	 
	/**
 	 * @param model
	 * @return
	 */
	
	public ArtExhibitionItemsGuidesPanel(String id, IModel<ArtExhibitionItem> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel=siteModel;
		setOutputMarkupId(false);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		addItems();
		
		 WebMarkupContainer noAudios = new  WebMarkupContainer("no-audios") {
			 
			 private static final long serialVersionUID = 1L;
	
			 public boolean isVisible() {
				 return  getItems().size()==0;
			 }
		 };
		 
		 add(noAudios);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.list != null)
			this.list.forEach(t -> t.detach());
		
		if (siteModel!=null)
			siteModel.detach();
	}
	
	protected IModel<String> getObjectInfo(IModel<GuideContent> model) {
		if (!model.getObject().isDependencies())
			model.setObject(  super.findGuideContentWithDeps(model.getObject().getId()).get());
		
		
		String audioGuide = model.getObject().getArtExhibitionGuide().getDisplayname();
		
		StringBuilder str = new StringBuilder();
		str.append(getLabel("audio-guide").getObject()+": " + audioGuide +"\n");
		str.append(TextCleaner.clean(getInfo(model.getObject())));
		
		return Model.of( str.toString());
	}
	
	protected IModel<String> getObjectSubtitle(IModel<GuideContent> model) {
		return null;
	}

	protected String getObjectImageSrc(IModel<GuideContent> model) {
		return super.getImageSrc(model.getObject());
	}
	
	@Override
	public List<ToolbarItem> getToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		return list;
	}
	
	protected Panel getObjectListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {

		model.setObject(super.findGuideContentWithDeps(model.getObject().getId()).get() );
		
		return new ObjectListItemExpandedPanel<GuideContent>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return ArtExhibitionItemsGuidesPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return ArtExhibitionItemsGuidesPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return ArtExhibitionItemsGuidesPanel.this.getObjectImageSrc(getModel());
			}
		};
	}

	
  	private void addItems() {

		this.itemsPanel = new ListPanel<GuideContent>("items") {

			private static final long serialVersionUID = 1L;

			protected List<IModel<GuideContent>> filter(List<IModel<GuideContent>> initialList,
					String filter) {
				
				return iFilter(initialList, filter);
				/**
				List<IModel<GuideContent>> list = new ArrayList<IModel<GuideContent>>();
				final String str = filter.trim().toLowerCase();
				initialList.forEach(s -> {
					if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
						list.add(s);
					}
				});
				return list;*/
			}
			
			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {
				return ArtExhibitionItemsGuidesPanel.this.getObjectListItemExpandedPanel(model, mode);
				
			}

			@Override
			protected Panel getListItemPanel(IModel<GuideContent> model) {
				DelleMuseObjectListItemPanel<GuideContent> panel = new DelleMuseObjectListItemPanel<GuideContent>("row-element",
						model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<String> getObjectTitle() {
						return  ArtExhibitionItemsGuidesPanel.this.getObjectTitle(getModel().getObject());
					}
					
					@Override
					protected String getImageSrc() {
						return ArtExhibitionItemsGuidesPanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						setResponsePage(new GuideContentPage(getModel(),ArtExhibitionItemsGuidesPanel.this.getItems()));
					}

					@Override
					protected IModel<String> getInfo() {
						return ArtExhibitionItemsGuidesPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
							return null;
						//return ArtExhibitionItemsGuidesPanel.this.getMenu(getModel());
					}
				};
				return panel;
			}
		
			@Override
			public List<IModel<GuideContent>> getItems()  {
				return  ArtExhibitionItemsGuidesPanel.this.getItems();
			}
			
			//@Override
			//protected void setItems(List<IModel<GuideContent>> list) {
			//	ArtExhibitionItemsGuidesPanel.this.setItems(list);
			//}
		};
		add(itemsPanel);

		itemsPanel.setListPanelMode(ListPanelMode.TITLE);
		itemsPanel.setLiveSearch(false);
		itemsPanel.setSettings(true);
		itemsPanel.setHasExpander(true);
	}

  	
	protected void setItems(List<IModel<GuideContent>> list2) {
		this.list=list2;
		
	}

	protected List<IModel<GuideContent>> getItems() {
		if (this.list == null) {
			this.list = new ArrayList<IModel<GuideContent>>();
			getGuideContens(getModel().getObject()).forEach(item -> this.list.add(new ObjectModel<>(item)));
		}
		return this.list;
	}


	 

}
