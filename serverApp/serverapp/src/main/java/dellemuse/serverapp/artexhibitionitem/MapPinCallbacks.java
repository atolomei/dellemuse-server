package dellemuse.serverapp.artexhibitionitem;

import java.io.Serializable;

/**
 * Serializable callbacks for reading and writing pin coordinates on any model
 * object. Allows RoomMapPinPanel and FloorMapPinPanel to be generic.
 */
public interface MapPinCallbacks<T> extends Serializable {

	Double getPinX(T item);
	Double getPinY(T item);
	/** Returns the stored floor or room id (depending on which pin this is). */
	Long   getPinEntityId(T item);

	void setPinX(T item, Double x);
	void setPinY(T item, Double y);
	void setPinEntityId(T item, Long id);
}
