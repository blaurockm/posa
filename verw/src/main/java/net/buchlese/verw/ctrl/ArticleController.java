package net.buchlese.verw.ctrl;

import net.buchlese.bofc.api.bofc.PosArticle;
import net.buchlese.bofc.api.bofc.QPosArticle;
import net.buchlese.verw.repos.ArticleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="article")
public class ArticleController {

	@Autowired ArticleRepository articleRepository;

	@RequestMapping(path="acceptArticle", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> acceptBalance(@RequestBody PosArticle article)  {
		try {
			PosArticle old = articleRepository.findOne(QPosArticle.posArticle.artikelIdent.eq(article.getArtikelIdent()).and(QPosArticle.posArticle.pointid.eq(article.getPointid())));
			if (old == null) {
				articleRepository.saveAndFlush(article);
			} else {
				article.setId(old.getId()); // damit wird gezeigt, dass wir ein update sind
				articleRepository.saveAndFlush(article);
			}
			return ResponseEntity.ok().build();
		} catch (Throwable t) {
			return ResponseEntity.unprocessableEntity().body(t.getMessage());
		}
	}

}
