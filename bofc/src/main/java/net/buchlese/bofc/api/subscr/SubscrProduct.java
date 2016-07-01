package net.buchlese.bofc.api.subscr;


import io.dropwizard.jackson.Jackson;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	private long publisherId;
	@JsonProperty
	private Period period;
	@JsonProperty
	private LocalDate lastDelivery;
	@JsonProperty
	private LocalDate nextDelivery;
	@JsonProperty
	private LocalDate startDate;
	@JsonProperty
	private LocalDate endDate;
	@JsonProperty
	private int quantity;
	@JsonProperty
	private String namePattern;
	@JsonProperty
	private double halfPercentage =1d; // prozentuale Anteil am Gesamtpreis , halber Steuersatz, 0 < x < 1
	@JsonProperty
	private long baseBrutto;

	@JsonProperty
	private int count;

	@JsonProperty
	private boolean payPerDelivery; // vorausbezahlt: bei Abos, Fortsetzungen bezahlt pro Lieferung

	@JsonProperty
	private LocalDate lastInterval;

	@JsonProperty
	private PayIntervalType intervalType;

	
	@JsonIgnore
	public SubscrArticle createNextArticle(LocalDate erschTag) {
		SubscrArticle na = new SubscrArticle();
		na.setProductId(getId());
		if (halfPercentage <= 0.0001d) {
			na.setHalfPercentage(1d);
		} else {
			na.setHalfPercentage(halfPercentage);
		}
		na.updateBrutto(baseBrutto);
		na.setErschTag(erschTag);
		na.setIssueNo(++count);
		na.setName(na.initializeName(namePattern));
		return na;
	}

	@JsonIgnore
	public SubscrInterval createNextInterval(LocalDate erschTag) {
		SubscrInterval na = new SubscrInterval();
		na.setProductId(getId());
		if (getIntervalType() == null) {
			na.setIntervalType(PayIntervalType.YEARLY);
		} else {
			na.setIntervalType(getIntervalType());
		}
		if (halfPercentage <= 0.0001d) {
			na.setHalfPercentage(1d);
		} else {
			na.setHalfPercentage(halfPercentage);
		}
		na.updateBrutto(baseBrutto);
		if (getLastInterval() != null) {
			na.setStartDate(getLastInterval().plusDays(1));
		} else {
			na.setStartDate(erschTag);
		}
		switch (na.getIntervalType()) {
		case YEARLY: na.setEndDate(na.getStartDate().plusYears(1).minusDays(1)); break;
		case HALFYEARLY: na.setEndDate(na.getStartDate().plusMonths(6).minusDays(1)); break;
		case MONTHLY: na.setEndDate(na.getStartDate().plusMonths(1).minusDays(1)); break;
		default: na.setEndDate(na.getStartDate().plus(getPeriod()));
		}
		setLastInterval(na.getEndDate());
		na.setName(na.initializeName(namePattern));
		return na;
	}
	
	// sich selber als json-object ausgeben
	@JsonIgnore
	public String getComplJson() throws JsonProcessingException {
		ObjectMapper om = Jackson.newObjectMapper();
		return om.writeValueAsString(this);
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

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isPayPerDelivery() {
		return payPerDelivery;
	}

	public void setPayPerDelivery(boolean payPerDelivery) {
		this.payPerDelivery = payPerDelivery;
	}

	public long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}

	public LocalDate getLastInterval() {
		return lastInterval;
	}

	public void setLastInterval(LocalDate lastInterval) {
		this.lastInterval = lastInterval;
	}

	public PayIntervalType getIntervalType() {
		return intervalType;
	}

	public void setIntervalType(PayIntervalType intervalType) {
		this.intervalType = intervalType;
	}


}
