package dellemuse.serverapp.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.model.TextCleaner;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

//@AuthorizeInstantiation("USER")
@WicketHomePage
@MountPath("/home")
public class DellemuseServerAppHomePage extends BasePage {

	private static final long serialVersionUID = 1L;

	private IModel<User> model;
	private ListPanel<Site> panel;
	private List<IModel<Site>> list;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(DellemuseServerAppHomePage.class.getName());

	public DellemuseServerAppHomePage(PageParameters parameters) {
		super(parameters);
	}

	public DellemuseServerAppHomePage() {
		super();
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

		Optional<User> o = super.getSessionUser();

		setModel(new ObjectModel<User>(o.get()));

		addSites();

		add(new GlobalTopPanel("top-panel", getModel()));
		add(new GlobalFooterPanel<Void>("footer-panel"));

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
		super.getSites().forEach(i -> list.add(new ObjectModel<Site>(i)));
	}

	private List<IModel<Site>> getList() {
		return list;
	}

	private void addSites() {

		loadList();

		this.panel = new ListPanel<>("siteList", getList()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getListItemPanel(IModel<Site> model, ListPanelMode mode) {

				ObjectListItemPanel<Site> panel = new ObjectListItemPanel<>("row-element", model, mode) {

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

			@Override
			public IModel<String> getItemLabel(IModel<Site> model) {
				return new Model<String>(model.getObject().getDisplayname());
			}

			@Override
			protected List<IModel<Site>> filter(List<IModel<Site>> initialList, String filter) {

				List<IModel<Site>> list = new ArrayList<IModel<Site>>();
				final String str = filter.trim().toLowerCase();
				initialList.forEach(s -> {
					if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
						list.add(s);
					}
				});
				return list;
			}
		};

		this.panel.setHasExpander(false);

		add(this.panel);
	}

}
