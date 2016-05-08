package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.subscr.SubscrArticle;

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
	
	@JsonIgnore
	public void initializeMoneyFieldsFromArticle(SubscrArticle art) {
		brutto = art.getBrutto();
		bruttoHalf = art.getBrutto_half();
		bruttoFull = art.getBrutto_full();
		halfPercentage = art.getHalfPercentage();
	}
}