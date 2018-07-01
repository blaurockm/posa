package net.buchlese.bofc.api.bofc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "posstockchange" )
@JsonIgnoreProperties(ignoreUnknown = true)
public class PosArticleStockChange {
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@JsonProperty
		private Long id;

		@JsonProperty
		private int pointid;

		@JsonProperty
		private long artikelIdent;
		
		@JsonProperty
		private java.sql.Timestamp changeDate;

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

		public java.sql.Timestamp getChangeDate() {
			return changeDate;
		}

		public void setChangeDate(java.sql.Timestamp changeDate) {
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
