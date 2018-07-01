package net.buchlese.bofc.api.bofc;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "posarticle" )
@JsonIgnoreProperties(ignoreUnknown = true)
public class PosArticle {
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@JsonProperty
		private Long id;

		@JsonProperty
		private int pointid;

		@JsonProperty
		private Long artikelIdent;
		
		@JsonProperty
		private String artikelnummer;

		@JsonProperty
		private String isbn;

		@JsonProperty
		private String ean;
		
		@JsonProperty
		private String wargrindex;

		@JsonProperty
		private String warengruppe;
		
		@JsonProperty
		private String matchcode;

		@JsonProperty
		private String bezeichnung;

		@JsonProperty
		private String author;

		@JsonProperty
		private String publisher;

		@JsonProperty
		private String auflage;

		@JsonProperty
		private Long weight;

		@JsonProperty
		private Long purchasePrice;
		
		@JsonProperty
		private Long sellingPrice;
		
		@JsonProperty
		private boolean bundle;
		
		@JsonProperty
		private Long sellingPrice_full;

		@JsonProperty
		private Long sellingPrice_half;
		
		@JsonProperty
		private java.sql.Date lastPurchaseDate;

		@JsonProperty
		private java.sql.Date lastSellingDate;
		
		@JsonProperty
		@Enumerated(EnumType.STRING)
		private Tax tax;

		@JsonProperty
		private Integer availableStock;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public int getPointid() {
			return pointid;
		}

		public void setPointid(int pointid) {
			this.pointid = pointid;
		}

		public Long getArtikelIdent() {
			return artikelIdent;
		}

		public void setArtikelIdent(Long artikelIdent) {
			this.artikelIdent = artikelIdent;
		}

		public String getArtikelnummer() {
			return artikelnummer;
		}

		public void setArtikelnummer(String artikelnummer) {
			this.artikelnummer = artikelnummer;
		}

		public String getIsbn() {
			return isbn;
		}

		public void setIsbn(String isbn) {
			this.isbn = isbn;
		}

		public String getEan() {
			return ean;
		}

		public void setEan(String ean) {
			this.ean = ean;
		}

		public String getWargrindex() {
			return wargrindex;
		}

		public void setWargrindex(String wargrindex) {
			this.wargrindex = wargrindex;
		}

		public String getWarengruppe() {
			return warengruppe;
		}

		public void setWarengruppe(String warengruppe) {
			this.warengruppe = warengruppe;
		}

		public String getMatchcode() {
			return matchcode;
		}

		public void setMatchcode(String matchcode) {
			this.matchcode = matchcode;
		}

		public String getBezeichnung() {
			return bezeichnung;
		}

		public void setBezeichnung(String bezeichnung) {
			this.bezeichnung = bezeichnung;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getPublisher() {
			return publisher;
		}

		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}

		public String getAuflage() {
			return auflage;
		}

		public void setAuflage(String auflage) {
			this.auflage = auflage;
		}

		public Long getWeight() {
			return weight;
		}

		public void setWeight(Long weight) {
			this.weight = weight;
		}

		public Long getPurchasePrice() {
			return purchasePrice;
		}

		public void setPurchasePrice(Long purchasePrice) {
			this.purchasePrice = purchasePrice;
		}

		public Long getSellingPrice() {
			return sellingPrice;
		}

		public void setSellingPrice(Long sellingPrice) {
			this.sellingPrice = sellingPrice;
		}

		public boolean isBundle() {
			return bundle;
		}

		public void setBundle(boolean bundle) {
			this.bundle = bundle;
		}

		public Long getSellingPrice_full() {
			return sellingPrice_full;
		}

		public void setSellingPrice_full(Long sellingPrice_full) {
			this.sellingPrice_full = sellingPrice_full;
		}

		public Long getSellingPrice_half() {
			return sellingPrice_half;
		}

		public void setSellingPrice_half(Long sellingPrice_half) {
			this.sellingPrice_half = sellingPrice_half;
		}

		public java.sql.Date getLastPurchaseDate() {
			return lastPurchaseDate;
		}

		public void setLastPurchaseDate(java.sql.Date lastPurchaseDate) {
			this.lastPurchaseDate = lastPurchaseDate;
		}

		public java.sql.Date getLastSellingDate() {
			return lastSellingDate;
		}

		public void setLastSellingDate(java.sql.Date lastSellingDate) {
			this.lastSellingDate = lastSellingDate;
		}

		public Tax getTax() {
			return tax;
		}

		public void setTax(Tax tax) {
			this.tax = tax;
		}

		public Integer getAvailableStock() {
			return availableStock;
		}

		public void setAvailableStock(Integer availableStock) {
			this.availableStock = availableStock;
		}

		
}
