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
    
    private void checkCleanTables() {
		List<InvoiceAgrDetail> pinvAgrDet = entityManager.getEntityManager().createQuery("from InvoiceAgrDetail", InvoiceAgrDetail.class).getResultList();
    	assertThat(pinvAgrDet).isEmpty();

    	List<PosInvoiceDetail> pinvDet = entityManager.getEntityManager().createQuery("from PosInvoiceDetail", PosInvoiceDetail.class).getResultList();
    	assertThat(pinvDet).isEmpty();
	}

	private void checkDeliveryFree(SubscrDelivery pdeliv) {
    	assertThat(pdeliv.isPayed()).isFalse();
	}

	private void checkDeliveryFree(SubscrIntervalDelivery pdeliv) {
    	assertThat(pdeliv.isPayed()).isFalse();
	}

	private void checkDeliveryGood(Settlement sett, SubscrDelivery pdeliv) {
    	assertThat(pdeliv.isPayed()).isTrue();
    	assertThat(sett.getAgreementDetails()).contains(pdeliv.getSettDetail());
	}

	private void checkDeliveryGood(Settlement sett, SubscrIntervalDelivery pdeliv) {
    	assertThat(pdeliv.isPayed()).isTrue();
    	assertThat(sett.getAgreementDetails()).contains(pdeliv.getSettDetail());
	}

	private void checkSettlement(Settlement sett, long amount) throws Exception {
    	assertThat(sett).isNotNull();
    	assertThat(sett.getDetails()).isNotEmpty();
    	assertThat(sett.getAmount()).isEqualTo(amount);
    	assertThat(sett.getAgreementDetails()).isNotEmpty();
    }

	private void checkSettlementLine(PosInvoiceDetail detail, long amount, String text) throws Exception {
    	assertThat(detail.getAmount()).isEqualTo(amount);
    	assertThat(detail.getText()).isEqualTo(text);
    }

	private SubscrArticle createArticle(SubscrProduct product) {
		SubscrArticle article = new SubscrArticle();
    	article.setName("artikel 1");
    	article.setProduct(product);
    	entityManager.persist(article);
		return article;
	}

	private SubscrDelivery createArticleDelivery(SubscrArticle article, Subscription sub) {
		SubscrDelivery deliv = new SubscrDelivery();
    	deliv.setArticle(article);
    	deliv.setTotal(12000);
    	deliv.setQuantity(3);
    	deliv.setDeliveryDate(new java.sql.Date(System.currentTimeMillis()));
    	deliv.setSubscription(sub);
    	entityManager.persist(deliv);
    	return deliv;
	}

	private SubscrInterval createInterval(SubscrProduct product) {
		if (product.isPayPerDelivery()) {
			throw new IllegalArgumentException("product is pay per Delivery, no interval possible");
		}
		SubscrInterval intvl = new SubscrInterval();
    	intvl.setName("Interval 1");
    	intvl.setStartDate(java.sql.Date.valueOf(LocalDate.now().minusMonths(1)));
    	intvl.setEndDate(java.sql.Date.valueOf(LocalDate.now()));
    	intvl.setProduct(product);
    	entityManager.persist(intvl);
		return intvl;
	}

	private SubscrIntervalDelivery createIntervalDelivery(SubscrInterval intvl, Subscription sub) {
		SubscrIntervalDelivery deliv = new SubscrIntervalDelivery();
    	deliv.setInterval(intvl);
    	deliv.setTotal(12600);
    	deliv.setQuantity(3);
    	deliv.setDeliveryDate(new java.sql.Date(System.currentTimeMillis()));
    	deliv.setSubscription(sub);
    	entityManager.persist(deliv);
    	return deliv;
	}

    private SubscrProduct createIntervalProduct() {
		SubscrProduct product = new SubscrProduct();
    	product.setName("testintervalproduct");
    	product.setPayPerDelivery(false);
    	entityManager.persist(product);
		return product;
	}

	private SubscrProduct createProduct() {
		SubscrProduct product = new SubscrProduct();
    	product.setName("testproduct");
    	product.setPayPerDelivery(true);
    	entityManager.persist(product);
		return product;
	}

	private Subscriber createSubscriber() {
		Subscriber subscri = new Subscriber();
    	subscri.setName("testkunde");
    	subscri.setInvoiceAddress(new Address().setName1("testkundeAdrLine1"));
    	entityManager.persist(subscri);
		return subscri;
	}

	private Subscription createSubscription(SubscrProduct product, Subscriber subscri) {
		Subscription sub = new Subscription();
    	sub.setSubscriber(subscri);
    	sub.setProduct(product);
    	if (product.isPayPerDelivery()) {
    		sub.setPaymentType(PayIntervalType.EACHDELIVERY);
    	} else {
    		sub.setPaymentType(PayIntervalType.MONTHLY);
    	}
    	entityManager.persist(sub);
		return sub;
	}

	@Test
    public void testCreateHomogenCollectiveSettlementDeliv() throws Exception {
    	SubscrProduct product = createProduct();
    	SubscrArticle article = createArticle(product);
    	SubscrProduct product2 = createProduct();
    	SubscrArticle article2 = createArticle(product2);
    	Subscriber subscri = createSubscriber();
    	entityManager.flush();

    	Subscription sub = createSubscription(product, subscri);
    	Subscription sub2 = createSubscription(product2, subscri);

    	assertThat(subscri.getSubscriptions()).hasSize(2);
    	
    	SubscrDelivery deli1 = createArticleDelivery(article, sub);
    	SubscrDelivery deli2 = createArticleDelivery(article2, sub2);
    	
    	entityManager.flush();
    	
    	Settlement sett = settCreator.createCollectiveSettlement(subscri);
    	
    	checkSettlement(sett, 24000L);
    	checkSettlementLine(sett.getDetails().get(0), 12000L, article.getName());
    	checkSettlementLine(sett.getDetails().get(1), 12000L, article2.getName());
    	assertThat(sett.getAgreementDetails()).hasSize(2);
    	
    	checkDeliveryGood(sett, deli1);
    	checkDeliveryGood(sett, deli2);
    	
    	assertThat(delivRepository.findAll()).hasSize(2);
    	assertThat(settRepository.findAll()).hasSize(1);
    	
    	// jetzt noch das löschen von Abrechnungen testen
    	settCreator.deleteSettlement(sett);

    	checkCleanTables();
   	
    	assertThat(settRepository.findAll()).isEmpty();
    }
	
	
    @Test
    public void testCreateMixedCollectiveSettlementDeliv() throws Exception {
    	SubscrProduct product = createProduct();
    	SubscrArticle article = createArticle(product);

    	SubscrProduct product2 = createIntervalProduct();
    	SubscrInterval intvl = createInterval(product2);

    	Subscriber subscri = createSubscriber();

    	entityManager.flush();

    	Subscription sub = createSubscription(product, subscri);
    	Subscription sub2 = createSubscription(product2, subscri);

    	assertThat(subscri.getSubscriptions()).hasSize(2);
    	
    	SubscrDelivery deli1 = createArticleDelivery(article, sub);
    	SubscrIntervalDelivery deli2 = createIntervalDelivery(intvl, sub2); 
    	
    	entityManager.flush();
    	
    	Settlement sett = settCreator.createCollectiveSettlement(subscri);
    	
    	checkSettlement(sett, 24600L);
    	checkSettlementLine(sett.getDetails().get(0), 12000L, article.getName());
    	checkSettlementLine(sett.getDetails().get(1), 12600L, intvl.getName());
    	assertThat(sett.getAgreementDetails()).hasSize(2);
    	assertThat(delivRepository.findAll()).hasSize(1);
    	assertThat(intdelivRepository.findAll()).hasSize(1);
    	assertThat(settRepository.findAll()).hasSize(1);
    	
    	checkDeliveryGood(sett, deli1);
    	checkDeliveryGood(sett, deli2);
    	
    	// jetzt noch das löschen von Abrechnungen testen
    	settCreator.deleteSettlement(sett);

    	checkCleanTables();

    	assertThat(settRepository.findAll()).isEmpty();

    	checkDeliveryFree(deli1);
    	checkDeliveryFree(deli2);
    }

    @Test
    public void testCreateSettlementDeliv() throws Exception {
    	SubscrProduct product = createProduct();
    	SubscrArticle article = createArticle(product);
    	Subscriber subscri = createSubscriber();
    	Subscription sub = createSubscription(product, subscri);
    	SubscrDelivery deliv = createArticleDelivery(article, sub);
    	
    	entityManager.flush();
    	
    	Settlement sett = settCreator.createSettlement(sub);
    	
    	checkSettlement(sett, 12000L);
    	checkSettlementLine(sett.getDetails().get(0), 12000L, article.getName());
    	
    	checkDeliveryGood(sett, deliv);
    	
    	assertThat(settRepository.findAll()).hasSize(1);
    	
    	// jetzt noch das löschen von Abrechnungen testen
    	settCreator.deleteSettlement(sett);

    	assertThat(settRepository.findAll()).isEmpty();
    	checkCleanTables();
    }

    
    @Test
    public void testCreateSettlementInterval() throws Exception {
    	SubscrProduct product = createIntervalProduct();
    	SubscrInterval intvl = createInterval(product);
    	Subscriber subscri = createSubscriber();
    	Subscription sub = createSubscription(product, subscri);
    	SubscrIntervalDelivery deliv = createIntervalDelivery(intvl, sub);
    	
    	entityManager.flush();
    	
    	Settlement sett = settCreator.createSettlement(sub);
    	
    	checkSettlement(sett, 12600L);
    	checkSettlementLine(sett.getDetails().get(0), 12600L, intvl.getName());

    	checkDeliveryGood(sett, deliv);
    	
    	assertThat(settRepository.findAll()).hasSize(1);
    }
    
}
