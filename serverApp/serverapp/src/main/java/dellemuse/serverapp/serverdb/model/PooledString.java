package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 
 * CREATE TABLE pooledString ( id bigint primary key not null, value character
 * varying (2048) );
 * 
 */
@Entity
@Table(name = "pooledString")
@JsonInclude(Include.NON_NULL)
public class PooledString {

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "value", length = 2048)
	private String value;

	public PooledString() {
	}

	public PooledString(Long id, String value) {
		this.id = id;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("PooledString { ");
		str.append("\"id\": ").append(id);
		if (value != null)
			str.append(", \"value\": \"").append(value).append("\"");
		str.append(" }");
		return str.toString();
	}
}
