package net.buchlese.posa;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import net.buchlese.posa.api.bofc.ArticleGroup;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PosAdapterConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory posDb;

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory bofcDb;
    
    @JsonProperty
    private Map<String, ArticleGroup> articleGroupMappings = new HashMap<>();
   
    @JsonProperty
    private String name;
    
    public PosAdapterConfiguration() {
    	this.posDb = new DataSourceFactory();
    	this.posDb.setDriverClass("net.sourceforge.jtds.jdbc.Driver");
    	
    	this.bofcDb = new DataSourceFactory();
    	this.bofcDb.setDriverClass("org.h2.Driver");
    	this.bofcDb.setUser("sa");
    	this.bofcDb.setPassword("");
    	this.bofcDb.setUrl("jdbc:h2:~/backoffice");
    }
    
    
    
    public DataSourceFactory getPointOfSaleDB() {
        return posDb;
    }
    
    public DataSourceFactory getBackOfficeDB() {
        return bofcDb;
    }

    public Map<String, ArticleGroup> getArticleGroupMappings() {
    	return articleGroupMappings;
    }
    
    public String getName() {
    	return name;
    }
}
