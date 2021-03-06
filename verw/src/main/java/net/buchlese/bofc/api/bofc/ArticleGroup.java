package net.buchlese.bofc.api.bofc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class ArticleGroup {

	public static Character UNDEF_WARGRINDEX = '-';
	
	@JsonIgnore
//	public static ArticleGroup NONE = new ArticleGroup("n.m.", "unbekannt",UNDEF_WARGRINDEX);
	
//	private static Map<String, ArticleGroup> articleGroupMappings;
	
//	public static void injectMappings(Map<String, ArticleGroup> m) {
//		articleGroupMappings = m;
//		articleGroupMappings.put(NONE.key, NONE);
//	}
//	@JsonIgnore
//	public static  Map<String, ArticleGroup> getArticleGroups() {
//		return articleGroupMappings;
//	}
	
//	public ArticleGroup() {
//	}
//
//	public ArticleGroup(String key, String name, Character wargridx) {
//		this.key = key;
//		this.text = name;
//		this.wargrindex = String.valueOf(wargridx);
//	}

	@JsonProperty
	private String wargrindex;
	@JsonProperty
	@Column(name="match_")
	private String match;
	@JsonProperty
	private String text;
	@JsonProperty
	@Id
	@Column(name="key_")
	private String key;
	@JsonProperty
	@Column(name="type_")
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
	public String getWargrindex() {
		return wargrindex;
	}
	public void setWargrindex(String wargrindex) {
		this.wargrindex = wargrindex;
	}

}
