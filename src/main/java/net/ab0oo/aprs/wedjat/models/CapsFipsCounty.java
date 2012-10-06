package net.ab0oo.aprs.wedjat.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cap_fips_counties")

public class CapsFipsCounty implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long capsId;
	private String fipsCode;

	public CapsFipsCounty(String fipsCode) {
		fipsCode.trim();
		this.fipsCode = fipsCode;
		if ( fipsCode.length() == 6 && fipsCode.startsWith("0")) {
			this.fipsCode = fipsCode.substring(1);
		}
	}
	
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="caps_id")
	@ManyToOne
	public Long getCapsId() {
		return capsId;
	}
	public void setCapsId(Long capsId) {
		this.capsId = capsId;
	}
	public String getFipsCode() {
		return fipsCode;
	}
	public void setFipsCode(String fipsCode) {
		fipsCode.trim();
		this.fipsCode = fipsCode;
		if ( fipsCode.length() == 6 && fipsCode.startsWith("0")) {
			this.fipsCode = fipsCode.substring(1);
		}
	}
}
