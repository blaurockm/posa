package net.buchlese.bofc.api.bofc;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Command  {

	@JsonProperty
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;

	@JsonProperty
	public String result;

	@JsonProperty
	public String param1;

	@JsonProperty
	public String param2;

	@JsonProperty
	public String param3;

	@JsonProperty
	public String param4;

	@JsonProperty
	public String action;

	@JsonProperty
	public int pointid;

	@JsonProperty
	public boolean fetched;

	@JsonProperty
	private LocalDateTime creationtime;

	@JsonProperty
	private LocalDateTime executiontime;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getPointid() {
		return pointid;
	}

	public void setPointid(int pointId) {
		this.pointid = pointId;
	}


	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getParam4() {
		return param4;
	}

	public void setParam4(String param4) {
		this.param4 = param4;
	}

	@Override
	public String toString() {
		return "Command [id=" + id + ", param1=" + param1 + ", action=" + action + ", pointId=" + pointid + "]";
	}
	
	
}
