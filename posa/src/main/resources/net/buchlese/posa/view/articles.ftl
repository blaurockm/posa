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
   
   <div class="container">

   <h2>Artikel</h2>
   
   <table>
   <thead>
   <tr>
     <th>Artikelident</th><th>letztes Verkaufsdatum</th><th>Bezeichnung</th><th>VK-Brutto</th>
   </tr>
   </thead>
   <tbody>
   <#list articles as art>
     <tr>
      <td>${art.ident}</td>
      <td><#if inv.date??>${inv.lastSellingDate.toString("dd.MM.yyyy")}<#else> kein Datum?</#if>  </td> 
      <td>${art.bezeichnung}</td>
   	  <td align="right" style="padding-left:10mm">${money(inv.sellingPrice)}</td>
	 </tr>   
   </#list>
   </tbody>
   </table>

  </div>
</body>
</html>
