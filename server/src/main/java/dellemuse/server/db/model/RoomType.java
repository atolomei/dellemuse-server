package dellemuse.server.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "roomType")
public class RoomType extends DelleMuseObject {

    @Column(name="name")
    private String name;
    
    @Column(name="nameKey")
    private String nameKey;
    
}
