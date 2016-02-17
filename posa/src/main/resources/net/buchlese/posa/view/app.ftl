<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=iso8859-1">
<title>${posName?html}</title>
</head>
<body>
   <h1>Kasse ${pointId} : ${posName?html}</h1>
   
   <table border="1">
   <thead>
   <tr>
     <th>Kassenbericht vom</th><th>Kassenanfang</th><th>Umsatz</th><th>Kassendiff</th><th>Kassenenendstand</th><th><span style="font-size:small">Actions</span></th>
   </tr>
   </thead>
   <tbody>
   <#list fortnight as day>
   <#if balances[day]??>
   <#assign inv = balances[day]> 
   	<tr>
   	  <td>${day.toString("EEEE dd.MMM yyyy")}  </td> 
   	  <td align="right" style="padding-left:10mm">${money(inv.cashStart)}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.revenue)}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.cashDiff)}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.cashEnd)}</td>
   	  <td align="center"><a href="/cashbalance/view/${day.toString("yyyyMMdd")}" target="_new">view</a>
   	  <a href="/cashbalance/sendbofc/${day.toString("yyyyMMdd")}" target="_new">send</a>
   	  <a href="/cashbalance/resync/${day.toString("yyyyMMdd")}" target="_new">resync</a>
   	  </td>
   	</tr>
   <#else>
     <#if isWorkday(day) >
	  	<tr>
	  	  <td>${day.toString("EEEE dd.MMM yyyy")}  </td>
	  	  <td></td>
	  	  <td></td>
	  	  <td></td>
	  	  <td></td>
	  	  <td><a href="/cashbalance/resync/${day.toString("yyyyMMdd")}" target="_new">resync</a>
	  	  </td>
	  	</tr>   
   	  <#else>
	  	<tr style="background:#FF8080">
	  	  <td>${day.toString("EEEE dd.MMM yyyy")}  </td>
	  	  <td></td>
	  	  <td></td>
	  	  <td></td>
	  	  <td></td>
	  	  <td></td>
	  	</tr>   
   	  </#if>
   </#if>
   </#list>
   </tbody>	  
   </table>

   <p>Rechnungen</p>
   
   <table border="1">
   <thead>
   <tr>
     <th>Rechnung Nr</th><th>Belegdatum</th><th>Kunde</th><th>Betrag</th>
   </tr>
   </thead>
   <tbody>
   <#list invoices as inv>
     <tr>
      <td>${inv.number}</td>
      <td><#if inv.date??>${inv.date.toString("dd.MM.yyyy")}<#else> kein Datum?</#if>  </td> 
      <td>${inv.name1}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.amount)}</td>
	 </tr>   
   </#list>
   </tbody>
   </table>

</body>
</html>
