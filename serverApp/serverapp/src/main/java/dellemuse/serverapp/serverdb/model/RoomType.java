package dellemuse.serverapp.serverdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "roomType")
public class RoomType extends DelleMuseObject {

	@Column(name = "language")
	private String language;
	
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String lang) {
		language = lang;
	}
    public RoomType() {
    }
 
}
