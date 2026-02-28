package dellemuse.serverapp.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibition.ArtExhibitionGuidesPanel;
import dellemuse.serverapp.artexhibitionguide.ArtExhibitionGuideContentsPanel;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.error.ErrorPanel;
import io.wktui.model.TextCleaner;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.DummyBlockPanel;
import wktui.base.InvisiblePanel;

@AuthorizeInstantiation({"ROLE_USER"})
@WicketHomePage
@MountPath("/home")
public class DellemuseServerAppHomePage extends BasePage {

	private static final long serialVersionUID = 1L;

	private IModel<User> model;
	private ListPanel<Site> panel;
	private List<IModel<Site>> list;

 
	static private Logger logger = Logger.getLogger(DellemuseServerAppHomePage.class.getName());

	public DellemuseServerAppHomePage(PageParameters parameters) {
		super(parameters);
	
		/**
		logger.debug("WICKET isSignedIn = " +
			    ((AuthenticatedWebSession)getSession()).isSignedIn());
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		logger.debug("AUTH = " + auth);
		logger.debug("AUTHORITIES = " + auth.getAuthorities());
		
		
		logger.debug("SESSION=" + getSession().getId());
		logger.debug("AUTH=" + SecurityContextHolder.getContext().getAuthentication());
		**/
	
	}

	public DellemuseServerAppHomePage() {
		super();
		
		/**
		logger.debug("WICKET isSignedIn = " +
			    ((AuthenticatedWebSession)getSession()).isSignedIn());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		logger.debug("AUTH = " + auth);
		logger.debug("AUTHORITIES = " + auth.getAuthorities());

		logger.debug("SESSION=" + getSession().getId());
		logger.debug("AUTH=" + SecurityContextHolder.getContext().getAuthentication());
		**/
		
	}
	
	

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (list != null)
			list.forEach(i -> i.detach());

		if (model != null)
			model.detach();
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();

			try {
				Optional<User> o = super.getSessionUser();
		
				if (o.isEmpty()) {
					throw new RuntimeException("no session");
				}
				
				setModel(new ObjectModel<User>(o.get()));
				add(new GlobalTopPanel("top-panel", getModel()));
				add(new InvisiblePanel("footer-panel"));
				
				if (isRoot() || isGeneralAdmin())
					add( new HomeAdminMainPanel("mainPanel", getModel()));
				else
					add( new HomeSiteUserMainPanel("mainPanel", getModel()));
				
			} catch (Exception e) {
				logger.error(e);
				addOrReplace(new InvisiblePanel("top-panel"));
				addOrReplace(new InvisiblePanel("footer-panel"));
				addOrReplace(new ErrorPanel("mainPanel", e));
			}
	}

	public IModel<User> getModel() {
		return model;
	}

	public void setModel(IModel<User> model) {
		this.model = model;
	}

	protected void onClick(IModel<Site> model) {
		setResponsePage(new SitePage(model, getList()));
	}

	protected String getObjectImageSrc(IModel<Site> model) {
		if (model.getObject().getPhoto() != null) {
			Resource photo = getResource(model.getObject().getPhoto().getId()).get();
			return getPresignedThumbnailSmall(photo);
		}
		return null;
	}

	protected IModel<String> getObjectSubtitle(IModel<Site> model) {
		return new Model<String>(model.getObject().getSubtitle());
	}

	protected IModel<String> getObjectTitle(IModel<Site> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	protected IModel<String> getObjectInfo(IModel<Site> model) {
		return new Model<String>(TextCleaner.clean(model.getObject().getInfo(), 280));
	}

	private void loadList() {
		list = new ArrayList<IModel<Site>>();
		super.getSites(ObjectState.EDITION, ObjectState.PUBLISHED).forEach(i -> list.add(new ObjectModel<Site>(i)));
	}

	private List<IModel<Site>> getList() {
		return list;
	}

	private void addSites() {

		loadList();

		this.panel = new ListPanel<>("siteList") {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<String> getItemLabel(IModel<Site> model) {
				return DellemuseServerAppHomePage.this.getObjectTitle(model);
			}

			@Override
			public List<IModel<Site>> getItems() {
				return DellemuseServerAppHomePage.this.getList();
			}

			@Override
			protected Panel getListItemPanel(IModel<Site> model, ListPanelMode mode) {

				DelleMuseObjectListItemPanel<Site> panel = new DelleMuseObjectListItemPanel<>("row-element", model, mode) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						DellemuseServerAppHomePage.this.onClick(getModel());
					}

					protected IModel<String> getInfo() {
						return DellemuseServerAppHomePage.this.getObjectInfo(getModel());
					}

					protected IModel<String> getObjectTitle() {
						return DellemuseServerAppHomePage.this.getObjectTitle(getModel());
					}

					protected IModel<String> getObjectSubtitle() {
						if (getMode() == ListPanelMode.TITLE)
							return null;
						return DellemuseServerAppHomePage.this.getObjectSubtitle(getModel());
					}

					@Override
					protected String getImageSrc() {
						return DellemuseServerAppHomePage.this.getObjectImageSrc(getModel());
					}

				};
				return panel;
			}

			protected void onClick(IModel<Site> model) {
				DellemuseServerAppHomePage.this.onClick(model);
			}

		};

		this.panel.setHasExpander(false);
		add(this.panel);
	}

	protected void setList(List<IModel<Site>> list) {
		this.list = list;

	}
}
