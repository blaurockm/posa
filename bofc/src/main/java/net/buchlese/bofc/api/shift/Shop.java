package net.buchlese.bofc.api.shift;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Shop {
	private static Map<Long, Shop> shopMapping;

	@JsonIgnore
	public static  Map<Long, Shop> getShops() {
		return shopMapping;
	}

	@JsonIgnore
	public static  Shop getShop(int num) {
		return shopMapping.get(num);
	}

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

	public static void inject(List<Shop> shops) {
		shopMapping = new HashMap<Long, Shop>();
		shops.stream().forEach(x -> shopMapping.put(x.getId(), x));
		
	}
	
}
