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

import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.subscr.Address;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.repos.IssueSlipRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("integrationtest")
@ComponentScan(value="net.buchlese.verw.core")
public class DeliveryNoteCreatorTest {
    @Autowired TestEntityManager entityManager;

    @Autowired IssueSlipRepository slipRepository;

    @Autowired  DeliveryNoteCreator noteCreator;
    @Autowired  SequenceGenerator generator;

    @Test
    public void testErzeugeLieferschein() throws Exception {
    	generator.initSequence("deliveryNotes1", 676, LocalDate.now().minusMonths(1));
    	Subscription sub = new Subscription();
    	Address a1 = new Address();
    	a1.setName1("liefername1");
    	sub.setDeliveryAddress(a1);

    	Subscriber subscr = new Subscriber();
    	subscr.setPointid(1);
    	Address a = new Address();
    	a.setName1("name1");
    	subscr.setInvoiceAddress(a);
    	
    	SubscrArticle art = new SubscrArticle();
    	art.setName("hallo");
    	
    	SubscrDelivery deli = new SubscrDelivery();
    	deli.setSubscriber(subscr);
    	deli.setSubscription(sub);
    	deli.setQuantity(1);
    	deli.setArticle(art);
    	deli.setArticleName(art.getName());
    	deli.setDeliveryDate(LocalDate.now());
    	
    	entityManager.persist(sub);
    	entityManager.persist(subscr);
    	entityManager.persist(art);
    	entityManager.persistAndFlush(deli);
    	
    	PosIssueSlip inv = noteCreator.createDeliveryNote(deli);
    	
    	assertThat(inv).isNotNull();
    	assertThat(inv.getNumber()).isEqualTo("677");
    	assertThat(inv.getDetails()).isNotEmpty();
    	assertThat(inv.getName1()).isEqualTo("liefername1");
    	assertThat(inv.getDetails().get(0).getText()).isEqualTo("hallo");
    	assertThat(inv.getDeliveryDetails()).isNotEmpty();
    	
    	List<PosIssueSlip> pinv = slipRepository.findAll();
    	
    	assertThat(pinv).hasSize(1);
    	
    }
    
}
