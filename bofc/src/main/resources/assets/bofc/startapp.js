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
    $("#output .content").load("/subscr");
}).enter(setPageBackground);

Path.map("#subscrProduct/:id").to(function(){
    $("#output .content").load("/subscr/product/" + this.params["id"]);
}).enter(setPageBackground);

Path.map("#subscrDispo/:id").to(function(){
    $("#output .content").load("/subscr/dispo/" + this.params["id"]);
}).enter(setPageBackground);

Path.map("#subscrDelivery/:id").to(function(){
    $("#output .content").load("/subscr/delivery/" + this.params["id"]);
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


$(document).ready(function(){
    // This line is used to start the PathJS listener.  It's good practice to put this
    // call inside some sort of "document ready" listener, in case the default route
    // relies on DOM elements that may not yet be ready.
    Path.listen();
});
