package net.buchlese.bofc.api.bofc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mapping {
	@JsonProperty
	private int pointid;
	@JsonProperty
	private int customerId;
	@JsonProperty
	private String name1;
	@JsonProperty
	private int debitorId;
	@JsonProperty
	private int count;
	public int getPointid() {
		return pointid;
	}
	public void setPointid(int pointid) {
		this.pointid = pointid;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public int getDebitorId() {
		return debitorId;
	}
	public void setDebitorId(int debitorId) {
		this.debitorId = debitorId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

}
