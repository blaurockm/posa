<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
   <xsl:output method="xml" version="1.0" indent="yes" encoding="UTF-8" />
   <xsl:template match="/">
      <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
         <fo:layout-master-set>
            <fo:simple-page-master master-name="DIN-A4" page-height="29.7cm" page-width="21cm" margin-top="2cm"
               margin-bottom="2cm" margin-left="2.5cm" margin-right="2.5cm">
               <fo:region-body margin-top="1.5cm" margin-bottom="1.8cm" margin-left="2cm" margin-right="2.5cm" />
               <fo:region-before region-name="header" extent="1.3cm" />
               <fo:region-after region-name="footer" extent="1.5cm" />
               <fo:region-start region-name="left" extent="1cm" />
               <fo:region-end region-name="right" extent="2cm" />
            </fo:simple-page-master>
         </fo:layout-master-set>
         <fo:page-sequence master-reference="DIN-A4">
            <fo:static-content flow-name="header">
               <fo:block font-size="14pt" text-align="center">
                  Kassenabschluss
               </fo:block>
            </fo:static-content>
            <fo:static-content flow-name="footer">
               <fo:block text-align="center">
                  Seite
                  <fo:page-number />
                  von
                  <fo:page-number-citation ref-id="LastPage" />
               </fo:block>
            </fo:static-content>
            <fo:flow flow-name="xsl-region-body">
               <xsl:apply-templates />
               <fo:block id="LastPage" />
            </fo:flow>
         </fo:page-sequence>
      </fo:root>
   </xsl:template>
   
   <xsl:template match="abschlussId">
      <fo:block><xsl:value-of select="."/></fo:block>
   </xsl:template>
   
   
</xsl:stylesheet>