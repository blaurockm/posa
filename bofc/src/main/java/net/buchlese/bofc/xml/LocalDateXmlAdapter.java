package net.buchlese.bofc.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.LocalDate;

public class LocalDateXmlAdapter   extends XmlAdapter<String, LocalDate>{

	public LocalDate unmarshal(String v) throws Exception {
		return LocalDate.parse(v);
	}

	public String marshal(LocalDate v) throws Exception {
		return v.toString("CCYY-MM-dd"); // xalan exslt-functions needs it this way...
	}

}
