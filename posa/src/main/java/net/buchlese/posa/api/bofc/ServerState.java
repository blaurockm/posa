package net.buchlese.posa.api.bofc;

import org.joda.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerState implements SendableObject {
	@JsonProperty
	private Instant timest;
	
	@JsonProperty
	private boolean dbConnection;
	
	@JsonProperty
	private String ipAddress;
	
	@JsonProperty
	private Instant lastDbConnection;

	public Instant getTimest() {
		return timest;
	}

	public void setTimest(Instant timest) {
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

	public Instant getLastDbConnection() {
		return lastDbConnection;
	}

	public void setLastDbConnection(Instant lastDbConnection) {
		this.lastDbConnection = lastDbConnection;
	}
	

}
