package net.buchlese.bofc.api.bofc;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserChange {

	@NotEmpty
	@JsonProperty
	private long id;

	@JsonProperty
	private long objectId;

	@JsonProperty
	private String login;
	
	@JsonProperty
	private String fieldId;
	
	@JsonProperty
	private String oldValue;

	@JsonProperty
	private String newValue;

	@JsonProperty
	private String action;

	@JsonProperty
	private DateTime modDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public DateTime getModDate() {
		return modDate;
	}

	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
	}
	
	
}
