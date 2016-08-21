<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
          xmlns:date="http://exslt.org/dates-and-times"
          extension-element-prefixes="date">
   <xsl:output method="xml" version="1.0" indent="yes" encoding="UTF-8" />
   <xsl:param name="report"/>
   <xsl:decimal-format name="euro" decimal-separator=',' grouping-separator='.' />
   <xsl:template match="/deliveryProtocol">
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
  		  	   	 <fo:conditional-page-master-reference master-reference="DIN-A4-oAdr" page-position="first"/>
                 <fo:conditional-page-master-reference master-reference="DIN-A4-oAdr" page-position="rest"  />
                </fo:repeatable-page-master-alternatives>
            </fo:page-sequence-master>
         </fo:layout-master-set>
         <fo:page-sequence master-reference="DIN-A4">
            <!-- <fo:static-content flow-name="header"> </fo:static-content> -->
            <fo:static-content flow-name="header">
               <xsl:call-template name="header" />
            </fo:static-content>
            <fo:static-content flow-name="footer">
               <xsl:call-template name="footer" />
            </fo:static-content>

            <fo:flow flow-name="xsl-region-body">
               
               <fo:table>
		         <fo:table-column column-width="40mm" />
		         <fo:table-column column-width="40mm" />
		         <fo:table-column column-width="40mm" />
		         <fo:table-column column-width="40mm" />
       			 <fo:table-header>
       		 		<fo:table-row border-top-style="solid" border-bottom-style="solid" border-top-width="0.2mm" border-bottom-width="0.2mm" space-after="3mm">
			 	     <fo:table-cell>
			 	       <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm">Artikel</fo:block>
			 	     </fo:table-cell>
			 	     <fo:table-cell>
			 	       <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm">Abonnent</fo:block>
			 	     </fo:table-cell>
			 	     <fo:table-cell>
			 	       <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm">Adresse</fo:block>
			 	     </fo:table-cell>
			 	     <fo:table-cell>
			 	       <fo:block font-size="10pt" margin-top="2mm" margin-bottom="2mm">Bemerkungen</fo:block>
			 	     </fo:table-cell>
       			 	</fo:table-row>
       			 </fo:table-header>
			      <fo:table-body>
			        <xsl:apply-templates select="details"/>
				  	  <fo:table-row border-top-style="solid" border-bottom-style="solid" border-top-width="0.2mm" border-bottom-width="0.2mm" space-before="3mm">
				  	     <fo:table-cell>
				        <fo:block font-size="10pt"></fo:block>   	       
				  	     </fo:table-cell>
				  	     <fo:table-cell>
				         <fo:block font-size="10pt" text-align="right">Ende der </fo:block>   	       
				  	     </fo:table-cell>
				  	     <fo:table-cell>
				        <fo:block font-size="10pt" text-align="left"> Lieferungen
				        </fo:block>   	       
				  	     </fo:table-cell>
				  	     <fo:table-cell>
				        <fo:block font-size="10pt" id="end">
				        </fo:block>   	       
				  	     </fo:table-cell>
				  	  </fo:table-row>
			      </fo:table-body>
               </fo:table>
            </fo:flow>
         </fo:page-sequence>
      </fo:root>
   </xsl:template>

   <xsl:template match="details">
   	  <fo:table-row>
   	     <fo:table-cell padding-top="2mm" padding-bottom="2mm">
	        <fo:block font-size="10pt"><xsl:value-of select="article/name"/></fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell padding-top="2mm" padding-bottom="2mm">
	        <fo:block font-size="10pt"><xsl:value-of select="subscriber/name"/></fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell padding-top="2mm" padding-bottom="2mm">
	        <fo:block font-size="10pt">
	              <xsl:value-of select="subscription/deliveryInfo1"/>
	              <xsl:value-of select="subscription/deliveryInfo2"/>
	        </fo:block>      
	        <fo:block font-size="9pt">
	              <xsl:apply-templates select="deliveryAddress"/>
	        </fo:block>   	       
   	     </fo:table-cell>
   	     <fo:table-cell padding-top="2mm" padding-bottom="2mm">
	        <fo:block font-size="10pt" text-align="right">
	          <xsl:if test="needsDeliveryNote = 'true'">
	             Lieferschein!
	          </xsl:if>
	          <xsl:if test="needsInvoice = 'true'">
	             Rechnung!
	          </xsl:if>
	        </fo:block>   	       
   	     </fo:table-cell>
   	  </fo:table-row>
   </xsl:template>


   <xsl:template name="header">
      <fo:block font-size="11pt" text-align="center" border-top-style="solid" border-bottom-style="solid" border-top-width="0.5mm"
         border-bottom-width="0.5mm" padding="1mm" span="all">
                Lieferungen am <xsl:value-of select="protocolDate" />

      </fo:block>
   </xsl:template>

   <xsl:template name="footer">
      <fo:block font-size="8pt" text-align="right">
         Seite <fo:page-number/> von <fo:page-number-citation ref-id="end"/> 
      </fo:block>
      <fo:block font-size="11pt" text-align="center" border-top-style="solid" border-bottom-style="solid" border-top-width="0.5mm"
         border-bottom-width="0.5mm" padding="1mm" span="all">
                Lieferungen am <xsl:value-of select="protocolDate" />
      </fo:block>
   </xsl:template>

  <xsl:template match="deliveryAddress">
    <xsl:param name="fontsize" select='"8pt"'/>

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