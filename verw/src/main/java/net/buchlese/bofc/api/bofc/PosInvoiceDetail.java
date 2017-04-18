package net.buchlese.bofc.api.bofc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "posinvoice_detail")
public class PosInvoiceDetail {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private Long id;

	@JsonProperty
	private Long amount;     // Positionsbetrag gesamt
	@JsonProperty
	private Long amountHalf;     // Positionsbetrag mit halben MwSt-Satz
	@JsonProperty
	private Long amountFull;     // Positionsbetrag mit vollem MwSt-Satz
	@JsonProperty
	private Long amountNone;     // Positionsbetrag ohne MwSt
	@JsonProperty
	private Long lfdNr;     // Laufende Nummer

	@JsonProperty
	private String text;

	@JsonProperty
	private boolean textonly;

	@JsonProperty
	private int quantity;

	@JsonProperty
	private int articleId;

	@JsonProperty
	private long rebate;  // rabatt mit bis zu 5 Nachkommastellen

	@JsonProperty
	private long singlePrice; // einzelpreis, unrabattiert

	@JsonProperty
	private long rebatePrice; // einzelpreis rabattiert
	
	@JsonProperty
	private boolean inclTax = true;
	

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getAmountHalf() {
		return amountHalf;
	}

	public void setAmountHalf(Long amountHalf) {
		this.amountHalf = amountHalf;
	}

	public Long getAmountFull() {
		return amountFull;
	}

	public void setAmountFull(Long amountFull) {
		this.amountFull = amountFull;
	}

	public Long getAmountNone() {
		return amountNone;
	}

	public void setAmountNone(Long amountNone) {
		this.amountNone = amountNone;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isTextonly() {
		return textonly;
	}

	public void setTextonly(boolean textonly) {
		this.textonly = textonly;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getRebate() {
		return rebate;
	}

	public void setRebate(long rebate) {
		this.rebate = rebate;
	}

	public long getSinglePrice() {
		return singlePrice;
	}

	public void setSinglePrice(long singlePrice) {
		this.singlePrice = singlePrice;
	}

	public long getRebatePrice() {
		return rebatePrice;
	}

	public void setRebatePrice(long rebatePrice) {
		this.rebatePrice = rebatePrice;
	}

	public boolean isInclTax() {
		return inclTax;
	}

	public void setInclTax(boolean inclTax) {
		this.inclTax = inclTax;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		PosInvoiceDetail other = (PosInvoiceDetail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLfdNr() {
		return lfdNr;
	}

	public void setLfdNr(Long lfdNr) {
		this.lfdNr = lfdNr;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

}
