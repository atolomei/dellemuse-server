package dellemuse.serverapp.artwork;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.editor.ObjectMetaEditor;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.ArtistArtWorksPanel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.ArtWorkType;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.ObjectType;
import dellemuse.serverapp.serverdb.model.Person;

import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;

import io.wktui.media.InvisibleImage;

import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

import wktui.base.InvisiblePanel;
import wktui.base.LabelPanel;

public class ArtWorkMainPanel extends DBModelPanel<ArtWork> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtWorkMainPanel.class.getName());

	private Image qrCode;

	private ArtWorkEditor editor;
	// private ObjectMetaEditor<ArtWork> metaEditor;

	private Link<Site> addSite;
	private ArtistArtWorksPanel more;

	 
	private WebMarkupContainer qrcodecontainer;
	private WebMarkupContainer moreContainer;

	private IModel<File> qrFileModel;

	/**
	 * 
	 * - ObjectState - Record -> translate
	 * 
	 * 
	 * 
	 * @param id
	 * @param model
	 */
	public ArtWorkMainPanel(String id, IModel<ArtWork> model) {
		super(id, model);
	}

	public ArtWorkEditor getEditor() {
		return this.editor;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		Optional<ArtWork> o_i = getArtWorkDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<ArtWork>(o_i.get()));

		this.editor = new ArtWorkEditor("artworkEditor", getModel());
		add(this.editor);

		boolean isMore = false;

		moreContainer = new WebMarkupContainer("moreContainer");
		
		
		if (	(getModel().getObject().getObjectType() == ObjectType.ARTWORK) && 
				(getModel().getObject().getArtists() != null) && (getModel().getObject().getArtists().size() > 0)) {
		
			Artist a = getModel().getObject().getArtists().iterator().next();
			moreContainer.add(new ArtistArtWorksPanel("more", new ObjectModel<Artist>(a), null));
			isMore = true;
		}

		if (!isMore)
			moreContainer.add(new InvisiblePanel("more"));

		moreContainer.setVisible(isMore);
		add(moreContainer);

		qrcodecontainer = new WebMarkupContainer("qrcodeContainer");
		add(qrcodecontainer);

		qrcodecontainer.setVisible(getModel().getObject().getQRCode() != null);

		try {
	
			if (getModel().getObject().getQRCode() != null) {

				qrFileModel = new dellemuse.serverapp.serverdb.objectstorage.ObjectStorageFileModel(getModel().getObject().getQRCode().getBucketName(), getModel().getObject().getQRCode().getObjectName(),
						getModel().getObject().getQRCode().getName());

				String presignedThumbnail = super.getPresignedThumbnail(getModel().getObject().getQRCode(), ThumbnailSize.MEDIUM);
				Url url = Url.parse(presignedThumbnail);
				UrlResourceReference resourceReference = new UrlResourceReference(url);

				Image image = new Image("qrcode", resourceReference);

				qrcodecontainer.add(image);
				DownloadLink link = new DownloadLink("qr-file-link", getQRFileModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public File getModelObject() {
						return getQRFileModel().getObject();
					}
				};

				Label f = new Label("qr-file-name", getModel().getObject().getQRCode().getName());
				link.add(f);
				qrcodecontainer.add(link);

				add(new InvisiblePanel("noqrcode"));

			} else {
				qrcodecontainer.add(new InvisibleImage("qrcode"));
				qrcodecontainer.add(new InvisiblePanel("qr-file-link"));
				add(new LabelPanel("noqrcode", getLabel("noqrcode")));

			}
		} catch (Exception e) {
			qrcodecontainer.add(new InvisibleImage("qrcode"));
			qrcodecontainer.add(new InvisiblePanel("qr-file-link"));
			add(new ErrorPanel("noqrcode", Model.of(e.getClass().getSimpleName() + " | " + e.getMessage())));

			logger.error(e, ServerConstant.NOT_THROWN);
		}
	}

	/**
	 * media qr
	 * 
	 */
	@Override
	public void onDetach() {
		super.onDetach();

		if (qrFileModel != null)
			qrFileModel.detach();
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<Person> create = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_artwork_edit_info, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(create);
		return list;
	}

	public void onEdit(AjaxRequestTarget target) {
		getEditor().onEdit(target);
		// getMetaEditor().onEdit(target);
	}

	// private ObjectMetaEditor<?> getMetaEditor() {
	// return this.metaEditor;
	// }

	private IModel<File> getQRFileModel() {
		return qrFileModel;
	}

}
