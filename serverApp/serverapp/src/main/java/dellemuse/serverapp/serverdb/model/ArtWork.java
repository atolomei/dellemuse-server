package dellemuse.serverapp.serverdb.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.jpa.events.ArtWorkEventListener;
import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artwork")
@EntityListeners(ArtWorkEventListener.class)
@JsonInclude(Include.NON_NULL)
public class ArtWork extends MultiLanguageObject {

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Site.class)
	@JoinColumn(name = "site_owner_id", nullable = true)
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private Site site;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "artwork_artist", joinColumns = @JoinColumn(name = "artwork_id"), inverseJoinColumns = @JoinColumn(name = "artist_id"))
	@JsonIgnoreProperties("artists")
	private Set<Artist> artists = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWorkType.class)
	@JoinColumn(name = "artworkType_id", nullable = true)
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private ArtWorkType artworkType;

	@Column(name = "year")
	private int year;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "qrcode", nullable = true)
	@JsonManagedReference
	@JsonProperty("qrcode")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource qrcode;

	@Column(name = "url")
	private String url;

	public ArtWork() {
	}

	@Override
	public String getObjectClassName() {
		return ArtWork.class.getSimpleName();
	}

	public String getPrefixUrl() {
		return PrefixUrl.ArtWork;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String geturl() {
		return this.url;
	}

	public ArtWorkType getArtworkType() {
		return artworkType;
	}

	public void setArtworkType(ArtWorkType artworkType) {
		this.artworkType = artworkType;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Resource getQRCode() {
		return this.qrcode;
	}

	public void setQRCode(Resource qrcode) {
		this.qrcode = qrcode;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return this.year;
	}

	public static String getIcon() {
		return Icons.ArtWork;
	}

	public Set<Artist> getArtists() {
		return artists;
	}

	public void setArtists(Set<Artist> artists) {
		this.artists = artists;
	}

	public void addArtist(Artist a) {
		artists.add(a);
		a.getArtworks().add(this);
	}

	public void removeArtist(Artist a) {
		artists.remove(a);
		a.getArtworks().remove(this);
	}

	public Resource getQrcode() {
		return qrcode;
	}

	public String getUrl() {
		return url;
	}

	public void setQrcode(Resource qrcode) {
		this.qrcode = qrcode;
	}

};

/**
 * @ManyToMany(fetch = FetchType.EAGER)
 * @JoinTable(name = "artWorkArtist", joinColumns = {@JoinColumn(name =
 *                 "artwork_id") }, inverseJoinColumns = {@JoinColumn(name =
 *                 "person_id") })
 * @JsonSerialize(using = DelleMuseSetPersonSerializer.class)
 * @JsonManagedReference
 * @JsonBackReference @JsonProperty("artists") Set<Person> artists = new
 *                    HashSet<>();
 **/

/**
 * 
 * @OneToMany( mappedBy = "artwork", cascade = CascadeType.ALL, orphanRemoval =
 * true, fetch = FetchType.LAZY )
 * 
 * 
 * @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, targetEntity
 *                  = ArtWorkArtist.class)
 * @JoinColumn(name = "artwork_id", nullable = true, insertable = true) private
 *                  Set<ArtWorkArtist> awartists = new HashSet<>();
 *
 *
 *
 * 
 * 
 * 
 *                  select aa.artist_id, p.id, p.lastname, p.name, aw.id,
 *                  aw.name
 * 
 *                  from artist a, artwork_artist aa, site s, artwork aw, person
 *                  p
 * 
 *                  where
 * 
 *                  a.id = ap.artist_id and a.person_id = p.id and aw.id =
 *                  ap.artwork_id and ap.artwork_id = aw.id and s.id=137;
 * 
 * 
 *                  update artwork_artist set artist_id = (select id from artist
 *                  a where person_id=a.person_id limit 1);
 * 
 *                  ALTER TABLE artwork_artist DROP CONSTRAINT
 *                  primary_key_constraint_name;
 * 
 * 
 * 
 *                  SELECT constraint_name, table_name, constraint_type FROM
 *                  information_schema.table_constraints WHERE table_name =
 *                  'artwork_artist' AND table_schema = 'public';
 * 
 * 
 * 
 *                  SELECT dellemuse-# constraint_name, dellemuse-# table_name,
 *                  dellemuse-# constraint_type dellemuse-# FROM dellemuse-#
 *                  information_schema.table_constraints dellemuse-# WHERE
 *                  dellemuse-# table_name = 'artwork_artist'
 *
 * 
 * 
 * 
 * 
 **/

//public Set<ArtWorkArtist> getAwArtists() {
//	return this.awartists;
//}

/**
 * @ManyToMany(fetch = FetchType.LAZY)
 * @JoinTable(name = "artwork_person", joinColumns = {@JoinColumn(name =
 *                 "artwork_id") }, inverseJoinColumns = {@JoinColumn(name =
 *                 "person_id") })
 * @JsonSerialize(using = DelleMuseSetPersonSerializer.class)
 * @JsonManagedReference
 * @JsonBackReference @JsonProperty("artists") Set<Person> artists = new
 *                    HashSet<>();
 */
