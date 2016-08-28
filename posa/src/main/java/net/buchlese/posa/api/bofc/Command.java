package net.buchlese.posa.api.bofc;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Command implements SendableObject {


	@JsonProperty
	public String id;

	@JsonProperty
	public Object result;

	@JsonProperty
	public Object[] params;

	@JsonProperty
	public String action;

	@JsonProperty
	public int pointId;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getPointId() {
		return pointId;
	}

	public void setPointId(int pointId) {
		this.pointId = pointId;
	}

	@Override
	public String toString() {
		return "Command [id=" + id + ", params=" + Arrays.toString(params)
				+ ", action=" + action + "]";
	}
	
}
