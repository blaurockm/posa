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

<link rel="icon"       type="image/x-icon" href="/assets/posa/images/favicon.ico">
</head>
<body  class="code-snippets-visible">

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
   
   <div class="docs-section">
      <h6 class="docs-header">Resync-Warteschlange</h6>
      <div class="docs-example">
	  <ol>
	    <#list resyncQueue as sy>
	 	   <li>${sy}</li>
	 	 </#list>
	  </ol>
	  </div>
   </div>
	  
   <div class="docs-section">
      <h6 class="docs-header">Command-Warteschlange</h6>
	  <ol>
	    <#list commandQueue as sy>
	 	   <li>${sy}</li>
	 	 </#list>
	  </ol>
   </div>

   <div class="docs-section">
      <h6 class="docs-header">Homing-Warteschlange</h6>
	  <ol>
	    <#list homingQueue as ho>
	 	   <li>${ho}</li>
	 	 </#list>
	  </ol>
   </div>

   <div class="docs-section">
      <h6 class="docs-header">Nachrichten</h6>
	  <ol>
	    <#list problems as probl>
	 	   <li>${probl}</li>
	 	 </#list>
	  </ol>
   </div>

   <div class="docs-section">
      <h6 class="docs-header">JVM-Daten</h6>
 	  <table>
 	    <tbody>
  	    <tr><td>JVM</td><td>${jvmVersion}</td></tr>
  	    <tr><td>Memory</td><td>${jvmMemory}</td></tr>
  	    </tbody>
  	  </table>
   </div>

   <div class="docs-section">
      <h6 class="docs-header">Performance</h6>
 	  <table>
 	    <tbody>
  		<tr><td>Sync-Max</td><td>${syncTimeMax}</td></tr>
  		<tr><td>letzter Homing-Lauf</td><td>${lastHomingRun}</td></tr>
  		<tr><td>letzter Sync-Lauf</td><td>${lastSyncRun}</td></tr>
   		<tr><td>letzter Sync-Lauf mit DB-Verbindung</td><td>${lastSyncRunWithDbConnection}</td></tr>
        <tr><td>letzter Command-Get-Lauf</td><td>${lastCommandRun}</td></tr>
  	    </tbody>
  	  </table>
   </div>

   <div class="docs-section">
      <h6 class="docs-header">Konfiguration</h6>
 	  <table>
 	    <tbody>
  		<tr><td>Point-ID</td><td>${pointid}</td></tr>
  		<tr><td>POS-Datenbank</td><td>${dbconfigPosa}</td></tr>
  		<tr><td>BOFC-Datenbank</td><td>${dbconfigBofc}</td></tr>
  		<tr><td>Days Back</td><td>${config.daysBack}</td></tr>
  	    </tbody>
  	  </table>
   </div>

</body>
</html>
