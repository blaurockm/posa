// This example makes use of the jQuery library.
   
// You can use any methods as actions in PathJS.  You can define them as I do below,
// assign them to variables, or use anonymous functions.  The choice is yours.
function notFound(){
    $("#output .content").html("404 Not Found, diese seite gibt es nicht!");
}

function setSubscrModule() {
	if ($("#homebutton").attr("myModule") != "subscr") {
		$("#navbar").load("/subscr/navigation");
		$("#homebutton").attr("myModule", "subscr");
	}
}

// Here we define our routes.  You'll notice that I only define three routes, even
// though there are four links.  Each route has an action assigned to it (via the 
// `to` method, as well as an `enter` method.  The `enter` method is called before
// the route is performed, which allows you to do any setup you need (changes classes,
// performing AJAX calls, adding animations, etc.

///////////////////////////////////////
///////////////////////////////////////
///////////////////////////////////////
///////////////////////////////////////
Path.map("#index").to(function(){
    $("#output .content").load("/subscr/dashboard");
}).enter(setSubscrModule);

///////////////////////////////////////
///////////////////////////////////////
///////////////////////////////////////
///////////////////////////////////////
Path.map("#subscr").to(function(){
    $("#output .content").load("/subscr/dashboard");
}).enter(setSubscrModule);

Path.map("#subscrCust").to(function(){
    $("#output .content").load("/subscr/customers");
}).enter(setSubscrModule);

Path.map("#subscrProd").to(function(){
    $("#output .content").load("/subscr/products");
}).enter(setSubscrModule);

Path.map("#subscrInvoices").to(function(){
    $("#output .content").load("/subscr/invoices");
}).enter(setSubscrModule);

Path.map("#subscrInvoices/:spec").to(function(){
    $("#output .content").load("/subscr/invoices?spec=" + this.params["spec"]);
}).enter(setSubscrModule);

Path.map("#subscrDeliveryNotes").to(function(){
    $("#output .content").load("/subscr/deliveryNotes");
}).enter(setSubscrModule);

Path.map("#invoiceCancel/:id").to(function(){
    $("#output .content").load("/subscr/invoiceCancel/" + this.params["id"]);
    window.history.back();  // in der history einen schritt zurückgehen
}).enter(setSubscrModule);

Path.map("#subscrarticlecreate/:id").to(function(){
    $("#output .content").load("/subscr/subscrarticlecreate/" + this.params["id"]);
    window.history.back();  // in der history einen schritt zurückgehen
}).enter(setSubscrModule);

Path.map("#subscrintervalcreate/:id").to(function(){
    $("#output .content").load("/subscr/subscrintervalcreate/" + this.params["id"]);
    window.history.back();  // in der history einen schritt zurückgehen
}).enter(setSubscrModule);

Path.map("#subscrProduct/:id").to(function(){
    $("#output .content").load("/subscr/product/" + this.params["id"]);
}).enter(setSubscrModule);

Path.map("#deliverydelete/:id").to(function(){
    $("#output .content").load("/subscr/deliverydelete/" + this.params["id"]);
}).enter(setSubscrModule);

Path.map("#intervaldeliverydelete/:id").to(function(){
    $("#output .content").load("/subscr/intervaldeliverydelete/" + this.params["id"]);
}).enter(setSubscrModule);

Path.map("#subscription/:id").to(function(){
    $("#output .content").load("/subscr/subscription/" + this.params["id"]);
}).enter(setSubscrModule);

Path.map("#subscriber/:id").to(function(){
    $("#output .content").load("/subscr/subscriber/" + this.params["id"]);
}).enter(setSubscrModule);

Path.map("#subscrDispo/:id").to(function(){
    $("#output .content").load("/subscr/dispo/" + this.params["id"]);
}).enter(setSubscrModule);

Path.map("#subscrDispoNav/:id/:dir/:art").to(function(){
    $("#output .content").load("/subscr/disponav/" + this.params["id"] +"/" +this.params["dir"] +"/" +this.params["art"]);
}).enter(setSubscrModule);

Path.map("#subscrIntervalDispo/:id").to(function(){
    $("#output .content").load("/subscr/intervaldispo/" + this.params["id"]);
}).enter(setSubscrModule);

Path.map("#subscrIntervalDispoNav/:id/:dir/:art").to(function(){
    $("#output .content").load("/subscr/intervaldisponav/" + this.params["id"] +"/" +this.params["dir"] +"/" +this.params["art"]);
}).enter(setSubscrModule);

Path.map("#subscrDelivery/:id").to(function(){
	// this is a modal window content
    $("#modal-output .content").load("/subscr/delivery/" + this.params["id"]);
    $("#modal-title").text("Auslieferungsdetails");
    $("#myModal").modal('show');
}).enter(setSubscrModule);

Path.map("#subscrIntervalDelivery/:id").to(function(){
	// this is a modal window content
    $("#modal-output .content").load("/subscr/intervaldelivery/" + this.params["id"]);
    $("#modal-title").text("Auslieferungsdetails");
    $("#myModal").modal('show');
}).enter(setSubscrModule);

Path.map("#subscrCustAdd").to(function(){
	// this is a modal window content
    $("#entrymodal-output .content").load("/subscr/customerCreateForm");
    $("#entrymodal-title").text("Neuen Abonnenten anlegen");
    $("#entryModal").modal('show');
}).enter(setSubscrModule);

Path.map("#subscrSubscrAdd/:id/:id2").to(function(){
	// this is a modal window content
	var url = "/subscr/subscriptionCreateForm?";
	if (this.params['id'] > 0) {
		url += "sub=" + this.params['id'] + "&";
	}
	if (this.params['id2'] > 0) {
		url += "prod=" + this.params['id2'];
	}
    $("#entrymodal-output .content").load(url);
    $("#entrymodal-title").text("Neues Abo anlegen");
    $("#entryModal").modal('show');
}).enter(setSubscrModule);

Path.map("#subscrProductAdd").to(function(){
	// this is a modal window content
    $("#entrymodal-output .content").load("/subscr/productCreateForm");
    $("#entrymodal-title").text("Neues Periodikum anlegen");
    $("#entryModal").modal('show');
}).enter(setSubscrModule);



// Here we set a "root route".  You may have noticed that when you landed on this
// page you were automatically "redirected" to the "#/users" route.  The definition
// below tells PathJS to load this route automatically if one isn't provided.
Path.root("#index");

// The `Path.rescue()` method takes a function as an argument, and will be called when
// a route is activated that you have no yet defined an action for.  On this example
// page, you'll notice there is no defined route for the "Unicorns!?" link.  Since no
// route is defined, it calls this method instead.
Path.rescue(notFound);

var formatMoney = function(n, c, d, t){
	var c = isNaN(c = Math.abs(c)) ? 2 : c, 
	    d = d == undefined ? "," : d, 
	    t = t == undefined ? "." : t, 
	    s = n < 0 ? "-" : "", 
	    i = parseInt(n = Math.abs(+(n/100.0) || 0).toFixed(c)) + "", 
	    j = (j = i.length) > 3 ? j % 3 : 0;
	   return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "") + " €";
	 };

$(document).ready(function(){
    // This line is used to start the PathJS listener.  It's good practice to put this
    // call inside some sort of "document ready" listener, in case the default route
    // relies on DOM elements that may not yet be ready.
    Path.listen();
    $('[data-toggle="tooltip"]').tooltip(); 
});

var shiptypesList = [{value:"MAIL", text:"Per Post"},{value:"MAILCOST", text:"Per Post m. Porto"},{value:"PICKUP",text:"Abholung"}, {value:"PUBLISHER", text:"über Verlag"}, {value:"DELIVERY", text:"Lieferung d. Buchlese"}];
var laedenList = [{value:1, text:"Dornhan"},{value:2, text:"Sulz"},{value:3, text:"Schramberg"}];
var paytypesList = [{value:"EACHDELIVERY", text:"Pro Lieferung"},{value:"MONTHLY",text:"Monatlich"},{value:"QUARTERLY",text:"Vierteljährlich"}, {value:"HALFYEARLY", text:"Halbjährlich"}, {value:"YEARLY", text:"Jährlich"}];
