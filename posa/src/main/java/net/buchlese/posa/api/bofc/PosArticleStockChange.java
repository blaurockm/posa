package net.buchlese.posa.api.bofc;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PosArticleStockChange implements SendableObject {
		@JsonProperty
		private Long id;

		@JsonProperty
		private int pointid;

		@JsonProperty
		private long artikelIdent;
		
		@JsonProperty
		private DateTime changeDate;

		@JsonProperty
		private Integer stockChange;
		
		@JsonProperty
		private Integer stockBefore;

		@JsonProperty
		private Integer stockAfter;

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

		public DateTime getChangeDate() {
			return changeDate;
		}

		public void setChangeDate(DateTime changeDate) {
			this.changeDate = changeDate;
		}

		public Integer getStockChange() {
			return stockChange;
		}

		public void setStockChange(Integer stockChange) {
			this.stockChange = stockChange;
		}

		public Integer getStockBefore() {
			return stockBefore;
		}

		public void setStockBefore(Integer stockBefore) {
			this.stockBefore = stockBefore;
		}

		public Integer getStockAfter() {
			return stockAfter;
		}

		public void setStockAfter(Integer stockAfter) {
			this.stockAfter = stockAfter;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

}
