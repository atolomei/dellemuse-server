package dellemuse.serverapp.branded.panel;

import java.util.ArrayList;
import java.util.List;

 
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.branded.BrandedGuideContentPage;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.error.ErrorPage;
 
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.BaseSiteSearcherPanel;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
 
import io.wktui.nav.toolbar.ToolbarItem;
 
 

public class BrandedSiteSearcherPanel2 extends BaseSiteSearcherPanel implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedSiteSearcherPanel2.class.getName());

	public BrandedSiteSearcherPanel2(String id, IModel<Site> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
	
		WebMarkupContainer m=super.getItemsGuideContentContainer();
		
		AjaxLink<Void> close = new AjaxLink<Void>( "close") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				getItemsGuideContentContainer().setVisible( false);
				target.add(BrandedSiteSearcherPanel2.this );
			}
		};
		m.add(close);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (getListToolbarItems()!=null)
			getListToolbarItems().forEach(i->i.detach());
	}
 
	@Override
	protected  List<ToolbarItem> getListToolbarItems() {
		return null;
	}

	@Override
	protected void addListeners() {
		super.addListeners();
	}
	
	@Override
	protected String getSaveCss() {
		return "btn btn-outline btn-sm";
	}
	
	protected String getSaveStyle() {
		return "margin-top: 0px;"
				+ "    padding-top: 8px;"
				+ "    padding-bottom: 5px;"
				+ "    border-top: none;"
				+ "    border-bottom: none;"
				+ "    border-right: none;"
				+ "    border-left: 1px solid #495057;"
				+ "    font-size: 13px;"
				+ "    border-radius: 0 6px 6px 0;"
				+ "    padding-left: 12px;"
				+ "    padding-right: 12px;";
	}

	@Override
	protected List<IModel<ArtExhibitionGuide>> generateArtExhibitionGuideList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<IModel<GuideContent>> generateGuideContentList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadResultsPage(List<IModel<GuideContent>> gc_list, List<IModel<ArtExhibitionGuide>> ag_list) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

/**	 
	@Override
	protected List<IModel<GuideContent>> generateList() {
		List<IModel<GuideContent>> list = new ArrayList<IModel<GuideContent>>();
		Long l_aid = null;
		try {
			if (getAudioId()!=null && getAudioId().length()>0)
				l_aid = Long.valueOf(getAudioId());
		} catch (Exception e) {
			l_aid = Long.valueOf(-1);
		}
		// getGuideContentDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.EDITION, ObjectState.PUBLISHED).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));
		getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid, ObjectState.PUBLISHED).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));
		return list;
	}

	@Override
	protected void onClick(IModel<GuideContent> model) {
		setResponsePage( new BrandedGuideContentPage(model, getList()));
		
	}

	@Override
	protected void onClick(IModel<GuideContent> model, AjaxRequestTarget target) {
	 
		
	}
 	**/
	
}
