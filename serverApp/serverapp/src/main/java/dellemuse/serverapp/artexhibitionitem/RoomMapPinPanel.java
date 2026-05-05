package dellemuse.serverapp.artexhibitionitem;

import java.util.Optional;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.page.model.ObjectModelPanel;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Room;

/**
 * Shows the Room map image and lets the user click to place a pin.
 * Pin position is stored as normalized coordinates (0.0–1.0) in
 * ArtExhibitionItem.mapPosX / mapPosY.
 * mapPosRoomId records which Room the pin belongs to so stale pins are detected.
 */
public class RoomMapPinPanel extends ObjectModelPanel<ArtExhibitionItem> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(RoomMapPinPanel.class.getName());

	private WebMarkupContainer mapImage;
	private WebMarkupContainer pin;
	private Label coordsLabel;
	private AbstractDefaultAjaxBehavior placePinBehavior;

	/** Serializable room ID driving which map to display */
	private Long roomId;

	/** Serializable pin state – survives detach/re-attach between requests */
	private Double pinX;
	private Double pinY;
	private boolean pinDirty = false;

	/** Reference to the owning editor so we can mark updatedParts dirty */
	private final DBObjectEditor<ArtExhibitionItem> editor;

	public RoomMapPinPanel(String id, IModel<ArtExhibitionItem> model, DBObjectEditor<ArtExhibitionItem> editor) {
		super(id, model);
		this.editor = editor;
	}

	/** Called by the editor when the Room selection changes. */
	public void setRoom(Room room, AjaxRequestTarget target) {
		Long newRoomId = (room != null) ? room.getId() : null;
		if (newRoomId == null || !newRoomId.equals(roomId)) {
			pinX = null;
			pinY = null;
			pinDirty = true;
		}
		roomId = newRoomId;
		if (target != null)
			target.add(this);
	}

	/**
	 * Called by the editor just before save().
	 * Writes the current pin state from panel fields into the model object.
	 */
	public void applyToModel() {
		ArtExhibitionItem item = getModel().getObject();
		item.setMapPosX(pinX);
		item.setMapPosY(pinY);
		item.setMapPosRoomId(pinX != null ? roomId : null);
		pinDirty = false;
	}

	/** True when the pin was placed or cleared since last save. */
	public boolean isPinDirty() {
		return pinDirty;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		setOutputMarkupId(true);

		// Restore pin state from model on first render
		ArtExhibitionItem item = getModel().getObject();
		if (roomId == null && item.getMapPosRoomId() != null)
			roomId = item.getMapPosRoomId();
		if (pinX == null) pinX = item.getMapPosX();
		if (pinY == null) pinY = item.getMapPosY();

		// ── Ajax behavior that receives pinX / pinY as URL parameters ─────────
		placePinBehavior = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
				String xStr = params.getParameterValue("pinX").toString(null);
				String yStr = params.getParameterValue("pinY").toString(null);
				if (xStr != null && yStr != null) {
					try {
						pinX = Math.max(0.0, Math.min(1.0, Double.parseDouble(xStr)));
						pinY = Math.max(0.0, Math.min(1.0, Double.parseDouble(yStr)));
						pinDirty = true;
						if (editor != null)
							editor.setUpdatedPart("mapPos");
						logger.debug("pin placed -> x=" + pinX + " y=" + pinY + " room=" + roomId);
					} catch (NumberFormatException e) {
						logger.error(e);
					}
				}
				target.add(RoomMapPinPanel.this);
			}
		};
		add(placePinBehavior);

		// ── map container (visible when map image is available) ───────────────
		WebMarkupContainer mapContainer = new WebMarkupContainer("mapContainer") {
			private static final long serialVersionUID = 1L;
			@Override public boolean isVisible() { return getMapImageSrc() != null; }
		};
		mapContainer.setOutputMarkupPlaceholderTag(true);
		add(mapContainer);

		// Map image – src injected in onComponentTag
		mapImage = new WebMarkupContainer("mapImage") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				String src = getMapImageSrc();
				tag.put("src", src != null ? src : "");
			}
		};
		mapImage.setOutputMarkupId(true);
		mapContainer.add(mapImage);

		// ── pin marker ───────────────────────────────────────────────────────
		pin = new WebMarkupContainer("pin") {
			private static final long serialVersionUID = 1L;
			@Override public boolean isVisible() {
				return pinX != null && pinY != null;
			}
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				if (pinX != null && pinY != null)
					tag.put("style",
						"position:absolute;left:" + (pinX * 100) + "%;top:" + (pinY * 100)
						+ "%;transform:translate(-50%,-100%);pointer-events:none;");
			}
		};
		pin.setOutputMarkupPlaceholderTag(true);
		mapContainer.add(pin);

		// ── coordinates feedback ─────────────────────────────────────────────
		coordsLabel = new Label("coords", () -> {
			if (pinX == null || pinY == null) return "";
			return String.format("%.1f%% / %.1f%%", pinX * 100, pinY * 100);
		});
		coordsLabel.setOutputMarkupId(true);
		mapContainer.add(coordsLabel);

		// ── clear pin ────────────────────────────────────────────────────────
		AjaxLink<ArtExhibitionItem> clearPin = new AjaxLink<ArtExhibitionItem>("clearPin", getModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				pinX = null;
				pinY = null;
				pinDirty = true;
				if (editor != null)
					editor.setUpdatedPart("mapPos");
				target.add(RoomMapPinPanel.this);
			}
			@Override
			public boolean isVisible() {
				return pinX != null;
			}
		};
		clearPin.setOutputMarkupPlaceholderTag(true);
		mapContainer.add(clearPin);

		// ── no-map placeholder ────────────────────────────────────────────────
		WebMarkupContainer noMap = new WebMarkupContainer("noMap") {
			private static final long serialVersionUID = 1L;
			@Override public boolean isVisible() { return getMapImageSrc() == null; }
		};
		noMap.setOutputMarkupPlaceholderTag(true);
		add(noMap);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		// Emit JS after every render (initial + Ajax updates) so the image click
		// is always wired to the current callback URL of placePinBehavior.
		String callbackUrl = placePinBehavior.getCallbackUrl().toString();
		String imgId = mapImage.getMarkupId();
		String js =
			"(function() {" +
			"  var img = document.getElementById('" + imgId + "');" +
			"  if (!img || img._pinInitialized) return;" +
			"  img._pinInitialized = true;" +
			"  img.style.cursor = 'crosshair';" +
			"  img.addEventListener('click', function(e) {" +
			"    var rect = img.getBoundingClientRect();" +
			"    var x = ((e.clientX - rect.left) / rect.width).toFixed(6);" +
			"    var y = ((e.clientY - rect.top)  / rect.height).toFixed(6);" +
			"    Wicket.Ajax.ajax({" +
			"      'u': '" + callbackUrl + "&pinX=' + x + '&pinY=' + y" +
			"    });" +
			"  });" +
			"})();";
		response.render(OnDomReadyHeaderItem.forScript(js));
	}

	// ── helpers ───────────────────────────────────────────────────────────────

	private String getMapImageSrc() {
		if (roomId == null) return null;
		try {
			Optional<Room> opt = getRoomDBService().findWithDeps(roomId);
			if (opt.isEmpty()) return null;
			Resource map = opt.get().getMap();
			if (map == null) return null;
			return getPresignedThumbnail(map, ThumbnailSize.LARGE);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}
}
