      
   <table border="1">
   <thead>
   <tr>
     <th>Kassen</th><th>Kunde</th><th>Name</th><th>Debitkto</th><th>Anzahl</th><th></th>
   </tr>
   </thead>
   <tbody>
   <#list mappings as ma>
     <tr>
      <td>${ma.pointid}</td>
      <td>${ma.customerId?string.computer}</td>
      <td>${ma.name1}</td>
      <td align="right">${ma.debitorId}</td>
   	  <td align="right" style="padding-left:10mm">${ma.count}</td>
   	  <td>
   	  <form action="/mapping/update" method="post" target="_blank">
   	     <input type="hidden" name="point" value="${ma.pointid?string.computer}"/>
   	     <input type="hidden" name="cust" value="${ma.customerId?string.computer}"/>
   	     <input type="text" name="deb" value=""/>
   	     <input type="submit" value="upd"/>
   	  </form>
   	  <td>
	 </tr>   
   </#list>
   </tbody>
   </table>
