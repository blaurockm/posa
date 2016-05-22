<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
          xmlns:date="http://exslt.org/dates-and-times"
          extension-element-prefixes="date">
   <xsl:output method="xml" version="1.0" indent="yes" encoding="UTF-8" />
   <xsl:param name="name_of_kasse"/>
   <xsl:decimal-format name="euro" decimal-separator=',' grouping-separator='.' />
   <xsl:template match="/invoice">
      <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
         <fo:layout-master-set>
            <fo:simple-page-master master-name="DIN-A4-mAdr" page-height="29.7cm" page-width="21cm" margin-left="2cm" margin-right="2.5cm" margin-top="0.5cm" margin-bottom="1cm">
               <fo:region-body margin-top="10.5cm" margin-bottom="1.8cm"/>
               <fo:region-before region-name="header" extent="10.3cm" />
               <fo:region-after region-name="footer" extent="1.5cm" />
            </fo:simple-page-master>
            <fo:simple-page-master master-name="DIN-A4-oAdr" page-height="29.7cm" page-width="21cm" margin-left="2cm" margin-right="2.5cm" margin-top="0.5cm" margin-bottom="1cm">
               <fo:region-body margin-top="1.5cm" margin-bottom="1.8cm" />
               <fo:region-before region-name="header" extent="1.3cm" />
               <fo:region-after region-name="footer" extent="1.5cm" />
            </fo:simple-page-master>
            <fo:page-sequence-master master-name="DIN-A4" page-height="29.7cm" page-width="21cm" margin-left="2cm" margin-right="2.5cm" margin-top="0.5cm" margin-bottom="1cm">
               <fo:repeatable-page-master-alternatives>
  		  	   	 <fo:conditional-page-master-reference master-reference="DIN-A4-mAdr" page-position="first"/>
                 <fo:conditional-page-master-reference master-reference="DIN-A4-oAdr" page-position="rest"  />
                </fo:repeatable-page-master-alternatives>
            </fo:page-sequence-master>
         </fo:layout-master-set>
         <fo:page-sequence master-reference="DIN-A4">
            <!-- <fo:static-content flow-name="header"> </fo:static-content> -->
            <fo:static-content flow-name="header">
               <xsl:call-template name="briefkopf" />
            </fo:static-content>
            <fo:static-content flow-name="footer">
               <xsl:call-template name="brieffuss" />
            </fo:static-content>

            <fo:flow flow-name="xsl-region-body">
               <xsl:call-template name="adressat" />

               <xsl:call-template name="rechnungsinfozeile" />
               <xsl:call-template name="rechnungspositionen" />
               <xsl:call-template name="endbetrag" />
            </fo:flow>
         </fo:page-sequence>
      </fo:root>
   </xsl:template>

   <xsl:template match="details[textonly='true']">
   	  <fo:table-row>
   	     <fo:table-cell>
   	       <fo:block></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block font-size="10pt" margin-left="-1mm" font-weight="bold">
	        <xsl:if test="preceding-sibling::details[1][textonly='false']">
	         <xsl:attribute name="margin-top">
               <xsl:value-of select="'2mm'"/>
             </xsl:attribute>
             </xsl:if>
	        <xsl:value-of select="text"/></fo:block>   	       
   	     </fo:table-cell>
   	  </fo:table-row>
   </xsl:template>

   <xsl:template match="details[textonly='false']">
   	  <fo:table-row>
   	     <fo:table-cell>
   	       <fo:block></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block font-size="10pt"><xsl:value-of select="text"/></fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block font-size="10pt" text-align="center"><xsl:value-of select="quantity"/></fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block font-size="10pt" text-align="right"><xsl:value-of select="format-number(singlePrice div 100, '###.###,00', 'euro')"/></fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block font-size="10pt" text-align="right">7%</fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block font-size="10pt" text-align="right"><xsl:value-of select="format-number(amountHalf div 100, '###.###,00 €', 'euro')"/></fo:block>   	       
   	     </fo:table-cell>
   	  </fo:table-row>
   	  <xsl:if test="amountFull > 0">
   	  <fo:table-row>
   	     <fo:table-cell>
   	       <fo:block></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block font-size="10pt" text-align="right">19%</fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block font-size="10pt" text-align="right"><xsl:value-of select="format-number(amountFull div 100, '###.###,00 €', 'euro')"/></fo:block>   	       
   	     </fo:table-cell>
   	  </fo:table-row>
   	  </xsl:if>
   </xsl:template>

   <xsl:template name="rechnungspositionen">
      <fo:block-container width="152mm" margin-top="5mm">
       <fo:block>Anbei erhalten Sie die Rechnung für die von Ihnen bestellten Periodika.</fo:block>
      </fo:block-container>
      <fo:table margin-top="5mm" table-layout="fixed" width="160mm">
         <fo:table-column column-width="1cm" />
         <fo:table-column column-width="8cm" />
         <fo:table-column column-width="12mm" />
         <fo:table-column column-width="16mm" />
         <fo:table-column column-width="20mm" />
         <fo:table-column column-width="24mm" />
       <fo:table-header>
	  	  <fo:table-row border-top-style="solid" border-bottom-style="solid" border-top-width="0.2mm" border-bottom-width="0.2mm" 
	  	      space-after="3mm">
	  	     <fo:table-cell>
	  	       <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm">Pos</fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm">Bezeichnung</fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	           <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm" text-align="center">Menge</fo:block>   	       
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	           <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm" text-align="right">EP €</fo:block>   	       
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm" text-align="right">MwSt</fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	           <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm" text-align="right">GP</fo:block>   	       
	  	     </fo:table-cell>
	  	  </fo:table-row>
	  	  <fo:table-row> 
	  	     <fo:table-cell>
	           <fo:block  margin-bottom="2mm"></fo:block>   	       
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	           <fo:block ></fo:block>   	       
	  	     </fo:table-cell>
	  	  </fo:table-row>
       </fo:table-header>  
       <fo:table-body>
         <xsl:apply-templates select="details"/>
       </fo:table-body>
      </fo:table>
   </xsl:template>

   <xsl:template name="endbetrag">
      <fo:table margin-top="10mm" table-layout="fixed" width="160mm" keep-together="always">
         <fo:table-column column-width="9cm" />
         <fo:table-column column-width="28mm" />
         <fo:table-column column-width="44mm" />
       <fo:table-header>
	  	  <fo:table-row>
	  	     <fo:table-cell>
	  	       <fo:block></fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block></fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block border-bottom-style="solid" border-bottom-width="0.1mm"></fo:block>
	  	     </fo:table-cell>
	  	  </fo:table-row>
       </fo:table-header>  
       <fo:table-body>
	  	  <fo:table-row >
	  	     <fo:table-cell>
	  	     <fo:block>
          <fo:table table-layout="fixed" width="9cm" font-size="8pt" text-align="right" margin-top="1mm">
              <fo:table-column column-width="1cm" />
              <fo:table-column column-width="2cm" />
              <fo:table-column column-width="2cm" />
              <fo:table-column column-width="16mm" />
             <fo:table-body> 
	  	  <fo:table-row>
	  	     <fo:table-cell>
	  	       <fo:block>Satz</fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block>Brutto</fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block>Netto</fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block>Steuer</fo:block>
	  	     </fo:table-cell>
	  	  </fo:table-row>
	  	  <fo:table-row>
	  	     <fo:table-cell>
	  	       <fo:block>7%</fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block><xsl:value-of select="format-number(amountHalf div 100, '###.###,00 €', 'euro')"/></fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block><xsl:value-of select="format-number(nettoHalf div 100, '###.###,00 €', 'euro')"/></fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block><xsl:value-of select="format-number(taxHalf div 100, '###.###,00 €', 'euro')"/></fo:block>
	  	     </fo:table-cell>
	  	  </fo:table-row>
	  	  <xsl:if test="amountFull > 0">
	  	  <fo:table-row>
	  	     <fo:table-cell>
	  	       <fo:block>19%</fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block><xsl:value-of select="format-number(amountFull div 100, '###.###,00 €', 'euro')"/></fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block><xsl:value-of select="format-number(nettoFull div 100, '###.###,00 €', 'euro')"/></fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block><xsl:value-of select="format-number(taxFull div 100, '###.###,00 €', 'euro')"/></fo:block>
	  	     </fo:table-cell>
	  	  </fo:table-row>
	  	  <fo:table-row>
	  	     <fo:table-cell>
	  	       <fo:block></fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block></fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block></fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block border-top-style="solid" border-top-width="0.1mm"><xsl:value-of select="format-number(tax div 100, '###.###,00 €', 'euro')"/></fo:block>
	  	     </fo:table-cell>
	  	  </fo:table-row>
	  	  </xsl:if>
	  	  </fo:table-body>
         </fo:table>
         </fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block margin-top="4mm" margin-bottom="4mm">Rechnungsbetrag</fo:block>
	  	     </fo:table-cell>
	  	     <fo:table-cell>
	  	       <fo:block text-align="right" margin-top="4mm" margin-bottom="4mm">
	  	       <xsl:value-of select="format-number(amount div 100, '###.###,00 €', 'euro')"/></fo:block>
	  	       <fo:block border-bottom-style="double" border-bottom-width="0.8mm" text-align="right" margin-top="4mm" margin-bottom="4mm">
	  	       </fo:block>
	  	     </fo:table-cell>
	  	  </fo:table-row>
       </fo:table-body>
      </fo:table>
      <fo:block-container width="152mm" margin-top="5mm">
      <fo:block id="end">Vielen Dank für Ihren Auftrag. Die Rechnung ist ohne Abzüge sofort fällig.</fo:block>
      </fo:block-container>
   </xsl:template>

   <xsl:template name="rechnungsinfozeile">
     <fo:block-container>
        <fo:block font-size="16pt" font-weight="bold">Rechnung <xsl:value-of select="number"/></fo:block>
        <fo:table margin-top="5mm" table-layout="fixed" width="15cm">
           <fo:table-column column-width="3cm" />
           <fo:table-column column-width="3cm" />
           <fo:table-column column-width="3cm" />
           <fo:table-column column-width="3cm" />
           <fo:table-column column-width="3cm" />
        <fo:table-body>
   	    <fo:table-row>
   	     <fo:table-cell>
   	       <fo:block>Debitor</fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block>Leistung von</fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block>Leistung bis</fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block>Erfassung</fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block>Rechnungsdatum</fo:block>   	       
   	     </fo:table-cell>
   	     </fo:table-row>
   	    <fo:table-row>
   	     <fo:table-cell>
   	       <fo:block><xsl:value-of select="debitorId"/></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block><xsl:value-of select="date:format-date(deliveryFrom, 'dd.MM.yyyy')" /></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block><xsl:value-of select="date:format-date(deliveryTill, 'dd.MM.yyyy')" /></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block><xsl:value-of select="date:format-date(creationTime, 'dd.MM.yyyy')" /></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block><xsl:value-of select="date:format-date(date, 'dd.MM.yyyy')" /></fo:block>   	       
   	     </fo:table-cell>
   	     </fo:table-row>
        </fo:table-body>
        </fo:table>
     </fo:block-container>
   </xsl:template>

   <xsl:template name="adressat">
     <xsl:param name="vermerk"></xsl:param>
     <xsl:param name="fontsize" select='"11pt"'/>
   
	 <!--   laut DIN 5008 muss er 20mm vom link rand und 46 mm vom oberen Rand sein und maximal 85mm breit sein -->
     <fo:block-container height="5mm" width="82mm" top="46mm" left="20mm" position="fixed">
        <fo:block font-size="9pt" text-decoration="underline">Buchlese, Hauptstr. 12, 78713 Schramberg</fo:block>
     </fo:block-container>
		<!--   laut DIN 5008 muss er 20mm vom link rand und 51 mm vom oberen Rand sein und maximal 85mm breit sein -->
		<!--   laut deutscher Post muss ein padding von mindestens 3mm eingehalten werden. -->
		<!--     wir gehen von einer DIN-A4-Seite mit 1cm linken und 4cm oberen rand aus -->
		<!--     wir gehen weiterhin davon aus, dass der drucker einen rechten rand von ca. 1 mm einbaut. -->
        <!--     margin-left, damit die Adresse zum restlichen Text aligned wird -->
    <fo:block-container height="40mm" width="82mm" top="51mm" left="20mm" position="fixed" margin-left="5mm">
		   <!-- die ersten 2 Zeilen sind die Zusatz und Vermerkzone "Express" "persönlich" "nicht nachsenden" etc. -->
		   <!--   eigentlich sind es 3 zeilen, aber wir haben eine adresszeile mehr als der standard vorsieht -->
      <fo:block white-space-collapse="false" linefeed-treatment="preserve">
        <xsl:attribute name="font-size">
          <xsl:value-of select="$fontsize"/>
        </xsl:attribute>
        <xsl:text>&#xA;</xsl:text>
        <xsl:value-of select="$vermerk"/>
        <xsl:text>&#xA;</xsl:text>
      </fo:block>
   	   	   <!-- jetzt kommt die eigentliche Adresse, maximal 7 Zeilen (insgesamt dürfen es 9 zeilen sein) -->
      <xsl:call-template name="renderLetterAddress">
        <xsl:with-param name="address" select="."/>
        <xsl:with-param name="fontsize" select="$fontsize"/>
      </xsl:call-template>
    </fo:block-container>
   </xsl:template>


   <xsl:template name="briefkopf">
      <fo:block font-size="14pt" text-align="center" border-top-style="solid" border-bottom-style="solid" border-top-width="0.5mm"
         border-bottom-width="0.5mm" padding="3mm" span="all">
         Bild vom Briefkopf 
      </fo:block>
   </xsl:template>

   <xsl:template name="brieffuss">
      <fo:block font-size="8pt" text-align="right">
         Seite <fo:page-number/> von <fo:page-number-citation ref-id="end"/> 
      </fo:block>
      <fo:block font-size="14pt" text-align="center" border-top-style="solid" border-bottom-style="solid" border-top-width="0.5mm"
         border-bottom-width="0.5mm" padding="3mm" span="all">
         Bild vom Brieffuss 
      </fo:block>
   </xsl:template>

  <xsl:template name="renderLetterAddress">
    <xsl:param name="address"/>
    <xsl:param name="fontsize" select='"9pt"'/>

    <fo:block>
      <xsl:attribute name="font-size">
        <xsl:value-of select="$fontsize"/>
      </xsl:attribute>
      <xsl:value-of select="name1"/>
    </fo:block>
    <xsl:if test="name2!=''">
      <fo:block>
        <xsl:attribute name="font-size">
          <xsl:value-of select="$fontsize"/>
        </xsl:attribute>
        <xsl:value-of select="nam2"/>
      </fo:block>
    </xsl:if>
    <xsl:if test="name3!=''">
      <fo:block>
        <xsl:attribute name="font-size">
          <xsl:value-of select="$fontsize"/>
        </xsl:attribute>
        <xsl:value-of select="name3"/>
      </fo:block>
    </xsl:if>
    <fo:block>
      <xsl:attribute name="font-size">
          <xsl:value-of select="$fontsize"/>
        </xsl:attribute>
      <xsl:value-of select="street"/>
    </fo:block>
    <fo:block>
      <xsl:attribute name="font-size">
      <xsl:value-of select="$fontsize"/>
    </xsl:attribute>
      <xsl:value-of select="city"/>
    </fo:block>
  </xsl:template>

</xsl:stylesheet>