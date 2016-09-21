package net.buchlese.verw.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashSet;
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
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosInvoiceDetail;
import net.buchlese.bofc.api.bofc.Settlement;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.repos.InvoiceRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("integrationtest")
@ComponentScan(value="net.buchlese.verw.core")
public class InvoiceCreatorTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired 
    private InvoiceRepository invRepository;


    @Autowired 
    private InvoiceCreator invCreator;
    @Autowired 
    private SequenceGenerator generator;

    @Test
    public void testfakturiereInvoice() throws Exception {
    	generator.initSequence("invoice1", 676, LocalDate.now().minusMonths(1));
    	Subscription sub = new Subscription();
    	
    	Settlement sett = new Settlement();
    	sett.setPointid(1);
    	
    	InvoiceAgrDetail iad = new InvoiceAgrDetail();
    	iad.setSettledAgreement(sub);
    	iad.setDeliveryTill(LocalDate.now());
    	iad.setPayType(PayIntervalType.EACHDELIVERY);
    	iad.setDeliveries(new HashSet<>());
    	
    	sett.addAgreementDetail(iad);
    	
    	PosInvoiceDetail id = new PosInvoiceDetail();
    	id.setAmount(12000L);
    	
    	sett.addInvoiceDetail(id);
    	
    	entityManager.persist(sub);
    	entityManager.persistAndFlush(sett);
    	
    	PosInvoice inv = invCreator.fakturiereSettlement(sett);
    	
    	assertThat(inv).isNotNull();
    	assertThat(inv.getNumber()).isEqualTo("677");
    	assertThat(inv.getAmount()).isEqualTo(12000L);
    	assertThat(inv.getDetails()).isNotEmpty();
    	assertThat(inv.getDetails().get(0).getAmount()).isEqualTo(12000L);
    	assertThat(inv.getAgreementDetails()).isNotEmpty();
    	
    	
    	List<PosInvoice> pinv = invRepository.findAll();
    	
    	assertThat(pinv).hasSize(1);
    }

    // TODO cancelInvoice testen
    
    
}
