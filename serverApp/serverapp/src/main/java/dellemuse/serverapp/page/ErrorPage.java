package dellemuse.serverapp.page;

import org.wicketstuff.annotation.mount.MountPath;

import io.wktui.error.ErrorPanel;
import wktui.base.InvisiblePanel;


@MountPath("/error/${id}")
public class ErrorPage extends BasePage {

	private static final long serialVersionUID = 1L;

	public ErrorPage(Exception exceptionError) {
		super();
		this.exceptionError=exceptionError;
	}
	
	Exception exceptionError;
	
	public void onInitialize() {
		super.onInitialize();
		
		getPageParameters().add("id", "500");
		
		if (this.exceptionError!=null) {
			add( new ErrorPanel("error", this.exceptionError ));
		}
		else {
			add( new InvisiblePanel("error"));
		}
	}
}
