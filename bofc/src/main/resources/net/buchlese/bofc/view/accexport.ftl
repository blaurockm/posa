<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=iso8859-1">
<title>${export.description?html}</title>

</head>
<body>
   <h1>${export.description?html} Nr ${export.key} vom ${export.execDate}</h1>
   
   <p>Erster Kassenbericht vom ${export.from}
   <br>Letzter Kassenbericht vom ${export.till}
   <br>Kassenkonto ${account}
   
   <table border="1">
   <thead>
   <tr>
     <th>Kassenbericht vom</th><th>Einnahmen</th><th>Ausgaben</th><th>Kassenenendstand</th>
   </tr>
   </thead>
   <tbody>
   <#list export.balances as inv>
   	<tr>
   	  <td>${localDate(inv.lastCovered).toString("EE dd.MMM yyyy")}  </td> 
   	  <td align="right" style="padding-left:10mm">${money(inv.revenue - inv.couponTradeIn)}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.couponTradeIn + inv.cashOutSum)}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.cashEnd)}</td>
   	</tr>
   </#list>
   </tbody>	  
   </table>
     
</body>
