package net.buchlese.posa.api.bofc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerState {
	@JsonProperty
	private long timest;
	
	@JsonProperty
	private boolean dbConnection;
	
	@JsonProperty
	private String ipAddress;
	
	@JsonProperty
	private java.util.Date lastDbConnection;

	public long getTimest() {
		return timest;
	}

	public void setTimest(long timest) {
		this.timest = timest;
	}

	public boolean isDbConnection() {
		return dbConnection;
	}

	public void setDbConnection(boolean dbConnection) {
		this.dbConnection = dbConnection;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public java.util.Date getLastDbConnection() {
		return lastDbConnection;
	}

	public void setLastDbConnection(java.util.Date lastDbConnection) {
		this.lastDbConnection = lastDbConnection;
	}
	

}
