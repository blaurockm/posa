<div class="container">
<h1>Abonnent ${sub.customerId?c}  <a href="#" class="editable" data-type="text" data-name="subscriber.name">${sub.name}</a>
<a href="#subscrSubscrAdd/${sub.id?c}/0" class="btn btn-primary">Neues Abo</a>
<#if sub.collectiveInvoice! >
<a href="/subscr/pdfcreateCollInvoice/${sub.id?c}" class="btn btn-primary" target="_blank">Sammelrechnung erstellen</a>
</#if>
</h1>

<div class="row">
<div class="col-md-2">Ladengeschäft</div>
<div class="col-md-2"><a href="#" id="laeden"></a></div>
</div>
<div class="row">
<div class="col-md-2">Debitorenkonto</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="subscriber.debitorId">${sub.debitorId?c}</a></div>
</div>
<div class="row">
<div class="col-md-2">Sammelrechnung</div>
<div class="col-md-2"><input type="checkbox" ${sub.collectiveInvoice?string('checked','')} onChange="switchCheckbox('subscriber.collectiveInvoice', this.checked)"></div>
</div>
<div class="row">
<div class="col-md-2">Lieferschein nötig</div>
<div class="col-md-2"><input type="checkbox" ${sub.needsDeliveryNote?string('checked','')} onChange="switchCheckbox('subscriber.needsDeliveryNote', this.checked)"></div>
</div>
<div class="row">
<div class="col-md-2">Standard-Versandart</div>
<div class="col-md-2"><a href="#" id="shiptype"></a></div>
</div>
<div class="row">
<div class="col-md-2">Telefon</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="subscriber.telephone">${sub.telephone!}</a></div>
</div>
<div class="row">
<div class="col-md-2">Email</div>
<div class="col-md-2"><a href="#" class="editable" data-type="email" data-name="subscriber.email">${sub.email!}</a></div>
</div>
<div class="row">
<div class="col-md-2">Rechnungsadresse</div>
<div class="col-md-4">
<a href="#" class="editable" data-type="text" data-name="subscriber.name1">${(sub.invoiceAddress.name1)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscriber.name2">${(sub.invoiceAddress.name2)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscriber.name3">${(sub.invoiceAddress.name3)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscriber.street">${(sub.invoiceAddress.street)!}</a> <br>
<a href="#" class="editable" data-type="text" data-name="subscriber.postalcode">${(sub.invoiceAddress.postalcode)!}</a> <a href="#" class="editable" data-type="text" data-name="subscriber.city">${(sub.invoiceAddress.city)!}</a> 
</div>
</div>

<ul class="nav nav-tabs">
  <li class="active"><a data-toggle="tab" href="#abos">Abonnements</a></li>
  <li><a data-toggle="tab" href="#rechnungen">Rechnungen</a></li>
</ul>

<div class="tab-content">
  <div id="abos" class="tab-pane fade in active">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Abonummer</th>
					<th>Periodikum</th>
					<th>Seit</th>
					<th>Versandart</th>
					<th>Zahlungweise</th>
					<th>abrechenbar</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list subscriptions as s>
					<tr>
						<td>${s.id} <a href="#subscription/${s.id?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></td>
						<td>${product(s)}  <a href="#subscrProduct/${s.productId?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></td>
						<td>${(s.startDate.toString("dd.MM.yyyy"))!}</td>
					    <td>${s.shipmentType.text}</td>
					    <td>${s.paymentType.text}</td>
					    <td>${willBeSettled(s)?string("ja","")}</td>					
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
<script>
     

     var switchCheckbox = function(fieldname, val) {
    	 $.post("/subscr/update", { pk: '${sub.id?c}', name: fieldname, value : val});
     }
     
	$('.editable').editable({
	    url: '/subscr/update', pk:'${sub.id?c}'
	});

	$('#laeden').editable({
	    url: '/subscr/update', pk:'${sub.id?c}',
		type : "select",
		value : "${sub.pointid}",
		name : "subscriber.pointid",
		source : laedenList,
		showbuttons:false
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


