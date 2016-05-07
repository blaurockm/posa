<div class="container">
<h1>Abonnement ${sub.id}</h1>


<div id="accordion" role="tablist" aria-multiselectable="true">
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingDelivsWithout">
      <h4 class="panel-title">
        <a data-toggle="collapse" data-parent="#accordion" href="#delivsWithout" aria-expanded="false" aria-controls="delivsWithout">
          Lieferungen unberechnet <span class="label label-pill label-default">${sub.quantity}</span> 
          <a href="/subscr/createInvoice/${sub.id?c}" class="btn btn-primary" target="_blank">Rechnung erstellen</a>
        </a>
      </h4>
    </div>
    <div id="delivsWithout" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingDelivsWithout">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Lieferdatum</th>
					<th>Artikel</th>
					<th>Rech</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list deliveriesWithout as d>
					<tr>
						<td>${d.deliveryDate.toString("dd.MM.yy")}</td>
						<td>${artikelbez(d)!"n.bek."}</td>
						<td>${money(d.article.brutto)}</td>
						<td>  <a href="#subscrDelivery/${d.id?c}">see</a> </td>
					</tr>
				</#list>
			</tbody>
		</table>
    </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingDelivsWith">
      <h4 class="panel-title">
        <a data-toggle="collapse" data-parent="#accordion" href="#delivsWith" aria-expanded="false" aria-controls="delivsWith">
          Lieferungen berechnet <span class="label label-pill label-default">${sub.quantity}</span> 
        </a>
      </h4>
    </div>
    <div id="delivsWith" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingDelivsWith">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Lieferdatum</th>
					<th>Artikel</th>
					<th>Rech</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list deliveriesWith as d>
					<tr>
						<td>${d.deliveryDate.toString("dd.MM.yy")}</td>
						<td>${artikelbez(d)!"n.bek."}</td>
						<td>${money(d.article.brutto)}</td>
						<td>  <a href="#subscrDelivery/${d.id?c}">see</a> </td>
					</tr>
				</#list>
			</tbody>
		</table>
    </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingInv">
      <h4 class="panel-title">
        <a data-toggle="collapse" data-parent="#accordion" href="#invoices" aria-expanded="false" aria-controls="invoices">
          Rechnungen <span class="label label-pill label-default">${sub.quantity}</span> 
        </a>
      </h4>
    </div>
    <div id="invoices" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingInv">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Rechnungsnummer</th>
					<th>Datum</th>
					<th>Betrag</th>
					<th></th>
				</tr>
			</thead>
		</table>
    </div>
  </div>
</div>



<div cass="row">
<div class="col-md-4">
<div class="card card-block">
 	<h3 class="card-title">Berechnung</h3>
    <ul class="list-group list-group-flush">
    	<li class="list-group-item">Start</li>
    	<li class="list-group-item">Ende</li>
    	<li class="list-group-item">Menge <a href="#" class="editable" data-type="text" data-name="subscription.quantity">${sub.quantity}</a></li>
    	<li class="list-group-item">Sammelrechnung</li>
    	<li class="list-group-item">Versandart</li>
    	<li class="list-group-item">Versandkosten</li>
    	<li class="list-group-item">Rechnungsadresse</li> 
    </ul>
</div>
</div>
<div class="col-md-4">
<div class="card card-block">
 	<h3 class="card-title">Lieferung</h3>
    <ul class="list-group list-group-flush">
    	<li class="list-group-item">${kunde(sub)}</li>
    	<li class="list-group-item">Lieferhinweis1</li>
    	<li class="list-group-item">Lieferhinweis2</li>
    	<li class="list-group-item">Lieferschein</li>
    	<li class="list-group-item">Lieferaddresse</li> 
    	<li class="list-group-item">Debitor</li> 
    </ul>
</div>
</div>
<div class="col-md-4">
<div class="card card-block">
 	<h3 class="card-title">Produkt</h3>
    <ul class="list-group list-group-flush">
	<li class="list-group-item">Name <a href="#" class="editable" data-pk="${p.id?c}" data-type="text" data-name="product.publisher">${p.name}</a></li>
	<li class="list-group-item">Periodizität</li>
	<li class="list-group-item">letzte Erscheinung</li>
	<li class="list-group-item">nächste Erscheinung</li>
	<li class="list-group-item">Gesamtmenge</li>
	</ul>
</div>
</div>
</div>




</div>

<script>
	$('.editable').editable({
	    url: '/subscr/update', pk:'${sub.id?c}'
	});
</script>


