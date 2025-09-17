package dellemuse.serverapp.page.site;

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
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.model.TextCleaner;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

public class ArtWorkMainPanel extends DBModelPanel<ArtWork> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ArtWorkMainPanel.class.getName());
			
	private ArtWorkEditor editor;
	private Link<Site> addSite;
	
	public ArtWorkMainPanel(String id, IModel<ArtWork> model) {
		super(id, model);
	}

	public ArtWorkEditor getEditor() {
		return this.editor;
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		this.editor = new ArtWorkEditor("artworkEditor", getModel());
		add(this.editor);
		
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}


}
