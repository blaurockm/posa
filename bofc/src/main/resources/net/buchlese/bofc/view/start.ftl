<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Buchlese - Backoffice</title>
    <link rel="icon" href="../../favicon.ico">
    <!-- Bootstrap
    <link href="css/bootstrap.min.css" rel="stylesheet">  -->
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

	<link href='https://fonts.googleapis.com/css?family=Roboto:700' rel='stylesheet' type='text/css'>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
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
          <a class="navbar-brand" href="#">Buchlese</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
           <ul class="nav navbar-nav">
             <li><a href="#kasse">Kassenberichte</a></li>
             <li><a href="#rechnungen">Ausgangsrechnungen</a></li>
           </ul>
        </div><!--/.navbar-collapse -->
      </div>
    </nav>
    
    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron" style="background:#a4c408; font-family: 'Roboto', sans-serif">
		<div class="container">
			<div >
			  	<iframe  class="col-md-12" height="300px" src="/state"> </iframe> 
			</div>
		</div>
	</div>

    <section id="kasse">
    <div class="container">
      <!-- Example row of columns -->
      <div class="row">
        <div class="col-md-4">
          <h2>Kasse Dornhan</h2>
          <form class="form-horizontal" action="/cashbalance/fibuexport" method="POST">
            <input type="hidden" name="kasse" value="1">
            <div class="form-group">
              <label for="dornhanFrom">Ab Datum</label>
              <input type="date" placeholder="ab" name="from" class="form-control" id="dornhanFrom" value="${dornhanFrom}">
            </div>
            <div class="form-group">
              <label for="dornhanTill">Bis Datum</label>
              <input type="date" placeholder="bis" name="till" class="form-control" id="dornhanTill" value="${dornhanTill}">
            </div>
            <button type="submit" class="btn btn-success">Export</button>
          </form>  
    	</div>
        <div class="col-md-4">
          <h2>Kasse Sulz</h2>
          <form class="form-horizontal" action="/cashbalance/fibuexport" method="POST">
            <input type="hidden" name="kasse" value="2">
            <div class="form-group">
              <label for="sulzFrom">Ab Datum</label>
              <input type="date" placeholder="ab" name="from" class="form-control" id="sulzFrom" value="${sulzFrom}">
            </div>
            <div class="form-group">
              <label for="sulzTill">Bis Datum</label>
              <input type="date" placeholder="bis" name="till" class="form-control" id="sulzTill" value="${sulzTill}">
            </div>
            <button type="submit" class="btn btn-success">Export</button>
          </form>  
    	</div>
        <div class="col-md-4">
          <h2>Kasse Schramberg</h2>
          <form class="form-horizontal" action="/cashbalance/fibuexport" method="POST">
            <input type="hidden" name="kasse" value="3">
            <div class="form-group">
              <label for="schrambergFrom">Ab Datum</label>
              <input type="date" placeholder="ab" name="from" class="form-control" id="schrambergFrom" value="${schrambergFrom}">
            </div>
            <div class="form-group">
              <label for="schrambergTill">Bis Datum</label>
              <input type="date" placeholder="ab" name="till" class="form-control" id="schrambergTill" value="${schrambergTill}">
            </div>
            <button type="submit" class="btn btn-success">Export</button>
          </form>  
    	</div>
	  </div>    	
    
    </div> <!-- /container -->
	</section>
	
	<section id="rechnungen">
	   <div class="container">
	      <div class="col-md-12">
          <h2>Ausgangsrechnungen</h2>
	        <ul class="list-group">
   <#list invoices as inv>
     <li class="list-group-item">
      ${inv.number}
      ${inv.name1} (${inv.customerId})
   	  ${money(inv.amount)} vom <#if inv.date??>${inv.date.toString("dd.MM.yyyy")}<#else> kein Datum?</#if>  
   	  <span class="badge">${inv.debitorId}</span>
	 </li>   
   </#list>
	        </ul>
	      </div>
	   </div>
	</section>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed 
    <script src="js/bootstrap.min.js"></script> -->
</body>
</html>