package dellemuse.serverapp.register;
 
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
 
import org.wicketstuff.annotation.mount.MountPath;
 
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.MultipleSelectorPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.role.RolePage;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.Role;
import io.wktui.error.ErrorPanel;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import wktui.base.InvisiblePanel;
 
  

/**
 * 
 * I have a Dellemuse user  I dont have a Dellemuse user
 * 
 *  1. Crear el usuario
 *  2. enviar email confirmacion
 *  
 *  ----
 *  3. ingresar institution - validar que no exista - alta 
 *  4. pasar 
 *  
 */

@MountPath("/signup/institution")
public class InstitutionSignUpPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionSignUpPage.class.getName());

	
	public InstitutionSignUpPage() {
		super();
	}

	public InstitutionSignUpPage(PageParameters parameters) {
		super(parameters);
	}
 
	@Override
	public void onDetach() {
		super.onDetach();
	}
 
	@Override
	protected void addListeners() {
		super.addListeners();
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		add(createGlobalTopPanel("top-panel"));
		add(new InvisiblePanel("footer-panel"));
		add(createHeaderPanel());

		
		List<IModel<String>> list = List.of( Model.of("one"), Model.of("two"), Model.of("three"));
		
		io.wktui.panel.MultipleSelectorPanel<String> panel = new io.wktui.panel.MultipleSelectorPanel<String>("multipleSelectorPanel", list) {

			 
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getObjectSubtitle(IModel<String> model) {
				return Model.of("subtitle");
			}

			@Override
			protected String getObjectImageSrc(IModel<String> model) {
				return null;
			}

			@Override
			protected IModel<String> getObjectInfo(IModel<String> model) {
				return Model.of("info");
			}

			@Override
			protected void onClick(IModel<String> model) {
				logger.debug("onclick");
			}

			@Override
			protected void onObjectSelect(IModel<String> model, AjaxRequestTarget target) {
				logger.debug("onObjectSelect");
			}
			
			@Override
			protected boolean isEqual(String o1, String o2) {
				return o1.equals(o2);
			}
			
		};
		add(panel);
	}
	 
	/**
	 * 
	 * @return
	 */
	protected Panel createHeaderPanel() {

		try {
			BreadCrumb<Void> bc = createBreadCrumb();
		 
			bc.addElement(new BCElement(getLabel("signup")));
			JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("signup"));
			ph.setHeaderCss("mb-0 pb-2 border-none");
			ph.setIcon(Institution.getIcon());
			ph.setBreadCrumb(bc);
 			ph.setContext(getLabel("institution"));
			return (ph);

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}
	

	protected Panel createGlobalTopPanel(String id) {
		return new GlobalTopPanel("top-panel", new ObjectModel<User>(getSessionUser().get()));
	}
	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}
	
 
}
