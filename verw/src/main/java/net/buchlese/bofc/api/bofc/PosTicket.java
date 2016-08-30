package net.buchlese.bofc.api.bofc;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "posticket" )
@JsonIgnoreProperties(ignoreUnknown = true)
public class PosTicket {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private long id;

	@JsonProperty
	private long belegNr;
	
	@JsonProperty
	private int pointid;

	@JsonProperty
	private Long total;

	@JsonProperty
	private boolean cancelled;

	@JsonProperty
	private boolean cancel;

	@JsonProperty
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	@Column(name = "belegdatum")
	@JsonProperty
	private LocalDateTime timestamp;

	@JsonProperty
	@Transient
	private List<PosTx> txs;
	
	public long getId() {
		return id;
	}

	public long getBelegNr() {
		return belegNr;
	}

	public void setBelegNr(long belegNr) {
		this.belegNr = belegNr;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public List<PosTx> getTxs() {
		return txs;
	}

	public void setTxs(List<PosTx> txs) {
		this.txs = txs;
	}

	public int getPointid() {
		return pointid;
	}

	public void setPointid(int pointid) {
		this.pointid = pointid;
	}


}
