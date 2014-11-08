package net.buchlese.bofc.api.bofc;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PosTx {

	@NotEmpty
	@JsonProperty
	private long id;

	@JsonProperty
	private long belegNr;

	@JsonProperty
	private int belegIdx;

	@JsonProperty
	private int articleId;

	@JsonProperty
	private String articleKey;

	@NotEmpty
	@JsonProperty
	private String articleGroupKey;

	@JsonProperty
	private String ean;

	@JsonProperty
	private String isbn;

	@NotEmpty
	@JsonProperty
	private String matchCode;

	@JsonProperty
	private String description;

	@NotEmpty
	@JsonProperty
	private int count;

	@JsonProperty
	private Long sellingPrice;   // einzel-Verkaufspreis

	@JsonProperty
	private Long purchasePrice;  // einzel-Einkaufspreis

	@JsonProperty
	private double rebate;  // marge

	@NotEmpty
	@JsonProperty
	private Long total;

	@NotEmpty
	@JsonProperty
	private Tax tax;

	@NotEmpty
	@JsonProperty
	private TxType type;

	@NotEmpty
	@JsonProperty
	private DateTime timestamp;

	@NotEmpty
	@JsonProperty
	private boolean toBeIgnored;

	@NotEmpty
	@JsonProperty
	private boolean toBeCheckedAgain;

	// das ArticleGroup-Object wollen wir im json drin haben, aber nicht in der datenbank
	public ArticleGroup getArticleGroup() {
		ArticleGroup g =  ArticleGroup.getArticleGroups().get(getArticleGroupKey());
		if (g != null) {
			return g;
		}
		return ArticleGroup.NONE;
	}
	
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
	public DateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(DateTime timestamp) {
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
	
}
