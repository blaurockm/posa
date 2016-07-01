package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateResult {
	@JsonProperty
	public boolean success;
	@JsonProperty
	public String msg;
	@JsonProperty
	public long brutto;
	@JsonProperty
	public long bruttoFull;
	@JsonProperty
	public long bruttoHalf;
	@JsonProperty
	public double halfPercentage;
	@JsonProperty
	public String name;
	@JsonProperty
	public String oldValue;
	@JsonProperty
	public String newValue;

	@JsonIgnore
	public void initializeMoneyFieldsFromArticle(SubscrArticle art) {
		brutto = art.getBrutto();
		bruttoHalf = art.getBrutto_half();
		bruttoFull = art.getBrutto_full();
		halfPercentage = art.getHalfPercentage();
	}
	@JsonIgnore
	public void initializeMoneyFieldsFromArticle(SubscrDelivery art) {
		brutto = art.getTotal();
		bruttoHalf = art.getTotalHalf();
		bruttoFull = art.getTotalFull();
	}
	@JsonIgnore
	public void initializeMoneyFieldsFromArticle(SubscrInterval art) {
		brutto = art.getBrutto();
		bruttoHalf = art.getBrutto_half();
		bruttoFull = art.getBrutto_full();
		halfPercentage = art.getHalfPercentage();
	}
	@JsonIgnore
	public void initializeMoneyFieldsFromArticle(SubscrIntervalDelivery art) {
		brutto = art.getTotal();
		bruttoHalf = art.getTotalHalf();
		bruttoFull = art.getTotalFull();
	}
}