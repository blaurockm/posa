<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>${export.description?html}</title>

<script>
function drucken() {
	window.print();
}
</script>
</head>
<body>
   
   <h1>${export.description?html}  vom ${export.execDate.toString("dd.MM.")}</h1>
   
   <p>Export ID-Nr ${export.key}
   <br>Erster Kassenbericht vom ${export.from.toString("EEEE dd.MM.yyyy")}
   <br>Letzter Kassenbericht vom ${export.till.toString("EEEE dd.MM.yyyy")}
   <br>Kassenkonto ${account}

   <hr>
   Kassenanfangsbestand ${money(cashStart)} Kassenendbestand ${money(cashEnd)}
   <br>Kasseneinzahlungen ${money(cashInSum)} Kassenauszahlungen ${money(cashOutSum)}
   <br>Bar bezahlte Rechnungen ${money(invoicesPayedSum)} : 
   <#list payedInvoices as inv>
      ${inv}
   </#list>
   <br>Gutscheine eingelöst ${money(couponsInSum)} ausgegeben ${money(couponsOutSum)}
   <hr>
   
   <table border="1">
   <thead>
   <tr>
     <th>Kassenbericht vom</th><th>Kassenanfang</th><th>Umsatz</th><th>Abschöpfung</th><th>Tele</th><th>Kassendiff</th><th>Kassenenendstand</th><th><span style="font-size:small">Anzeigen</span></th>
   </tr>
   </thead>
   <tbody>
   <#list export.balances as bal>
   	<tr>
   	  <td>${localDate(bal.lastCovered).toString("EEEE dd.MMM yyyy")}  </td> 
   	  <td align="right" style="padding-left:10mm">${money(bal.cashStart)}</td>
   	  <td align="right" style="padding-left:10mm">${money(bal.revenue)}</td>
   	  <td align="right" style="padding-left:6mm">${money(bal.absorption)}</td>
   	  <td align="right" style="padding-left:6mm">${money(getTelecashForBalance(bal))}</td>
   	  <td align="right" style="padding-left:10mm">${money(bal.cashDiff)}</td>
   	  <td align="right" style="padding-left:10mm">${money(bal.cashEnd)}</td>
   	  <td align="center"><a href="/cashbalance/view/${bal.id?c}" target="_new">v</a>
   	  <a href="/cashbalance/complete/${bal.id?c}" target="_new">c</a></td>
   	</tr>
   </#list>
   
   <tr>
     <td><b>Summe</b></td>
     <td></td>
     <td align="right" style="padding-left:10mm"><b>${money(revenueSum)}</b></td>
     <td align="right" style="padding-left:6mm"><b>${money(absorptionSum)}</b></td>
     <td align="right" style="padding-left:6mm"><b>${money(telecashSum)}</b></td>
     <td></td>
     <td></td>
   </tr>
   
   </tbody>	  
   </table>
   
   <hr>
   <p>
   
   <table border="1">
   <thead>
   <tr>
     <th>Rechnung Nr</th><th>Belegdatum</th><th>Kunde</th><th>Debitkto</th><th>bezahlt?</th><th>Betrag</th>
   </tr>
   </thead>
   <tbody>
   <#list export.invoices as inv>
     <tr>
      <td>${inv.number}</td>
      <td><#if inv.date??>${inv.date.toString("dd.MM.yyyy")}<#else> kein Datum?</#if>  </td> 
      <td>${inv.name1} (${inv.customerId})</td>
      <td align="right">${inv.debitorId}</td>
      <td align="right">${inv.payed?string("ja", "")}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.amount)}</td>
	  <td align="center"><a href="/invoice/${inv.number}" target="_new">v</a></td>
	 </tr>   
   </#list>
   <tr>
     <td><b>Summe</b></td>
     <td></td>
     <td></td>
     <td></td>
     <td></td>
     <td align="right" style="padding-left:10mm"><b>${money(invoicesSum)}</b></td>
   </tr>
   </tbody>
   </table>

   <hr>
   <p>
   <a href="/fibu/exportfile?key=${export.key}">Dateidownload</a>
   <p>
   <a href="javascript:drucken()">Protokoll drucken</a>
     
</body>
