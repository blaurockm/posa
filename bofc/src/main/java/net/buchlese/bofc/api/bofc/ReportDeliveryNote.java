package net.buchlese.bofc.api.bofc;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.LocalDate;

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
	public List<ReportDeliveryNoteDetail> deliveryItems;
	
	
	public static class ReportDeliveryNoteDetail {
		@JsonProperty
		public String name;
		
		@JsonProperty
		public int quantity;
		
		@JsonProperty
		public double weight;
	}
	
}
