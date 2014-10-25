package net.buchlese.posa.core;



import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosCashBalance;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;

public class PDFCashBalance {

	public static String nameOfKasse ="Buchlese";

	private PosCashBalance bal;
	private Transformer transformer;
	
	public PDFCashBalance(PosCashBalance balance) {
		this.bal = balance;
	}
	
	private Transformer getPDFTransformer() throws TransformerConfigurationException {
		if (transformer == null) {
		    TransformerFactory factory = TransformerFactory.newInstance();
		    transformer = factory.newTransformer(new StreamSource(PDFCashBalance.class.getResourceAsStream("/xsl/cashBalance.xsl")));
		    transformer.setParameter("name_of_kasse", nameOfKasse);
		}
		return transformer;
	}
	
	
	public void generatePDF(OutputStream output)  throws JAXBException, FOPException, TransformerException {
    	JAXBContext jc = JAXBContext.newInstance(new Class[] {PosCashBalance.class});
    	Marshaller m = jc.createMarshaller();
    	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

    	// Step 3: Construct fop with desired output format
	    Fop fop = PosAdapterConfiguration.getFopFactory().newFop(MimeConstants.MIME_PDF, output);

	    Source src = new JAXBSource(jc, bal);
	    Result res = new SAXResult(fop.getDefaultHandler());

	    // Start XSLT transformation and FOP processing
	    getPDFTransformer().transform(src, res);
	}




	

	
	
	
}
