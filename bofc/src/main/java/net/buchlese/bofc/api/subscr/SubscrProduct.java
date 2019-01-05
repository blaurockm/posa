package net.buchlese.bofc.api.subscr;


import java.sql.Date;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table( name = "subscrproduct" )
public class SubscrProduct {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private Long id;

	@JsonProperty
	private String abbrev;
	@JsonProperty
	private String name;
	@JsonProperty
	private String publisher;
	@JsonProperty
	private long publisherId;
	@JsonProperty
	private String issn;
	@JsonProperty
	private String memo;
	@JsonProperty
	private String url;
	@Transient
	@JsonIgnore
	private Period period;
	@JsonProperty
	private Date lastDelivery;
	@JsonProperty
	private Date nextDelivery;
	@JsonProperty
	private Date startDate;
	@JsonProperty
	private Date endDate;
	@JsonProperty
	private int quantity;
	@JsonProperty
	private String namePattern;
	@JsonProperty
	private String intervalPattern;
	@JsonProperty
	private double halfPercentage =1d; // prozentuale Anteil am Gesamtpreis , halber Steuersatz, 0 < x < 1
	@JsonProperty
	private long baseBrutto;

	@JsonProperty
	private int count;

	@JsonProperty
	private boolean payPerDelivery; // vorausbezahlt: bei Abos, Fortsetzungen bezahlt pro Lieferung

	@JsonProperty
	private Date lastInterval;

	@JsonProperty
	@Enumerated(EnumType.STRING)
	private PayIntervalType intervalType;

	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	@XmlTransient
	private Set<SubscrInterval> intervals;

	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	@XmlTransient
	private Set<SubscrArticle> articles;

	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	@XmlTransient
	private Set<Subscription> subscriptions;

	
	public long getSubscriptionCount() {
		if (getSubscriptions() != null) {
			return getSubscriptions().stream().filter(Subscription::isValid).count();
		}
		return 0;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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

	public PayIntervalType getIntervalType() {
		return intervalType;
	}

	public void setIntervalType(PayIntervalType intervalType) {
		this.intervalType = intervalType;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIntervalPattern() {
		return intervalPattern;
	}

	public void setIntervalPattern(String intervalPattern) {
		this.intervalPattern = intervalPattern;
	}

	@XmlTransient
	public Set<SubscrArticle> getArticles() {
		return articles;
	}

	public void setArticles(Set<SubscrArticle> articles) {
		this.articles = articles;
	}

	@XmlTransient
	public Set<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Set<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	@XmlTransient
	public Set<SubscrInterval> getIntervals() {
		return intervals;
	}

	public void setIntervals(Set<SubscrInterval> intervals) {
		this.intervals = intervals;
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
		SubscrProduct other = (SubscrProduct) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Date getLastDelivery() {
		return lastDelivery;
	}

	public void setLastDelivery(Date lastDelivery) {
		this.lastDelivery = lastDelivery;
	}

	public Date getNextDelivery() {
		return nextDelivery;
	}

	public void setNextDelivery(Date nextDelivery) {
		this.nextDelivery = nextDelivery;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getLastInterval() {
		return lastInterval;
	}

	public void setLastInterval(Date lastInterval) {
		this.lastInterval = lastInterval;
	}

	public void addSubscrArticle(SubscrArticle d) {
		if (articles == null) {
			articles = new HashSet<>();
		}
		articles.add(d);
	}


}
