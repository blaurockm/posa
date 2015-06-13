package net.buchlese.posa.core;

import net.buchlese.posa.api.bofc.ArticleGroup;
import net.buchlese.posa.api.pos.KassenVorgang;

public class ArticleGroupMapping {

	public static ArticleGroup mappingFrom(KassenVorgang vorg) {
		for (ArticleGroup grp : ArticleGroup.getArticleGroups().values()) {
			if (grp.getWargrindex().contains(vorg.getWarGrIndex().toString())) {
				return grp;
			}
		}
		return ArticleGroup.NONE;
	}

}
