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
		  <h1><b>Buchlese Statistik-Provider</b></h1>
		</div>
		<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props="region:'center', tabStrip:true" id="workingArea">
  		    <!--  die wochenübersicht -->
			<div data-dojo-type="dijit/layout/LayoutContainer" data-dojo-props='title:"Wochenübersicht", style:"padding:10px;"'>
			    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'top'">
			      <span style="font-size:100%;font-weight:bold">				       
			      <button id="prevWeekButton" type="button"></button>
			      Woche <span id="weeknum">6</span>
			      <button id="nextWeekButton" type="button"></button>
			      </span>
			      <button id="fibuWeekButton" type="button"></button>
			      <button id="selectCashBalWeekButton" type="button"></button>
			    </div>
			    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'center', layoutPriority:1">
			      	<iframe id="weekviewdiv" style="height:100%; width:100%;background-color:#eeeeee;">
			      	</iframe>
				</div>
			</div>
  		    <!--  die Monatsübersicht -->
			<div data-dojo-type="dijit/layout/LayoutContainer" data-dojo-props='title:"Monatsübersicht", style:"padding:10px;"'>
			    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'top'">
			      <span style="font-size:100%;font-weight:bold">				       
			      <button id="prevMonthButton" type="button"></button>
			      Monat <span id="monthnum">3</span>
			      <button id="nextMonthButton" type="button"></button>
			      </span>
			      <button id="selectCashBalMonthButton" type="button"></button>
			    </div>
			    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'center', layoutPriority:1">
			      	<iframe id="monthviewdiv" style="height:100%; width:100%;background-color:#eeeeee;">
			      	</iframe>
				</div>
			</div>
			<div id="balTab" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='title:"Kassenabschlüsse", style:"padding:10px;"'>
	           <div id="balGrid" style="height:40%; width:50%">
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

  		    <!--  technisches brimborium -->
  		    <a href="/state" data-dojo-type="dijit/layout/LinkPane">Status</a>

  		    <!--  technisches brimborium -->
  		    <a href="/technics" data-dojo-type="dijit/layout/LinkPane">Technisches</a>
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
			         "dijit/layout/ContentPane", "dijit/layout/LinkPane", "dijit/form/Select", "dijit/form/ComboBox", "dojo/domReady!"], function(demoApp, registry, parser) {
				parser.parse();
				demoApp.init(registry);
			});
		</script>
</body>
</html>