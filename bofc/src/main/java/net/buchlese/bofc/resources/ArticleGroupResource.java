package net.buchlese.bofc.resources;

import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.bofc.api.bofc.ArticleGroup;

import com.google.common.base.Optional;

@Path("/articlegroup")
public class ArticleGroupResource {


	public ArticleGroupResource() {
		super();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<ArticleGroup> fetchAll(@QueryParam("key") Optional<String> key)  {
		if (key.isPresent()) {
			return Arrays.asList(ArticleGroup.getArticleGroups().get(key));
		}
		return ArticleGroup.getArticleGroups().values();
	}



}
