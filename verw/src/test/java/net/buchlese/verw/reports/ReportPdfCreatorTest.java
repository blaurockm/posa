package net.buchlese.verw.reports;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import net.buchlese.bofc.api.bofc.InvoiceAgrDetail;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosInvoiceDetail;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.bofc.Settlement;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.Subscription;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("integrationtest")
@ComponentScan(value= {"net.buchlese.verw.core","net.buchlese.verw.reports"})
public class ReportPdfCreatorTest {
	
    @Autowired private ReportPdfCreator pdfCreator;
    
    @Test
    public void testInvoicePdf() throws Exception {
    	Subscription sub = new Subscription();
    	
    	Settlement sett = new Settlement();
    	sett.setPointid(1);
    	sett.setName1("Zeile1");
    	sett.setName2("2222Zeile2");
    	sett.setName3("3333Zeile3333");
    	sett.setStreet("Holzweg");
    	sett.setCity("78727 Oberndorf");
    	
    	InvoiceAgrDetail iad = new InvoiceAgrDetail();
    	iad.setSettledAgreement(sub);
    	iad.setDeliveryTill(new java.sql.Date(System.currentTimeMillis()));
    	iad.setPayType(PayIntervalType.EACHDELIVERY);
    	iad.setDeliveries(new HashSet<>());
    	
    	sett.addAgreementDetail(iad);
    	
    	PosInvoiceDetail id = new PosInvoiceDetail();
    	id.setAmount(12000L);
    	
    	sett.addInvoiceDetail(id);
		PosInvoice inv = new PosInvoice();
		inv.setNumber("4712");
		// formalia
		inv.setDate(sett.getDate()); 
		inv.setCreationTime(sett.getCreationTime());
		inv.setCustomerId(sett.getCustomerId());
		inv.setDebitorId(sett.getDebitorId());
		inv.setPointid(sett.getPointid());
		inv.setAmount(sett.getAmount());
		inv.setAmountFull(sett.getAmountFull());
		inv.setAmountHalf(sett.getAmountHalf());
		inv.setAmountNone(sett.getAmountNone());
		inv.setNetto(sett.getNetto());
		inv.setNettoFull(sett.getNettoFull());
		inv.setNettoHalf(sett.getNettoHalf());
		inv.setTax(sett.getTax());
		inv.setTaxHalf(sett.getTaxHalf());
		inv.setTaxFull(sett.getTaxFull());
		inv.setDeliveryFrom(sett.getDeliveryFrom());
		inv.setDeliveryTill(sett.getDeliveryTill());
		inv.setCollective(sett.isCollective());
		inv.setType(sett.getType());
		
		// Rechnungsadresse
		inv.setName1(sett.getName1());
		inv.setName2(sett.getName2());
		inv.setName3(sett.getName3());
		inv.setStreet(sett.getStreet());
		inv.setCity(sett.getCity());
		
		inv.setExported(false);
		inv.setPrinted(false);
		inv.setPayed(false);
		inv.setCancelled(false);
		
		inv.setDetails(new ArrayList<>(sett.getDetails()));
		inv.setAgreementDetails(new ArrayList<>(sett.getAgreementDetails()));
		
		inv.setPointid(sett.getPointid());
    	
		byte[] pdf = pdfCreator.createReport(inv,"static/templates/report/invoice.xsl", null);
		
		FileUtils.writeByteArrayToFile(new File("target/reportInvoiceTest.pdf"), pdf);
    }
    
    @Test
    public void testDeliveryNotePdf() throws Exception {
    	Subscription sub = new Subscription();
    	
    	Settlement sett = new Settlement();
    	sett.setPointid(1);
    	sett.setName1("Zeile1");
    	sett.setName2("2222Zeile2");
    	sett.setName3("3333Zeile3333");
    	sett.setStreet("Holzweg");
    	sett.setCity("78727 Oberndorf");
    	
    	InvoiceAgrDetail iad = new InvoiceAgrDetail();
    	iad.setSettledAgreement(sub);
    	iad.setDeliveryTill(new java.sql.Date(System.currentTimeMillis()));
    	iad.setPayType(PayIntervalType.EACHDELIVERY);
    	iad.setDeliveries(new HashSet<>());
    	
    	sett.addAgreementDetail(iad);
    	
    	PosInvoiceDetail id = new PosInvoiceDetail();
    	id.setAmount(12000L);
    	
    	sett.addInvoiceDetail(id);
		PosIssueSlip inv = new PosIssueSlip();
		inv.setNumber("4712");
		// formalia
		inv.setDate(sett.getDate()); 
		inv.setCreationTime(sett.getCreationTime());
		inv.setCustomerId(sett.getCustomerId());
		inv.setDebitorId(sett.getDebitorId());
		inv.setPointid(sett.getPointid());
		inv.setAmount(sett.getAmount());
		inv.setAmountFull(sett.getAmountFull());
		inv.setAmountHalf(sett.getAmountHalf());
		inv.setAmountNone(sett.getAmountNone());
		inv.setDeliveryFrom(sett.getDeliveryFrom());
		inv.setDeliveryTill(sett.getDeliveryTill());
		inv.setType(sett.getType());
		
		// Rechnungsadresse
		inv.setName1(sett.getName1());
		inv.setName2(sett.getName2());
		inv.setName3(sett.getName3());
		inv.setStreet(sett.getStreet());
		inv.setCity(sett.getCity());
		
		inv.setPrinted(false);
		inv.setPayed(false);
		inv.setCancelled(false);
		
		inv.setDetails(new ArrayList<>(sett.getDetails()));
		
		inv.setPointid(sett.getPointid());
    	
		byte[] pdf = pdfCreator.createReport(inv,"static/templates/report/deliveryNote.xsl", null);
		
		FileUtils.writeByteArrayToFile(new File("target/reportDeliveryNoteTest.pdf"), pdf);
    }
    
}
