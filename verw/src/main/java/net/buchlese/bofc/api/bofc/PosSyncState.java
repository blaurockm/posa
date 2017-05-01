package net.buchlese.bofc.api.bofc;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "possyncstate" )
@JsonIgnoreProperties(ignoreUnknown = true)
public class PosSyncState {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private Long id;

	@JsonProperty
	private int pointid;

	@JsonProperty
	@Column(name = "synckey")
	private String key;

	@JsonProperty
	private Timestamp timest;

	@JsonProperty
	@Column(name = "syncmark")
	private BigDecimal mark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPointid() {
		return pointid;
	}

	public void setPointid(int pointid) {
		this.pointid = pointid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Timestamp getTimest() {
		return timest;
	}

	public void setTimest(Timestamp timest) {
		this.timest = timest;
	}

	public BigDecimal getMark() {
		return mark;
	}

	public void setMark(BigDecimal mark) {
		this.mark = mark;
	}

	@Override
	public String toString() {
		return "PosSyncState [pointid=" + pointid + ", key=" + key + "]";
	}

	
}
