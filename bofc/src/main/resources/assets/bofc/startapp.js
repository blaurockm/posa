        // This example makes use of the jQuery library.
       
        // You can use any methods as actions in PathJS.  You can define them as I do below,
        // assign them to variables, or use anonymous functions.  The choice is yours.
        function notFound(){
            $("#output .content").html("404 Not Found, diese seite gibt es nicht!");
            $("#output .content").addClass("alert alert-danger");
        }

        function setPageBackground(){
            $("#output .content").removeClass("alert alert-danger");
        }        

// Here we define our routes.  You'll notice that I only define three routes, even
// though there are four links.  Each route has an action assigned to it (via the 
// `to` method, as well as an `enter` method.  The `enter` method is called before
// the route is performed, which allows you to do any setup you need (changes classes,
// performing AJAX calls, adding animations, etc.
Path.map("#index").to(function(){
    $("#output .content").load("/pages/index");
}).enter(setPageBackground);

Path.map("#export").to(function(){
    $("#output .content").load("/pages/export");
}).enter(setPageBackground);

Path.map("#subscr").to(function(){
    $("#output .content").load("/subscr/dashboard");
}).enter(setPageBackground);

Path.map("#subscrCust").to(function(){
    $("#output .content").load("/subscr/customers");
}).enter(setPageBackground);

Path.map("#subscrProduct/:id").to(function(){
    $("#output .content").load("/subscr/product/" + this.params["id"]);
}).enter(setPageBackground);

Path.map("#subscription/:id").to(function(){
    $("#output .content").load("/subscr/subscription/" + this.params["id"]);
}).enter(setPageBackground);

Path.map("#subscriber/:id").to(function(){
    $("#output .content").load("/subscr/subscriber/" + this.params["id"]);
}).enter(setPageBackground);

Path.map("#subscrDispo/:id").to(function(){
    $("#output .content").load("/subscr/dispo/" + this.params["id"]);
}).enter(setPageBackground);

Path.map("#subscrDelivery/:id").to(function(){
	// this is a modal window content
    $("#modal-output .content").load("/subscr/delivery/" + this.params["id"]);
    $("#modal-title").text("Auslieferungsdetails");
    $("#myModal").modal('show');
}).enter(setPageBackground);

Path.map("#subscrCustAdd").to(function(){
	// this is a modal window content
    $("#entrymodal-output .content").load("/subscr/customerCreateForm");
    $("#entrymodal-title").text("Neuen Abonnenten anlegen");
    $("#entryModal").modal('show');
}).enter(setPageBackground);

Path.map("#subscrSubscrAdd/:id").to(function(){
	// this is a modal window content
    $("#entrymodal-output .content").load("/subscr/subscriptionCreateForm?sub=" + this.params['id']);
    $("#entrymodal-title").text("Neues Abo anlegen");
    $("#entryModal").modal('show');
}).enter(setPageBackground);

Path.map("#subscrProductAdd").to(function(){
	// this is a modal window content
    $("#entrymodal-output .content").load("/subscr/productCreateForm");
    $("#entrymodal-title").text("Neues Periodikum anlegen");
    $("#entryModal").modal('show');
}).enter(setPageBackground);

Path.map("#exports").to(function(){
    $("#output .content").load("/pages/exports");
}).enter(setPageBackground);

Path.map("#rechnungen").to(function(){
    $("#output .content").load("/pages/rechnungen");
}).enter(setPageBackground);

Path.map("#commands").to(function(){
   $("#output .content").load("/pages/commands");
}).enter(setPageBackground);

Path.map("#mappings/:pos").to(function(){
	   $("#output .content").load("/pages/mappings?point=" + this.params["pos"]);
	}).enter(setPageBackground);

Path.map("#mappings").to(function(){
	   $("#output .content").load("/pages/mappings");
	}).enter(setPageBackground);

Path.map("#technics").to(function(){
	   $("#output .content").load("/pages/technics");
	}).enter(setPageBackground);


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
});

var shiptypesList = [{value:"MAIL", text:"Per Post"},{value:"PICKUP",text:"Abholung"}, {value:"PUBLISHER", text:"über Verlag"}, {value:"DELIVERY", text:"Lieferung d. Buchlese"}];
var laedenList = [{value:1, text:"Dornhan"},{value:2, text:"Sulz"},{value:3, text:"Schramberg"}];
