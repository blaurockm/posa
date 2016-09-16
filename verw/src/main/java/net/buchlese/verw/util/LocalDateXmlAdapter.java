package net.buchlese.verw.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;


public class LocalDateXmlAdapter   extends XmlAdapter<String, LocalDate>{

	public LocalDate unmarshal(String v) throws Exception {
		return LocalDate.parse(v);
	}

	public String marshal(LocalDate v) throws Exception {
		return v.format(DateTimeFormatter.ISO_LOCAL_DATE); // "CCYY-MM-dd"); // xalan exslt-functions needs it this way...
	}

}
