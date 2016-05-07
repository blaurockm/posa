
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
      <li class="list-group-item">Artikel-ID ${art.id?c}</li>
      <li class="list-group-item">Name <a href="#"  class="editable" data-type="text"  data-name="article.name"  data-title="Artikelbezeichnung">${art.name}</a></li>
      <li class="list-group-item">Erscheinungsdatum <a href="#" class="editable" data-type="date"  data-name="article.erschTag"  data-title="Betrag Print-Anteil"  >${art.erschTag.toString("dd.MM.yy")}</a></li>
      <li class="list-group-item">Ausgabe <a href="#" class="editable" data-type="number"  data-name="article.issueNo"  data-title="Betrag Print-Anteil" >${art.issueNo}</a></li>
    </ul>
   </div>
</div>

<div class="col-md-5">
  <div class="card card-block">
   <h3 class="card-title">Preis</h3>
    <ul class="list-group list-group-flush">
      <li class="list-group-item">Gesamt Brutto <a href="#" class="editable" data-type="text"  data-name="article.brutto"  data-title="Gesamtbetrag">${art.brutto?c}</a> € </li>
      <li class="list-group-item">7% Brutto <a href="#" class="editable" data-type="text"  data-name="article.brutto_half"  data-title="Betrag Print-Anteil" >${art.brutto_half?c}</a> €    % Anteil <a href="#"  class="editable" data-type="text"  data-name="article.halfPercentage"  data-title="Prozent Print-Anteil" >${art.halfPercentage?c}</a></li>
      <li class="list-group-item">19% Brutto <a href="#"  class="editable" data-type="text"  data-name="article.brutto_full"  data-title="Betrag Online-Anteil"  >${art.brutto_full?c}</a> € </li>
	</ul>
   </div>
</div>      
<div class="col-md-1">
  <button id="#artikelpluseins" class="btn btn-primary">Artikel +1</button>
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
			<th>Rech?</th>
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
			<td>${sub.needsDeliveryNote?c}</td>
			<td>${sub.needsInvoice?c}</td>
			<td></td>
			<td>
			<#assign deli = delivery(sub, art)!"hh" >
			<#if deli != "hh" >
   			   <a href="#subscrDelivery/${deli.id?c}">see</a>
			<#else>
			   <button id="create${sub.id}">anlegen</button>
			 <script>
   	   	 	   $( "#create${sub.id}" ).click(function() {
   	    		 $.getJSON("/subscr/deliverycreate/${sub.id?c}/${art.id?c}/${dispoDate}", function() {console.log( "deliverycreate success" );})
   	    		 $( "#create${sub.id}" ).hide();
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
	$('.editable').editable({
	    url: '/subscr/update', pk:'${art.id?c}'
	});
    $( "#artikelpluseins" ).click(function() {
	 	 $.getJSON("/subscr/articlecreate/${art.id?c}", function() {console.log( "articlecreate success" );})
	 	 $( "#artikelpluseins" ).hide();
	});
</script>
