package dellemuse.serverapp.page.error;

import java.util.Optional;

import org.apache.wicket.model.IModel;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.error.ErrorPanel;
import wktui.base.InvisiblePanel;

@MountPath("/error/${id}")
public class ErrorPage extends BasePage {

	private static final long serialVersionUID = 1L;

	Exception exceptionError;
	IModel<String> info;
	
	public ErrorPage(Exception exceptionError) {
		super();
		this.exceptionError=exceptionError;
	}
	
	
	public ErrorPage(IModel<String> message) {
		super();
		info=message;
	}
	
	public void onInitialize() {
		super.onInitialize();
		
		getPageParameters().add("id", "500");
		
		if (this.exceptionError!=null) {
			add(new ErrorPanel("error", this.exceptionError ));
		}
		else if (this.info!=null) {
			add(new ErrorPanel("error", this.info ));
		}
		else {
			add( new InvisiblePanel("error"));
		}
	}


	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}
}
