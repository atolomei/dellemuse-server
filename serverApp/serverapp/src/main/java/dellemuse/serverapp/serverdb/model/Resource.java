package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dellemuse.serverapp.jpa.events.ResourceEventListener;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;

@Entity
@Table(name = "resource")
@EntityListeners(ResourceEventListener.class)
@JsonInclude(Include.NON_NULL)
public class Resource extends DelleMuseObject {

	
	
	@Column(name = "bucketName")
	private String bucketName;

	@Column(name = "objectName")
	private String objectName;

	@Column(name = "fileName")
	private String fileName;

	@Column(name = "media")
	private String media;

	@Column(name = "info")
	private String info;

	@Column(name = "infoKey")
	private String infoKey;

	@Column(name = "usethumbnail")
	@JsonProperty("usethumbnail")
	private boolean usethumbnail;

	@Column(name = "size")
	@JsonProperty("size")
	private long size;

	@Column(name = "width")
	@JsonProperty("width")
	private int width;

	@Column(name = "height")
	@JsonProperty("height")
	private int height;

	@Column(name = "durationMilliseconds")
	@JsonProperty("durationMilliseconds")
	private long durationMilliseconds;

	@Column(name = "tag")
	private String tag;

	@Column(name = "audit")
	private String audit;

	@Column(name = "language")
	private String language;
	
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String lang) {
		language = lang;
	}
	public Resource() {
	}

	@Override
	public String getDisplayname() {
		return getName();
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucket) {
		this.bucketName = bucket;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfoKey() {
		return infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String f) {
		this.fileName = f;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public boolean isUsethumbnail() {
		return usethumbnail;
	}

	public void setUsethumbnail(boolean usethumbnail) {
		this.usethumbnail = usethumbnail;
	}

	public long getWidth() {
		return width;
	}

	public long getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public long getDurationMilliseconds() {
		return durationMilliseconds;
	}

	public void setDurationMilliseconds(long durationMilliseconds) {
		this.durationMilliseconds = durationMilliseconds;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	
	@Override
	public boolean equals(Object o) {

		if (o==null)
			return false;
		 
		if (this == o) return true;

		if (!(o instanceof RoleGeneral)) return false;
		 
		if (this.getId()==null)
			return false;
	 
		if ((o instanceof Resource)) {
			
			if (((Resource) o).getId()==null)
					return false;
			
			return ((Resource) o).getId().equals(getId());
		}
		
		return false;
	}

	
	public static String getIcon() {
		return "fa-duotone fa-file";
	}

	
};
