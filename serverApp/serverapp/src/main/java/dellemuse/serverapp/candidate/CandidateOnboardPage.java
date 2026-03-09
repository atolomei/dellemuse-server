package dellemuse.serverapp.candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import dellemuse.serverapp.audit.panel.AuditPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.music.MusicEditor;
import dellemuse.serverapp.music.MusicPage;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.model.ObjectWithDepModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.CandidateStatus;
import dellemuse.serverapp.serverdb.model.Music;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.DummyBlockPanel;
import wktui.base.INamedTab;
import wktui.base.InvisiblePanel;
import wktui.base.NamedTab;


@MountPath("/signup/${lang}")
public class CandidateOnboardPage extends BasePage {

     
    private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(Candidate.class.getName());

	private CandidateOnboardingEditor editor;
	
	    
	private String slang;
	
    public CandidateOnboardPage() {
        super();
        
        this.slang=Locale.getDefault().getLanguage();
        this.setOutputMarkupId(true);
    
        if (slang!=null && slang.equals("spa"))
        	slang="es";
        getSession().setLocale( Locale.forLanguageTag(slang));
        
    }

    public CandidateOnboardPage(PageParameters parameters) {
    	super(parameters);
    	
    	if( getPageParameters()!=null) {
    		slang=getPageParameters().get("lang").toString();
    	}
    	
    	if (slang==null)
            this.slang=Locale.getDefault().getLanguage();

        getSession().setLocale( Locale.forLanguageTag(slang));
    }


	@Override
	public Locale getLocale() {
		return Locale.forLanguageTag(slang);	
	}
	
    
    @Override
    public boolean hasAccessRight(Optional<User> ouser) {
	 return true;
    }

    
    @Override
	public void onDetach() {
		super.onDetach();
    	
    }

    
    

	public void onInitialize() {
		 super.onInitialize();

		 getSession().setLocale(Locale.forLanguageTag(slang));
		 
		 logger.debug( getSession().getLocale().getLanguage() );
		 
		 add( new CandidateOnboardingEditor("candidateEditor", slang));

		 add( new GlobalTopPanel("top-panel"));
		 add( new InvisiblePanel("footer-panel"));
		 add( new InvisiblePanel("error"));
			
		 
		 
		 
		 
		 
		 
		 
	 }
	 
	 
   

  
    
    
    
}