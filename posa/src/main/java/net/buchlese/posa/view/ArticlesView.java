package net.buchlese.posa.view;

import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import io.dropwizard.views.View;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosArticle;
import net.buchlese.posa.jdbi.bofc.PosArticleDAO;

public class ArticlesView extends View {

	private final PosAdapterConfiguration cfg;
	private final PosArticleDAO dao;

	public ArticlesView(PosAdapterConfiguration config, PosArticleDAO dao) {
		super("articles.ftl");
		this.cfg = config;
		this.dao = dao;
}

	public String getPosName() {
		return cfg.getName();
	}

	public String getPointId() {
		return String.valueOf(cfg.getPointOfSaleId());
	}

	public List<PosArticle> getArticles() {
		List<PosArticle> res = dao.fetchArticles();
		Collections.reverse(res);
		return res;
	}

	
	
	public TemplateMethodModelEx getMoney() {
    	return new TemplateMethodModelEx() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object exec(List args) throws TemplateModelException {
				if (args.size() != 1) {
					throw new TemplateModelException("nur ein int als Argument");
				}
				if (args.get(0) == null) {
					return "0,00 EUR";
				}
				if (args.get(0) instanceof TemplateNumberModel) {
					return String.format("%,.2f EUR", ((TemplateNumberModel)args.get(0)).getAsNumber().intValue() / 100.0d);
				}
				return "";
			}
		};
    }
	
    public TemplateMethodModelEx getLocalDate() {
    	return new TemplateMethodModelEx() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object exec(List args) throws TemplateModelException {
				if (args.size() != 1) {
					throw new TemplateModelException("nur ein DateTime als Argument");
				}
				return ((DateTime)((BeanModel)args.get(0)).getWrappedObject()).toLocalDate();
			}
		};
    }

}
