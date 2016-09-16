package net.buchlese.verw.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateTimeXmlAdapter   extends XmlAdapter<String, LocalDateTime>{

	public LocalDateTime unmarshal(String v) throws Exception {
		return LocalDateTime.parse(v);
	}

	public String marshal(LocalDateTime v) throws Exception {
//		return v.format(DateTimeFormatter.ofPattern("CCYY-MM-dd'T'hh:mm:ss")); // xalan exslt-functions needs it this way...
		return v.format(DateTimeFormatter.ISO_DATE_TIME); // xalan exslt-functions needs it this way...
	}

}
