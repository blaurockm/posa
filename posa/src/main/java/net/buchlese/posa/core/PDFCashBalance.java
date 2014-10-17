package net.buchlese.posa.core;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosCashBalance;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;

public class PDFCashBalance {

	public static byte[] create(PosCashBalance bal) throws JAXBException, FOPException, TransformerException, IOException {
    	JAXBContext jc = JAXBContext.newInstance(new Class[] {PosCashBalance.class});
    	Marshaller m = jc.createMarshaller();
    	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    	m.marshal(bal, System.out);
    	
    	// Step 2: Set up output stream.
    	// Note: Using BufferedOutputStream for performance reasons (helpful with FileOutputStreams).
    	ByteArrayOutputStream out = new ByteArrayOutputStream();

    	try {
    	    // Step 3: Construct fop with desired output format
    	    Fop fop = PosAdapterApplication.getFopFactory().newFop(MimeConstants.MIME_PDF, out);

    	    // Step 4: Setup JAXP using identity transformer
    	    TransformerFactory factory = TransformerFactory.newInstance();
    	    Transformer transformer = factory.newTransformer(new StreamSource(PDFCashBalance.class.getResourceAsStream("xsl/cashBalance.xsl")));
    	    		
    	    // Step 5: Setup input and output for XSLT transformation
    	    // Setup input stream
    	    Source src = new JAXBSource(jc, bal);

    	    // Resulting SAX events (the generated FO) must be piped through to FOP
    	    Result res = new SAXResult(fop.getDefaultHandler());

    	    // Step 6: Start XSLT transformation and FOP processing
    	    transformer.transform(src, res);

    	} finally {
    	    //Clean-up
    	    out.close();
    	}    	
		return out.toByteArray();
	}
	

	
	
	
}
