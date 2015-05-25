package net.buchlese.inve;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Stock {
	private Map<Article, BigDecimal> stock;
	public Stock() {
		stock = new HashMap<Article, BigDecimal>();
	}
	public void add(Article a, int quant) {
		BigDecimal q = new BigDecimal(quant);
		if (stock.containsKey(a)) {
			q = q.add(stock.get(a));
		}
		stock.put(a, q);
	}
	public BigDecimal printVK() {
		BigDecimal vk = new BigDecimal(0);
		for (Map.Entry<Article, BigDecimal> e : stock.entrySet()) {
			vk = vk.add(e.getKey().getVk().multiply(e.getValue()));
		}
		return vk;
	}
}
