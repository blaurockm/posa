package net.buchlese.bofc.api.shift;

import java.util.List;

public class Store {
	private long id;
	private String abbrev;
	private String name;
	private List<ShopHours> shopHours;
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
	public List<ShopHours> getShopHours() {
		return shopHours;
	}
	public void setShopHours(List<ShopHours> shopHours) {
		this.shopHours = shopHours;
	}
	
}
