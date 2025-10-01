package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

public class ArtWorkMainPanel extends DBModelPanel<ArtWork>  implements InternalPanel {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ArtWorkMainPanel.class.getName());
			
	private ArtWorkEditor editor;
	private Link<Site> addSite;
	ArtistArtWorksPanel more;
	
	public ArtWorkMainPanel(String id, IModel<ArtWork> model) {
		super(id, model);
	}

	public ArtWorkEditor getEditor() {
		return this.editor;
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		Optional<ArtWork> o_i = getArtWorkDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<ArtWork>(o_i.get()));
		
		this.editor = new ArtWorkEditor("artworkEditor", getModel());
		add(this.editor);
		
		if (getModel().getObject().getArtists()!=null && getModel().getObject().getArtists().size()>0) {
			
			Person person = getModel().getObject().getArtists().iterator().next();
			add( new ArtistArtWorksPanel("more", new ObjectModel<Person>(person)));
		}
		else {
			add( new InvisiblePanel("more"));
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}


	@Override
	public List<ToolbarItem> getToolbarItems() {
		
	List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		AjaxButtonToolbarItem<Person> create = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
 				fire(new SimpleAjaxWicketEvent(ServerAppConstant.action_site_edit, target));
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(create);
		return list;
	}

}
