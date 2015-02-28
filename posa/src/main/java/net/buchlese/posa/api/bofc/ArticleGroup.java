package net.buchlese.posa.api.bofc;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleGroup {

	@JsonIgnore
	public static ArticleGroup NONE = new ArticleGroup("n.m.", "unbekannt");
	
	private static Map<String, ArticleGroup> articleGroupMappings;
	
	public static void injectMappings(Map<String, ArticleGroup> m) {
		articleGroupMappings = m;
		articleGroupMappings.put(NONE.key, NONE);
	}
	@JsonIgnore
	public static  Map<String, ArticleGroup> getArticleGroups() {
		return articleGroupMappings;
	}
	
	public ArticleGroup() {
	}

	public ArticleGroup(String key, String name) {
		this.key = key;
		this.text = name;
	}

	@JsonProperty
	private String match;
	@JsonProperty
	private String text;
	@JsonProperty
	private String key;
	@JsonProperty
	private String type;
	@JsonProperty
	private Double marge;
	@JsonProperty
	private Integer account;
	public String getMatch() {
		return match;
	}
	public void setMatch(String match) {
		this.match = match;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Double getMarge() {
		return marge;
	}
	public void setMarge(Double marge) {
		this.marge = marge;
	}
	public Integer getAccount() {
		return account;
	}
	public void setAccount(Integer account) {
		this.account = account;
	}
	
}
