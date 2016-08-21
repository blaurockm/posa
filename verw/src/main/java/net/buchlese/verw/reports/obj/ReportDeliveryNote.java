package net.buchlese.verw.reports.obj;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;

import net.buchlese.bofc.api.subscr.Address;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "deliveryNote")
public class ReportDeliveryNote {

	@JsonProperty
	public Address deliveryAddress;
	
	@JsonProperty
	public LocalDate deliveryDate;
	
	@JsonProperty
	public long delivNum;
	
	@JsonProperty
	public long customerId;

	@JsonProperty
	public List<ReportDeliveryNoteDetail> details;

	@JsonProperty
	public LocalDate creationTime;

	@JsonProperty
	public int pointId;
	
	public static class ReportDeliveryNoteDetail {
		@JsonProperty
		public boolean textonly;

		@JsonProperty
		public String text;
		
		@JsonProperty
		public int quantity;
		
		@JsonProperty
		public double weight;
	}
	
}
