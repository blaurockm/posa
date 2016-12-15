package net.buchlese.verw.ctrl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.core.DeliveryNoteCreator;
import net.buchlese.verw.core.SequenceGenerator;
import net.buchlese.verw.core.SettlementCreator;
import net.buchlese.verw.repos.CustomerRepository;
import net.buchlese.verw.repos.InvoiceRepository;
import net.buchlese.verw.repos.IssueSlipRepository;
import net.buchlese.verw.repos.SettlementRepository;
import net.buchlese.verw.repos.SubscrArticleRepository;
import net.buchlese.verw.repos.SubscrDeliveryRepository;
import net.buchlese.verw.repos.SubscrIntervalDeliveryRepository;
import net.buchlese.verw.repos.SubscrIntervalRepository;
import net.buchlese.verw.repos.SubscriptionRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(SubscriptionsController.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableSpringDataWebSupport
@ActiveProfiles("integrationtest")
public class SubscriptionControllerTest {

	@Autowired MockMvc mvc;

	@MockBean SubscrDeliveryRepository deliveryRepo;
	@MockBean SubscrIntervalDeliveryRepository intervalDeliveryRepo;
	@MockBean SettlementCreator settlementCreator;
	@MockBean DeliveryNoteCreator deliveryNoteCreator;
	@MockBean SubscriptionRepository subscriptionRepo;

	@MockBean SequenceGenerator numgen;
	@MockBean EntityManagerFactory emfact;
	@MockBean IssueSlipRepository slipRepo;
	@MockBean InvoiceRepository invoiceRepo;
	@MockBean CustomerRepository customerRepo;
	@MockBean SubscrArticleRepository articleRepo;
	@MockBean SubscrIntervalRepository intervalRepo;
	@MockBean SettlementRepository settlementRepo;

	// settleUnsettled
	@Test
	public void testSettleUnsettled() throws Exception {
		Subscriber sub1= new Subscriber();
		sub1.setId(22L);
		sub1.setCollectiveInvoice(false);
		
		Subscriber sub2coll = new Subscriber();
		sub2coll.setId(33L);
		sub2coll.setCollectiveInvoice(true);

		Subscriber sub3= new Subscriber();
		sub3.setId(44L);
		sub3.setCollectiveInvoice(false);

		Subscription s1 = new Subscription();
		s1.setId(1L);

		Subscription s2 = new Subscription();
		s1.setId(2L);

		Subscription s3 = new Subscription();
		s1.setId(3L);

		SubscrDelivery deliv01 = new SubscrDelivery();
		deliv01.setId(1L);
		deliv01.setSubscriber(sub1);
		deliv01.setSubscription(s1);
		deliv01.setPayed(false);

		SubscrDelivery deliv02 = new SubscrDelivery();
		deliv02.setId(2L);
		deliv02.setSubscriber(sub3);
		deliv02.setSubscription(s2);
		deliv02.setPayed(false);

		SubscrDelivery deliv03 = new SubscrDelivery();
		deliv03.setId(3L);
		deliv03.setSubscriber(sub2coll);
		deliv03.setPayed(false);

		SubscrDelivery deliv05 = new SubscrDelivery();
		deliv05.setId(5L);
		deliv05.setSubscriber(sub3);
		deliv05.setSubscription(s3);
		deliv05.setPayed(false);

		when(deliveryRepo.findByPayed(org.mockito.Matchers.eq(false))).thenReturn(Arrays.asList(deliv01, deliv02, deliv03, deliv05));
		
		this.mvc.perform(get("/subscriptions/settleunsettled")).andExpect(status().isOk());
		
		verify(settlementCreator, times(1)).createCollectiveSettlement(sub2coll);
		verify(settlementCreator, times(2)).createCollectiveSettlement(org.mockito.Matchers.eq(sub3)); // weil es 2 gibt und das zweite nicht als bezahlt markiert wurde
		verify(settlementCreator, times(1)).createSettlement(s1);
		
	}

	// noteUnnoted
	@Test
	public void testNoteUnnoted() throws Exception {
		Subscriber sub1= new Subscriber();
		sub1.setId(22L);
		sub1.setNeedsDeliveryNote(true);
		

		Subscriber sub3= new Subscriber();
		sub3.setId(44L);
		sub1.setNeedsDeliveryNote(false);

		SubscrDelivery deliv01 = new SubscrDelivery();
		deliv01.setId(1L);
		deliv01.setSubscriber(sub1);
		deliv01.setSlipped(false);

		SubscrDelivery deliv02 = new SubscrDelivery();
		deliv02.setId(2L);
		deliv02.setSubscriber(sub1);
		deliv02.setSlipped(false);

		SubscrDelivery deliv03 = new SubscrDelivery();
		deliv03.setId(3L);
		deliv03.setSubscriber(sub3);
		deliv03.setSlipped(false);

		SubscrDelivery deliv05 = new SubscrDelivery();
		deliv05.setId(4L);
		deliv05.setSubscriber(sub3);
		deliv05.setSlipped(false);

		when(deliveryRepo.findBySlipped(org.mockito.Matchers.eq(false))).thenReturn(Arrays.asList(deliv01, deliv02, deliv03, deliv05));
		
		this.mvc.perform(get("/subscriptions/noteunnoted")).andExpect(status().isOk());
		
		verify(deliveryNoteCreator, times(1)).createDeliveryNote(deliv01);
		verify(deliveryNoteCreator, times(1)).createDeliveryNote(deliv02);
		verify(deliveryNoteCreator, times(1)).createDeliveryNote(deliv03);
		verify(deliveryNoteCreator, times(1)).createDeliveryNote(deliv05);
		
	}


}
