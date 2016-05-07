package net.buchlese.bofc.api.subscr;


import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscrProduct {
	@NotEmpty
	@JsonProperty
	private long id;

	@JsonProperty
	private String abbrev;
	@JsonProperty
	private String name;
	@JsonProperty
	private String publisher;
	@JsonProperty
	private Period period;
	@JsonProperty
	private LocalDate lastDelivery;
	@JsonProperty
	private LocalDate nextDelivery;
	@JsonProperty
	private LocalDate startDate;
	@JsonProperty
	private int quantity;
	@JsonProperty
	private String namePattern;
	@JsonProperty
	private double halfPercentage; // prozentuale Anteil am Gesamtpreis , halber Steuersatz, 0 < x < 1
	@JsonProperty
	private long baseBrutto;

	@JsonProperty
	private int count;

	@JsonProperty
	private boolean payedInAdvance; // vorausbezahlt: bei Abos, Fortsetzungen bei jeder jeder Lieferung

	@JsonProperty
	private int issuePayInterval = 1; // mit einer Zahlen werden x deliveries auf einmal bezahlt
	
	@JsonIgnore
	public SubscrArticle createNextArticle(LocalDate erschTag) {
		SubscrArticle na = new SubscrArticle();
		na.setProductId(getId());
		na.setHalfPercentage(halfPercentage);
		na.setBrutto(baseBrutto);
		na.initializeBrutto();
		na.setErschTag(erschTag);
		na.setIssueNo(count++);
		na.setName(na.initializeName(namePattern));
		return na;
	}
	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAbbrev() {
		return abbrev;
	}
	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public Period getPeriod() {
		return period;
	}
	public void setPeriod(Period period) {
		this.period = period;
	}
	public LocalDate getLastDelivery() {
		return lastDelivery;
	}
	public void setLastDelivery(LocalDate lastDelivery) {
		this.lastDelivery = lastDelivery;
	}
	public LocalDate getNextDelivery() {
		return nextDelivery;
	}
	public void setNextDelivery(LocalDate nextDelivery) {
		this.nextDelivery = nextDelivery;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getNamePattern() {
		return namePattern;
	}

	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
	}

	public double getHalfPercentage() {
		return halfPercentage;
	}

	public void setHalfPercentage(double halfPercentage) {
		this.halfPercentage = halfPercentage;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


	public long getBaseBrutto() {
		return baseBrutto;
	}


	public void setBaseBrutto(long baseBrutto) {
		this.baseBrutto = baseBrutto;
	}


	public boolean isPayedInAdvance() {
		return payedInAdvance;
	}


	public void setPayedInAdvance(boolean payedInAdvance) {
		this.payedInAdvance = payedInAdvance;
	}


	public int getIssuePayInterval() {
		return issuePayInterval;
	}


	public void setIssuePayInterval(int issuePayInterval) {
		this.issuePayInterval = issuePayInterval;
	}

}
