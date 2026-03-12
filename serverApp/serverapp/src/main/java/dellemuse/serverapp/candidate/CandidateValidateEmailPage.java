package dellemuse.serverapp.candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import dellemuse.serverapp.audit.panel.AuditPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectWithDepModel;
import dellemuse.serverapp.page.user.UserNavDropDownMenuToolbarItem;
import dellemuse.serverapp.person.PersonPage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.Music;
import dellemuse.serverapp.serverdb.model.PersistentToken;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.Voice;
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import io.wktui.error.AlertPanel;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;

import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;

import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

import wktui.base.INamedTab;
import wktui.base.InvisiblePanel;
import wktui.base.NamedTab;

/**
 * 
 * gracias por registrarte en Dellemuse.
 * El proximo paso es validar tu dirección de correo electrónico
 * haz click en el siguiente enlace:
 * 
 */

@MountPath("/candval/${token}")
public class CandidateValidateEmailPage extends BasePage {
	 
	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(CandidateValidateEmailPage.class.getName());


	 
	public CandidateValidateEmailPage() {
		super();
	}

	public CandidateValidateEmailPage(PageParameters parameters) {
		super(parameters);
	}

	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}
	

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		 add( new GlobalTopPanel("top-panel"));
		 add( new InvisiblePanel("footer-panel"));
		 add( new InvisiblePanel("info"));
		 add( new InvisiblePanel("error"));
		 add( new InvisiblePanel("success"));
		 
		 
		addOrReplace( new AlertPanel<>("info", AlertPanel.NEUTRAL, Model.of("Nombre apellido email")));
		addOrReplace( new AlertPanel<>("success", AlertPanel.SUCCESS, Model.of("Email address confirmed successfully")));
		
			
		if (getPageParameters().get("token").isNull()) {
			addOrReplace( new ErrorPanel("error", Model.of("Invalid token")));
			return;
		}
		
		String token = getPageParameters().get("token").toString();
		
		if (token.length()==0) {
			addOrReplace( new ErrorPanel("error", Model.of("Invalid token 0")));
			return;
		}
		
		
		String arr[] = token.split("-");
		
		if (arr.length!=2) {
			addOrReplace( new ErrorPanel("error", Model.of("Invalid token 1")));
			return;
		
		}
		
		String candidateIdStr = arr[0];
		String tokenValue = arr[1];			
		
		Iterable<PersistentToken> opt = getPersistentTokenDBServiceDBService().findByToken(tokenValue);

		if (opt==null) {
			addOrReplace( new ErrorPanel("error", Model.of("Invalid token 2")));
			return;
		
		}
		
		for (PersistentToken t: opt) {
			if (t.getEntityClass().equals(Candidate.class.getSimpleName()) && t.getEntity().equals(candidateIdStr)) {
				Candidate c = getCandidateDBService().findById(Long.parseLong(candidateIdStr)).orElse(null);
				if (c!=null) {
					c.setEmailValidated(true);
					getCandidateDBService().save(c);
					
					addOrReplace( new AlertPanel<>("info", AlertPanel.INFO, Model.of("Candidate Validate Email ok: candidateIdStr: "+ candidateIdStr + " tokenValue: "+ tokenValue)));
					addOrReplace( new AlertPanel<>("success", AlertPanel.WARNING, Model.of("Candidate Validate Email ok: candidateIdStr: "+ candidateIdStr + " tokenValue: "+ tokenValue)));
					logger.debug("Candidate Validate Email ok: candidateIdStr: "+ candidateIdStr + " tokenValue: "+ tokenValue);
				}
				
				
			}
		}
 
	 
	}

}
