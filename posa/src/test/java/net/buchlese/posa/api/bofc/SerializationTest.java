package net.buchlese.posa.api.bofc;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.joda.time.DateTime;
import org.junit.Test;


public class SerializationTest {

	@Test
    public void testApp() throws Exception {
    	PosCashBalance bal = new PosCashBalance();
    	bal.setCreationtime(new DateTime());
    	bal.setFirstCovered(new DateTime().minusHours(24));
    	bal.setLastCovered(new DateTime().plusHours(24));
    	Map<Tax,Long> taxbal = new HashMap<Tax, Long>();
    	taxbal.put(Tax.FULL, 555l);
    	bal.setTaxBalance(taxbal);
    	bal.setTicketCount(23);
    	
    	//JAXBContext jc = JAXBContext.newInstance( "net.buchlese.posa.api.bofc" );
    	JAXBContext jc = JAXBContext.newInstance(new Class[] {PosCashBalance.class});
    	Marshaller m = jc.createMarshaller();
    	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    	m.marshal(bal, System.out);

    }


}
