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
   
   <table border="1">
   <thead>
   <tr>
     <th>Kassenbericht vom</th><th>Kassenanfang</th><th>Umsatz</th><th>Kassendiff</th><th>Kassenenendstand</th><th><span style="font-size:small">Anzeigen</span></th>
   </tr>
   </thead>
   <tbody>
   <#list export.balances as inv>
   	<tr>
   	  <td>${localDate(inv.lastCovered).toString("EEEE dd.MMM yyyy")}  </td> 
   	  <td align="right" style="padding-left:10mm">${money(inv.cashStart)}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.revenue)}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.cashDiff)}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.cashEnd)}</td>
   	  <td align="center"><a href="view/${inv.id}" target="_new">§</a>
   	  <a href="pdf/${inv.id}">#</a></td>
   	</tr>
   </#list>
   </tbody>	  
   </table>
   
   <hr>
   <p>
   
   <table border="1">
   <thead>
   <tr>
     <th>Rechnung Nr</th><th>Belegdatum</th><th>Kunde</th><th>Debitkto</th><th>Betrag</th>
   </tr>
   </thead>
   <tbody>
   <#list export.invoices as inv>
     <tr>
      <td>${inv.number}</td>
      <td><#if inv.date??>${inv.date.toString("dd.MM.yyyy")}<#else> kein Datum?</#if>  </td> 
      <td>${inv.name1} (${inv.customerId})</td>
      <td align="right">${inv.debitorId}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.amount)}</td>
	  <td align="center"><a href="/invoice/${inv.number}" target="_new">view</a></td>
	 </tr>   
   </#list>
   </tbody>
   </table>

   <hr>
   <p>
   <a href="/fibu/exportfile?key=${export.key}">Dateidownload</a>
   <p>
   <a href="javascript:drucken()">Protokoll drucken</a>
     
</body>
