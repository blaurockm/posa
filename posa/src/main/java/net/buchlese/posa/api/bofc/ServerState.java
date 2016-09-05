package net.buchlese.posa.api.bofc;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.buchlese.posa.xml.LocalDateTimeSerializer;

public class ServerState implements SendableObject {
	@JsonProperty
	private int pointid;

	@JsonProperty
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	private LocalDateTime timest;
	
	@JsonProperty
	private boolean dbConnection;
	
	@JsonProperty
	private String ipAddress;
	
	@JsonProperty
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	private LocalDateTime lastDbConnection;
	@JsonProperty
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	private LocalDateTime lastSyncRun;
	@JsonProperty
	private long syncDuration;
	@JsonProperty
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	private LocalDateTime lastHomingRun;

	public LocalDateTime getLastSyncRun() {
		return lastSyncRun;
	}

	public void setLastSyncRun(LocalDateTime lastSyncRun) {
		this.lastSyncRun = lastSyncRun;
	}

	public long getSyncDuration() {
		return syncDuration;
	}

	public void setSyncDuration(long syncDuration) {
		this.syncDuration = syncDuration;
	}

	public LocalDateTime getLastHomingRun() {
		return lastHomingRun;
	}

	public void setLastHomingRun(LocalDateTime lastHomingRunc) {
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

	public LocalDateTime getTimest() {
		return timest;
	}

	public void setTimest(LocalDateTime timest) {
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

	public LocalDateTime getLastDbConnection() {
		return lastDbConnection;
	}

	public void setLastDbConnection(LocalDateTime lastDbConnection) {
		this.lastDbConnection = lastDbConnection;
	}

	public int getPointid() {
		return pointid;
	}

	public void setPointid(int pointid) {
		this.pointid = pointid;
	}

	@Override
	public String toString() {
		return "ServerState [timest=" + timest + "]";
	}
	

}
