package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artwork.ArtWorkPage;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.error.ErrorPanel;

public class SiteArtWorkExpandedPanel extends DBModelPanel<ArtWork> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(SiteArtWorkExpandedPanel.class.getName());

	private IModel<Site> siteModel;
	private WebMarkupContainer listItemContainer;
	private ListView<IModel<ArtWork>> listView;

	public SiteArtWorkExpandedPanel(String id, IModel<ArtWork> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel = siteModel;
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		getModel().setObject(getArtWorkDBService().findWithDeps(getModel().getObject().getId()).get());

		 

		this.listItemContainer = new WebMarkupContainer("list-items-container");
		this.listItemContainer.setOutputMarkupId(true);

		addOrReplace(this.listItemContainer);

		this.listView = new ListView<IModel<ArtWork>>("list-items", new PropertyModel<List<IModel<ArtWork>>>(this, "list")) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<IModel<ArtWork>> item) {

				WebMarkupContainer m = new WebMarkupContainer("row-element");

				Link<ArtWork> link = new Link<ArtWork>("title-link", item.getModel().getObject()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new ArtWorkPage(getModel()));
					}
				};

				Label la = new Label("title", getObjectTitle(item.getModel().getObject().getObject()));
				la.setEscapeModelStrings(false);
				link.add(la);
				m.add(link);
				item.add(m);
				item.setOutputMarkupId(true);
			}
		};

		this.listItemContainer.add(this.listView);

		ErrorPanel nitems = new ErrorPanel("noItems", null, getLabel("no-items")) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}

			@Override
			public String getCss() {
				return "alert border rounded-top-0";
			}
		};

		this.listItemContainer.add(nitems);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		 
		if (siteModel != null)
			siteModel.detach();

	}

	 
}
