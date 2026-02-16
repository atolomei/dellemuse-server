package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;

 
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
 
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
 
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
 
import io.wktui.event.UIEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.Field;
 
import io.wktui.form.field.TextField;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

public class SiteSearcherPanel extends BaseSiteSearcherPanel implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteSearcherPanel.class.getName());

	private List<ToolbarItem> listToolbar;
	
	private ObjectStateEnumSelector oses=ObjectStateEnumSelector.ALL;;
	public SiteSearcherPanel(String id, IModel<Site> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		 
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (getListToolbarItems()!=null)
			getListToolbarItems().forEach(i->i.detach());
	}
 
	public void setObjectStateEnumSelector(ObjectStateEnumSelector o) {
		this.oses = o;
	}

	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return this.oses;
	}
	
	protected void onClick(IModel<GuideContent> model, AjaxRequestTarget target) {
		
	}

	@Override
	protected String getSaveCss() {
		return "btn btn-primary btn-sm";
	}
	
	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<ObjectStateSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectStateSelectEvent event) {
				setObjectStateEnumSelector(event.getObjectStateEnumSelector());
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
	}
	
	
	@Override
	protected void onClick(IModel<GuideContent> model) {
		setResponsePage(new GuideContentPage(model,getItems()));
	}

	@Override
	protected  List<ToolbarItem> getListToolbarItems() {

		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		IModel<String> selected = Model.of(getObjectStateEnumSelector().getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);

		listToolbar.add(s);
		return listToolbar;
	}

	 
	protected synchronized  List<IModel<GuideContent>> generateList() {

		List<IModel<GuideContent>> list = new ArrayList<IModel<GuideContent>>();
		
		Long l_aid = null;
		
		try {
			if (getAudioId()!=null && getAudioId().length()>0)
				l_aid = Long.valueOf(getAudioId());
		} catch (Exception e) {
			l_aid = Long.valueOf(-1);
		}
		
		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDTIION_PUBLISHED)
			getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid, ObjectState.EDITION, ObjectState.PUBLISHED).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));
			//getGuideContentDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.EDITION, ObjectState.PUBLISHED).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));
	
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.PUBLISHED)
			getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid, ObjectState.PUBLISHED).forEach(s -> list.add(new ObjectModel<GuideContent>(s)));
		
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDITION)
			getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid, ObjectState.EDITION).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));
	
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.ALL)
			getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));
	
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.DELETED)
			getGuideContentDBService().getByArtWorkAudioId( getModel().getObject(), l_aid, ObjectState.DELETED).forEach(s ->  list.add(new ObjectModel<GuideContent>(s)));

		return list;
	
	}
	
	
	
	
}
