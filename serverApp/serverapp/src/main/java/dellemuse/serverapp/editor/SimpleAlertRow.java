package dellemuse.serverapp.editor;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.serverdb.model.ObjectState;
import io.wktui.error.AlertPanel;
import wktui.base.InvisiblePanel;
import wktui.base.ModelPanel;

public class SimpleAlertRow<T> extends ModelPanel<T> {

	private static final long serialVersionUID = 1L;
	
	private WebMarkupContainer infoContainer;
	AlertPanel<Void> info;
		
	public SimpleAlertRow(String id) {
		super(id, null);
	}
	
	
	public SimpleAlertRow(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		infoContainer = new WebMarkupContainer("infoContainer");		
		infoContainer.setOutputMarkupId(true);
		add(infoContainer);
		infoContainer.addOrReplace(new InvisiblePanel("info"));
	}
	
	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
		
		if (this.isVisible()) {
			IModel<String> s=getText();
			this.info=new AlertPanel<Void>("info", getAlertType(), (s!=null?s:Model.of("")));
			infoContainer.addOrReplace(info);
		}
		else {
			infoContainer.addOrReplace(new InvisiblePanel("info"));
		}	
	}

	protected int getAlertType() {
		return AlertPanel.WARNING;
	}

	//protected boolean isAlertInfo() {
	//	return false;
	//}

	protected IModel<String> getText() {
		return null;
	}
	

}
