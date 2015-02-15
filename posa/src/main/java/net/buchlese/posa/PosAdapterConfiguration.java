package net.buchlese.posa;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.server.SimpleServerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import net.buchlese.posa.api.bofc.ArticleGroup;

import org.joda.time.LocalDate;

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

    @JsonProperty
    private LocalDate syncStartDate;

    @JsonProperty
    private long idNumRangeStart;

    @JsonProperty
    private String homeUrl;

    @JsonProperty
    private int pointOfSaleId;
    
    public PosAdapterConfiguration() {
    	SimpleServerFactory server = new SimpleServerFactory();
    	server.setApplicationContextPath("/");
		setServerFactory(server);
    	this.posDb = new DataSourceFactory();
    	this.posDb.setDriverClass("net.sourceforge.jtds.jdbc.Driver");
    	
    	this.bofcDb = new DataSourceFactory();
    	this.bofcDb.setDriverClass("org.h2.Driver");
    	this.bofcDb.setUser("sa");
    	this.bofcDb.setPassword("");
    	this.bofcDb.setUrl("jdbc:h2:~/backoffice");
    	
    	this.idNumRangeStart = 10000000;
    	this.syncStartDate = new LocalDate(2014,9,1);
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

	public LocalDate getSyncStartDate() {
		return syncStartDate;
	}

	public long getIdNumRangeStart() {
		return idNumRangeStart;
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public int getPointOfSaleId() {
		return pointOfSaleId;
	}
    
}
