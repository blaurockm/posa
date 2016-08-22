package net.buchlese.verw.reports.obj;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

import net.buchlese.bofc.api.bofc.PosInvoice;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "balanceExport")
public class ReportInvoiceExport {

	@JsonProperty
	private LocalDateTime execDate;
	@JsonProperty
	private String description;
	@JsonProperty
	private String posname;
	@JsonProperty
	private PosInvoice firstInvoice;
	@JsonProperty
	private PosInvoice lastInvoice;
	@JsonProperty
	private long invoicesSum;
	@JsonProperty
	private long taxFullSum;
	@JsonProperty
	private long taxHalfSum;
	@JsonProperty
	private long taxNoneSum;
	@JsonProperty
	private long exportId;
	@JsonProperty
	private PosInvoice[] invoices;
	
	public LocalDateTime getExecDate() {
		return execDate;
	}
	public void setExecDate(LocalDateTime execDate) {
		this.execDate = execDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPosname() {
		return posname;
	}
	public void setPosname(String posname) {
		this.posname = posname;
	}
	public PosInvoice getFirstInvoice() {
		return firstInvoice;
	}
	public void setFirstInvoice(PosInvoice firstInvoice) {
		this.firstInvoice = firstInvoice;
	}
	public PosInvoice getLastInvoice() {
		return lastInvoice;
	}
	public void setLastInvoice(PosInvoice lastInvoice) {
		this.lastInvoice = lastInvoice;
	}
	public long getInvoicesSum() {
		return invoicesSum;
	}
	public void setInvoicesSum(long invoicesSum) {
		this.invoicesSum = invoicesSum;
	}
	public long getTaxFullSum() {
		return taxFullSum;
	}
	public void setTaxFullSum(long taxFullSum) {
		this.taxFullSum = taxFullSum;
	}
	public long getTaxHalfSum() {
		return taxHalfSum;
	}
	public void setTaxHalfSum(long taxHalfSum) {
		this.taxHalfSum = taxHalfSum;
	}
	public long getTaxNoneSum() {
		return taxNoneSum;
	}
	public void setTaxNoneSum(long taxNoneSum) {
		this.taxNoneSum = taxNoneSum;
	}
	public PosInvoice[] getInvoices() {
		return invoices;
	}
	public void setInvoices(PosInvoice[] invoices) {
		this.invoices = invoices;
	}
	public long getExportId() {
		return exportId;
	}
	public void setExportId(long exportId) {
		this.exportId = exportId;
	}


}
