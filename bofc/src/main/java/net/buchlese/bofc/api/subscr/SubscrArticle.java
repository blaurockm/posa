package net.buchlese.bofc.api.subscr;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscrArticle implements Comparable<SubscrArticle> {
	@NotEmpty
	@JsonProperty
	private long id;

	@JsonProperty
	private String name;

	@JsonProperty
	private String namePattern;

	@JsonProperty
	private int productId;

	@JsonProperty
	private int count;

	@JsonProperty
	private String isbn;
	@JsonProperty
	private long brutto;
	@JsonProperty
	private long brutto_half;
	@JsonProperty
	private long brutto_full;
	@JsonProperty
	private double halfPercentage; // prozentuale Anteil am Gesamtpreis , halber Steuersatz, 0 < x < 1
	
	@JsonProperty
	private DateTime timest;

	@JsonIgnore
	public SubscrArticle clone() {
		SubscrArticle na = new SubscrArticle();
		na.setNamePattern(namePattern);
		na.setHalfPercentage(halfPercentage);
		na.setProductId(productId);
		return na;
	}
	
	@JsonIgnore
	public void initializeName() {
		name = namePattern.replace("{number}", String.valueOf(count));
		String dp = "\\{date:(.+)\\}";
		Pattern p = Pattern.compile(dp);
		Matcher m = p.matcher(name);
		if (m.find() && timest != null) {
			String datePattern = m.group(1);
			SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
			String dateString = sdf.format(timest.toDate());
			name = name.replaceFirst(dp, dateString);
		}
	}
	
	@JsonIgnore
	public void initializeBrutto() {
		brutto_half = (long) (brutto * halfPercentage);
		brutto_full = brutto - brutto_half;
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

	public String getNamePattern() {
		return namePattern;
	}

	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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


	public DateTime getTimest() {
		return timest;
	}

	public void setTimest(DateTime timest) {
		this.timest = timest;
	}

	public double getHalfPercentage() {
		return halfPercentage;
	}

	public void setHalfPercentage(double halfPercentage) {
		this.halfPercentage = halfPercentage;
	}

	@Override
	public int compareTo(SubscrArticle o) {
		return getTimest().compareTo(o.getTimest());
	}

	
}
