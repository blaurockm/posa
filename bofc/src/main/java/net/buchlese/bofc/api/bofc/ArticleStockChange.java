package net.buchlese.bofc.api.bofc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleStockChange {
		@JsonProperty
		private long id;

		@JsonProperty
		private int pointid;

		@JsonProperty
		private long artikelIdent;
		
		@JsonProperty
		private java.sql.Timestamp changeDate;

		@JsonProperty
		private int stockChange;
		
		@JsonProperty
		private int stockBefore;

		@JsonProperty
		private int stockAfter;

		@JsonProperty
		private String comment;

		public int getPointid() {
			return pointid;
		}

		public void setPointid(int pointid) {
			this.pointid = pointid;
		}

		public long getArtikelIdent() {
			return artikelIdent;
		}

		public void setArtikelIdent(long artikelIdent) {
			this.artikelIdent = artikelIdent;
		}

		public java.sql.Timestamp getChangeDate() {
			return changeDate;
		}

		public void setChangeDate(java.sql.Timestamp changeDate) {
			this.changeDate = changeDate;
		}

		public int getStockChange() {
			return stockChange;
		}

		public void setStockChange(int stockChange) {
			this.stockChange = stockChange;
		}

		public int getStockBefore() {
			return stockBefore;
		}

		public void setStockBefore(int stockBefore) {
			this.stockBefore = stockBefore;
		}

		public int getStockAfter() {
			return stockAfter;
		}

		public void setStockAfter(int stockAfter) {
			this.stockAfter = stockAfter;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

}
