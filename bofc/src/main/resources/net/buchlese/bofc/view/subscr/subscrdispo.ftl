<ol class="breadcrumb">
  <li><a href="#subscr">Dashboard</a></li>
  <li class="active">Dispo</li>
</ol>

<div class="container">
<h1>Periodikadisposition ${p.name}</h1>

<div class="row">
<div class="col-md-6">von ${p.publisher}</div>
</div>
<div class="row">
<div class="col-md-6">für den ${dispoDate.toString("dd.MM.yy")}</div>
</div>
<div class="row">
<div class="col-md-6">Bestellte Menge ${p.quantity}</div>
</div>

<div class="row">

<div class="col-md-5">
  <div class="card card-block">
    <h3 class="card-title">auszuliefernder Artikel</h3>
    <ul class="list-group list-group-flush">
      <li class="list-group-item"><button onclick="loadPrevArticle()">&lt;&lt;</button> Artikel-ID <span id="artid">${art.id?c}</span>
       <button onclick="loadNextArticle()">&gt;&gt;</button>
      </li>
      <li class="list-group-item">Name <a href="#" id="artname" class="editable" data-type="text"  data-name="article.name"  data-title="Artikelbezeichnung">${art.name}</a></li>
      <li class="list-group-item">Erscheinungsdatum <a href="#" id="artersch" class="namechangerfield" data-type="date" data-format="dd.mm.yyyy" data-name="article.erschTag"  data-title="Erscheinungstag" >${art.erschTag.toString("dd.MM.yyyy")}</a></li>
      <li class="list-group-item">Ausgabe <a href="#" id="artcount" class="namechangerfield" data-type="number"  data-name="article.issueNo"  data-title="Ausgabennummer">${art.issueNo}</a></li>
    </ul>
   </div>
</div>

<div class="col-md-5">
  <div class="card card-block">
   <h3 class="card-title">Preis</h3>
    <ul class="list-group list-group-flush">
      <li class="list-group-item">Gesamt Brutto <a href="#" id="brutto" class="editablemoney" data-type="text"  data-name="article.brutto"  data-title="Gesamtbetrag">${art.brutto?c}</a></li>
      <li class="list-group-item">7% Brutto <a href="#" id="brutto_half" class="editablemoney" data-type="text"  data-name="article.brutto_half"  data-title="Betrag Print-Anteil" >${art.brutto_half?c}</a>    Anteil am Gesamtpreis <a href="#" id="half_percent" class="percentagefield" data-type="text"  data-name="article.halfPercentage"  data-title="Prozent Print-Anteil" >${art.halfPercentage?c}</a></li>
      <li class="list-group-item">19% Brutto <a href="#" id="brutto_full" class="editablemoney" data-type="text"  data-name="article.brutto_full"  data-title="Betrag Online-Anteil"  >${art.brutto_full?c}</a> </li>
	</ul>
   </div>
</div>
<div class="col-md-1">
  <button id="artikelpluseins" class="btn btn-primary">Artikel +1</button>
</div>
</div>


<h2>betroffene Abos</h2>

<table class="table table-striped">
	<thead>
		<tr>
			<th>AboNr</th>
			<th>Abonnent</th>
			<th>Menge</th>
			<th>LS</th>
			<th>Versandart</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
<#list subscriptions as sub>
		<tr>
			<td>${sub.id?c}</td>
			<td>${kunde(sub)} ${sub.deliveryInfo1!""}, ${sub.deliveryInfo2!""}</td>
			<td>${sub.quantity}</td>
			<td></td>
			<td>${sub.shipmentType}</td>
			<td>
			<#assign deli = delivery(sub, art)!"hh" >
			<#if deli != "hh" >
   	  	  	   <a href="#subscrDelivery/${deli.id?c}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
			<#else>
			   <button id="create${sub.id?c}" class="btn btn-default">anlegen</button>
			 <script>
   	   	 	   $( "#create${sub.id?c}" ).click(function() {
   	    		 $.getJSON("/subscr/deliverycreate/${sub.id?c}/" + article.id + "/${dispoDate}", function() {console.log( "deliverycreate success" );})
   	    		 $( "#create${sub.id?c}" ).hide();
				});
			 </script>
			</#if>
			 </td>
		</tr>
</#list>
	</tbody>
</table>
</div>

<script>
	var article = null;
	
	var loadPrevArticle = function() {
	 	 $.getJSON("/subscr/subscrarticle/prev/${art.productId?c}/" + article.id, displayArticle);
	} 

	var loadNextArticle = function() {
	 	 $.getJSON("/subscr/subscrarticle/next/${art.productId?c}/" + article.id, displayArticle);
	} 

	var displayArticle = function(res) {
		article = res;
        $("#artname").editable('setValue',res.name, false);
        $("#brutto").editable('setValue',res.brutto, true);
        $("#brutto_half").editable('setValue',res.brutto_half, true);
        $("#brutto_full").editable('setValue',res.brutto_full, true);
        $("#half_percent").editable('setValue',res.halfPercentage, false);
        $("#artid").text(res.id);
        $("#artcount").editable('setValue',res.issueNo, true);
	} 


    $('.editable').editable({
	    url: '/subscr/update', pk:'${art.id?c}',
	});
	$('.namechangerfield').editable({
	    url: '/subscr/update', pk:'${art.id?c}',
	    mode : "popup",
		success:function(res, newValue) {
	        if(!res.success) return res.msg;
	        $("#artname").editable('setValue',res.name, false);
	    }
    });
	$('.percentagefield').editable({
	    url: '/subscr/update', pk:'${art.id?c}',
	    mode : "popup",
	    display : function(value, jsonresponse) {
	    	$(this).text(value * 100 + " %");
	    },
		success:function(res, newValue) {
	        if(!res.success) return res.msg;
	        $("#brutto_half").editable('setValue',res.bruttoHalf, true);
	        $("#brutto_full").editable('setValue',res.bruttoFull, true);
	    }
    });
	$('.editablemoney').editable({
	    url: '/subscr/update', pk:'${art.id?c}',
	    mode : "popup",
	    display : function(value, jsonresponse) {
	    	$(this).text(formatMoney(value));
	    },
	    success: function(res, newValue) {
	        if(!res.success) return res.msg;
	        $("#brutto").editable('setValue',res.brutto, true);
	        $("#brutto_half").editable('setValue',res.bruttoHalf, true);
	        $("#brutto_full").editable('setValue',res.bruttoFull, true);
	        $("#half_percent").editable('setValue',res.halfPercentage, false);
	    },
	    validate: function(value) {
	        if($.trim(value) == '') {
	            return 'This field is required';
	        }
	    }
	});
    $( '#artikelpluseins' ).click(function() {
	 	 $.getJSON("/subscr/subscrarticlecreate/${art.productId?c}", displayArticle)
	 	 $( "#artikelpluseins" ).hide();
	});
    
    $.getJSON("/subscr/subscrarticle/ex/${art.productId?c}/${art.id?c}", displayArticle);
    
    
    
</script>
