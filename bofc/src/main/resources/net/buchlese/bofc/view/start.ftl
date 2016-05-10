<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" ng-app="phpro">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Buchlese - Backoffice</title>
    <link rel="icon" href="../../favicon.ico">
    <!-- Bootstrap
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
	
	<!-- Optional theme -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

	<link href='https://fonts.googleapis.com/css?family=Roboto:700' rel='stylesheet' type='text/css'>

    <!--  rout definitions -->
	<script type="text/javascript" src="https://raw.github.com/mtrpcic/pathjs/master/path.js"></script>

	<!--  in place editing -->
	<link href="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.1/bootstrap3-editable/css/bootstrap-editable.css" rel="stylesheet"/>

  </head>
  <body>
    <nav class="navbar navbar-default navbar-static-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#index">Buchlese</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
           <ul class="nav navbar-nav">
             <li><a href="#subscr" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Periodika <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#subscr">Dashboard</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#subscrCust">Kunden</a></li>
          </ul>             

             <li><a href="#export">Kassenberichte</a></li>
             <li><a href="#rechnungen">Ausgangsrechnungen</a></li>
             <li><a href="#exports">Exporte</a></li>
             <li><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Mappings <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#mappings/1">Dornhan</a></li>
            <li><a href="#mappings/2">Sulz</a></li>
            <li><a href="#mappings/3">Schramberg</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#mappings">Alle</a></li>
          </ul>             
             <li><a href="#commands">Kommandos</a></li>
             <li><a href="#technics">Interna</a></li>
           </ul>
        </div><!--/.navbar-collapse -->
      </div>
    </nav>
    
    <center><div id="postresult"></div></center>

    <div class="container" id="output">
       <!-- content is injected here -->
      <div class="content"></div>
    </div>  

	<div class="modal bs-example-modal-lg" tabindex="-1" role="dialog" id="myModal" aria-labelledby="modal-title">
  	<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="modal-title">Modal title</h4>
      </div>
      <div class="modal-body" id="modal-output">
         <!-- content is injected here -->
         <div class="content"></div>
      </div>
    </div>
  	</div>
	</div>

	<div class="modal bs-example-modal-lg" tabindex="-1" role="dialog" id="entryModal" aria-labelledby="entrymodal-title">
  	<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="entrymodal-title">Modal title</h4>
      </div>
      <div class="modal-body" id="entrymodal-output">
         <!-- content is injected here -->
         <div class="content"></div>
      </div>
	  <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Abbrechen</button>
        <button type="button" id="saveEntry" class="btn btn-primary">Speichern</button>
      </div>
    </div>
  	</div>
	</div>
   
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed 
    <script src="js/bootstrap.min.js"></script> -->
	<script src="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.1/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
	<!-- load up our app -->
	<script src="assets/bofc/startapp.js"></script>
	<script>
	    $(document).ready(function() {
			$.fn.editable.defaults.mode = 'inline';
	    });
	    $('#myModal').on('hidden.bs.modal', function (e) {
	    	  window.history.back();  // in der history einen schritt zurückgehen
	    })
	    $('#entryModal').on('hidden.bs.modal', function (e) {
	    	  window.history.back();  // in der history einen schritt zurückgehen
	    })
	    $('#saveEntry').on('click', function () {
    		// $('#entryForm').submit();
    		$.post( $('#entryForm').prop('action') , $( "#entryForm" ).serialize(), function(info) { 
                $("#postresult").html("Erfolgreich gespeichert!"); 
                //adding class
                $("#postresult").addClass("alert alert-success");
                //timing the alert box to close after 5 seconds
                window.setTimeout(function () {
                    $(".alert").fadeTo(500, 0).slideUp(500, function () {
                        $(this).remove();
                    });
                }, 5000);
            }).fail(function(jqXHR, textStatus) {
                $("#postresult").html("Problem beim Speichern: " + textStatus); 
                //adding class
                $("#postresult").addClass("alert alert-danger");
            });
    	    $("#entryModal").modal('hide');
  		})
	</script>
</body>
</html>