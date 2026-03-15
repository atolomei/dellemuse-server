package dellemuse.serverapp.serverdb.model;

import java.time.OffsetDateTime;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.jpa.events.ArtWorkEventListener;
import dellemuse.serverapp.jpa.events.CandidateEventListener;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseListIdSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseUserSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
 
import jakarta.persistence.Table;


/**
 * 
 * 
 * <p>See also {@link CandidateValidateEmailCommand}</p>
 */
@Entity
@Table(name = "candidate")
@JsonInclude(Include.NON_NULL)
@EntityListeners(CandidateEventListener.class)
public class Candidate extends DelleMuseObject {

	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Institution.class)
	@JoinColumn(name = "institution_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private Institution institution;

	
	@ManyToOne(fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.DETACH, targetEntity = User.class)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "user_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("user")
	@JsonSerialize(using = DelleMuseUserSerializer.class)
	private User user;
	
	
	@Column(name = "emailValidated")
	private boolean emailValidated;
	  
    @Column(name = "personname")
	private String personName;
	
    @Column(name = "personlastname")
	private String personLastname;

    @Column(name = "email")
	private String email;

    @Column(name = "phone")
	private String phone;
    
    @Column(name = "institutionname")
  	private String institutionName;

    @Column(name = "institutionaddress")
  	private String institutionAddress;

    @Column(name = "comments")
  	private String comments;
    
    @Column(name = "contactreferences")
  	private String references;


    @Column(name = "validationEmailSent")
  	private  OffsetDateTime validationEmailSent;

    @Column(name = "language")
    private String language;
    
    @Column(name = "password")
    private String password;
    
    
    
	public String getDisplayname() {
		return (getInstitutionName() != null) ? getInstitutionName() : "null";
	}

	
    // -------
    
    @Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
    private CandidateStatus status;
    
    @Column(name = "internalcomments")
  	private String internalcomments;
   
    @ManyToOne(fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.DETACH, targetEntity = User.class)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "evaluatedby", nullable = true)
	@JsonManagedReference
	@JsonProperty("evaluatedby")
	@JsonSerialize(using = DelleMuseUserSerializer.class)
	private User evaluatedBy;  
    
    
    
	@Override
	public String getObjectClassName() {
		return Candidate.class.getSimpleName();
	}
	
    public Candidate() {
    }

	public String getPersonName() {
		return personName;
	}

	public String getPersonLastname() {
		return personLastname;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getInstitutionAddress() {
		return institutionAddress;
	}

	public String getComments() {
		return comments;
	}

	public String getReferences() {
		return references;
	}

	public CandidateStatus getStatus() {
		return status;
	}

	public String getInternalcomments() {
		return internalcomments;
	}

	public User getEvaluatedBy() {
		return evaluatedBy;
	}

	public boolean isEmailValidated() {
		return emailValidated;
	}

	public void setEmailValidated(boolean emailValidated) {
		this.emailValidated = emailValidated;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public Institution getInstitution() {
		return institution;
	}

	public User getUser() {
		return user;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setPersonLastname(String personLastname) {
		this.personLastname = personLastname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public void setInstitutionAddress(String institutionAddress) {
		this.institutionAddress = institutionAddress;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setReferences(String references) {
		this.references = references;
	}

	public OffsetDateTime getValidationEmailSent() {
		return validationEmailSent;
	}

	public void setValidationEmailSent(OffsetDateTime validationEmailSent) {
		this.validationEmailSent = validationEmailSent;
	}

	public void setStatus(CandidateStatus status) {
		this.status = status;
	}

	public void setInternalcomments(String internalcomments) {
		this.internalcomments = internalcomments;
	}

	public void setEvaluatedBy(User evaluatedBy) {
		this.evaluatedBy = evaluatedBy;
	}

	public static String getIcon() {
		return Icons.Candidate; // "fa-duotone fa-solid fa-building-circle-check";
	}

};
