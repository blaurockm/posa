package net.buchlese.posa.core;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.buchlese.posa.api.bofc.ArticleGroup;
import net.buchlese.posa.api.pos.KassenVorgang;

public class ArticleGroupMapping {

	public static ArticleGroup mappingFrom(KassenVorgang vorg) {
		for (ArticleGroup grp : ArticleGroup.getArticleGroups().values()) {
			if (grp.getWargrindex() != null && grp.getWargrindex().contains(vorg.getWarGrIndex().toString())) {
				return grp;
			}
		}
		for (ArticleGroup grp : ArticleGroup.getArticleGroups().values()) {
			if (grp.getKey().equals(ArticleGroup.NONE.getKey()) || grp.getMatch() == null) {
				// mit nix wollen wir nicht vergleichen
				continue;
			}
			Predicate<String> p = Pattern.compile(grp.getMatch()).asPredicate();
			if (vorg.getArtikelNummer() != null && p.test(vorg.getArtikelNummer())) {
				return grp;
			}
			if (vorg.getIsbn() != null && p.test(vorg.getIsbn())) {
				return grp;
			}
			if (vorg.getMatchCode() != null && p.test(vorg.getMatchCode())) {
				return grp;
			}
			if (grp.getMatch().startsWith("mwst=")) {
				Character mwst = grp.getMatch().charAt(5);
				if (mwst.equals(vorg.getMWSt())) {
					return grp;
				}
			}
			
		}
		return ArticleGroup.NONE;
	}

}
