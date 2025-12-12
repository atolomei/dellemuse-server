package dellemuse.serverapp.page.error;

import java.util.Optional;

import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.serverdb.model.User;


@MountPath("/expired")
public class SessionExpiredPage extends BasePage {

	private static final long serialVersionUID = 1L;

	public SessionExpiredPage() {
		
	}
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}
	
}
