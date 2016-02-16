<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=iso8859-1">
<title>${export.description?html}</title>

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

   <p>
   <a href="/cashbalance/fibuexportfile?key=${export.key}">Dateidownload</a>
     
</body>
