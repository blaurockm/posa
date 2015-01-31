<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>Buchlese</title>
<link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/dojo/1.10.1/dijit/themes/claro/document.css" />
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/dojo/1.10.1/dijit/themes/claro/claro.css" />
<link rel="stylesheet" href="/assets/posa/app.css" />
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/dojo/1.10.1/dojox/grid/resources/Grid.css" >
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/dojo/1.10.1/dojox/grid/resources/claroGrid.css" >

</head>
<body class="claro">
	<!-- basic preloader: 
	<div id="loader">
		<div id="loaderInner" style="direction: ltr;">Loading ...</div>
	</div>
	<script type="dojo/require">
dom: "dojo/dom",
registry: "dijit/registry"
</script> -->
	<div id="main" data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="liveSplitters:false, design:'headline'">
		<div id="header" data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'top'">
		  <h1>Buchlese PoS-Adapter für ${posName?html}</h1>
		</div>
		<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props="region:'center', tabStrip:true" id="workingArea">
  		    <!--  das dashboard -->
			<div id="dashboardTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props="title:'Dashboard'">
				<div id="info" class="boardlet" style="float:left">
				   <b>Tagesstatistik <span id="balanceDate"></span></b>
				   <p>Erster Beleg <span id="firstTicket"></span> um <span id="firstTicketDate"></span><br/>
				   Letzer Beleg <span id="lastTicket"></span> um <span id="lastTicketDate"></span></p>
				   <p>insgesamt <span id="ticketCount"></span> Belege</p>
				</div>
				<div id="umsatz"  class="boardlet" style="clear:right; float:right">
					<p><span style="font-size:200%;font-weight:bold">Umsatz</span></p>
					<p style="text-align:right"><span id="umsatzEUR" style="font-size:200%" >400,00</span></p>
				</div>
				<div id="zahlen"   class="boardlet"  style="clear:left; float:left">
					<b>Details</b>
					<table width="90%">
						<tr><td width="50%">Bar</td><td id="cashEUR" style="text-align:right"></td></tr>
						<tr><td>TeleCash</td><td id="teleEUR" style="text-align:right"></td></tr>
						<tr><td>Warenverkauf</td><td id="goodsoutEUR" style="text-align:right"></td></tr>
						<tr><td>Geschätzer Gewinn</td><td id="profitEUR" style="text-align:right"></td></tr>
						<tr><td>Auszahlungen</td><td id="payoutEUR" style="text-align:right"></td></tr>
						<tr><td>Einzahlungen</td><td id="payinEUR" style="text-align:right"></td></tr>
						<tr><td>Rechnungen bez.</td><td id="invPayedEUR" style="text-align:right"></td></tr>
						<tr><td>Gutscheine eingelöst</td><td id="tradeinEUR" style="text-align:right"></td></tr>
						<tr><td>Gutscheine verkauft</td><td id="tradeoutEUR" style="text-align:right"></td></tr>
					</table>
					<span style="font-size:80%">
					<p>
					<table width="90%" id="gutscheineInGrp" >
					</table>
					<p>
					<table width="90%" id="gutscheineOutGrp"  >
					</table>
					</span>
				</div>
				<div id="warengruppen"  class="boardlet" style="clear:right; float:right">
				    <div id="pieFan" style="margin:0pt"></div>
				</div>
			</div>
			
  		    <!--  die Kassenbelege -->
			<div id="tickTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='title:"Belege", style:"padding:10px;"'>
			   <h2>Kassenbelege von <select id="ticketPeriod" data-dojo-type="dijit/form/ComboBox">
			           <option value="today">Heute</option>
			           <option value="yesterday">Gestern</option>
			           <option value="preyesterday">Vorgestern</option>
			           <option value="prepreyesterday">Vor 3 Tagen</option>
			           <option value="preprepreyesterday">Vor 4 Tagen</option>
			           <option value="todayLastWeek">gleicherWoTag letze Woche</option>
			           <option value="yesterdayLastWeek">gestrigerWoTag letze Woche</option>
					 </select> </h2>
	           <div id="tickGrid" style="height:80%"></div>
			</div>

  		    <!--  die Kassentransaktionen -->
			<div id="txTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='title:"Transaktionen", style:"padding:10px;"'>
			   <h2>Kassentransaktionen von <select id="txPeriod" data-dojo-type="dijit/form/ComboBox">
			           <option value="today">Heute</option>
			           <option value="yesterday">Gestern</option>
			           <option value="preyesterday">Vorgestern</option>
			           <option value="prepreyesterday">Vor 3 Tagen</option>
			           <option value="preprepreyesterday">Vor 4 Tagen</option>
			           <option value="todayLastWeek">gleicherWoTag letze Woche</option>
			           <option value="yesterdayLastWeek">gestrigerWoTag letze Woche</option>
					 </select> </h2>
	           <div id="txGrid" style="height:80%"></div>
			</div>

  		    <!--  die Kassentransaktionen -->
			<div id="balTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='title:"Kassenabschlüsse", style:"padding:10px;"'>
			   <h2>Kassenabschlüsse von <div id="balPeriod" data-dojo-type="dijit/form/Select">
						<span data-dojo-value=" ">-- Zeitraum --</span>
						<span data-dojo-value="thisweek">Dieser Woche</span>
						<span data-dojo-value="lastweek">letzter Woche</span>
						<span data-dojo-value="thismonth">diesem Monat</span>
						<span data-dojo-value="lastmonth">letztem Monat</span>
						<span data-dojo-value=""></span>
						<span data-dojo-value="notExported">die der FiBu Unbekannten</span>
						<span data-dojo-value=""></span>
						<span data-dojo-value="thisquarter">diesem Quartal</span>
						<span data-dojo-value="lastquarter">letztem Quartal</span>
						<span data-dojo-value=""></span>
						<span data-dojo-value="thisyear">diesem Jahr</span>
						<span data-dojo-value="lastyear">letztem Jahr</span>
					 </div> </h2>
	           <div id="balGrid" style="height:30%; width:50%">
	           </div>
<div data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="design:'sidebar', gutters:true, liveSplitters:true" id="balDetailContainer" style="height:60%; width:100%;">
    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="splitter:true, region:'leading'" style="width: 100px;">
               <button id="showButton" type="button"></button>
               <button id="showJSONButton" type="button"></button>
               <button id="balExportButton" type="button"></button>
               <button id="goryDetailsButton" type="button"></button>
               <button id="resyncButton" type="button"></button>
               <button id="sendAgainButton" type="button"></button>
               <button id="printButton" type="button"></button>
    </div>
    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="splitter:true, region:'center'">
        <iframe id="balpdf" style="height:100%; width:100%;background-color:#eeeeee;"></iframe>
    </div>
</div>
			</div>

  		    <!--  die technisches brimborium -->
			<div id="adminTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='title:"Technisches", style:"padding:10px;display:none;"'>
				<form method="POST" action="http://localhost:8080/admin/tasks/synchronize" target="syncresult">
					<input type="submit" value="Jetzt synchronisieren!" />
				</form>
				<div class="boardlet">
				<p>Sync-Result</p>
				<iframe id="syncresult" name="syncresult">
				</iframe>
				</div>
			</div>
		</div>
	</div>

		<!-- provide config via dojoConfig global -->
		<script>
			var dojoConfig = (function(){
				var base = location.href.split("/");
				base.pop();
				base = base.join("/");

				return {
					async: true,
					isDebug: true,
					parseOnLoad: false,
					packages: [{
						name: "posa",
						location: "/assets/posa"
					}],
				};
			})();
		</script>
		<!-- load dojo -->
		<script src="//ajax.googleapis.com/ajax/libs/dojo/1.10.1/dojo/dojo.js"></script>
		<script>
			require(["posa/app", "dijit/registry", "dojo/parser", "dijit/layout/BorderContainer", "dijit/layout/TabContainer", 
			         "dijit/layout/ContentPane", "dijit/form/Select", "dijit/form/ComboBox", "dojo/domReady!"], function(demoApp, registry, parser) {
				parser.parse();
				demoApp.init(registry);
			});
		</script>
</body>
</html>