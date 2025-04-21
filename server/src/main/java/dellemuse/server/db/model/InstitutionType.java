package dellemuse.server.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "institutionType")
public class InstitutionType extends DelleMuseObject {

    @Column(name="name")
    private String name;
    
    @Column(name="nameKey")
    private String nameKey;
    
    public InstitutionType() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }
    
    
    
}
