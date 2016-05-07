<div class="container">

<div class="container">
<h1>Abonnent ${sub.customerId?c}</h1>

<h3><a href="#" class="editable" data-type="text" data-name="subscriber.name">${sub.name}</a> </h3>
<div class="row">
<div class="col-md-2">Debitorenkonto</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="subscriber.debitorId">${sub.debitorId?c}</a></div>
</div>
<div class="row">
<div class="col-md-2">Sammelrechnung gewünscht</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="subscriber.collectiveInvoice">${sub.collectiveInvoice?c}</a></div>
</div>
<div class="row">
<div class="col-md-2">Lieferschein nötig</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="subscriber.needsDeliveryNote">${sub.needsDeliveryNote?c}</a></div>
</div>
<div class="row">
<div class="col-md-2">Standard-Versandart</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="subscriber.shipmentType">${sub.shipmentType}</a></div>
<div class="col-md-5"></div>
<div class="col-md-2"><a href="/subscr/createCollInvoice/${sub.id?c}" class="btn btn-primary" target="_blank">Sammelrechnung erstellen</a></div>
</div>

<h3>Abos</h3>
<div id="accordion" role="tablist" aria-multiselectable="true">
<#list subscriptions as s>
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading${s.id}">
      <h4 class="panel-title">
        <a data-toggle="collapse" data-parent="#accordion" href="#delivs${s.id}" aria-expanded="false" aria-controls="delivs${s.id}">
          Abo Nr ${s.id}, ${product(s)}, ${s.deliveryInfo1!""}, ${s.deliveryInfo2!""} 
          <span class="label label-pill label-default">${s.quantity}</span> 
          <a href="#subscription/${s.id?c}" class="btn btn-info">Abodetails</a>
        </a>
      </h4>
    </div>
    <div id="delivs${s.id}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${s.id}">
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
				<#list deliveriesWithout(s) as d>
					<tr>
						<td>${d.deliveryDate.toString("dd.MM.yy")}</td>
						<td>${artikelbez(d)!"n. bek."}</td>
						<td>${money(d.article.brutto)}</td>
						<td>  <a href="#subscrDelivery/${d.id?c}">see</a> </td>
					</tr>
				</#list>
			</tbody>
		</table>
    </div>
  </div>
</#list>
</div>  


</div>
<script>
	$('.editable').editable({
	    url: '/subscr/update', pk:'${sub.id?c}'
	});
</script>


