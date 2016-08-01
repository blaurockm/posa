package net.buchlese.bofc.api.bofc;

import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.buchlese.bofc.api.subscr.Address;

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
