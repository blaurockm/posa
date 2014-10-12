package net.buchlese.posa.core;

import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.buchlese.posa.api.bofc.ArticleGroup;
import net.buchlese.posa.api.pos.KassenVorgang;

public class ArticleGroupMapping {

	
	
	public static ArticleGroup mappingFrom(KassenVorgang vorg) {
		for (Map.Entry<String, ArticleGroup> mapping : ArticleGroup.getArticleGroups().entrySet()) {
			Predicate<String> p = Pattern.compile(mapping.getValue().getMatch()).asPredicate();
			if (vorg.getArtikelNummer() != null && p.test(vorg.getArtikelNummer())) {
				return mapping.getValue();
			}
			if (vorg.getIsbn() != null && p.test(vorg.getIsbn())) {
				return mapping.getValue();
			}
			if (vorg.getMatchCode() != null && p.test(vorg.getMatchCode())) {
				return mapping.getValue();
			}
			if (mapping.getValue().getMatch().startsWith("mwst=")) {
				Character mwst = mapping.getValue().getMatch().charAt(5);
				if (mwst.equals(vorg.getMWSt())) {
					return mapping.getValue();
				}
			}
		}
		return ArticleGroup.NONE;
	}

}
