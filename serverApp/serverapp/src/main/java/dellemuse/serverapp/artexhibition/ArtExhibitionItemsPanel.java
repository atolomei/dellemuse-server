package dellemuse.serverapp.artexhibition;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.ArtExhibitionItemPage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.model.TextCleaner;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

public class ArtExhibitionItemsPanel extends DBModelPanel<ArtExhibition> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ArtExhibitionItemsPanel.class.getName());
			
	private List<IModel<ArtExhibitionItem>> list;

	
	public ArtExhibitionItemsPanel(String id, IModel<ArtExhibition> model) {
		super(id, model);
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		addItems();
		
	}

	@Override
	public void onDetach() {
		super.onDetach();
	
		if (list!=null)
			list.forEach(t->t.detach());
	}

	
	private void addItems() {
		 
			ListPanel<ArtExhibitionItem> panel = new ListPanel<ArtExhibitionItem>("items", getItems()) {
	
				private static final long serialVersionUID = 1L;

				protected List<IModel<ArtExhibitionItem>> filter(List<IModel<ArtExhibitionItem>> initialList, String filter) {
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
				protected Panel getListItemPanel(IModel<ArtExhibitionItem> model) {
					ObjectListItemPanel<ArtExhibitionItem> panel = new ObjectListItemPanel<ArtExhibitionItem>("row-element",
							model, getListPanelMode()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected String getImageSrc() {
							//if (getModel().getObject().getPhoto() != null) {
							//	Resource photo = getResource(model.getObject().getPhoto().getId()).get();
							//	return getPresignedThumbnailSmall(photo);
							//}
							return null;
						}

						@Override
						public void onClick() {
							setResponsePage( new ArtExhibitionItemPage(getModel(), getList())); 
						}

						protected IModel<String> getInfo() {
							//String str = TextCleaner.clean(getModel().getObject().getIntro());
							//return new Model<String>(str);
							return null;
						}
					};
					return panel;
				}
			};
			 add(panel);
			
			//panel.setTitle(getLabel("exhibitions-permanent"));
	        panel.setListPanelMode(ListPanelMode.TITLE);
			panel.setLiveSearch(false);
	        panel.setSettings(true);
	}

	
	
	private List<IModel<ArtExhibitionItem>> getItems() {
		
		if (list==null) {
			list = new ArrayList<IModel<ArtExhibitionItem>>();
			getArtExhibitionItems(getModel().getObject()).forEach( item -> list.add( new ObjectModel<>(item)));
		}
		return list;
	}

	
}
