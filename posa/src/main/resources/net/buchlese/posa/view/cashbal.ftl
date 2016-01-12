<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>${posName?html}</title>

</head>
<body>
   <h1>Kassenabschluss vom ${localDate(balance.lastCovered)}</h1>
   
   <table>
   <tr><td valign="top" style="padding-right:5mm">
   <h3>Kassenvorgänge</h3>
   <table>
   	<tr>
   	  <td>Kassenanfangsbestand</td>	<td align="right" style="padding-left:10mm">${money(balance.cashStart)}</td>
   	</tr>
   	<tr>
   	  <td>Bareinnahmen</td>	<td align="right">${money(balance.paymentMethodBalance(paymentMethod.CASH))}</td>
   	</tr>
   	<tr>
   	  <td>Barbestandsdifferenz</td>	<td align="right">${money(balance.cashDiff)}</td>
   	</tr>
   	<tr>
   	  <td>Bareinzahlungen</td>	<td align="right">${money(balance.cashInSum)}</td>
   	</tr>
   	<tr>
   	  <td>Barauszahlungen</td>	<td align="right">${money(balance.cashOutSum)}</td>
   	</tr>
   	<tr>
   	  <td>Abschöpfung</td>	<td align="right">${money(balance.absorption)}</td>
   	</tr>
   	<tr>  
   	  <td>Kassenendbestand</td>	<td align="right">${money(balance.cashEnd)}</td>
   	</tr>
   </table>
   </td>
   <td valign="top">
   <h3>Zahlungsarten</h3>
   <table>
   	<tr>
   	  <td>Bar</td>	<td align="right" style="padding-left:10mm">${money(balance.paymentMethodBalance(paymentMethod.CASH))}</td>
   	</tr>
   	<tr>
   	  <td>Telecash</td>	<td align="right">${money(balance.paymentMethodBalance(paymentMethod.TELE))}</td>
   	</tr>
   	<tr>
   	  <td>Wertgutscheine (ang)</td> <td align="right">${money(-balance.couponTradeIn!0)}</td>
   	</tr>
   </table>
   </td></tr>
   </table>
   
   <h3>Umsatzsteuerverteilung der Gesamteinnahmen von ${money(balance.revenue!0 - balance.couponTradeIn!0)}</h3>
   <table>
   	<tr>
   	  <td>Voller Satz (19%)</td>	<td align="right" style="padding-left:10mm">${money(balance.taxBalance(tax.FULL))}</td>
   	</tr>
   	<tr>
   	  <td>Halber Satz (7%)</td>	<td align="right">${money(balance.taxBalance(tax.HALF))}</td>
   	</tr>
   	<tr>
   	  <td>Ohne Umsatzsteuer (0%)</td> <td align="right">${money(balance.taxBalance(tax.NONE))}</td>
   	</tr>
   	<#if balance.couponTradeOut??>
   	<#if balance.couponTradeOut gt 0>
   	<tr>
   	  <td>Verkaufte Gutscheine</td> <td align="right">${money(balance.couponTradeOut!0)}</td>
   	</tr>
   	</#if>
   	</#if>
   	<#if balance.payedInvoices??>
   <#assign invs = balance.payedInvoices?keys>
   <#list invs as inv>
   	<tr>
   	  <td>siehe ${inv}  </td> <td align="right" style="padding-left:10mm">${money(balance.payedInvoices[inv])}</td>
   	</tr>
   </#list>
   </#if>	  
   	  
   </table>
   
   <h3>Verteilung der Einnahmen auf Warengruppen</h3>
   <#if balance.articleGroupBalance??>
   <table>
   <#assign keys = balance.articleGroupBalance?keys>
   <#list keys as key>
   	<tr>
   	  <td>${key}</td> <td align="right" style="padding-left:10mm">${money(balance.articleGroupBalance[key])}</td>
   	</tr>
   </#list>	
   </table>
   </#if>	  
   
   <p style="font-size:small; margin-top:2cm">Erzeugt am ${balance.creationtime}</p>
</body>
