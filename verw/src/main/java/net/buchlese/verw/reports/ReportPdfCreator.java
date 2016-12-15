package net.buchlese.verw.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

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

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Scope(value="singleton")
public class ReportPdfCreator {

	@Autowired FopFactoryComponent fopFactory;
	
	// "/static/templates/report/invoice.xsl"
	public byte[] createReport(Object reportObj, String template, Map<String, Object> params) throws JAXBException, FOPException, TransformerException, IOException {
		
		JAXBContext jc = JAXBContext.newInstance(reportObj.getClass());
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		FOUserAgent userAgent = fopFactory.newFOUserAgent();

		userAgent.setAuthor("Buchlese");
		userAgent.setTargetResolution(300); // =300dpi (dots/pixels per Inch)
		// Step 3: Construct fop with desired output format
		Fop fop = userAgent.newFop(MimeConstants.MIME_PDF, outStream);

		Source src = new JAXBSource(jc, reportObj);
		Result res = new SAXResult(fop.getDefaultHandler());

		// Start XSLT transformation and FOP processing
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(new ClassPathResource(template).getInputStream()));
		if (transformer != null) {
			transformer.transform(src, res);
		} else {
			throw new TransformerException("cannot parse XSL " + template);
		}
		return outStream.toByteArray();
	}


}
