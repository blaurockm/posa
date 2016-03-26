package net.buchlese.bofc;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.server.SimpleServerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import net.buchlese.bofc.api.bofc.ArticleGroup;

import org.apache.fop.apps.FopFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BackOfcConfiguration extends Configuration {
	
	@JsonIgnore
	private static FopFactory fopFactory;
	public static FopFactory getFopFactory() {
		if (fopFactory == null) {
			// Step 1: Construct a FopFactory by specifying a reference to the configuration file
			// (reuse if you plan to render multiple documents!)
			fopFactory = FopFactory.newInstance();
		}
		return fopFactory;
	}

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory bofcDb;
    
    @JsonProperty
    private Map<String, ArticleGroup> articleGroupMappings = new HashMap<>();

    public BackOfcConfiguration() {
    	SimpleServerFactory server = new SimpleServerFactory();
    	server.setApplicationContextPath("/");
		setServerFactory(server);
    	
    	this.bofcDb = new DataSourceFactory();
    	this.bofcDb.setDriverClass("org.h2.Driver");
    	this.bofcDb.setUser("sa");
    	this.bofcDb.setPassword("");
    	this.bofcDb.setUrl("jdbc:h2:~/backoffice");
    }
    
    public DataSourceFactory getBackOfficeDB() {
        return bofcDb;
    }

    public Map<String, ArticleGroup> getArticleGroupMappings() {
    	return articleGroupMappings;
    }
    
}
