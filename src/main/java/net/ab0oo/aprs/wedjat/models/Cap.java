package net.ab0oo.aprs.wedjat.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="cap",
		uniqueConstraints = {@UniqueConstraint(columnNames={"eventId"})}
)

public class Cap implements Serializable {
	@OneToMany
	@JoinTable(
			name="CapFipsCounties",
			joinColumns = @JoinColumn( name = "capsid"),
			inverseJoinColumns = @JoinColumn( name = "capsid")
	)
	private static final long serialVersionUID = 1L;
	private Long Id;
	private Date updateTime, expireTime;
	private String event;
	@OneToMany(mappedBy="capsId")
	private Set<CapsFipsCounty> fipsAreas;
	private String category;
	private String eventId;
	private String severity;
	private String urgency;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Set<CapsFipsCounty> getFipsAreas() {
		return fipsAreas;
	}
	public void setFipsAreas(Set<CapsFipsCounty> fipsAreas) {
		this.fipsAreas = fipsAreas;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	
}