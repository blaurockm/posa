package net.buchlese.bofc.api.subscr;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscrArticle {
	@NotEmpty
	@JsonProperty
	private long id;

	@JsonProperty
	private String abbrev;
	@JsonProperty
	private String name;

	@JsonProperty
	private String productAbbrev;

	@JsonProperty
	private String isbn;
	@JsonProperty
	private long brutto;
	@JsonProperty
	private long brutto_half;
	@JsonProperty
	private long brutto_full;
	@JsonProperty
	private long tax_half;
	@JsonProperty
	private long tax_full;
}
