package net.buchlese.bofc.api.bofc;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SequenceGen {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(nullable=false)
	private String seqKey;
	
	private long current;
	
	private long start;
	
	private java.sql.Date validFrom;
	
	private java.sql.Date validTill;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSeqKey() {
		return seqKey;
	}

	public void setSeqKey(String seqKey) {
		this.seqKey = seqKey;
	}

	public long getCurrent() {
		return current;
	}

	public void setCurrent(long current) {
		this.current = current;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public LocalDate getValidFromasLocalDate() {
		return validFrom.toLocalDate();
	}

	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = java.sql.Date.valueOf(validFrom);
	}

	public LocalDate getValidTillasLocalDate() {
		return validTill.toLocalDate();
	}

	public void setValidTill(LocalDate validTill) {
		this.validTill =  java.sql.Date.valueOf(validTill);
	}

	public java.sql.Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(java.sql.Date validFrom) {
		this.validFrom = validFrom;
	}

	public java.sql.Date getValidTill() {
		return validTill;
	}

	public void setValidTill(java.sql.Date validTill) {
		this.validTill = validTill;
	}
	
	
}
