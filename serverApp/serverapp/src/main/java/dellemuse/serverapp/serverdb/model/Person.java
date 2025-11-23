package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseUserSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


/**
 * 
 * alter table person  add column translation integer default 0;
   alter table person  add column masterlanguage character varying(24) default 'es';;
 */

@Entity
@Table(name = "person")
@JsonInclude(Include.NON_NULL)
public class Person extends MultiLanguageObject {

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "lastnamekey")
    private String lastnameKey;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "sex")
    private String sex;

    @Column(name = "physicalid")
    private String physicalid;

    @Column(name = "address")
    private String address;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "sortlastfirstname")
    private String sortlastfirstname;
    
    @Column(name = "webpage")
    private String webpage;
    
    /**
     * 
     * alter table person add column webpage character varying (2048);
     * 
     * Lazy gives JSON error (No Session) when serializing
     * 
     * hay que probar ->
     * https://stackoverflow.com/questions/21708339/avoid-jackson-serialization-on-non-fetched-lazy-objects/21760361#21760361
     * 
     * 
     */
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseUserSerializer.class)
    @JsonProperty("user")
    private User user;

    
  

    public Person() {
    }
    
	public String getPrefixUrl() {
		return PrefixUrl.Person;
	}


    public String getTitle() {
        if (super.getTitle() != null)
            super.getTitle();
        return getName() + " " + getLastname();
    }

    @JsonIgnore
    public String getDisplayName() {

        if (getTitle() != null)
            return getTitle();

        if (getName() != null)
            return getName() + " " + getLastname();
        
        return getLastname();
    }

    public String getLastname() {
        return lastname;
    }

    
    public void setName(String name) {
    	super.setName(name);
    	generateSortName();
	}
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    	generateSortName();
    }

    
    

    public String getLastnameKey() {
        return lastnameKey;
    }

    public void setLastnameKey(String lastnameKey) {
        this.lastnameKey = lastnameKey;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhysicalid() {
        return physicalid;
    }

    public void setPhysicalid(String physicalid) {
        this.physicalid = physicalid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
 
 

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	public String getLastFirstname() {
		
		StringBuilder str = new StringBuilder();
		
		if (getLastname()!=null) {
			str.append( getLastname());
		}
		
		if (getName()!=null && getName().length()>0) {
			if (str.length()>0)
				str.append(", ");
			
			str.append(getName());
		}
		return str.toString();
	}

	public String getWebpage() {
		return webpage;
	}

	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

 	private void generateSortName() {
	    	this.sortlastfirstname= (lastname!=null?lastname.toLowerCase().trim():"") + ( getName()!=null? (" "+getName().toLowerCase().trim() ) :"");
	}
	  
}
