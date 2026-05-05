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
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Room;

/**
 * Generic panel that shows a Room map image and lets the user click to place a pin.
 * Pin coordinates are read/written via {@link MapPinCallbacks} so the panel works
 * with any model type (ArtExhibitionItem, ArtExhibition, …).
 */
public class RoomMapPinPanel<T extends DelleMuseObject> extends ObjectModelPanel<T> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(RoomMapPinPanel.class.getName());

	private WebMarkupContainer mapImage;
	private AbstractDefaultAjaxBehavior placePinBehavior;

	private Long roomId;
	private Double pinX;
	private Double pinY;
	private boolean pinDirty = false;

	private final DBObjectEditor<T>      editor;
	private final MapPinCallbacks<T>     callbacks;

	public RoomMapPinPanel(String id, IModel<T> model,
			DBObjectEditor<T> editor, MapPinCallbacks<T> callbacks) {
		super(id, model);
		this.editor    = editor;
		this.callbacks = callbacks;
	}

	public void setRoom(Room room, AjaxRequestTarget target) {
		Long newId = (room != null) ? room.getId() : null;
		if (newId == null || !newId.equals(roomId)) {
			pinX = null;
			pinY = null;
			pinDirty = true;
		}
		roomId = newId;
		if (target != null) target.add(this);
	}

	public void applyToModel() {
		T item = getModel().getObject();
		callbacks.setPinX(item, pinX);
		callbacks.setPinY(item, pinY);
		callbacks.setPinEntityId(item, pinX != null ? roomId : null);
		pinDirty = false;
	}

	public boolean isPinDirty() { return pinDirty; }

	@Override
	public void onInitialize() {
		super.onInitialize();
		setOutputMarkupId(true);

		T item = getModel().getObject();
		if (roomId == null && callbacks.getPinEntityId(item) != null)
			roomId = callbacks.getPinEntityId(item);
		if (pinX == null) pinX = callbacks.getPinX(item);
		if (pinY == null) pinY = callbacks.getPinY(item);

		placePinBehavior = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void respond(AjaxRequestTarget target) {
				IRequestParameters p = RequestCycle.get().getRequest().getRequestParameters();
				String xStr = p.getParameterValue("pinX").toString(null);
				String yStr = p.getParameterValue("pinY").toString(null);
				if (xStr != null && yStr != null) {
					try {
						pinX = Math.max(0.0, Math.min(1.0, Double.parseDouble(xStr)));
						pinY = Math.max(0.0, Math.min(1.0, Double.parseDouble(yStr)));
						pinDirty = true;
						if (editor != null) editor.setUpdatedPart("mapPos");
						logger.debug("room-pin -> x=" + pinX + " y=" + pinY + " room=" + roomId);
					} catch (NumberFormatException e) { logger.error(e); }
				}
				target.add(RoomMapPinPanel.this);
			}
		};
		add(placePinBehavior);

		WebMarkupContainer mapContainer = new WebMarkupContainer("mapContainer") {
			private static final long serialVersionUID = 1L;
			@Override public boolean isVisible() { return getMapImageSrc() != null; }
		};
		mapContainer.setOutputMarkupPlaceholderTag(true);
		add(mapContainer);

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

		WebMarkupContainer pin = new WebMarkupContainer("pin") {
			private static final long serialVersionUID = 1L;
			@Override public boolean isVisible() { return pinX != null && pinY != null; }
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				if (pinX != null && pinY != null)
					tag.put("style", "position:absolute;left:" + (pinX * 100) + "%;top:" + (pinY * 100)
							+ "%;transform:translate(-50%,-100%);pointer-events:none;");
			}
		};
		pin.setOutputMarkupPlaceholderTag(true);
		mapContainer.add(pin);

		Label coordsLabel = new Label("coords", () -> {
			if (pinX == null || pinY == null) return "";
			return String.format("%.1f%% / %.1f%%", pinX * 100, pinY * 100);
		});
		coordsLabel.setOutputMarkupId(true);
		mapContainer.add(coordsLabel);

		AjaxLink<T> clearPin = new AjaxLink<T>("clearPin", getModel()) {
			private static final long serialVersionUID = 1L;
			@Override public void onClick(AjaxRequestTarget target) {
				pinX = null; pinY = null; pinDirty = true;
				if (editor != null) editor.setUpdatedPart("mapPos");
				target.add(RoomMapPinPanel.this);
			}
			@Override public boolean isVisible() { return pinX != null; }
		};
		clearPin.setOutputMarkupPlaceholderTag(true);
		mapContainer.add(clearPin);

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
		String callbackUrl = placePinBehavior.getCallbackUrl().toString();
		String imgId = mapImage.getMarkupId();
		String js = "(function(){" +
			"var img=document.getElementById('" + imgId + "');" +
			"if(!img||img._pinInitialized)return;" +
			"img._pinInitialized=true;img.style.cursor='crosshair';" +
			"img.addEventListener('click',function(e){" +
			"var r=img.getBoundingClientRect();" +
			"var x=((e.clientX-r.left)/r.width).toFixed(6);" +
			"var y=((e.clientY-r.top)/r.height).toFixed(6);" +
			"Wicket.Ajax.ajax({'u':'" + callbackUrl + "&pinX='+x+'&pinY='+y});});" +
			"})();";
		response.render(OnDomReadyHeaderItem.forScript(js));
	}

	private String getMapImageSrc() {
		if (roomId == null) return null;
		try {
			Optional<Room> opt = getRoomDBService().findWithDeps(roomId);
			if (opt.isEmpty()) return null;
			Resource map = opt.get().getMap();
			if (map == null) return null;
			return getPresignedThumbnail(map, ThumbnailSize.LARGE);
		} catch (Exception e) { logger.error(e); return null; }
	}
}