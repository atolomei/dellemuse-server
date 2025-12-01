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
	private AlertPanel<Void> info;
	private int alertType = AlertPanel.WARNING;
	private IModel<String> text;
	
	
	public SimpleAlertRow(String id) {
		super(id, null);
	}
	
	
	public SimpleAlertRow(String id, IModel<T> model) {
		super(id, model);
	}

	public SimpleAlertRow(String id, Exception e) {
		super(id, null);
		text=Model.of( e.getClass().getSimpleName() + " | " + e.getMessage());
		this.alertType=AlertPanel.DANGER;
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

	
	public void setAlertType( int t) {
		this.alertType=t;
	}
	
	public int getAlertType() {
		return alertType;
	}
 	protected IModel<String> getText() {
		return text;
	}
	public void setText(IModel<String>  t) {
		this.text=t;
	}

	
}
