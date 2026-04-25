package dellemuse.serverapp.help;

import java.io.File;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.User;

public class HelpManualPagePanel extends DBModelPanel<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(HelpManualPagePanel.class.getName());

	private String key;

	public HelpManualPagePanel(String id, String key, IModel<User> model) {
		super(id, model);
		this.key = key;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		String language = "es";
		try {
			if (getSessionUser().isPresent() && getSessionUser().get().getLanguage() != null)
				language = getSessionUser().get().getLanguage();
		} catch (Exception e) {
			logger.error(e, "Could not get session user language");
		}

		String templateKey = "manual" + File.separator + key;
		String html = getHelpService().gethelp(templateKey, language);

		if (html == null || html.isBlank())
			html = "<p>" + key + "</p>";

		Label content = new Label("main", html);
		content.setEscapeModelStrings(false);
		add(content);
	}

}
