<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>Wochenübersicht</title>
<link rel="stylesheet" href="/assets/posa/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/assets/posa/blueprint/print.css" type="text/css" media="print"> 

</head>
<body>
<div class="container">
  <div class="span-16">
    <h3>Kassen-Synchronisation</h3>
	<form method="POST" action="/admin/tasks/synchronize" target="_blank">
		Von <input type="text" name="from" value="2015-01-04" /><br>
		Bis <input type="text" name="till" value="2015-01-20" /><br>
		Nur Homing  <input type="checkbox" name="sendHome" value="true" /> <br>
		<input type="submit" value="Jetzt synchronisieren!" /><br>
	</form>
	
	<p>
	<h3>Resync- Warteschlange</h3>
	  <ol>
	    <#list resyncQueue as sy>
	 	   <li>${sy}</li>
	 	 </#list>
	 	   </ol>
    </p>
	<p>
	<h3>Homing - Warteschlange</h3>
  	  <ol>
     <#list homingQueue as ho>
  	   <li>${ho}</li>
  	 </#list>
  	   </ol>
  	   
  	<p>
  	<h3>Nachrichten</h3>
    <ul>
     <#list problems as probl>
  	   <li>${probl}</li>
  	 </#list>
  	</ul>
  	
  </div>
  
  <div class="span-8 last">
	<h1>Pos-Adapter Version 1.0</h1>  
  
  	<h3>JVM-Daten</h3>
  	<table border=1>
  	  <tr><td>JVM</td><td>${jvmVersion}</td></tr>
  	  <tr><td>Memory</td><td>${jvmMemory}</td></tr>
  	</table>
  
  	<h3>Performance</h3>
  	<table border=1>
  		<tr><td>Mobileversion</td><td>${mobileViewAvg}</td></tr>
  		<tr><td>Desktopversion</td><td>${deskViewAvg}</td></tr>
  		<tr><td>Sync-Max</td><td>${syncTimeMax}</td></tr>
  		<tr><td>letzter Homing-Lauf</td><td>${lastHomingRun}</td></tr>
  		<tr><td>letzter Sync-Lauf</td><td>${lastSyncRun}</td></tr>
  		<tr><td>letzter Sync-Lauf mit DB-Verbindung</td><td>${lastSyncRunWithDbConnection}</td></tr>
  	</table>

  	<h3>Konfiguration</h3>
  	<table border=1>
  		<tr><td>Point-ID</td><td>${pointid}</td></tr>
  		<tr><td>POS-Datenbank</td><td>${dbconfigPosa}</td></tr>
  		<tr><td>BOFC-Datenbank</td><td>${dbconfigBofc}</td></tr>
  	</table>
  
  </div>
  
</div>
</body>
</html>
