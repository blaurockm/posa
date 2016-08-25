package net.buchlese.bofc.api.subscr;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table( name = "subscrinterval" )
public class SubscrInterval implements Comparable<SubscrInterval> {
	@JsonProperty
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@JsonProperty
	private String name;

	@JsonProperty
	@Transient
	private long productId;

	@JsonProperty
	private long brutto;
	@JsonProperty
	private long brutto_half;
	@JsonProperty
	private long brutto_full;
	@JsonProperty
	private double halfPercentage =1d; // prozentuale Anteil am Gesamtpreis , halber Steuersatz, 0 < x < 1
	
	@JsonProperty
	private LocalDate startDate;
	@JsonProperty
	private LocalDate endDate;

	@JsonProperty
	@Column(name =" interval_")
	private Period interval;

	@JsonProperty
	@Enumerated(EnumType.STRING)
	private PayIntervalType intervalType;

	@JsonIgnore
	@ManyToOne
	private SubscrProduct product;

	@JsonIgnore
	@OneToMany(mappedBy="interval")
	private Set<SubscrIntervalDelivery> deliveries;

	@JsonIgnore
	public String initializeName(String namePattern) {
		String beginStr = startDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));
		String endStr = endDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));
		String intervalStr = beginStr + "-" + endStr;
		String name = namePattern.replace("#", intervalStr);
		name = name.replace("{start}", beginStr);
		name = name.replace("{end}", endStr);
		name = name.replace("{interval}", intervalStr);
		switch (intervalType) {
		case YEARLY : name = name.replace("{type}", "Jahr"); break;
		case HALFYEARLY : name = name.replace("{type}", "Halbjahr"); break;
		case MONTHLY : name = name.replace("{type}", "Monat"); break;
		default: name = name.replace("{type}", ""); break;
		}
		name = name.replace("{type}", intervalStr);
		String dp = "\\{date:(.+)\\}";
		Pattern p = Pattern.compile(dp);
		Matcher m = p.matcher(name);
		if (m.find()) {
			String datePattern = m.group(1);
			String dateString = startDate.format(DateTimeFormatter.ofPattern(datePattern));
			name = name.replaceFirst(dp, dateString);
			dateString = endDate.format(DateTimeFormatter.ofPattern(datePattern));
			name = name.replaceFirst(dp, dateString);
		}
		return name;
	}
	
	@JsonIgnore
	public void updateEndDate() {
		switch (getIntervalType()) {
		case YEARLY: setEndDate(getStartDate().plusYears(1).minusDays(1)); break;
		case HALFYEARLY: setEndDate(getStartDate().plusMonths(6).minusDays(1)); break;
		case MONTHLY: setEndDate(getStartDate().plusMonths(1).minusDays(1)); break;
		default: setEndDate(getStartDate().plusDays(1));
		}
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
	public int compareTo(SubscrInterval o) {
		return getStartDate().compareTo(o.getStartDate());
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}


	public Period getInterval() {
		return interval;
	}

	public void setInterval(Period interval) {
		this.interval = interval;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public PayIntervalType getIntervalType() {
		return intervalType;
	}

	public void setIntervalType(PayIntervalType intervalType) {
		this.intervalType = intervalType;
	}

	public SubscrProduct getProduct() {
		return product;
	}

	public void setProduct(SubscrProduct product) {
		this.product = product;
	}

	public Set<SubscrIntervalDelivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(Set<SubscrIntervalDelivery> deliveries) {
		this.deliveries = deliveries;
	}

	
}
