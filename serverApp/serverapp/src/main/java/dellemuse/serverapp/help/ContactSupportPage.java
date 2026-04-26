package dellemuse.serverapp.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;

import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.toolbar.ToolbarItem;

import wktui.base.INamedTab;
import wktui.base.NamedTab;

@AuthorizeInstantiation({ "ROLE_USER" })
@MountPath("/contact-support")
public class ContactSupportPage extends ObjectPage<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ContactSupportPage.class.getName());

	private ContactSupportEditor editor;

	public ContactSupportPage() {
		super();
	}

	public ContactSupportPage(PageParameters parameters) {
		super(parameters);
	}

	public ContactSupportPage(IModel<User> model) {
		super(model);
	}

	@Override
	public String getHelpKey() {
		return Help.CONTACT_SUPPORT;
	}

	@Override
	protected boolean calculateHasAccessRight(Optional<User> ouser) {
		return ouser.isPresent();
	}

	

	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab = new NamedTab(Model.of("contact-support"), ServerAppConstant.contact_support) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getContactSupportEditor(panelId);
			}
		};
		tabs.add(tab);

		if (getStartTab() == null)
			setStartTab(ServerAppConstant.contact_support);

		return tabs;
	}

	private ContactSupportEditor getContactSupportEditor(String panelId) {
		if (editor == null)
			editor = new ContactSupportEditor(panelId, getModel());
		return editor;
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {
		return new ArrayList<>();
	}

	@Override
	protected IModel<String> getPageTitle() {
		return getLabel("contact-support");
	}

	@Override
	protected Panel createHeaderPanel() {
		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new BCElement(getLabel("contact-support")));
			JumboPageHeaderPanel<User> ph = new JumboPageHeaderPanel<User>("page-header", getModel(), getLabel("contact-support"));
			ph.setHeaderCss("mb-0 pb-2 border-none");
			ph.setBreadCrumb(bc);
			ph.setContext(getLabel("help"));
			return ph;
		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}

	@Override
	protected Panel createSearchPanel() {
		return null;
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<User> model, List<IModel<User>> list) {
		return null;
	}

	@Override
	protected Optional<User> getObject(Long id) {
		return getUser(id);
	}

	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				logger.debug(event.toString());
			}

			@Override
			public boolean handle(UIEvent event) {
				return event instanceof MenuAjaxEvent;
			}
		});

		add(new io.wktui.event.WicketEventListener<SimpleWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleWicketEvent event) {
			}

			@Override
			public boolean handle(UIEvent event) {
				return event instanceof SimpleWicketEvent;
			}
		});
	}
}
