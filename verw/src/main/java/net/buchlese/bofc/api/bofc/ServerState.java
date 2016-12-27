package net.buchlese.bofc.api.bofc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerState {
	@JsonProperty
	private int pointid;

	@JsonProperty
	private java.sql.Timestamp timest;
	
	@JsonProperty
	private boolean dbConnection;
	
	@JsonProperty
	private String ipAddress;
	
	@JsonProperty
	private java.sql.Timestamp lastDbConnection;
	@JsonProperty
	private java.sql.Timestamp lastSyncRun;
	@JsonProperty
	private long syncDuration;
	@JsonProperty
	private java.sql.Timestamp lastHomingRun;

	public java.sql.Timestamp getLastSyncRun() {
		return lastSyncRun;
	}

	public void setLastSyncRun(java.sql.Timestamp lastSyncRun) {
		this.lastSyncRun = lastSyncRun;
	}

	public long getSyncDuration() {
		return syncDuration;
	}

	public void setSyncDuration(long syncDuration) {
		this.syncDuration = syncDuration;
	}

	public java.sql.Timestamp getLastHomingRun() {
		return lastHomingRun;
	}

	public void setLastHomingRun(java.sql.Timestamp lastHomingRunc) {
		this.lastHomingRun = lastHomingRunc;
	}

	@JsonProperty
	private boolean[] thisWeek;
	
	@JsonProperty
	private boolean[] lastWeek;
	
	public boolean[] getThisWeek() {
		return thisWeek;
	}

	public void setThisWeek(boolean[] thisWeek) {
		this.thisWeek = thisWeek;
	}

	public boolean[] getLastWeek() {
		return lastWeek;
	}

	public void setLastWeek(boolean[] lastWeek) {
		this.lastWeek = lastWeek;
	}

	public java.sql.Timestamp getTimest() {
		return timest;
	}

	public void setTimest(java.sql.Timestamp timest) {
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

	public java.sql.Timestamp getLastDbConnection() {
		return lastDbConnection;
	}

	public void setLastDbConnection(java.sql.Timestamp lastDbConnection) {
		this.lastDbConnection = lastDbConnection;
	}

	public int getPointid() {
		return pointid;
	}

	public void setPointid(int pointid) {
		this.pointid = pointid;
	}
	

}
