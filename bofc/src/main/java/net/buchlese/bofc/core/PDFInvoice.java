package net.buchlese.bofc.core;



import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;

import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.api.bofc.PosInvoice;

public class PDFInvoice {

	public static String nameOfKasse ="Buchlese";

	private final Lists<PosInvoice> invs;
	private Transformer transformer;
	
	public PDFInvoice(PosInvoice i) {
		invs = new Lists<>();
		invs.getValues().add(i);
	}

	public PDFInvoice(List<PosInvoice> i) {
		invs = new Lists<>();
		invs.getValues().addAll(i);
	}

	private Transformer getPDFTransformer() throws TransformerConfigurationException {
		if (transformer == null) {
		    TransformerFactory factory = TransformerFactory.newInstance();
		    transformer = factory.newTransformer(new StreamSource(PDFInvoice.class.getResourceAsStream("/xsl/invoice.xsl")));
		    transformer.setParameter("name_of_kasse", nameOfKasse);
		}
		return transformer;
	}

	@XmlRootElement
	public static class Lists<VALUE> {

	    private final List<VALUE> values = new ArrayList<>();

	    @XmlAnyElement(lax=true)
	    public List<VALUE> getValues() {
	        return this.values;
	    }

	}
	
	public void generatePDF(OutputStream output)  throws JAXBException, FOPException, TransformerException {
    	JAXBContext jc = JAXBContext.newInstance(new Class[] {PosInvoice.class, Lists.class});
    	Marshaller m = jc.createMarshaller();
    	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

    	// Step 3: Construct fop with desired output format
	    Fop fop = BackOfcConfiguration.getFopFactory().newFop(MimeConstants.MIME_PDF, output);

	    Source src = new JAXBSource(jc, invs);
	    Result res = new SAXResult(fop.getDefaultHandler());

	    // Start XSLT transformation and FOP processing
	    getPDFTransformer().transform(src, res);
	}




	

	
	
	
}
