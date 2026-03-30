package dellemuse.serverapp.candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
  
 * <p>See also {@link CandidateValidateEmailCommand}</p>
 * 
 */

@MountPath("/ca-email-validate/${token}")
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
	
	String slang = "en";

	@Override
	public Locale getLocale() {
		return Locale.forLanguageTag(slang);	
	} 
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		 add( new GlobalTopPanel("top-panel"));
		 add( new InvisiblePanel("footer-panel"));
		 add( new InvisiblePanel("info"));
		 add( new InvisiblePanel("error"));
		 add( new InvisiblePanel("success"));
		 
		if (getPageParameters().get("token").isNull()) {
			addOrReplace( new ErrorPanel("error", Model.of("Invalid token 1")));
			return;
		}
		
		String token = getPageParameters().get("token").toString();
		
		if (token.length()==0) {
			addOrReplace( new ErrorPanel("error", Model.of("Invalid token 2")));
			return;
		}
		
		
		String arr[] = token.split("-");
		
		if (arr.length!=3) {
			addOrReplace( new ErrorPanel("error", Model.of("Invalid token 3")));
			return;
		
		}
		
		String candidateIdStr = arr[0];
		String tokenValue = arr[1];		
		this.slang = arr[2];			

		
		Iterable<PersistentToken> opt = getPersistentTokenDBServiceDBService().findByToken(tokenValue);

		if (opt==null) {
			addOrReplace( new ErrorPanel("error", Model.of("Invalid token 4")));
			return;
		
		}
		
		
		try {
			Long cid=Long.parseLong(candidateIdStr);
		} catch(Exception e) {
			addOrReplace( new ErrorPanel("error", Model.of("Invalid token 5")));
			return;
		}
		
		Candidate c = getCandidateDBService().findById(Long.parseLong(candidateIdStr)).orElse(null);
	
		if (c==null) {
			addOrReplace( new ErrorPanel("error", Model.of("Invalid token 6")));
			return;
		}
		
		
		if (c.isEmailValidated()) {
			addOrReplace( new AlertPanel<>("success", AlertPanel.SUCCESS, Model.of("Email is already validated for ->  "+ c.getEmail()  + " | "+ c.getPersonLastname())));
			return;
		}
		
		for (PersistentToken t: opt) {
			
			if (t.getEntityClass().equals(Candidate.class.getSimpleName()) && t.getEntity().equals(candidateIdStr)) {
		
				c.setEmailValidated(true);
				getCandidateDBService().save(c);
				getPersistentTokenDBServiceDBService().delete(t);
				
				addOrReplace( new AlertPanel<>("success", AlertPanel.SUCCESS, Model.of("Email validated ok ->  "+ c.getEmail()  + " | "+ c.getPersonLastname())));
				logger.debug("Email validated ok ->  "+ c.getEmail()  + " | "+ c.getPersonLastname());
				return;
			}
		}
		
		addOrReplace( new ErrorPanel("error", Model.of("Invalid token 7")));
	
	}

}
