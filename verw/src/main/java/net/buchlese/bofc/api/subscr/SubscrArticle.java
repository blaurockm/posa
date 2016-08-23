package net.buchlese.bofc.api.subscr;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "subscrarticle" )
public class SubscrArticle implements Comparable<SubscrArticle> {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	@JsonProperty
	private long id;

	@JsonProperty
	private String name;

	@JsonProperty
	@Transient
	private long productId;

	@JsonProperty
	private String isbn;
	@JsonProperty
	private long brutto;
	@JsonProperty
	private long brutto_half;
	@JsonProperty
	private long brutto_full;
	@JsonProperty
	private double halfPercentage =1d; // prozentuale Anteil am Gesamtpreis , halber Steuersatz, 0 < x < 1
	
	@JsonProperty
	private LocalDate erschTag;
	
	@JsonProperty
	private int issueNo =1;

	@JsonIgnore
	@ManyToOne
	private SubscrProduct product;


	@JsonIgnore
	public String initializeName(String namePattern) {
		String name = namePattern.replace("#", String.valueOf(issueNo));
		name = name.replace("{number}", String.valueOf(issueNo));
		String dp = "\\{date:(.+)\\}";
		Pattern p = Pattern.compile(dp);
		Matcher m = p.matcher(name);
		if (m.find() && erschTag != null) {
			String datePattern = m.group(1);
			String dateString = erschTag.format(DateTimeFormatter.ofPattern(datePattern));
			name = name.replaceFirst(dp, dateString);
		}
		return name;
	}

	@JsonIgnore
	public void updateBrutto(long br) {
		brutto =  br;
		brutto_half = (long) (brutto * halfPercentage);
		brutto_full = brutto - brutto_half;
	}

	@JsonIgnore
	public void updateHalfPercentage(double p) {
		halfPercentage = p;
		brutto_half = (long) (brutto * halfPercentage);
		brutto_full = brutto - brutto_half;
	}

	@JsonIgnore
	public void updateBruttoHalf(long br) {
		brutto_half =  br;
		halfPercentage = ((double) br / (double) brutto);
		brutto_full = brutto - brutto_half;
	}

	@JsonIgnore
	public void updateBruttoFull(long br) {
		brutto_full =  br;
		halfPercentage =  1- ((double) br / (double) brutto);
		brutto_half = brutto - brutto_full;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public long getBrutto() {
		return brutto;
	}

	public void setBrutto(long brutto) {
		this.brutto = brutto;
	}

	public long getBrutto_half() {
		return brutto_half;
	}

	public void setBrutto_half(long brutto_half) {
		this.brutto_half = brutto_half;
	}

	public long getBrutto_full() {
		return brutto_full;
	}

	public void setBrutto_full(long brutto_full) {
		this.brutto_full = brutto_full;
	}

	public double getHalfPercentage() {
		return halfPercentage;
	}

	public void setHalfPercentage(double halfPercentage) {
		this.halfPercentage = halfPercentage;
	}

	@Override
	public int compareTo(SubscrArticle o) {
		return getErschTag().compareTo(o.getErschTag());
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public LocalDate getErschTag() {
		return erschTag;
	}

	public void setErschTag(LocalDate erschTag) {
		this.erschTag = erschTag;
	}

	public int getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(int issueNo) {
		this.issueNo = issueNo;
	}

	public SubscrProduct getProduct() {
		return product;
	}

	public void setProduct(SubscrProduct subscrProduct) {
		this.product = subscrProduct;
	}

	
}
