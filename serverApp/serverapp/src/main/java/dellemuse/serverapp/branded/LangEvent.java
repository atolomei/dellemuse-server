package dellemuse.serverapp.branded;

import java.util.List;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.event.SimpleWicketEvent;

public class LangEvent extends SimpleWicketEvent   {

	 
	private static final long serialVersionUID = 1L;
	
	private String lang;
	
	public LangEvent(String name, String lang) {
		super(name);
		this.lang=lang;
		
	}

	public String getLang() {
		return this.lang;
	}

 
	
}
