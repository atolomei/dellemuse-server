package dellemuse.serverapp.serverdb.model.record;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "personRecord")
@JsonInclude(Include.NON_NULL)
public class PersonRecord extends TranslationRecord {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Person.class)
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    @JsonProperty("person")
    private Person person;
    
   


    public PersonRecord() {
    }

    public String getTitle() {
        if (super.getTitle() != null)
            super.getTitle();
        return getName()  ;
    }

    @JsonIgnore
    public String getDisplayName() {

        if (getTitle() != null)
            return getTitle();

        if (getName() != null)
            return getName() ;
        
        return "null";
    }

   
    
    public void setName(String name) {
    	super.setName(name);
    	//generateSortName();
	}
    
    
    
   

   

	public void setPerson(Person person) {
		this.person = person;
	}

	 
   

	public String getLastFirstname() {
		
		StringBuilder str = new StringBuilder();
		
		 
		
		if (getName()!=null && getName().length()>0) {
			if (str.length()>0)
				str.append(", ");
			
			str.append(getName());
		}
		return str.toString();
	}

	 

	@Override
	public MultiLanguageObject getParentObject() {
		 return this.person !=null? this.person : null;
	 }

	  
}
