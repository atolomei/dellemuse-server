package dellemuse.serverapp.server;


import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.BasePage;
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
