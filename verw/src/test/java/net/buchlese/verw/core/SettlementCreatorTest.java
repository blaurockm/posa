package net.buchlese.verw.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import net.buchlese.bofc.api.bofc.InvoiceAgrDetail;
import net.buchlese.bofc.api.bofc.PosInvoiceDetail;
import net.buchlese.bofc.api.bofc.Settlement;
import net.buchlese.bofc.api.subscr.Address;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.repos.SettlementRepository;
import net.buchlese.verw.repos.SubscrDeliveryRepository;
import net.buchlese.verw.repos.SubscrIntervalDeliveryRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("integrationtest")
@ComponentScan(value="net.buchlese.verw.core")
public class SettlementCreatorTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired 
    private SettlementRepository settRepository;

    @Autowired 
    private SubscrDeliveryRepository delivRepository;

    @Autowired 
    private SubscrIntervalDeliveryRepository intdelivRepository;


    @Autowired 
    private SettlementCreator settCreator;
    
    @Test
    public void testCreateSettlementDeliv() throws Exception {
    	SubscrProduct product = new SubscrProduct();
    	product.setName("testproduct");

    	SubscrArticle article = new SubscrArticle();
    	article.setName("artikel 1");
    	article.setProduct(product);
    	
    	Subscriber subscri = new Subscriber();
    	subscri.setName("testkunde");
    	subscri.setInvoiceAddress(new Address().setName1("testkundeAdrLine1"));

    	Subscription sub = new Subscription();
    	sub.setSubscriber(subscri);
    	sub.setProduct(product);
    	
    	sub.setPaymentType(PayIntervalType.EACHDELIVERY);
    	
    	entityManager.persist(product);
    	entityManager.persist(article);
    	entityManager.persist(subscri);
    	sub = entityManager.persistFlushFind(sub);

    	SubscrDelivery deliv = new SubscrDelivery();
    	deliv.setArticle(article);
    	deliv.setTotal(12000);
    	deliv.setQuantity(3);
    	deliv.setDeliveryDate(LocalDate.now());
    	deliv.setSubscription(sub);

    	entityManager.persist(deliv);
    	
    	entityManager.merge(sub);
    	
    	entityManager.flush();
    	
    	Settlement sett = settCreator.createSettlement(sub);
    	
    	assertThat(sett).isNotNull();
    	assertThat(sett.getDetails()).isNotEmpty();
    	assertThat(sett.getDetails().get(0).getAmount()).isEqualTo(12000L);
    	assertThat(sett.getAgreementDetails()).isNotEmpty();
    	
    	List<SubscrDelivery> pdeliv = delivRepository.findAll();
    	
    	assertThat(pdeliv).hasSize(1);
    	assertThat(pdeliv.get(0).isPayed()).isTrue();
    	assertThat(pdeliv.get(0).getSettDetail()).isEqualTo(sett.getAgreementDetails().iterator().next());
    	
    	List<Settlement> psett = settRepository.findAll();
    	
    	assertThat(psett).hasSize(1);
    	
    	// jetzt noch das löschen von Abrechnungen testen
    	settCreator.deleteSettlement(psett.get(0));

    	List<InvoiceAgrDetail> pinvAgrDet = entityManager.getEntityManager().createQuery("from InvoiceAgrDetail", InvoiceAgrDetail.class).getResultList();
    	assertThat(pinvAgrDet).isEmpty();

    	List<PosInvoiceDetail> pinvDet = entityManager.getEntityManager().createQuery("from PosInvoiceDetail", PosInvoiceDetail.class).getResultList();
    	assertThat(pinvDet).isEmpty();

    	List<Settlement> psett2 = settRepository.findAll();
    	
    	assertThat(psett2).isEmpty();
    }

    @Test
    public void testCreateSettlementInterval() throws Exception {
    	SubscrProduct product = new SubscrProduct();
    	product.setName("testproduct");

    	SubscrInterval intvl = new SubscrInterval();
    	intvl.setName("Interval 1");
    	intvl.setStartDate(LocalDate.now().minusMonths(1));
    	intvl.setEndDate(LocalDate.now());
    	intvl.setProduct(product);
    	
    	Subscriber subscri = new Subscriber();
    	subscri.setName("testkunde");
    	subscri.setInvoiceAddress(new Address().setName1("testkundeAdrLine1"));

    	Subscription sub = new Subscription();
    	sub.setSubscriber(subscri);
    	sub.setProduct(product);
    	
    	sub.setPaymentType(PayIntervalType.MONTHLY);
    	
    	entityManager.persist(product);
    	entityManager.persist(intvl);
    	entityManager.persist(subscri);
    	sub = entityManager.persistFlushFind(sub);

    	SubscrIntervalDelivery deliv = new SubscrIntervalDelivery();
    	deliv.setInterval(intvl);
    	deliv.setTotal(12600);
    	deliv.setQuantity(3);
    	deliv.setDeliveryDate(LocalDate.now());
    	deliv.setSubscription(sub);

    	entityManager.persist(deliv);
    	
    	entityManager.merge(sub);
    	
    	entityManager.flush();
    	
    	Settlement sett = settCreator.createSettlement(sub);
    	
    	assertThat(sett).isNotNull();
    	assertThat(sett.getDetails()).isNotEmpty();
    	assertThat(sett.getDetails().get(0).getAmount()).isEqualTo(12600L);
    	assertThat(sett.getAgreementDetails()).isNotEmpty();
    	
    	List<SubscrIntervalDelivery> pdeliv = intdelivRepository.findAll();
    	
    	assertThat(pdeliv).hasSize(1);
    	assertThat(pdeliv.get(0).isPayed()).isTrue();
    	assertThat(pdeliv.get(0).getSettDetail()).isEqualTo(sett.getAgreementDetails().iterator().next());
    	
    	List<Settlement> psett = settRepository.findAll();
    	
    	assertThat(psett).hasSize(1);
    }

    // TODO sammelrechnungen prüfen
    
}
