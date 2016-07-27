package net.buchlese.bofc.api.bofc;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "posticket" )
public class PosTicket {
	@Id
//	@GeneratedValue(generator="increment")
//	@GenericGenerator(name="increment", strategy = "increment")
//	@NotEmpty
	@JsonProperty
	private long id;

	@JsonProperty
	private long belegNr;
	
	@JsonProperty
	private int pointid;

//	@NotEmpty
	@JsonProperty
	private Long total;

	@JsonProperty
	private boolean cancelled;

	@JsonProperty
	private boolean cancel;

//	@NotEmpty
	@JsonProperty
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "belegdatum")
//	@NotEmpty
	@JsonProperty
	private DateTime timestamp;

	@JsonProperty
	private boolean toBeCheckedAgain;

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

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
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

	public boolean isToBeCheckedAgain() {
		return toBeCheckedAgain;
	}

	public void setToBeCheckedAgain(boolean toBeCheckedAgain) {
		this.toBeCheckedAgain = toBeCheckedAgain;
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
