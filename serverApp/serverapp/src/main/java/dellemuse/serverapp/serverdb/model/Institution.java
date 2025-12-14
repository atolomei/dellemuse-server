package dellemuse.serverapp.serverdb.model;

import java.time.ZoneId;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseListIdSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "institution")
@JsonInclude(Include.NON_NULL)
public class Institution extends MultiLanguageObject {
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = InstitutionType.class)
	@JoinColumn(name = "institutionType_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdSerializer.class)
	private InstitutionType institutionType;

	@Column(name = "shortName")
	private String shortName;
 
	@Column(name = "address")
	private String address;

	@Column(name = "addressKey")
	private String addressKey;

	@Column(name = "moreinfo")
	private String moreinfo;

	@Column(name = "moreinfoKey")
	private String moreinfoKey;

	@Column(name = "website")
	private String website;

	@Column(name = "mapurl")
	private String mapurl;

	@Column(name = "email")
	private String email;

	@Column(name = "instagram")
	private String instagram;

	@Column(name = "whatsapp")
	private String whatsapp;

	@Column(name = "phone")
	private String phone;

	@Column(name = "twitter")
	private String twitter;
	
	@JsonProperty("zoneId")
	@Column(name = "zoneid")
	private String zoneId;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, targetEntity = Site.class)
	@JoinColumn(name = "institution_id", nullable = true, insertable = false)
	@JsonSerialize(using = DelleMuseListIdSerializer.class)
	@JsonManagedReference
	@JsonBackReference
	@OrderBy("lower(name) ASC")
	private List<Site> sites;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "logo", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("logo")
	@JsonSerialize(using = DelleMuseIdSerializer.class)
	private Resource logo;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "map", updatable = true, nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("map")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource map;

	public Institution() {
	}


	public String getPrefixUrl() {
		return PrefixUrl.Institution;
	}

	@Override
	public String getTitle() {
		return getName();
	}
	
	public InstitutionType getInstitutionType() {
		return institutionType;
	}

	public void setInstitutionType(InstitutionType institutionType) {
		this.institutionType = institutionType;
	}
  

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressKey() {
		return addressKey;
	}

	public void setAddressKey(String addressKey) {
		this.addressKey = addressKey;
	}

	public String getMoreinfo() {
		return moreinfo;
	}

	public void setMoreinfo(String moreinfo) {
		this.moreinfo = moreinfo;
	}

	public String getMoreinfoKey() {
		return moreinfoKey;
	}

	public void setMoreinfoKey(String moreinfoKey) {
		this.moreinfoKey = moreinfoKey;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getMapUrl() {
		return mapurl;
	}

	public void setMapUrl(String linktomap) {
		this.mapurl = linktomap;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getWhatsapp() {
		return whatsapp;
	}

	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

 

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Resource getLogo() {
		return logo;
	}

	public void setLogo(Resource logo) {
		this.logo = logo;
	}
	
	@Override
	public String toString() {

		StringBuilder str = new StringBuilder();
	
		str.append(getClass().getSimpleName());
		
		str.append(" { \"id\": "+ getId().toString());
		
		if (getName()!=null)
			str.append(", \"name\": \""+ getName().toString()+"\"");
		
		if (getTitle()!=null)
			str.append(", \"title\": \""+ getName().toString()+"\"");
		
		if (getShortName()!=null)
			str.append(", \"shortName\": \""+ getShortName().toString()+"\"");
		
		if (getWebsite()!=null)
			str.append(", \"website\": \""+ getWebsite().toString()+"\"");
		
		if (getEmail()!=null)
			str.append(", \"email\": \""+ getEmail().toString()+"\"");
		
		if (getMapUrl()!=null)
			str.append(", \"mapUrl\": \""+ getMapUrl().toString()+"\"");
		
		if (getLastModified()!=null)
			str.append(", \"lastModified\": \""+ df.format(getLastModified())+"\" }");

		return str.toString();
	}

	@Override
	public boolean equals(Object o) {

		if (o==null)
			return false;
		 
		if (this == o) return true;

		if (!(o instanceof Institution)) return false;
		 
		if (this.getId()==null)
			return false;
	 
		if ((o instanceof GuideContent)) {
			
			if (((Institution) o).getId()==null)
					return false;
			
			return ((Institution) o).getId().equals(getId());
		}
		
		return false;
	}
	

	public String getZoneIdStr() {
		return zoneId;
	}

	public void setZoneIdStr(String zoneid) {
		this.zoneId = zoneid;
	}
	public void setZoneId(ZoneId z) {
		this.setZoneIdStr(z.getId());
	}
	
	public ZoneId getZoneId() {
		
		if (getZoneIdStr()==null)
			return ZoneId.systemDefault();
		
		return ZoneId.of(zoneId);
	}
	
	public static final String getIcon() {
		return "fa-duotone fa-building";
	}
	
};
