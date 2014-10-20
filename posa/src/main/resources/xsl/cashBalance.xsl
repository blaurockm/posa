<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
          xmlns:date="http://exslt.org/dates-and-times"
          extension-element-prefixes="date">
   <xsl:output method="xml" version="1.0" indent="yes" encoding="UTF-8" />
   <xsl:decimal-format name="euro" decimal-separator=',' grouping-separator='.' />
   <xsl:template match="/CashBalance">
      <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
         <fo:layout-master-set>
            <fo:simple-page-master master-name="DIN-A4" page-height="29.7cm" page-width="21cm" margin-left="2cm"
               margin-right="2.5cm" margin-top="0.5cm" margin-bottom="1cm">
               <fo:region-body margin-top="1.5cm" margin-bottom="1.8cm" column-count="2" />
               <fo:region-before region-name="header" extent="1.3cm" />
               <fo:region-after region-name="footer" extent="1.5cm" />
            </fo:simple-page-master>
         </fo:layout-master-set>
         <fo:page-sequence master-reference="DIN-A4">
            <!-- <fo:static-content flow-name="header"> </fo:static-content> -->
            <fo:static-content flow-name="header">
               <xsl:call-template name="ueberschrift" />
            </fo:static-content>
            <fo:static-content flow-name="footer">
               <fo:block font-size="8pt" text-align="center" border-top-style="solid" border-top-width="0.5mm" padding="3mm" span="all">
                  erzeugt am
                  <xsl:value-of select="date:format-date(creationtime, 'dd.MM.yyyy hh:mm')" />
               </fo:block>
            </fo:static-content>

            <fo:flow flow-name="xsl-region-body">
               <xsl:call-template name="einnahmen" />

               <xsl:call-template name="zahlungsarten" />
               <xsl:call-template name="warengruppen" />
               <xsl:call-template name="gutscheine" />
               <fo:block break-before="column" />
               <xsl:call-template name="steuersaetze" />
               <xsl:call-template name="sonstige" />
               <xsl:call-template name="offeneposten" />


               <xsl:call-template name="statistik" />
            </fo:flow>
         </fo:page-sequence>
      </fo:root>
   </xsl:template>

   <xsl:template name="ueberschrift">
      <fo:block font-size="14pt" text-align="center" border-top-style="solid" border-bottom-style="solid" border-top-width="0.5mm"
         border-bottom-width="0.5mm" padding="3mm" span="all">
         Kassenbericht ... vom
         <xsl:value-of select="date:format-date(lastCovered, 'dd.MM.yyyy')" />
      </fo:block>
   </xsl:template>

   <xsl:template name="einnahmen">
      <fo:block font-size="10pt" span="all" padding="5mm" border-bottom-style="solid" border-bottom-width="0.1mm">
         <fo:inline font-size="14pt" padding="1mm">
            Einnahmen gesamt
            <xsl:value-of select="format-number(revenue div 100, '###.###,00 EUR', 'euro')" />
         </fo:inline>

         <fo:table margin-top="1mm" margin-left="3mm">
            <fo:table-column column-width="8cm" />
            <fo:table-column column-width="3cm" />
            <fo:table-body>
               <fo:table-row>
                  <fo:table-cell>
                     <fo:block>Kassen-Anfangsbestand</fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
                     <fo:block text-align="right">
                        <xsl:value-of select="format-number(cashStart div 100, '###.###,00 EUR', 'euro')" />
                     </fo:block>
                  </fo:table-cell>
               </fo:table-row>
               <fo:table-row>
                  <fo:table-cell>
                     <fo:block>Kassen-Endbestand</fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
                     <fo:block text-align="right">
                        <xsl:value-of select="format-number(cashEnd div 100, '###.###,00 EUR', 'euro')" />
                     </fo:block>
                  </fo:table-cell>
               </fo:table-row>
               <fo:table-row>
                  <fo:table-cell>
                     <fo:block>Abschöpfung</fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
                     <fo:block text-align="right">
                        <xsl:value-of select="format-number(absorption div 100, '###.###,00 EUR', 'euro')" />
                     </fo:block>
                  </fo:table-cell>
               </fo:table-row>
               <fo:table-row>
                  <fo:table-cell>
                     <fo:block>Warenverkauf</fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
                     <fo:block text-align="right">
                        <xsl:value-of select="format-number(goodsOut div 100, '###.###,00 EUR', 'euro')" />
                     </fo:block>
                  </fo:table-cell>
               </fo:table-row>
               <fo:table-row>
                  <fo:table-cell>
                     <fo:block>Gutscheinsaldo</fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
                     <fo:block text-align="right">
                        <xsl:value-of select="format-number((couponTradeIn - couponTradeOut) div 100, '###.###,00 EUR', 'euro')" />
                     </fo:block>
                  </fo:table-cell>
               </fo:table-row>
            </fo:table-body>
         </fo:table>
      </fo:block>
   </xsl:template>

   <xsl:template name="zahlungsarten">
      <fo:block font-size="10pt" border-right-width="0.1mm" border-right-style="solid" padding="3mm">
         <fo:inline font-size="12pt">Zahlungsarten</fo:inline>
         <xsl:call-template name="tabelle">
            <xsl:with-param name="tablenode" select="paymentMethodBalance" />
         </xsl:call-template>
      </fo:block>
   </xsl:template>

   <xsl:template name="warengruppen">
      <fo:block font-size="10pt" padding="3mm" border-right-width="0.1mm" border-right-style="solid" border-top-width="0.1mm"
         border-top-style="solid">
         <fo:inline font-size="12pt">Warengruppen</fo:inline>
         <xsl:call-template name="tabelle">
            <xsl:with-param name="tablenode" select="articleGroupBalance" />
         </xsl:call-template>
      </fo:block>
   </xsl:template>

   <xsl:template name="gutscheine">
      <fo:block font-size="10pt" padding="3mm" border-right-width="0.1mm" border-right-style="solid" border-top-width="0.1mm"
         border-top-style="solid">
         <fo:inline font-size="12pt">Gutscheine eingelöst</fo:inline>
         <xsl:call-template name="tabelle">
            <xsl:with-param name="tablenode" select="oldCoupon" />
         </xsl:call-template>
         <fo:inline font-size="12pt">Gutscheine verkauft</fo:inline>
         <xsl:call-template name="tabelle">
            <xsl:with-param name="tablenode" select="newCoupon" />
         </xsl:call-template>
      </fo:block>
   </xsl:template>

   <xsl:template name="offeneposten">
      <fo:block font-size="10pt" padding="3mm" border-top-width="0.1mm" border-top-style="solid">
         <fo:inline font-size="12pt">Ausgleich von Offenen Posten</fo:inline>
         <xsl:call-template name="tabelle">
            <xsl:with-param name="tablenode" select="payedInvoices" />
         </xsl:call-template>
         <fo:inline font-size="12pt">Neue Offene Posten</fo:inline>
         <xsl:call-template name="tabelle">
            <xsl:with-param name="tablenode" select="createdInvoices" />
         </xsl:call-template>
      </fo:block>
   </xsl:template>

   <xsl:template name="sonstige">
      <fo:block font-size="10pt" padding="3mm" border-top-width="0.1mm" border-top-style="solid">
         <fo:inline font-size="12pt">Sonstige Einnahmen</fo:inline>
         <xsl:call-template name="tabelle">
            <xsl:with-param name="tablenode" select="cashIn" />
         </xsl:call-template>
         <fo:inline font-size="12pt">Sonstige Ausgaben</fo:inline>
         <xsl:call-template name="tabelle">
            <xsl:with-param name="tablenode" select="cashOut" />
         </xsl:call-template>
      </fo:block>
   </xsl:template>

   <xsl:template name="steuersaetze">
      <fo:block font-size="10pt" padding="3mm">
         <fo:inline font-size="12pt">Steuersätze</fo:inline>
         <xsl:call-template name="tabelle">
            <xsl:with-param name="tablenode" select="taxBalance" />
         </xsl:call-template>
      </fo:block>
   </xsl:template>

   <xsl:template name="statistik">
      <fo:block font-size="10pt" padding="3mm" border-top-style="solid" border-top-width="0.1mm" span="all">
         Erster Beleg
         <xsl:value-of select="date:format-date(firstTimestamp, 'hh:mm')" />
         , Letzer Beleg
         <xsl:value-of select="date:format-date(lastTimestamp, 'hh:mm')" />
         , Anzahl Belege
         <xsl:value-of select="ticketCount" />
      </fo:block>
   </xsl:template>


   <xsl:template name="tabelle">
      <xsl:param name="tablenode" />
      <xsl:choose>
         <xsl:when test="count($tablenode/entry) > 0">
            <fo:table margin-left="3mm" margin-top="1mm">
               <fo:table-column column-width="4.5cm" />
               <fo:table-column column-width="2.5cm" />
               <fo:table-body>
                  <xsl:for-each select="$tablenode/entry">
                     <fo:table-row>
                        <fo:table-cell>
                           <fo:block>
                              <xsl:value-of select="key" />
                           </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                           <fo:block text-align="right">
                              <xsl:value-of select="format-number(value div 100, '###.###,00 EUR', 'euro')" />  
                           </fo:block>
                        </fo:table-cell>
                     </fo:table-row>
                  </xsl:for-each>
               </fo:table-body>
            </fo:table>
         </xsl:when>
         <xsl:otherwise>
            --
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>

</xsl:stylesheet>