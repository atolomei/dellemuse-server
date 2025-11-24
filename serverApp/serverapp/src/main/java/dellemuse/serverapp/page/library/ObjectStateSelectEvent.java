package dellemuse.serverapp.page.library;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import io.wktui.event.SimpleAjaxWicketEvent;

public class ObjectStateSelectEvent extends SimpleAjaxWicketEvent {

	/**
	public static final String ALL 			= "all";
	public static final String PUBLISHED 	= "published";
	public static final String DELETED 		= "deleted";
	public static final String EDITION 		= "edition";
	public static final String ACTIVE 		= "active";
	**/
	
	ObjectStateEnumSelector es;
	
	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return es;
	}
	
	public ObjectStateSelectEvent(AjaxRequestTarget target) {
		super(null, target, null);
	}

	public ObjectStateSelectEvent(ObjectStateEnumSelector es, AjaxRequestTarget target) {
		super(es.getLabel(), target, null);
			this.es=es;
	}

	public IModel<String> getLabel() {
		return Model.of(this.es.getLabel());
	}
	
}
