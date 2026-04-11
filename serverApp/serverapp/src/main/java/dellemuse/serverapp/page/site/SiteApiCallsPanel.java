package dellemuse.serverapp.page.site;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.nav.toolbar.ToolbarItem;

public class SiteApiCallsPanel extends DBModelPanel<Site> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteApiCallsPanel.class.getName());

	public SiteApiCallsPanel(String id, IModel<Site> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		Site site = getModel().getObject();

		long totalCalls = 0;
		long totalCharacters = 0;

		try {
			totalCalls = getElevenLabsRequestDBService().countBySite(site.getId());
			totalCharacters = getElevenLabsRequestDBService().totalCharactersBySite(site.getId());
		} catch (Exception e) {
			logger.error(e);
		}

		NumberFormat nf = NumberFormat.getInstance(Locale.US);

		add(new Label("totalCalls", nf.format(totalCalls)));
		add(new Label("totalCharacters", nf.format(totalCharacters)));
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return null;
	}

}
