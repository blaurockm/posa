<ol class="breadcrumb">
  <li><a href="#subscr">Dashboard</a></li>
  <li><a href="#subscrProduct/${p.id?c}">Periodikum</a></li>
  <li class="active">Abonnement</li>
</ol>

<div class="container">
<h1>Abonnement ${sub.id}</h1>

  <h4>Periodikum ${p.name}</h4>
  <h4>Abonnent <a href="#" class="editable" data-type="text" data-name="subscriber.name">${kunde()}</a></h4>
<div class="row">
  <div class="col-md-2">Letzte Lieferung</div>
  <div class="col-md-4">${(lastDelivery.articleName)!} am ${(lastDelivery.deliveryDate.toString("dd.MM.yyyy"))!}</div>
  <div class="col-md-2  col-md-offset-4">
      <a href="/subscr/createInvoice/${sub.id?c}" class="btn btn-primary" target="_blank">Rechnung erstellen</a>
  </div>
</div>
<div class="row">
  <div class="col-md-2">Letzte Rechnung</div>
</div>

<div id="accordion" role="tablist" aria-multiselectable="true">
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingDelivsWithout">
      <h4 class="panel-title">
        <a data-toggle="collapse" data-parent="#accordion" href="#delivsWithout" aria-expanded="false" aria-controls="delivsWithout">
          Lieferungen unberechnet  
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
						<td>${d.articleName!}</td>
						<td>${money(d.total)}</td>
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
          Lieferungen berechnet 
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
						<td>${d.articleName!}</td>
						<td>${money(d.total)}</td>
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
          Rechnungen 
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
					<th>bezahlt</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list invoices as inv>
					<tr>
						<td>${inv.number}</td>
						<td><#if inv.date??>${inv.date.toString("dd.MM.yyyy")}<#else>kein Datum!</#if></td>
						<td align="right">${money(inv.amount)}</td>
						<td align="right">${money(inv.payed)}</td>
						<td>  <a href="/invoice/${inv.number}" target="_blank">view</a> </td>
					</tr>
				</#list>
			</tbody>
		</table>
    </div>
  </div>
</div>



<div cass="row">
<div class="col-md-4">
<div class="card card-block">
 	<h3 class="card-title">Berechnung</h3>
    <ul class="list-group list-group-flush">
    	<li class="list-group-item">Start <a href="#" class="editable" data-type="date" data-format="dd.mm.yyyy" data-mode="popup" data-name="subscription.startdDate">${(sub.startDate.toString("dd.MM.YYYY"))!}</a></li>
    	<li class="list-group-item">Ende <a href="#" class="editable" data-type="date" data-format="dd.mm.yyyy" data-mode="popup" data-name="subscription.endDate">${(sub.endDate.toString("dd.MM.YYYY"))!}</a></li>
    	<li class="list-group-item">Menge <a href="#" class="editable" data-type="text" data-name="subscription.quantity">${sub.quantity}</a></li>
    	<li class="list-group-item">Versandkosten <a href="#" class="editablemoney" data-type="text" data-name="subscription.shipmentcost">${sub.shipmentCost}</a></li>
    </ul>
</div>
</div>
<div class="col-md-4">
<div class="card card-block">
 	<h3 class="card-title">Lieferung</h3>
    <ul class="list-group list-group-flush">
    	<li class="list-group-item">Versandart <a href="#" id="shiptype"></a></li>
    	<li class="list-group-item">Lieferhinweis1 <a href="#" class="editable" data-type="text" data-name="subscription.deliveryInfo1">${sub.deliveryInfo1!}</a></li>
    	<li class="list-group-item">Lieferhinweis2 <a href="#" class="editable" data-type="text" data-name="subscription.deliveryInfo2">${sub.deliveryInfo2!}</a></li>
    	<li class="list-group-item">Lieferaddresse <br>
<a href="#" class="editable" data-type="text" data-name="subscription.name1">${(sub.deliveryAddress.name1)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscription.name2">${(sub.deliveryAddress.name2)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscription.name3">${(sub.deliveryAddress.name3)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscription.street">${(sub.deliveryAddress.street)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscription.postalcode">${(sub.deliveryAddress.postalcode)!}</a> <a href="#" class="editable" data-type="text" data-name="subscription.city">${(sub.deliveryAddress.city)!}</a> 
    	</li> 
    </ul>
</div>
</div>

</div>




</div>

<script>
	 $('.editable').editable({
	    url: '/subscr/update', pk:'${sub.id?c}',
	    success: function(res, newValue) {
	        if(!res.success) return res.msg;
	    }
	});
	 
	$('.editablemoney').editable({
	    url: '/subscr/update', pk:'${sub.id?c}',
	    mode : "popup",
	    display : function(value, jsonresponse) {
	    	$(this).text(formatMoney(value));
	    },
	    success: function(res, newValue) {
	        if(!res.success) return res.msg;
	    }
	});
		
	$('#shiptype').editable({
	    url: '/subscr/update', pk:'${sub.id?c}',
		type : "select",
		value : "${sub.shipmentType}",
		name : "subscriber.pointid",
		showbuttons:false,
		source : shiptypesList
	});

</script>


