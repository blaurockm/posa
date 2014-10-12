// dependencies
define(["dojo/dom", "dojo/dom-construct", "dojo/store/JsonRest", "dojox/charting/Chart", 
        "dojox/charting/action2d/Tooltip", "dojox/charting/plot2d/Pie", "dojox/grid/DataGrid","dojo/data/ObjectStore"
        ],
function(dom, domConstruct, JsonRest, Chart, Tooltip, Pie, DataGrid, ObjectStore) {
	
	// ein paar variablen definieren
	var store = new JsonRest({
	    target: "/cashbalance"
	  }),
	txStore = new JsonRest({
		    target: "/tx"
	  }),
	tickStore = new JsonRest({
		    target: "/ticket"
	  }),
    wgrpKuchen = new Chart("pieFan"),

    startup = function() {
        // create the data store
        initUi();
    },
 
    initUi = function() {
        // summary:
        //      create and setup the UI with layout and widgets
//        lightbox = new LightboxNano({});
        // Set the theme
        // Create the chart within it's "holding" node
//        pieChart.setTheme(dojox.charting.themes.Claro);

        // Add the only/default plot
    	wgrpKuchen.addPlot("default", {
            type: Pie,
            radius: 20,
            labelOffset: -30,
            radGrad: dojox.gfx.renderer == "vml" ? "fan" : "native"
        });
    	var tip = new Tooltip(wgrpKuchen, "default");
    	
        txGridUi = new DataGrid( {store: new ObjectStore( {objectStore: txStore  } ),
        	query: { date: "today" },
            structure: [
                { name: "Belegnummer", field: "belegNr", width: "90px"},
                { name: "Warengruppe", field: "articleGroupKey", width: "120px"},
                { name: "Beschreibung", field: "description", width: "300px"},
                { name: "Anzahl", field: "count", width: "40px"},
                { name: "Einzel", field: "sellingPrice", width: "50px"},
                { name: "Gesamt", field: "total", width: "50px"},
                { name: "Steuer", field: "tax", width: "40px"},
                { name: "Typ", field: "type", width: "75px"},
                { name: "Uhrzeit", field: "timestamp", width: "100px"}]
        }, "txGrid");
        tickGridUi = new DataGrid( {store: new ObjectStore( {objectStore: tickStore  } ),
        	query: { date: "today" },
            structure: [
                { name: "Belegnummer", field: "belegNr", width: "90px"},
                { name: "Gesamt", field: "total", width: "50px"},
                { name: "Typ", field: "paymentMethod", width: "75px"},
                { name: "Uhrzeit", field: "timestamp", width: "100px"}]
        }, "tickGrid");
    },
    update = function() {
      // Get an object by identity
  	  store.get("today").then(function(balance){
 	       dom.byId("balanceDate").innerHTML = new Date(balance.creationtime).toLocaleDateString() + " " + new Date(balance.creationtime).toLocaleTimeString();
  	       dom.byId("firstTicket").innerHTML = balance.firstBelegNr;
  	       dom.byId("lastTicket").innerHTML = balance.lastBelegNr;
  	       dom.byId("firstTicketDate").innerHTML = new Date(balance.firstTimestamp).toLocaleTimeString();
  	       dom.byId("lastTicketDate").innerHTML = new Date(balance.lastTimestamp).toLocaleTimeString();
  	       dom.byId("ticketCount").innerHTML = balance.ticketCount;

  	       dom.byId("umsatzEUR").innerHTML = balance.revenue.formatMoney();

  	       dom.byId("goodsoutEUR").innerHTML = balance.goodsOut.formatMoney();
  	       dom.byId("profitEUR").innerHTML = balance.profit.formatMoney();

  	       dom.byId("cashEUR").innerHTML = balance.paymentMethodBalance["CASH"].formatMoney();
  	       if (balance.paymentMethodBalance.TELE != undefined) {
  	       	  dom.byId("teleEUR").innerHTML = balance.paymentMethodBalance["TELE"].formatMoney();
  	       }
  	       dom.byId("payoutEUR").innerHTML = balance.cashOut.formatMoney();
  	       dom.byId("payinEUR").innerHTML = balance.cashIn.formatMoney();
  	       dom.byId("tradeoutEUR").innerHTML = balance.couponTradeOut.formatMoney();
  	       dom.byId("tradeinEUR").innerHTML = balance.couponTradeIn.formatMoney();

  	       domConstruct.empty("gutscheineOutGrp");
  	       var tableOutNode = dom.byId("gutscheineOutGrp");
  	       for (var cgrpo in balance.newCoupon) {
  	    	   if (balance.newCoupon.hasOwnProperty(cgrpo)) {
 	    		    var tro = domConstruct.create("tr", tableOutNode);
  	    		    domConstruct.create("td", { innerHTML: cgrpo }, tro);
  	    		    domConstruct.create("td", { innerHTML: balance.newCoupon[cgrpo].formatMoney(), style  : {"text-align" : "right" } }, tro);
  	    	   }
  	       }

  	       domConstruct.empty("gutscheineInGrp");
  	       var tableInNode = dom.byId("gutscheineInGrp");
  	       for (var cgrpi in balance.oldCoupon)   {
  	    	   if (balance.oldCoupon.hasOwnProperty(cgrpi)) {
 	    		    var tri = domConstruct.create("tr", {}, tableInNode);
  	    		    domConstruct.create("td", { innerHTML: cgrpi, style : { width : "50%" } }, tri);
  	    		    domConstruct.create("td", {style  : {"text-align" : "right" },  innerHTML: balance.oldCoupon[cgrpi].formatMoney() }, tri);
  	    	   }
  	       }

  	       var chartData = [];
  	       for (var grp in balance.articleGroupBalance) {
  	    	   if (balance.articleGroupBalance.hasOwnProperty(grp)) {
  	    		    chartData.push({ y : balance.articleGroupBalance[grp] / 100.0, text : grp, tooltip : balance.articleGroupBalance[grp].formatMoney() });
  	    	   }
  	       }
  	       // Add the series of data
  	       wgrpKuchen.addSeries("Warengruppen",chartData);
  	       wgrpKuchen.render();
  	       
  	       
  	       txGridUi.startup();
  	  });
    },
    renderItem = function(item, refNode, posn) {
        // summary:
        //      Create HTML string to represent the given item
    };
    
    return {
        init: function() {
            // proceed directly with startup
            startup();
            update();
            dashboardupdate = window.setInterval(update, 5 * 60 * 1000);
        }
    };
});


Number.prototype.formatMoney = function(c, d, t){
	var n = this, 
	    c = isNaN(c = Math.abs(c)) ? 2 : c, 
	    d = d == undefined ? "," : d, 
	    t = t == undefined ? "." : t, 
	    s = n < 0 ? "-" : "", 
	    i = parseInt(n = Math.abs(+(n/100.0) || 0).toFixed(c)) + "", 
	    j = (j = i.length) > 3 ? j % 3 : 0;
	   return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "") + " €";
	 };
 
	 