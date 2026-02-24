package dellemuse.serverapp.branded;

import java.util.List;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.event.SimpleWicketEvent;

public class SearchAudioEvent extends SimpleWicketEvent implements IDetachable {

	public List<IModel<GuideContent>> getGuideContentsList() {
		return gc_list;
	}

	public List<IModel<ArtExhibitionGuide>> getArtExhibitionGuidesList() {
		return ag_list;
	}

	 

	private static final long serialVersionUID = 1L;
	
	IModel<Site> site;
	private List<IModel<GuideContent>> gc_list;
	private List<IModel<ArtExhibitionGuide>> ag_list;
	
	public SearchAudioEvent(String name, IModel<Site> site,  List<IModel<GuideContent>> gc_list, List<IModel<ArtExhibitionGuide>> ag_list) {
		super(name);
		this.site=site;
		
		this.gc_list=gc_list;
		this.ag_list=ag_list;
		
	}

	public IModel<Site> getModel() {
		return site;
	}

	public void setModel(IModel<Site> site) {
		this.site = site;
	}

	@Override
	public void detach() {
		if (site!=null)
			site.detach();
		
		if (ag_list!=null)
			ag_list.forEach(i->i.detach());
		
		
		if (gc_list!=null)
			gc_list.forEach(i->i.detach());
		
	}

	
}
