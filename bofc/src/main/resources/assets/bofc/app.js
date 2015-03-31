// dependencies
define(["dojo/dom", "dojo/dom-construct", "dojo/store/JsonRest", "dijit/form/Button", "dojox/grid/DataGrid",
        "dojo/data/ObjectStore", "dojo/store/Memory"
        ],
function(dom, domConstruct, JsonRest, Button, DataGrid, ObjectStore, Memory) {
	// ein paar variablen definieren
	var store = new JsonRest({
	    target: "/cashbalance"
	  }),
	  
    startup = function(registry) {
    	currentBalance = null; // das aktuell ausgewählte Balance-Sheet
    	currentWeek = new Date().getWeek();
        
        balGridUi = new DataGrid( {
            structure: [
                { name: "Datum", field: "abschlussId", width: "90px"},
                { name: "Kasse", field: "pointid", width: "50px"},
                { name: "Umsatz", field: "revenue", styles: 'text-align: right;',width: "90px", formatter: formatMoney},
                { name: "Ges. Ertrag", field: "profit", styles: 'text-align: right;',width: "90px", formatter: formatMoney},
                { name: "Anf", field: "cashStart", styles: 'text-align: right;',width: "90px", formatter: formatMoney},
                { name: "End", field: "cashEnd", styles: 'text-align: right;',width: "90px", formatter: formatMoney},
                { name: "check", field: "checked", width: "50px", type: dojox.grid.cells.Bool, editable: true },
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
            pdfembed.src = '/cashbalance/view/' + currentBalance.id;
        }, true);
        balGridUi.startup();

        var showPrevWeekButton = new Button({
            label: "\u25c4",
            onClick: function(){
            	currentWeek = currentWeek -1;
            	dom.byId("weeknum").innerHTML = currentWeek;
                var weekview = dom.byId("weekviewdiv");
                domConstruct.empty(weekview);
                weekview.src = '/accrualweek/view/' + currentWeek;
            }
        }, "prevWeekButton").startup();

        var showNextWeekButton = new Button({
            label: "\u25ba",
            onClick: function(){
            	currentWeek = currentWeek +1;
            	dom.byId("weeknum").innerHTML = currentWeek;
                var weekview = dom.byId("weekviewdiv");
                domConstruct.empty(weekview);
                weekview.src = '/accrualweek/view/' + currentWeek;
            }
        }, "nextWeekButton").startup();

        var showShowButton = new Button({
            label: "Show",
            onClick: function(){
                var pdfembed = dom.byId("balpdf")
                domConstruct.empty(pdfembed);
                pdfembed.src = '/cashbalance/view/' + currentBalance.id;
            }
        }, "showButton").startup();

        var showShowJSONButton = new Button({
            label: "JSON",
            onClick: function(){
                var pdfembed = dom.byId("balpdf")
                domConstruct.empty(pdfembed);
                pdfembed.src = '/cashbalance/complete/' + currentBalance.id;
            }
        }, "showJSONButton").startup();
        
        var showGroyDetailsButton = new Button({
            label: "Details",
            onClick: function(){
                var pdfembed = dom.byId("balpdf")
                domConstruct.empty(pdfembed);
                pdfembed.src = '/cashbalance/extended/' + currentBalance.id;
            }
        }, "goryDetailsButton").startup();
        
        var printButton = new Button({
            label: "Abschluss drucken",
            onClick: function(){
                var pdfembed = dom.byId("balpdf")
                domConstruct.empty(pdfembed);
                pdfembed.src = '/cashbalance/pdf/' + currentBalance.id;
//            	var pdfembed = dom.byId("balpdf"); pdfembed.focus(); pdfembed.contentWindow.print();
            }
        }, "printButton").startup();

    }
    
    updateBalGrid = function(newVal ) {
    	  store.query({"date": newVal}).then(function(balances){
              var dataStore = new ObjectStore({ objectStore:new Memory({ data: balances}) });
              balGridUi.setStore(dataStore);
    	  });
    }
    
    formatMoney = function(n){
    	return n.formatMoney();
	}
    
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
            registry.byId("balPeriod").on("change", updateBalGrid);
        	dom.byId("weeknum").innerHTML = currentWeek;
            dom.byId("weekviewdiv").src = '/accrualweek/view/thisweek';
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
 
 /**
 * Returns the week number for this date. dowOffset is the day of week the week
 * "starts" on for your locale - it can be from 0 to 6. If dowOffset is 1 (Monday),
 * the week returned is the ISO 8601 week number.
 * @param int dowOffset
 * @return int
 */
 Date.prototype.getWeek = function (dowOffset) {
	 /*getWeek() was developed by Nick Baicoianu at MeanFreePath: http://www.epoch-calendar.com */

	 dowOffset = typeof(dowOffset) == 'int' ? dowOffset : 1; //default dowOffset to one
	 var newYear = new Date(this.getFullYear(),0,1);
	 var day = newYear.getDay() - dowOffset; //the day of week the year begins on
	 day = (day >= 0 ? day : day + 7);
	 var daynum = Math.floor((this.getTime() - newYear.getTime() -
	 (this.getTimezoneOffset()-newYear.getTimezoneOffset())*60000)/86400000) + 1;
	 var weeknum;
	 //if the year starts before the middle of a week
	 if(day < 4) {
	 weeknum = Math.floor((daynum+day-1)/7) + 1;
	 if(weeknum > 52) {
	 nYear = new Date(this.getFullYear() + 1,0,1);
	 nday = nYear.getDay() - dowOffset;
	 nday = nday >= 0 ? nday : nday + 7;
	 /*if the next year starts before the middle of
	 the week, it is week #1 of that year*/
	 weeknum = nday < 4 ? 1 : 53;
	 }
	 }
	 else {
	 weeknum = Math.floor((daynum+day-1)/7);
	 }
	 return weeknum;
};	 
	 