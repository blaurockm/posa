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
    private String emailUser;

    @JsonProperty
    private String emailPass;
    
    
    @JsonProperty
    private String homeUrl;

    @JsonProperty
    private String commandHomeUrl;
    @JsonProperty
    private String syncHomeUrl;
    @JsonProperty
    private String sendHomeUrl;

    @JsonProperty
    private String homeHost;

    @JsonProperty
    private int homePort = 8080;

    @JsonProperty
    private int pointOfSaleId;

    @JsonProperty
    private int daysBack = 365;

    @JsonProperty
    private int guiDaysBack = 14;

    @JsonProperty
    private int syncOffset = 5;

    @JsonProperty
    private int syncInterval = 15;
    
    @JsonProperty
    private int sendOffset = 1;

    @JsonProperty
    private int sendInterval = 2;

    @JsonProperty
    private int commandOffset = 30;

    @JsonProperty
    private int commandInterval = 10;

    @JsonProperty
    private String cashBalanceResource = "cashbalance/acceptBalance";
    
    @JsonProperty
    private String invoiceResource = "invoice/acceptInvoice";

    @JsonProperty
    private String articleResource = "article/acceptArticle";

    @JsonProperty
    private String stockChangeResource = "article/acceptStockChange";

    @JsonProperty
    private String issueSlipResource = "invoice/acceptIssueSlip";

    @JsonProperty
    private String posStateResource = "posState/acceptState";

    @JsonProperty
    private String serverStateResource = "serverState/acceptState";

    @JsonProperty
    private String articleGroupResource = "articleGroup";

    @JsonProperty
    private String commandGetResource = "getcmds";

    @JsonProperty
    private String commandSendResource = "answercmds";

    @JsonProperty
    private boolean sshEnabled;

    public boolean isSshEnabled() {
		return sshEnabled;
	}

	public void setSshEnabled(boolean sshEnabled) {
		this.sshEnabled = sshEnabled;
	}

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
    	this.sshEnabled = true;
    	this.homeUrl = "homeless";
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

	public String getHomeHost() {
		return homeHost;
	}

	public int getDaysBack() {
		return daysBack;
	}

	public int getSyncOffset() {
		return syncOffset;
	}

	public int getSyncInterval() {
		return syncInterval;
	}

	public int getSendOffset() {
		return sendOffset;
	}

	public int getSendInterval() {
		return sendInterval;
	}

	public int getCommandOffset() {
		return commandOffset;
	}

	public int getCommandInterval() {
		return commandInterval;
	}

	public String getCommandHomeUrl() {
		return commandHomeUrl != null ? commandHomeUrl : homeUrl;
	}

	public String getSyncHomeUrl() {
		return syncHomeUrl != null ? syncHomeUrl : homeUrl;
	}

	public String getSendHomeUrl() {
		return sendHomeUrl != null ? sendHomeUrl : homeUrl;
	}

	public String getCashBalanceResource() {
		return cashBalanceResource;
	}

	public String getInvoiceResource() {
		return invoiceResource;
	}

	public String getArticleResource() {
		return articleResource;
	}

	public String getStockChangeResource() {
		return stockChangeResource;
	}

	public String getIssueSlipResource() {
		return issueSlipResource;
	}

	public String getPosStateResource() {
		return posStateResource;
	}

	public String getServerStateResource() {
		return serverStateResource;
	}

	public String getArticleGroupResource() {
		return articleGroupResource;
	}

	public String getCommandGetResource() {
		return commandGetResource;
	}

	public String getCommandSendResource() {
		return commandSendResource;
	}

	public int getHomePort() {
		return homePort;
	}

	public int getGuiDaysBack() {
		return guiDaysBack;
	}

	public String getEmailUser() {
		return emailUser;
	}

	public String getEmailPass() {
		return emailPass;
	}
    
}
