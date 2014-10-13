<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>Buchlese</title>
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
			
  		    <!--  die Kassenberichte -->
			<div id="tickTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='title:"Belege", style:"padding:10px;"'>
			   <h2>Kassenbelege</h2>
			   <p>von <div id="ticketPeriod" data-dojo-type="dijit/form/Select" data-dojo-props="value:'t'">
						<span data-dojo-value="t">Heute</span>
						<span data-dojo-value="y">Gestern</span>
						<span data-dojo-value="tw">Dieser Woche</span>
						<span data-dojo-value="lw">letzter Woche</span>
						<span data-dojo-value="vlw">vorletzter Woche</span>
						<span data-dojo-value="tm">diesem Monat</span>
						<span data-dojo-value="lm">letztem Monat</span>
					 </div> </p>
	           <div id="tickGrid" style="height:100%"></div>
			</div>

  		    <!--  die Kassenberichte -->
			<div id="txTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='title:"Transaktionen", style:"padding:10px;"'>
			   <h2>Kassentransaktionen</h2>
			   <p>von <div id="txPeriod" data-dojo-type="dijit/form/Select" data-dojo-props="value:'t'">
						<span data-dojo-value="t">Heute</span>
						<span data-dojo-value="y">Gestern</span>
						<span data-dojo-value="tw">Dieser Woche</span>
						<span data-dojo-value="lw">letzter Woche</span>
						<span data-dojo-value="vlw">vorletzter Woche</span>
						<span data-dojo-value="tm">diesem Monat</span>
						<span data-dojo-value="lm">letztem Monat</span>
					 </div> </p>
	           <div id="txGrid" style="height:100%"></div>
			</div>

  		    <!--  die Gutscheine -->
			<div id="gutscheinTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='title:"Gutscheine", style:"padding:10px;display:none;"'>
			   <h2>Gutscheine</h2>
			   <p>von <div id="couponPeriod" data-dojo-type="dijit/form/Select" data-dojo-props="value:'tw'">
						<span data-dojo-value="tw"><b>Dieser Woche</b></span>
						<span data-dojo-value="lw">letzter Woche</span>
						<span data-dojo-value="vlw">vorletzter Woche</span>
						<span data-dojo-value="tm">diesem Monat</span>
						<span data-dojo-value="lm">letztem Monat</span>
					 </div> </p>
	           <p> Hier kommt das Grid </p>
			</div>


  		    <!--  die Rechnungen-->
			<div id="rechnungsTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='title:"Rechnungen", style:"padding:10px;display:none;"'>
			   <h2>Rechnungen</h2>
			   <p>von <div id="invoicePeriod" data-dojo-type="dijit/form/Select" data-dojo-props="value:'tw'">
						<span data-dojo-value="tw"><b>Dieser Woche</b></span>
						<span data-dojo-value="lw">letzter Woche</span>
						<span data-dojo-value="vlw">vorletzter Woche</span>
						<span data-dojo-value="tm">diesem Monat</span>
						<span data-dojo-value="lm">letztem Monat</span>
					 </div> </p>
	           <p> Hier kommt das Grid </p>
			</div>

  		    <!--  die technisches brimborium -->
			<div id="adminTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='title:"Technisches", style:"padding:10px;display:none;"'>
				<form method="POST" action="http://localhost:8081/tasks/synchronize" target="syncresult">
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
					parseOnLoad: true,
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
			require(["posa/app", "dijit/layout/BorderContainer", "dijit/layout/TabContainer", 
			         "dijit/layout/ContentPane", "dijit/form/Select", "dojo/domReady!"], function(demoApp) {
				demoApp.init();
			});
		</script>
</body>
</html>