// dependencies
define(["dojo/dom", "dojo/dom-construct", "dojo/store/JsonRest", "dojox/charting/Chart", 
        "dojox/charting/action2d/Tooltip", "dojox/charting/plot2d/Pie"
        ],
function(dom, domConstruct, JsonRest, Chart, Tooltip, Pie) {
	
	// ein paar variablen definieren
	var store = new JsonRest({
	    target: "/cashbalance"
	  }),
    wgrpKuchen = new Chart("pieFan"),

    startup = function() {
        // create the data store
        initUi();
    },
 
    initUi = function() {
    	//      create and setup the UI with layout and widgets
        // Add the only/default plot
    	wgrpKuchen.addPlot("default", {
            type: Pie,
            radius: 20,
            labelOffset: -30,
            radGrad: dojox.gfx.renderer == "vml" ? "fan" : "native"
        });
    	var tip = new Tooltip(wgrpKuchen, "default");
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
      // Get an object by identity
	  store.get("thisweek").then(function(balance){
 	       dom.byId("thisweekrevenueEUR").innerHTML = balance.revenue.formatMoney();
 	       dom.byId("thisweekprofitEUR").innerHTML = balance.profit.formatMoney();
	  });
	  store.get("lastweek").then(function(balance){
	       dom.byId("lastweekrevenueEUR").innerHTML = balance.revenue.formatMoney();
	       dom.byId("lastweekprofitEUR").innerHTML = balance.profit.formatMoney();
	  });

	  store.get("thismonth").then(function(balance){
	       dom.byId("thismonthrevenueEUR").innerHTML = balance.revenue.formatMoney();
	       dom.byId("thismonthprofitEUR").innerHTML = balance.profit.formatMoney();
	  });

    };
    
    
    formatMoney = function(n){
    	return n.formatMoney();
	};
	formatDate = function(d) {
		return new Date(d).toLocaleTimeString();
	}
    return {
        init: function() {
            // proceed directly with startup
            startup();
            update();
            dashboardupdate = window.setInterval(update, 5 * 60 * 1000);
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
 
	 