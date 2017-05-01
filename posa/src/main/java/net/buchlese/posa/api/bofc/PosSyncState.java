package net.buchlese.posa.api.bofc;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PosSyncState {

	@JsonProperty
	private Long id;

	@JsonProperty
	private int pointid;

	@JsonProperty
	private String key;

	@JsonProperty
	private DateTime timest;

	@JsonProperty
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

	public DateTime getTimest() {
		return timest;
	}

	public void setTimest(DateTime timest) {
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
		return "PosSyncState [key=" + key + ", timest=" + timest + ", mark="
				+ mark + "]";
	}

	
}
