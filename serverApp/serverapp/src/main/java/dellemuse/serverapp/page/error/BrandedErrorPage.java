package dellemuse.serverapp.page.error;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.util.Optional;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.branded.BrandedSitePage;
import dellemuse.serverapp.branded.panel.BrandedGlobalTopPanel;
import dellemuse.serverapp.branded.panel.BrandedSiteSearcherPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.error.ErrorPanel;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import wktui.base.InvisiblePanel;

@MountPath("/error/${id}")
public class BrandedErrorPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedErrorPage.class.getName());

	private IModel<Site> siteModel;
	
	private Exception exceptionError;
	private IModel<String> info;
	
	public BrandedErrorPage() {
	 
	}
	
	public BrandedErrorPage(Exception exceptionError, IModel<Site> siteModel) {
		super();
		this.exceptionError=exceptionError;
		this.siteModel=siteModel;
	}
	
	
	public BrandedErrorPage(IModel<String> message, IModel<Site> siteModel) {
		super();
		info=message;
		this.siteModel=siteModel;
	}
	
	protected void setUpModel() {
		Optional<Site> o_s = getSiteDBService().findWithDeps(getSiteModel().getObject().getId());
		setSiteModel(new ObjectModel<Site>(o_s.get()));
	}
	
	
	public void onInitialize() {
		super.onInitialize();

		getPageParameters().add("id", "500");

		
		try {
			
			setUpModel();
			
			add( new BrandedGlobalTopPanel("top-panel", getSiteModel()));
		
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("top-panel", e));
		}

		
		try {
		
			add(createSearchPanel());
			add(createHeaderPanel());
		
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new InvisiblePanel("globalSearch"));
			addOrReplace(new InvisiblePanel("page-header"));
		}

	
		
		if (this.exceptionError!=null) {
			add(new ErrorPanel("error", this.exceptionError ));
		}
		else if (this.info!=null) {
			add(new ErrorPanel("error", this.info ));
		}
		else {
			add( new InvisiblePanel("error"));
		}
	}

	
	protected Panel createGlobalTopPanel(String id) {
		return new BrandedGlobalTopPanel("top-panel", getSiteModel());
	}

 
	protected Panel createSearchPanel() {
		return new BrandedSiteSearcherPanel("globalSearch", getSiteModel());
	}
	
	
	@Override
	public void onDetach() {
		super.onDetach();

		if (this.siteModel != null)
			this.siteModel.detach();
	}
	
	
	
	protected Panel createHeaderPanel() {

		try {

			BreadCrumb<Void> bc = createBreadCrumb();

			bc.addElement(new HREFBCElement("/ag/" + getSiteModel().getObject().getId().toString(), getLabel("audio-guides")));
			bc.addElement(new HREFBCElement("/ag/" + getSiteModel().getObject().getId().toString(), getLabel("error")));
			
			
			
			JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getSiteModel(), getObjectTitle(getSiteModel().getObject()));
			ph.setBreadCrumb(bc);

			ph.add(new org.apache.wicket.AttributeModifier("class", "row mt-0 mb-0 text-center imgReduced"));

			ph.setImageLinkCss("jumbo-img jumbo-md mb-2 mb-lg-0 border-none bg-dark");
			ph.setHeaderCss("mb-0 mt-0 pt-0 pb-2 border-none");

			boolean isPhoto = false;

			ph.setContext(getLabel("error"));

		 
			return ph;

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}
	
	
	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}

	@Override
	protected boolean isDarkTheme() {
		return true;
	}


	public IModel<Site> getSiteModel() {
		return siteModel;
	}


	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}
	
	
}
