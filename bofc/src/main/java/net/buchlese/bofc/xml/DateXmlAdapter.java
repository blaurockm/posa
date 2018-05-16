package net.buchlese.bofc.xml;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateXmlAdapter   extends XmlAdapter<String, Date>{

	public Date unmarshal(String v) throws Exception {
		return (Date) new SimpleDateFormat("yyyy-MM-dd").parse(v);
	}

	public String marshal(Date v) throws Exception {
		return new SimpleDateFormat("yyyy-MM-dd").format(v); // xalan exslt-functions needs it this way...
	}

}
