<div class="container">
<h1>Abonnement ${sub.id} <a href="#subscrDispo/${sub.product.id?c}" class="btn btn-primary">Dispo</a>
<#if needsInvoice() ><a href="/subscr/pdfcreateInvoice/${sub.id?c}" class="btn btn-primary" target="_blank">Rechnungsvorschau</a></#if>
</h1>

  <h4>Periodikum ${p.name}  <a href="#subscrProduct/${p.id?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></h4>
  <h4>Abonnent ${kunde().name}   <a href="#subscriber/${sub.subscriber.id?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a> </h4>
<div class="row">
  <div class="col-md-2">Letzte Lieferung</div>
  <div class="col-md-4">${(lastDelivery.articleName)!} am ${(lastDelivery.deliveryDate)!}</div>
</div>
<div class="row">
  <div class="col-md-2">Letztes Intervall </div>
  <div class="col-md-4">${(lastIntDelivery.intervalName)!}</div>
</div>
<div class="row">
  <div class="col-md-2">Letzte Rechnung am</div>
<div class="col-md-4">${(sub.lastInvoiceDate)!}</div>
</div>


<ul class="nav nav-tabs">
  <li class="active"><a data-toggle="tab" href="#details">Abodaten</a></li>
  <li><a data-toggle="tab" href="#lieferungenub">unberechnete Lieferungen</a></li>
  <li><a data-toggle="tab" href="#lieferungenb">berechnete Lieferungen</a></li>
  <li><a data-toggle="tab" href="#intervalleub">unberechnete Intervalle</a></li>
  <li><a data-toggle="tab" href="#intervalleb">berechnete Intervalle</a></li>
  <li><a data-toggle="tab" href="#rechnungen">Rechnungen</a></li>
</ul>

<div class="tab-content">
  <div id="details" class="tab-pane fade in active">
<div cass="row">
<div class="col-md-4">
<div class="card card-block">
 	<h3 class="card-title">Berechnung</h3>
    <ul class="list-group list-group-flush">
    	<li class="list-group-item">Start <a href="#" class="editable" data-type="date" data-format="dd.mm.yyyy" data-mode="popup" data-name="subscription.startDate">${(sub.startDate)!}</a></li>
    	<li class="list-group-item">Ende <a href="#" class="editable" data-type="date" data-format="dd.mm.yyyy" data-mode="popup" data-name="subscription.endDate">${(sub.endDate)!}</a></li>
    	<li class="list-group-item">Menge <a href="#" class="editable" data-type="text" data-name="subscription.quantity">${sub.quantity}</a></li>
    	<li class="list-group-item">Zahlungsintervall <a href="#" id="paytype"></a></li>
    	<li class="list-group-item">Bezahlt bis <a href="#" class="editable" data-type="date" data-format="mm/yyyy" data-mode="popup" data-name="subscription.payedUntil">${sub.payedUntil!}</a></li>
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
<a href="#" class="editable" data-type="text" data-name="subscription.deliveryAddress.name1">${(sub.deliveryAddress.name1)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscription.deliveryAddress.name2">${(sub.deliveryAddress.name2)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscription.deliveryAddress.name3">${(sub.deliveryAddress.name3)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscription.deliveryAddress.street">${(sub.deliveryAddress.street)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscription.deliveryAddress.postalcode">${(sub.deliveryAddress.postalcode)!}</a> <a href="#" class="editable" data-type="text" data-name="subscription.deliveryAddress.city">${(sub.deliveryAddress.city)!}</a> 
    	</li> 
    </ul>
</div>
</div>

<div class="col-md-4">
<div class="card card-block">
 	<h3 class="card-title">Kontaktdaten</h3>
    <ul class="list-group list-group-flush">
    	<li class="list-group-item">Telefon <a href="#" class="editableSub" data-type="text" data-name="subscriber.telephone">${(kunde().telephone)!}</a></li>
    	<li class="list-group-item">Email <a href="#" class="editableSub" data-type="email" data-name="subscriber.email">${(kunde().email)!}</a></li>
    	<li class="list-group-item">Bookmark <input type="checkbox" ${sub.needsAttention?string('checked','')} onChange="switchCheckbox('subscription.needsAttention', this.checked)"></li>
    	<li class="list-group-item">Memo <a href="#" class="editable" data-type="textarea" data-name="subscription.memo">${sub.memo!}</a></li>
    </ul>
</div>
</div>
</div>
  </div>
  <div id="lieferungenub" class="tab-pane fade">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Lieferdatum</th>
					<th>Artikel</th>
					<th>Preis</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list deliveriesWithout as d>
					<tr>
						<td>${d.deliveryDate}</td>
						<td>${d.articleName!}</td>
						<td>${money(d.total)}</td>
						<td>  <a href="#subscrDelivery/${d.id?c}">see</a> </td>
					</tr>
				</#list>
			</tbody>
		</table>
  </div>
  <div id="lieferungenb" class="tab-pane fade">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Lieferdatum</th>
					<th>Artikel</th>
					<th>Preis</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list deliveriesWith as d>
					<tr>
						<td>${d.deliveryDate}</td>
						<td>${d.articleName!}</td>
						<td>${money(d.total)}</td>
						<td>  <a href="#subscrDelivery/${d.id?c}">see</a> </td>
					</tr>
				</#list>
			</tbody>
		</table>
  </div>
  <div id="intervalleub" class="tab-pane fade">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Interval</th>
					<th>Preis</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list intervalDeliveriesWithout as d>
					<tr>
						<td>${d.intervalName!}</td>
						<td>${money(d.total)}</td>
						<td>  <a href="#subscrIntervalDelivery/${d.id?c}">see</a> </td>
					</tr>
				</#list>
			</tbody>
		</table>
  </div>
  <div id="intervalleb" class="tab-pane fade">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Intervall</th>
					<th>Preis</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list intervalDeliveriesWith as d>
					<tr>
						<td>${d.intervalName!}</td>
						<td>${money(d.total)}</td>
						<td>  <a href="#subscrIntervalDelivery/${d.id?c}">see</a> </td>
					</tr>
				</#list>
			</tbody>
		</table>
  </div>
  <div id="rechnungen" class="tab-pane fade">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Rechnungsnummer</th>
					<th>Datum</th>
					<th>Betrag</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list invoices as inv>
					<tr>
						<td>${inv.number}</td>
						<td><#if inv.date??>${inv.date}<#else>kein Datum!</#if></td>
						<td align="right">${money(inv.amount)}</td>
						<td>  <a href="/invoice/${inv.number}" target="_blank">view</a> </td>
					</tr>
				</#list>
			</tbody>
		</table>
  </div>
</div>


</div>

<script>
	var switchCheckbox = function(fieldname, val) {
		 $.post("/subscr/update", { pk: '${sub.id?c}', name: fieldname, value : val});
	}

	 $('.editableSub').editable({
	    url: '/subscr/update', pk:'${sub.subscriber.id?c}',
	    success: function(res, newValue) {
	        if(!res.success) return res.msg;
	    }
	});

	 $('.editable').editable({
		    url: '/subscr/update', pk:'${sub.id?c}',
		    success: function(res, newValue) {
		        if(!res.success) return res.msg;
		    }
		});

	$('.editablemoney').editable({
	    url: '/subscr/update', pk:'${newestArticle.id?c}',
	    mode : "popup",
	    display : function(value, jsonresponse) {
	    	$(this).text(formatMoney(value));
	    },
	    success: function(res, newValue) {
	        if(!res.success) return res.msg;
	        $("#brutto").editable('setValue',res.brutto, true);
	        $("#brutto2").editable('setValue',res.brutto, true);
	        $("#brutto_half").editable('setValue',res.bruttoHalf, true);
	        $("#brutto_full").editable('setValue',res.bruttoFull, true);
	    }
	});
		
	$('#shiptype').editable({
	    url: '/subscr/update', pk:'${sub.id?c}',
		type : "select",
		value : "${sub.shipmentType}",
		name : "subscription.shipmentType",
		showbuttons:false,
		source : shiptypesList
	});

	$('#paytype').editable({
	    url: '/subscr/update', pk:'${sub.id?c}',
		type : "select",
		value : "${sub.paymentType}",
		name : "subscription.paymentType",
		showbuttons:false,
		source : paytypesList
	});

</script>


