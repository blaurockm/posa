<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=iso8859-1">
<title>${posName?html}</title>
<link rel="stylesheet" type="text/css" href="/assets/posa/css/normalize.css"> 
<link rel="stylesheet" type="text/css" href="/assets/posa/css/skeleton.css">
<link rel="stylesheet" href="/assets/posa/css/custom.css">

<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
<link rel="stylesheet" href="/assets/posa/css/github-prettify-theme.css">
<script src="/assets/posa/js/site.js"></script>

<link rel="icon"       type="image/x-icon" href="/assets/posa/favicon.ico">
</head>
<body>

   <div class="container">
       <h2 class="title">Kasse ${pointId} : ${posName?html}</h2>
   </div>
   
   <div class="navbar-spacer"></div>
    <nav class="navbar">
      <div class="container">
        <ul class="navbar-list">
          <li class="navbar-item"><a class="navbar-link" href="index">Status</a></li>
          <li class="navbar-item">
            <a class="navbar-link" href="#" data-popover="#codeNavPopover">Daten</a>
            <div id="codeNavPopover" class="popover">
              <ul class="popover-list">
                <li class="popover-item">
                  <a class="popover-link" href="data">Kassenberichte</a>
                </li>
                <li class="popover-item">
                  <a class="popover-link" href="data">Rechnungen</a>
                </li>
                <li class="popover-item">
                  <a class="popover-link" href="data">Lieferscheine</a>
                </li>
                <li class="popover-item">
                  <a class="popover-link" href="data">Artikel</a>
                </li>
              </ul>
            </div>
          </li>
          <li class="navbar-item"><a class="navbar-link" href="logfiles">Logfiles</a></li>
        </ul>
      </div>
    </nav>
   <div class="navbar-spacer"></div>
   
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
   	  <a href="/cashbalance/sendbof/${day.toString("yyyyMMdd")}" target="_new">send</a>
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
	  <td align="center"><a href="/invoice/${inv.number}" target="_new">view</a>
   	  <a href="/invoice/sendbof/${inv.number}" target="_new">send</a>
	 </tr>   
   </#list>
   </tbody>
   </table>

   <p>Lieferscheine</p>
   
   <table border="1">
   <thead>
   <tr>
     <th>Lieferschein Nr</th><th>Belegdatum</th><th>Kunde</th><th>Betrag</th>
   </tr>
   </thead>
   <tbody>
   <#list issueSlips as inv>
     <tr>
      <td>${inv.number}</td>
      <td><#if inv.date??>${inv.date.toString("dd.MM.yyyy")}<#else> kein Datum?</#if>  </td> 
      <td>${inv.name1}</td>
      <td align="right" style="padding-left:10mm">${money(inv.amount)}</td>
    <td align="center"><a href="/invoice/slip/${inv.number}" target="_new">view</a>
      <a href="/invoice/slip/sendbof/${inv.number}" target="_new">send</a>
   </tr>   
   </#list>
   </tbody>
   </table>

</body>
</html>
