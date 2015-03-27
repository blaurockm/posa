<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>Buchlese</title>
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/dojo/1.10.1/dijit/themes/claro/document.css" />
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/dojo/1.10.1/dijit/themes/claro/claro.css" />
<link rel="stylesheet" href="/assets/bofc/app.css" />
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
           <a href="/accrualweek/view/thisweek" data-dojo-type="dijit/layout/LinkPane">Diese Woche</a>

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
				               <button id="goryDetailsButton" type="button"></button>
				    </div>
				    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="splitter:true, region:'center'">
				        <iframe id="balpdf" style="height:100%; width:100%;background-color:#eeeeee;"></iframe>
				    </div>
				</div>
		    </div>

  		    <!--  die technisches brimborium -->
			<div id="adminTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='title:"Technisches", style:"padding:10px;display:none;"'>
				<form method="POST" action="/cashbalance/fibuexport" target="expresult">
				    <table>
					<tr><td><input type="submit" value="Fibuexport" /></td><td></td></tr>
					<tr><td>Von</td><td><input type="text" name="from" value="20150207" /></td></tr>
					<tr><td>Bis</td><td><input type="text" name="till" value="20150228" /></td></tr>
					<tr><td>Kasse</td><td> Dornhan <input type="checkbox" name="kasse" value="1" checked="checked" /> Sulz <input type="checkbox" name="kasse" value="2" checked="checked" /></td></tr>
					</table>
				</form>
				<p>Export-Ergebnis</p>
				<iframe id="expresult" name="expresult" style="height:60%; width:100%; background-color:#eeeeee;"></iframe>
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
						name: "bofc",
						location: "/assets/bofc"
					}],
				};
			})();
		</script>
		<!-- load dojo -->
		<script src="//ajax.googleapis.com/ajax/libs/dojo/1.10.1/dojo/dojo.js"></script>
		<script>
			require(["bofc/app", "dijit/registry", "dojo/parser", "dijit/layout/BorderContainer", "dijit/layout/TabContainer", "dijit/layout/LayoutContainer", 
			         "dijit/layout/ContentPane", "dijit/form/Select", "dijit/form/ComboBox", "dojo/domReady!"], function(demoApp, registry, parser) {
				parser.parse();
				demoApp.init(registry);
			});
		</script>
</body>
</html>