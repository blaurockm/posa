package net.buchlese.posa.api.bofc;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.buchlese.posa.xml.LocalDateSerializer;

public class ServerState implements SendableObject {
	@JsonProperty
	private int pointid;

	@JsonProperty
	@JsonSerialize(using=LocalDateSerializer.class)
	private LocalDateTime timest;
	
	@JsonProperty
	private boolean dbConnection;
	
	@JsonProperty
	private String ipAddress;
	
	@JsonProperty
	private Instant lastDbConnection;
	@JsonProperty
	private Instant lastSyncRun;
	@JsonProperty
	private Duration syncDuration;
	@JsonProperty
	private Instant lastHomingRun;

	public Instant getLastSyncRun() {
		return lastSyncRun;
	}

	public void setLastSyncRun(Instant lastSyncRun) {
		this.lastSyncRun = lastSyncRun;
	}

	public Duration getSyncDuration() {
		return syncDuration;
	}

	public void setSyncDuration(Duration syncDuration) {
		this.syncDuration = syncDuration;
	}

	public Instant getLastHomingRun() {
		return lastHomingRun;
	}

	public void setLastHomingRun(Instant lastHomingRunc) {
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

	public Instant getLastDbConnection() {
		return lastDbConnection;
	}

	public void setLastDbConnection(Instant lastDbConnection) {
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
