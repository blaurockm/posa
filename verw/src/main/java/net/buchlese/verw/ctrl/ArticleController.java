package net.buchlese.verw.ctrl;

import net.buchlese.bofc.api.bofc.PosArticle;
import net.buchlese.bofc.api.bofc.PosArticleStockChange;
import net.buchlese.bofc.api.bofc.QPosArticle;
import net.buchlese.bofc.api.bofc.QPosArticleStockChange;
import net.buchlese.verw.repos.ArticleRepository;
import net.buchlese.verw.repos.StockChangeRepository;

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
	@Autowired StockChangeRepository changeRepository;

	@RequestMapping(path="acceptArticle", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> acceptArticle(@RequestBody PosArticle article)  {
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

	@RequestMapping(path="acceptStockChange", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> acceptStockChange(@RequestBody PosArticleStockChange change)  {
		try {
			PosArticleStockChange old = changeRepository.findOne(QPosArticleStockChange.posArticleStockChange.artikelIdent.eq(change.getArtikelIdent())
					.and(QPosArticleStockChange.posArticleStockChange.pointid.eq(change.getPointid())
							.and(QPosArticleStockChange.posArticleStockChange.changeDate.eq(change.getChangeDate()))));
			if (old == null) {
				changeRepository.saveAndFlush(change);
			} else {
				change.setId(old.getId()); // damit wird gezeigt, dass wir ein update sind
				changeRepository.saveAndFlush(change);
			}
			return ResponseEntity.ok().build();
		} catch (Throwable t) {
			return ResponseEntity.unprocessableEntity().body(t.getMessage());
		}
	}

}
