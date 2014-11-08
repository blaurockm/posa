package net.buchlese.bofc.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.joda.time.DateTime;

public class DateTimeXmlAdapter   extends XmlAdapter<String, DateTime>{

	public DateTime unmarshal(String v) throws Exception {
		return new DateTime(v);
	}

	public String marshal(DateTime v) throws Exception {
		return v.toString("CCYY-MM-dd'T'hh:mm:ss"); // xalan exslt-functions needs it this way...
	}

}
