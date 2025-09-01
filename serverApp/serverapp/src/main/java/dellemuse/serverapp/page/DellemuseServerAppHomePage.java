package dellemuse.serverapp.page;

import java.util.Optional;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.User;

@WicketHomePage
@MountPath("home")
public class DellemuseServerAppHomePage extends BasePage {

    private static final long serialVersionUID = 1L;

    IModel<User> model;
    
    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(DellemuseServerAppHomePage.class.getName());
    
    
    public DellemuseServerAppHomePage(PageParameters parameters) {
        super(parameters);
    }

    public DellemuseServerAppHomePage() {
        super();
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        Optional<User> o = super.getSessionUser();
        
        setModel(new ObjectModel<User>(o.get()));
        
        add( new GlobalTopPanel("top-panel", getModel()));
        add( new GlobalFooterPanel<Void>("footer-panel"));
        
       
    }

	public IModel<User> getModel() {
		return model;
	}

	public void setModel(IModel<User> model) {
		this.model = model;
	}
}
