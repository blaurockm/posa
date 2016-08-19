package net.buchlese.bofc.api.bofc;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "postx" )
public class PosTx {

	@Id
	@NotNull
	@JsonProperty
	private long id;

	@JsonProperty
	private long belegNr;
	@JsonProperty
	private int pointid;

	@JsonProperty
	private int belegIdx;

	@JsonProperty
	private int articleId;

	@JsonProperty
	private String articleKey;

	@JsonProperty
	private String articleGroupKey;

	@JsonProperty
	private String ean;

	@JsonProperty
	private String isbn;

	@NotNull
	@JsonProperty
	private String matchCode;

	@JsonProperty
	private String description;

	@NotNull
	@JsonProperty
	@Column(name = "count_")
	private int count;

	@JsonProperty
	private Long sellingPrice;   // einzel-Verkaufspreis

	@JsonProperty
	private Long purchasePrice;  // einzel-Einkaufspreis

	@JsonProperty
	private double rebate;  // marge

	@NotNull
	@JsonProperty
	private Long total;

	@NotNull
	@JsonProperty
	@Enumerated(EnumType.STRING)
	private Tax tax;

	@NotNull
	@JsonProperty
	@Enumerated(EnumType.STRING)
	private TxType type;

	@NotNull
	@JsonProperty
	@Column(name = "belegdatum")
	private LocalDateTime timestamp;

	@JsonProperty
	private boolean toBeIgnored;

	@JsonProperty
	private boolean toBeCheckedAgain;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getBelegNr() {
		return belegNr;
	}
	public void setBelegNr(long belegNr) {
		this.belegNr = belegNr;
	}
	public String getEan() {
		return ean;
	}
	public void setEan(String ean) {
		this.ean = ean;
	}
	public String getMatchCode() {
		return matchCode;
	}
	public void setMatchCode(String matchCode) {
		this.matchCode = matchCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Long getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(Long sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public Long getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(Long purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public double getRebate() {
		return rebate;
	}
	public void setRebate(double rebate) {
		this.rebate = rebate;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public String getArticleKey() {
		return articleKey;
	}
	public void setArticleKey(String articleNr) {
		this.articleKey = articleNr;
	}
	public Tax getTax() {
		return tax;
	}
	public void setTax(Tax tax) {
		this.tax = tax;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public int getBelegIdx() {
		return belegIdx;
	}
	public void setBelegIdx(int belegIdx) {
		this.belegIdx = belegIdx;
	}
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public String getArticleGroupKey() {
		return articleGroupKey;
	}
	public void setArticleGroupKey(String articleGroupKey) {
		this.articleGroupKey = articleGroupKey;
	}
	public TxType getType() {
		return type;
	}
	public void setType(TxType type) {
		this.type = type;
	}
	public String getIsbn() {
		return isbn;
	}
	public boolean isToBeIgnored() {
		return toBeIgnored;
	}
	public void setToBeIgnored(boolean toBeIgnored) {
		this.toBeIgnored = toBeIgnored;
	}
	public boolean isToBeCheckedAgain() {
		return toBeCheckedAgain;
	}
	public void setToBeCheckedAgain(boolean toBeCheckedAgain) {
		this.toBeCheckedAgain = toBeCheckedAgain;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getPointid() {
		return pointid;
	}

	public void setPointid(int pointid) {
		this.pointid = pointid;
	}
	
}
