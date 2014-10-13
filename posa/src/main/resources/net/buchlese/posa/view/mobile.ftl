<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>Buchlese</title>
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/dojo/1.10.1/dijit/themes/claro/document.css" />
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/dojo/1.10.1/dijit/themes/claro/claro.css" />
<link rel="stylesheet" href="/assets/posa/mobile.css" />
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
		  <h1>Buchlese ${posName?html}</h1>
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
			require(["posa/mobile", "dijit/layout/BorderContainer", "dijit/layout/TabContainer", 
			         "dijit/layout/ContentPane", "dojo/domReady!"], function(demoApp) {
				demoApp.init();
			});
		</script>
</body>
</html>