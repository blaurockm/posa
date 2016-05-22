package net.buchlese.bofc.core;



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

import net.buchlese.bofc.BackOfcConfiguration;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;

public class PDFReport {

	private final Object report;
	private final String xslFilename;
	private Transformer transformer;
	
	public PDFReport(Object i, String xslfile) {
		this.report = i;
		this.xslFilename = xslfile;
	}
	
	private Transformer getPDFTransformer() throws TransformerConfigurationException {
		if (transformer == null) {
		    TransformerFactory factory = TransformerFactory.newInstance();
		    transformer = factory.newTransformer(new StreamSource(PDFReport.class.getResourceAsStream("/xsl/" + xslFilename)));
		    transformer.setParameter("report", report.getClass().getSimpleName());
		}
		return transformer;
	}
	
	
	public void generatePDF(OutputStream output)  throws JAXBException, FOPException, TransformerException {
    	JAXBContext jc = JAXBContext.newInstance(new Class[] {report.getClass()});
    	Marshaller m = jc.createMarshaller();
    	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

    	// Step 3: Construct fop with desired output format
	    Fop fop = BackOfcConfiguration.getFopFactory().newFop(MimeConstants.MIME_PDF, output);

	    Source src = new JAXBSource(jc, report);
	    Result res = new SAXResult(fop.getDefaultHandler());

	    // Start XSLT transformation and FOP processing
	    getPDFTransformer().transform(src, res);
	}




	

	
	
	
}
