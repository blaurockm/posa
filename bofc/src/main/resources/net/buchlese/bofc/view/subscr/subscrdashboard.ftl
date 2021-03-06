
<div class="row">
<div class="col-md-6">
<h1>Dashboard</h1>
</div>
<div class="col-md-offset-4 col-md-2">
 <a href="/subscr/deliveraddresslist/" class="btn btn-primary" target="_blank">Adressprotokoll</a>
 </div>
</div>
<ul class="nav nav-tabs">
  <li class="active"><a data-toggle="tab" href="#heutigeRech">unberechnete Lieferungen <span class="label label-pill label-default">${deliveries?size}</span></a></li>
  <li><a data-toggle="tab" href="#heutigeSchein">Lieferung für Lieferschein <span class="label label-pill label-default">${unslippedDeliveries?size}</span></a></li>
  <li><a data-toggle="tab" href="#intervalle">unbezahlte Intervalle <span class="label label-pill label-default">${intervalDeliveries?size}</span></a></li>
</ul>

<div class="tab-content">
<div id="heutigeRech" class="tab-pane fade in active">
<table class="table table-striped">
	<thead>
		<tr>
			<th>Kunde</th>
			<th>Artikel</th>
			<th>Menge</th>
			<th>Versandart</th>
			<th align="right">Betrag</th>
			<th>Datum</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list deliveries as d>
		<tr>
			<td>${d.subscriber.name!} ${d.subscription.deliveryInfo1!} ${d.subscription.deliveryInfo2!}</td>
			<td>${d.articleName}</td>
			<td>${d.quantity}</td>
			<td>${abo(d).shipmentType.text}</td>
			<td align="right">${money(d.total)}</td>
			<td>${(d.deliveryDate)!}</td>
			<td><a href="#subscrDelivery/${d.id?c}" data-toggle="tooltip" title="Details" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
            <a href="#subscription/${d.subscription.id?c}" data-toggle="tooltip" title="Abo" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
			<a href="/subscr/deliverynote/${d.id?c}" data-toggle="tooltip" title="Lieferschein" class="btn btn-default btn-sm" target="_blank"><span class="glyphicon glyphicon-folder-open" aria-hidden="true" alt="Lieferschein"></span></a>
			<a href="#deliverydelete/${d.id?c}" data-toggle="tooltip" title="Löschen" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></td>
		</tr>
		</#list>
	</tbody>
</table>
</div>
<div id="heutigeSchein" class="tab-pane fade">
<table class="table table-striped">
	<thead>
		<tr>
			<th>Kunde</th>
			<th>Artikel</th>
			<th>Menge</th>
			<th>Versandart</th>
			<th>Zahlweise</th>
			<th>Datum</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list unslippedDeliveries as d>
		<tr>
			<td>${d.subscriber.name!} ${d.subscription.deliveryInfo1!} ${d.subscription.deliveryInfo2!}</td>
			<td>${d.articleName}</td>
			<td>${d.quantity}</td>
			<td>${d.subscription.shipmentType.text}</td>
			<td>${d.subscription.paymentType.text}</td>
			<td>${d.deliveryDate!}</td>
			<td><a href="#subscrDelivery/${d.id?c}" data-toggle="tooltip" title="Details" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
            <a href="#subscription/${d.subscription.id?c}" data-toggle="tooltip" title="Abo" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
			<a href="/subscr/deliverynote/${d.id?c}" data-toggle="tooltip" title="Lieferschein" class="btn btn-default btn-sm" target="_blank"><span class="glyphicon glyphicon-folder-open" aria-hidden="true" alt="Lieferschein"></span></a>
			<a href="#deliverydelete/${d.id?c}" data-toggle="tooltip" title="Löschen" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></td>
		</tr>
		</#list>
	</tbody>
</table>
</div>
<div id="intervalle" class="tab-pane fade">
<table class="table table-striped">
	<thead>
		<tr>
			<th>Kunde</th>
			<th>Artikel</th>
			<th>Menge</th>
			<th>Versandart</th>
			<th align="right">Betrag</th>
			<th>Berechnet</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list intervalDeliveries as d>
		<tr>
			<td>${d.subscriber.name!} ${abo(d).deliveryInfo1!} ${abo(d).deliveryInfo2!}</td>
			<td>${d.intervalName}</td>
			<td>${d.quantity}</td>
			<td>${abo(d).shipmentType.text}</td>
			<td align="right">${money(d.total)}</td>
			<td>${d.payed?string("ja","")}</td>
			<td><a href="#subscrIntervalDelivery/${d.id?c}" data-toggle="tooltip" title="Details" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
            <a href="#subscription/${d.subscription.id?c}" data-toggle="tooltip" title="Abo" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
			<a href="#intervaldeliverydelete/${d.id?c}" data-toggle="tooltip" title="Löschen" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></td>
		</tr>
		</#list>
	</tbody>
</table>
</div>
</div>	<!-- end nav-content -->	
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
});

</script>

