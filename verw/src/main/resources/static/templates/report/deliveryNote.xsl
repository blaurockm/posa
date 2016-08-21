<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
          xmlns:date="http://exslt.org/dates-and-times"
          extension-element-prefixes="date">
   <xsl:output method="xml" version="1.0" indent="yes" encoding="UTF-8" />
   <xsl:param name="report"/>
   <xsl:decimal-format name="euro" decimal-separator=',' grouping-separator='.' />
   <xsl:template match="/deliveryNote">
      <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
         <fo:layout-master-set>
            <fo:simple-page-master master-name="DIN-A4-mAdr" page-height="29.7cm" page-width="21cm" margin-left="2cm" margin-right="2.5cm" margin-top="0.5cm" margin-bottom="1cm">
               <fo:region-body margin-top="10.5cm" margin-bottom="1.8cm"/>
               <fo:region-before region-name="header-DIN-A4-mAdr" extent="10.3cm" />
               <fo:region-after region-name="footer-DIN-A4-mAdr" extent="1.5cm" />
            </fo:simple-page-master>
            <fo:simple-page-master master-name="DIN-A4-oAdr" page-height="29.7cm" page-width="21cm" margin-left="2cm" margin-right="2.5cm" margin-top="0.5cm" margin-bottom="1cm">
               <fo:region-body margin-top="1.5cm" margin-bottom="1.8cm" />
            </fo:simple-page-master>
            <fo:page-sequence-master master-name="DIN-A4" page-height="29.7cm" page-width="21cm" margin-left="2cm" margin-right="2.5cm" margin-top="0.5cm" margin-bottom="1cm">
               <fo:repeatable-page-master-alternatives>
  		  	   	 <fo:conditional-page-master-reference master-reference="DIN-A4-mAdr" page-position="first"/>
                 <fo:conditional-page-master-reference master-reference="DIN-A4-oAdr" page-position="rest"  />
                </fo:repeatable-page-master-alternatives>
            </fo:page-sequence-master>
         </fo:layout-master-set>
         <fo:page-sequence master-reference="DIN-A4">
            <fo:static-content flow-name="header-DIN-A4-mAdr">
               <xsl:call-template name="briefkopf" />
            </fo:static-content>
            <fo:static-content flow-name="footer-DIN-A4-mAdr">
               <xsl:call-template name="brieffuss" />
            </fo:static-content>

            <fo:flow flow-name="xsl-region-body">
               <xsl:apply-templates select="deliveryAddress" />

               <xsl:call-template name="infozeile" />
               <xsl:call-template name="positionen" />
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
	        <fo:block font-size="10pt" text-align="right"><xsl:value-of select="weight"/></fo:block>   	       
   	     </fo:table-cell>
   	  </fo:table-row>
   </xsl:template>

   <xsl:template name="positionen">
      <fo:table margin-top="5mm" table-layout="fixed" width="160mm">
         <fo:table-column column-width="1cm" />
         <fo:table-column column-width="10cm" />
         <fo:table-column column-width="20mm" />
         <fo:table-column column-width="20mm" />
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
	           <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm" text-align="right">Gewicht</fo:block>   	       
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
	  	  <fo:table-row border-bottom-style="solid" border-top-width="0.2mm" space-before="3mm">
	  	     <fo:table-cell>
	  	       <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm" id="end"></fo:block>
	  	     </fo:table-cell>
	  	   </fo:table-row>  
       </fo:table-body>
      </fo:table>
   </xsl:template>


   <xsl:template name="infozeile">
     <fo:block-container>
        <fo:block font-size="16pt" font-weight="bold">Lieferschein <xsl:value-of select="delivNum"/></fo:block>
        <fo:table margin-top="5mm" table-layout="fixed" width="12cm">
           <fo:table-column column-width="4cm" />
           <fo:table-column column-width="4cm" />
           <fo:table-column column-width="4cm" />
        <fo:table-body>
   	    <fo:table-row>
   	     <fo:table-cell>
   	       <fo:block>Kundennummer</fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block>Lieferdatum</fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block>Erfassung</fo:block>
   	     </fo:table-cell>
   	     </fo:table-row>
   	    <fo:table-row>
   	     <fo:table-cell>
   	       <fo:block><xsl:value-of select="customerId"/></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block><xsl:value-of select="date:format-date(deliveryDate, 'dd.MM.yyyy')" /></fo:block>
   	     </fo:table-cell>
   	     <fo:table-cell>
   	       <fo:block><xsl:value-of select="date:format-date(creationTime, 'dd.MM.yyyy')" /></fo:block>
   	     </fo:table-cell>
   	     </fo:table-row>
        </fo:table-body>
        </fo:table>
     </fo:block-container>
   </xsl:template>

   <xsl:template match="deliveryAddress">
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
     <fo:block>
        <fo:external-graphic content-width="170mm">
          <xsl:attribute name="src">
           <xsl:value-of select="concat('/xsl/images/header', pointId, '.jpg')" />
          </xsl:attribute>
        </fo:external-graphic>
     </fo:block>
   </xsl:template>

   <xsl:template name="brieffuss">
      <fo:block font-size="8pt" text-align="right">
         Seite <fo:page-number/> von <fo:page-number-citation ref-id="end"/> 
      </fo:block>
      <fo:block>
        <fo:external-graphic src="url(/xsl/images/footer.jpg)"  content-width="170mm"/>
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
        <xsl:value-of select="name2"/>
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
      <xsl:value-of select="postalcode"/>&#160;<xsl:value-of select="city"/>
    </fo:block>
  </xsl:template>

</xsl:stylesheet>