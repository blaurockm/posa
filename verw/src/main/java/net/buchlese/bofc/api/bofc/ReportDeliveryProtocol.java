package net.buchlese.bofc.api.bofc;

import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.buchlese.bofc.api.subscr.Address;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;

@XmlRootElement(name = "deliveryProtocol")
public class ReportDeliveryProtocol {

	@JsonProperty
	public LocalDate protocolDate;
	
	
	@JsonProperty
	public List<ProtocolDetail> details;
	
	
	public static class ProtocolDetail {
		@JsonProperty
		public Address deliveryAddress;

		@JsonProperty
		public SubscrArticle article;

		@JsonProperty
		public Subscription subscription;

		@JsonProperty
		public Subscriber subscriber;

		@JsonProperty
		public int quantity;

		@JsonProperty
		public boolean needsInvoice;
		
		@JsonProperty
		public boolean needsDeliveryNote;
	}
}
