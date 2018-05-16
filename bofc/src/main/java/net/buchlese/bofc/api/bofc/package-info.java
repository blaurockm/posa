@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters (
  { @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type=org.joda.time.DateTime.class, value=net.buchlese.bofc.xml.DateTimeXmlAdapter.class),
	@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type=org.joda.time.LocalDate.class, value=net.buchlese.bofc.xml.LocalDateXmlAdapter.class),
	@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type=java.sql.Date.class, value=net.buchlese.bofc.xml.DateXmlAdapter.class),
	@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type=java.sql.Timestamp.class, value=net.buchlese.bofc.xml.TimestampXmlAdapter.class)
  })
package net.buchlese.bofc.api.bofc;

