package dellemuse.serverapp.branded.panel;

import java.util.ArrayList;
import java.util.List;

 
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.BaseSiteSearcherPanel;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.event.UIEvent;
import io.wktui.nav.toolbar.ToolbarItem;
import wktui.base.InvisiblePanel;
 

public class BrandedSiteSearcherPanel extends BaseSiteSearcherPanel implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedSiteSearcherPanel.class.getName());

	public BrandedSiteSearcherPanel(String id, IModel<Site> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
	
		WebMarkupContainer m=super.getItemsContainer();
		
		AjaxLink<Void> close = new AjaxLink<Void>( "close") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				getItemsContainer().setVisible( false);
				target.add(BrandedSiteSearcherPanel.this );
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
/**
		add(new io.wktui.event.WicketEventListener<ObjectStateSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectStateSelectEvent event) {
				 
				reloadList();
				
				event.getTarget().add( getItemsContainer());
				event.getTarget().add( getListToolbarContainer());
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});
		**/
	}
	
	@Override
	protected void onClick(IModel<GuideContent> model, AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

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
		getGuideContentDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.EDITION, ObjectState.PUBLISHED).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));
		return list;
	}

	@Override
	protected void onClick(IModel<GuideContent> model) {
		setResponsePage( new ErrorPage(Model.of("not done")));
		
	}
 	
	
}
