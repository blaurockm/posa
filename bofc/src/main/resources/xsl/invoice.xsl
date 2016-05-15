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
            <fo:simple-page-master master-name="DIN-A4" page-height="29.7cm" page-width="21cm" margin-left="2cm"
               margin-right="2.5cm" margin-top="0.5cm" margin-bottom="1cm">
               <fo:region-body margin-top="10.5cm" margin-bottom="1.8cm" column-count="2" />
               <fo:region-before region-name="header" extent="1.3cm" />
               <fo:region-after region-name="footer" extent="1.5cm" />
            </fo:simple-page-master>
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
	        <fo:block><xsl:value-of select="text"/></fo:block>   	       
   	     </fo:table-cell>
   	  </fo:table-row>
   </xsl:template>

   <xsl:template match="details[textonly='false']">
   	  <fo:table-row>
   	     <fo:table-cell>
   	       <fo:block></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block><xsl:value-of select="text"/></fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block><xsl:value-of select="format-number(singlePrice div 100, '###.###,00 €', 'euro')"/></fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block><xsl:value-of select="quantity"/> 7% </fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block><xsl:value-of select="format-number(amountHalf div 100, '###.###,00 €', 'euro')"/></fo:block>   	       
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
	        <fo:block> 19% </fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell>
	        <fo:block><xsl:value-of select="format-number(amountFull div 100, '###.###,00 €', 'euro')"/></fo:block>   	       
   	     </fo:table-cell>
   	  </fo:table-row>
   	  </xsl:if>
   </xsl:template>

   <xsl:template name="rechnungspositionen">
      <fo:table margin-top="1mm" margin-left="3mm" table-layout="fixed" width="11cm">
         <fo:table-column column-width="1cm" />
         <fo:table-column column-width="7cm" />
         <fo:table-column column-width="3cm" />
         <fo:table-column column-width="1cm" />
         <fo:table-column column-width="3cm" />
       <fo:table-body>
         <xsl:apply-templates select="details"/>
       </fo:table-body>
      </fo:table>
   </xsl:template>

   <xsl:template name="endbetrag">
   </xsl:template>

   <xsl:template name="rechnungsinfozeile">
     <fo:block-container height="25mm" width="152mm" top="106mm" left="20mm">
        <fo:block>Rechnung <xsl:value-of select="number"/></fo:block>
     </fo:block-container>
   </xsl:template>

   <xsl:template name="adressat">
     <xsl:param name="vermerk"></xsl:param>
     <xsl:param name="fontsize" select='"9pt"'/>
   
	 <!--   laut DIN 5008 muss er 20mm vom link rand und 46 mm vom oberen Rand sein und maximal 85mm breit sein -->
     <fo:block-container height="5mm" width="82mm" top="46mm" left="20mm" position="fixed">
        <fo:block font-size="7pt">Buchlese, Hauptstr. 12, 78713 Schramberg</fo:block>
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
      <fo:block font-size="8pt" text-align="center" border-top-style="solid" border-top-width="0.5mm" padding="3mm" span="all">
         erzeugt am
         <xsl:value-of select="date:format-date(creationTime, 'dd.MM.yyyy hh:mm')" />
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