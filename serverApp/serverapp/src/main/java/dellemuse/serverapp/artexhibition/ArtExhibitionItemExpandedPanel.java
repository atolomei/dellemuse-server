package dellemuse.serverapp.artexhibition;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ExternalImage;
import org.apache.wicket.model.IModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.IExpandedPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import io.wktui.media.InvisibleImage;

public class ArtExhibitionItemExpandedPanel extends DBModelPanel<ArtExhibitionItem> implements IExpandedPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionItemExpandedPanel.class.getName());

	public ArtExhibitionItemExpandedPanel(String id, IModel<ArtExhibitionItem> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		ArtExhibitionItem item = getModel().getObject();
		ArtWork aw = item.getArtWork();

		// Thumbnail
		String imgSrc = getImageSrc(item);
		if (imgSrc != null) {
			add(new ExternalImage("thumbnail", imgSrc));
		} else {
			add(new InvisibleImage("thumbnail"));
		}

		// Name
		String name = getLanguageObjectService().getObjectDisplayName(item, getLocale());
		add(new Label("itemName", name != null ? name : ""));

		// Artists
		String artists = (aw != null) ? getArtistStr(aw) : null;
		Label artistsLabel = new Label("artists", (artists != null && !artists.isEmpty()) ? artists : "");
		artistsLabel.setVisible(artists != null && !artists.isEmpty());
		add(artistsLabel);

		// Floor
		String floorStr = item.getFloorStr();
		if ((floorStr == null || floorStr.isEmpty()) && item.getFloor() != null) {
			floorStr = item.getFloor().getDisplayname();
		}
		Label floorLabel = new Label("floor", (floorStr != null && !floorStr.isEmpty()) ? floorStr : "");
		floorLabel.setVisible(floorStr != null && !floorStr.isEmpty());
		add(floorLabel);

		// Room
		String roomStr = item.getRoomStr();
		if ((roomStr == null || roomStr.isEmpty()) && item.getRoom() != null) {
			roomStr = item.getRoom().getDisplayname();
		}
		Label roomLabel = new Label("room", (roomStr != null && !roomStr.isEmpty()) ? roomStr : "");
		roomLabel.setVisible(roomStr != null && !roomStr.isEmpty());
		add(roomLabel);

		// Order
		int order = item.getExhibitionOrder();
		Label orderLabel = new Label("exhibitionOrder", order > 0 ? String.valueOf(order) : "");
		orderLabel.setVisible(order > 0);
		add(orderLabel);
	}
}
