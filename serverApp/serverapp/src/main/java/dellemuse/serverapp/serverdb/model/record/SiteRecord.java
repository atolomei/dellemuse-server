package dellemuse.serverapp.serverdb.model.record;
 
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * 
 * Indexes are -> lower( title )
 * 
 */
@Entity
@Table(name = "siteRecord")
@JsonInclude(Include.NON_NULL)
public class SiteRecord extends TranslationRecord {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Site.class)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    @JsonProperty("site")
    private Site site;
 
 	@Column(name = "abstract")
	private String siteAbstract;
 	
	@Column(name = "abstract_hash")
	private int abstractHash;

	@Column(name = "opens")
	private String opens;
	
	@Column(name = "opens_hash")
	private int opensHash;
	

	@Column(name = "address")
	private String address;
	
	@Column(name = "address_hash")
	private int addressHash;
	
	public SiteRecord() {
	}

	@Override
	public String getDisplayname() {
		return getName();
	}

	public String getSiteAbstract() {
		return siteAbstract;
	}

	public void setSiteAbstract(String siteAbstract) {
		this.siteAbstract = siteAbstract;
	}

	public String getOpens() {
		return opens;
	}

	public void setOpens(String opens) {
		this.opens = opens;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Override
	public MultiLanguageObject getParentObject() {
		 return this.site !=null? this.site : null;
	 }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAbstractHash() {
		return abstractHash;
	}

	public int getOpensHash() {
		return opensHash;
	}

	public int getAddressHash() {
		return addressHash;
	}

	public void setAbstractHash(int abstractHash) {
		this.abstractHash = abstractHash;
	}

	public void setOpensHash(int opensHash) {
		this.opensHash = opensHash;
	}

	public void setAddressHash(int addressHash) {
		this.addressHash = addressHash;
	}


};
