package net.buchlese.posa.jdbi.bofc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.buchlese.posa.api.bofc.PosArticle;

public class PosArticleDAO {

	public static Map<Long, PosArticle> store = new HashMap<>();
	
	public Integer getLastErfasst() {
		return null;
	}

	public void insertAll(Iterator<PosArticle> iterator) {
		while (iterator.hasNext()) {
			PosArticle x = iterator.next();
			store.put(x.getArtikelIdent(), x);
		}
	}

	public List<PosArticle> fetchArticle(int artikelident) {
		return Arrays.asList(store.get(artikelident));
	}

	public void updateArticle(PosArticle x) {
		store.put(x.getArtikelIdent(), x);		
	}

	public List<PosArticle> fetchArticles() {
		return new ArrayList<PosArticle>(store.values());
	}


}
