package dellemuse.serverapp.server;


import java.util.Optional;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.serverdb.model.User;
import jakarta.transaction.Transactional;

/**
 * 
 * site foto Info - exhibitions
 * 
 * 
 * 
 * ZoneId -> geografia
 * Locale -> idioma
 * 
 * 
 * 
 */

@MountPath("/server/info")
public class ServerPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ServerPage.class.getName());

	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}
	
	public ServerPage() {
		super();
	}

	public ServerPage(PageParameters parameters) {
		super(parameters);
	}

	@Transactional
	@Override
	public void onInitialize() {
		super.onInitialize();
 
	}

}
