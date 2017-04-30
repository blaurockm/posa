package net.buchlese.posa.api.bofc;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PosArticle implements SendableObject {
		@JsonProperty
		private long id;

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
		private DateTime lastPurchaseDate;

		@JsonProperty
		private DateTime lastSellingDate;
		
		@JsonProperty
		private Tax tax;

		@JsonProperty
		private Integer availableStock;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

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

		public DateTime getLastPurchaseDate() {
			return lastPurchaseDate;
		}

		public void setLastPurchaseDate(DateTime lastPurchaseDate) {
			this.lastPurchaseDate = lastPurchaseDate;
		}

		public DateTime getLastSellingDate() {
			return lastSellingDate;
		}

		public void setLastSellingDate(DateTime lastSellingDate) {
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

		@Override
		public String toString() {
			return "PosArticle [artikelIdent=" + artikelIdent + ", matchcode="
					+ matchcode + "]";
		}

		
}
