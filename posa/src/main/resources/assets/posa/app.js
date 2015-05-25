// dependencies
define(["dojo/dom", "dojo/dom-construct", "dojo/store/JsonRest", "dijit/form/Button", "dojox/charting/Chart", 
        "dojox/charting/action2d/Tooltip", "dojox/charting/plot2d/Pie", "dojox/grid/DataGrid",
        "dojo/data/ObjectStore", "dojo/store/Memory"
        ],
function(dom, domConstruct, JsonRest, Button, Chart, Tooltip, Pie, DataGrid, ObjectStore, Memory) {
	
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

    startup = function(registry) {
    	//      create and setup the UI with layout and widgets
        // Add the only/default plot
    	wgrpKuchen.addPlot("default", {
            type: Pie,
            radius: 20,
            labelOffset: -30,
            radGrad: dojox.gfx.renderer == "vml" ? "fan" : "native"
        });
    	var tip = new Tooltip(wgrpKuchen, "default");
    	currentBalance = null; // das aktuell ausgewählte Balance-Sheet
    	
        txGridUi = new DataGrid( {structure: [
                { name: "Belegnummer", field: "belegNr", width: "90px"},
                { name: "Warengruppe", field: "articleGroupKey", width: "120px"},
                { name: "Beschreibung", field: "description", width: "300px"},
                { name: "Anzahl", field: "count", width: "40px"},
                { name: "Einzel", field: "sellingPrice", styles: 'text-align: right;', width: "50px", formatter: formatMoney },
                { name: "Gesamt", field: "total",styles: 'text-align: right;', width: "50px" , formatter: formatMoney},
                { name: "Steuer", field: "tax", width: "40px"},
                { name: "Typ", field: "type", width: "75px"},
                { name: "Uhrzeit", field: "timestamp", width: "90px", formatter : formatDate}]
        }, "txGrid");
        txGridUi.startup();
        
        tickGridUi = new DataGrid( {
            structure: [
                { name: "Belegnummer", field: "belegNr", width: "90px"},
                { name: "Gesamt", field: "total", styles: 'text-align: right;',width: "50px", formatter: formatMoney},
                { name: "Typ", field: "paymentMethod", width: "75px"},
                { name: "Storno", field: "cancel", width: "50px", type: dojox.grid.cells.Bool, editable: true },
                { name: "storniert", field: "cancelled", width: "50px", type: dojox.grid.cells.Bool, editable: true},
                { name: "Uhrzeit", field: "timestamp", width: "90px", formatter : formatDate}]
        }, "tickGrid");
        tickGridUi.startup();
        
        balGridUi = new DataGrid( {
            structure: [
                { name: "Datum", field: "abschlussId", width: "90px"},
                { name: "Umsatz", field: "revenue", styles: 'text-align: right;',width: "90px", formatter: formatMoney},
                { name: "Ges. Ertrag", field: "profit", styles: 'text-align: right;',width: "90px", formatter: formatMoney},
                { name: "Anf", field: "cashStart", styles: 'text-align: right;',width: "90px", formatter: formatMoney},
                { name: "End", field: "cashEnd", styles: 'text-align: right;',width: "90px", formatter: formatMoney},
                { name: "check", field: "checked", width: "50px", type: dojox.grid.cells.Bool, editable: true },
                { name: "Home", field: "exported", width: "50px", type: dojox.grid.cells.Bool, editable: true},
                { name: "homed am", field: "exportDate", width: "120px", formatter : formatDateTime},
                { name: "Erzeugt am", field: "creationtime", width: "120px", formatter : formatDateTime}],
               rowSelector : '20px',
               selectionMode : 'single',
               autoWidth : true
        }, "balGrid");
        balGridUi.on("RowClick", function(evt){
            var idx = evt.rowIndex;
            var pdfembed = dom.byId("balpdf");
            domConstruct.empty(pdfembed);
            currentBalance = balGridUi.getItem(idx); // globale speicherung
            pdfembed.src = '/cashbalance/view/' + currentBalance.abschlussId;
        }, true);
        balGridUi.startup();
        
        var showShowButton = new Button({
            label: "Show",
            onClick: function(){
                var pdfembed = dom.byId("balpdf")
                domConstruct.empty(pdfembed);
                pdfembed.src = '/cashbalance/view/' + currentBalance.abschlussId;
            }
        }, "showButton").startup();

        var showShowJSONButton = new Button({
            label: "JSON",
            onClick: function(){
                var pdfembed = dom.byId("balpdf")
                domConstruct.empty(pdfembed);
                pdfembed.src = '/cashbalance/' + currentBalance.abschlussId;
            }
        }, "showJSONButton").startup();
        
        var showGroyDetailsButton = new Button({
            label: "Details",
            onClick: function(){
                var pdfembed = dom.byId("balpdf")
                domConstruct.empty(pdfembed);
                pdfembed.src = '/cashbalance/extended/' + currentBalance.abschlussId;
            }
        }, "goryDetailsButton").startup();

        var showResyncButton = new Button({
            label: "Resync K",
            onClick: function(){
                var pdfembed = dom.byId("balpdf")
                domConstruct.empty(pdfembed);
                pdfembed.src = '/cashbalance/resync/' + currentBalance.abschlussId;
            }
        }, "resyncButton").startup();

        var showResendButton = new Button({
            label: "Send Bof",
            onClick: function(){
                var pdfembed = dom.byId("balpdf")
                domConstruct.empty(pdfembed);
                pdfembed.src = '/cashbalance/sendbof/' + currentBalance.abschlussId;
            }
        }, "sendAgainButton").startup();

        var printButton = new Button({
            label: "Abschluss drucken",
            onClick: function(){
            	var pdfembed = dom.byId("balpdf"); pdfembed.focus(); pdfembed.contentWindow.print();
            }
        }, "printButton").startup();

    },
    updateBalGrid = function(newVal ) {
    	  store.query({"date": newVal}).then(function(balances){
              var dataStore = new ObjectStore({ objectStore:new Memory({ data: balances}) });
              balGridUi.setStore(dataStore);
    	  });
    },
    updateTickGrid = function(newVal ) {
          tickStore.query({"date": newVal}).then(function(txdata){
            var dataStore = new ObjectStore({ objectStore:new Memory({ data: txdata}) });
            tickGridUi.setStore(dataStore);
          });
    },
    updateTxGrid = function(newVal ) {
    	  txStore.query({"date": newVal}).then(function(tickData){
              var dataStore = new ObjectStore({ objectStore:new Memory({ data: tickData}) });
              txGridUi.setStore(dataStore);
    	  });
    },
    renderTable = function(tablenode, map) {
       domConstruct.empty(tablenode);
       for (var cgrpo in map) {
    	   if (map.hasOwnProperty(cgrpo)) {
    		    var tro = domConstruct.create("tr", null,  tablenode);
    		    domConstruct.create("td", { innerHTML: cgrpo, style : 'width: 50%;' }, tro);
    		    domConstruct.create("td", { innerHTML: map[cgrpo].formatMoney(), style : 'text-align: right;'  }, tro);
    	   }
       }
    }
    update = function() {
      // Get an object by identity
  	  store.get("today").then(function(balance){
 	       dom.byId("balanceDate").innerHTML = new Date(balance.creationtime).toLocaleDateString() + " " + new Date(balance.creationtime).toLocaleTimeString();
  	       dom.byId("firstTicket").innerHTML = balance.firstBelegNr;
  	       dom.byId("lastTicket").innerHTML = balance.lastBelegNr;
  	       dom.byId("firstTicketDate").innerHTML = new Date(balance.firstTimestamp).toLocaleTimeString();
  	       dom.byId("lastTicketDate").innerHTML = new Date(balance.lastTimestamp).toLocaleTimeString();
  	       dom.byId("ticketCount").innerHTML = balance.ticketCount;

  	       if (balance.revenue == undefined) {
  	    	   return;
  	       }
  	       dom.byId("umsatzEUR").innerHTML = balance.revenue.formatMoney();

  	       dom.byId("goodsoutEUR").innerHTML = balance.goodsOut.formatMoney();
  	       dom.byId("profitEUR").innerHTML = balance.profit.formatMoney();

  	       dom.byId("cashEUR").innerHTML = balance.paymentMethodBalance["CASH"].formatMoney();
  	       if (balance.paymentMethodBalance.TELE != undefined) {
  	       	  dom.byId("teleEUR").innerHTML = balance.paymentMethodBalance["TELE"].formatMoney();
  	       }
  	       dom.byId("payoutEUR").innerHTML = balance.cashOutSum.formatMoney();
  	       dom.byId("payinEUR").innerHTML = balance.cashInSum.formatMoney();
  	       dom.byId("invPayedEUR").innerHTML = balance.payedInvoicesSum.formatMoney();
  	       dom.byId("tradeoutEUR").innerHTML = balance.couponTradeOut.formatMoney();
  	       dom.byId("tradeinEUR").innerHTML = balance.couponTradeIn.formatMoney();

  	       renderTable(dom.byId("gutscheineOutGrp"), balance.newCoupon);
  	       renderTable(dom.byId("gutscheineInGrp"), balance.oldCoupon);

  	       var chartData = [];
  	       for (var grp in balance.articleGroupBalance) {
  	    	   if (balance.articleGroupBalance.hasOwnProperty(grp)) {
  	    		    chartData.push({ y : balance.articleGroupBalance[grp] / 100.0, text : grp, tooltip : balance.articleGroupBalance[grp].formatMoney() });
  	    	   }
  	       }
  	       // Add the series of data
  	       wgrpKuchen.addSeries("Warengruppen",chartData);
  	       wgrpKuchen.render();
  	  });
    };
    
    
    formatMoney = function(n){
    	if (n == null) {
    		return "(null)";
    	}
    	return n.formatMoney();
	};
	formatDate = function(d) {
		return new Date(d).toLocaleTimeString();
	}
	formatDateTime = function(d) {
		if (d == undefined || d == null) {
			return "--";
		}
		return new Date(d).toLocaleDateString() + " um " + new Date(d).toLocaleTimeString();
	}
    return {
        init: function(registry) {
            // proceed directly with startup
            startup(registry);
            update();
            dashboardupdate = window.setInterval(update, 5 * 60 * 1000);
            registry.byId("balPeriod").on("change", updateBalGrid);
            registry.byId("ticketPeriod").on("change", updateTickGrid);
            registry.byId("txPeriod").on("change", updateTxGrid);
        }
    };
    
}); // end of define


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
 
	 