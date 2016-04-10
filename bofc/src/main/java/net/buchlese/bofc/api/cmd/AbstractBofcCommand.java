package net.buchlese.bofc.api.cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractBofcCommand {
	private final long id;
	@JsonProperty
	private final String action;
	@JsonProperty
	private int pointId;
	@JsonProperty
	private String result;
	@JsonProperty
	private Object[] params;
	public AbstractBofcCommand(String action) {
		super();
		id = System.currentTimeMillis();
		this.action = action;
	}
	public String getId() {
		return String.valueOf(id);
	}
	public String getAction() {
		return action;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	
	public String toString() {
		return getClass().getSimpleName() + " " + (params != null ? params.toString() : "-");
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractBofcCommand other = (AbstractBofcCommand) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public int getPointId() {
		return pointId;
	}
	public void setPointId(int pointId) {
		this.pointId = pointId;
	}
}
