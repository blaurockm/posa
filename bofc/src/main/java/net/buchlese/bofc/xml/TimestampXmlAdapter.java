package net.buchlese.bofc.xml;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TimestampXmlAdapter   extends XmlAdapter<String, Timestamp>{

	public Timestamp unmarshal(String v) throws Exception {
		return (Timestamp) new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(v);
	}

	public String marshal(Timestamp v) throws Exception {
		return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(v); // xalan exslt-functions needs it this way...
	}

}
