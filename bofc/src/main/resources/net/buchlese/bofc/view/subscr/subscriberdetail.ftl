<div class="container">

<div class="container">
<h1>Abonnent ${sub.customerId?c}</h1>

<h3><a href="#" class="editable" data-type="text" data-name="subscriber.name">${sub.name}</a> </h3>
<div class="row">
<div class="col-md-2">Ladengeschäft</div>
<div class="col-md-2"><a href="#" id="laeden"></a></div>
<div class="col-md-5"></div>
<div class="col-md-2"><a href="/subscr/createCollInvoice/${sub.id?c}" class="btn btn-primary" target="_blank">Sammelrechnung erstellen</a></div>
</div>
<div class="row">
<div class="col-md-2">Debitorenkonto</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="subscriber.debitorId">${sub.debitorId?c}</a></div>
</div>
<div class="row">
<div class="col-md-2">Sammelrechnung</div>
<div class="col-md-2"><input type="checkbox" ${sub.collectiveInvoice?string('checked','')} onChange="switchCheckbox('subscriber.collectiveInvoice', this.value)"></div>
</div>
<div class="row">
<div class="col-md-2">Lieferschein nötig</div>
<div class="col-md-2"><input type="checkbox" ${sub.needsDeliveryNote?string('checked','')} onChange="switchCheckbox('subscriber.needsDeliveryNote', this.value)"></div>
</div>
<div class="row">
<div class="col-md-2">Standard-Versandart</div>
<div class="col-md-2"><a href="#" id="shiptype"></a></div>
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


<div class="row">
<div class="col-md-3"><h3>Abos</h3></div>
<div class="col-md-offset-6 col-md-2"><a href="#subscrSubscrAdd/${sub.id?c}" class="btn btn-primary">Neues Abo</a></div>
</div>
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
						<td>${d.articleName!}</td>
						<td>${money(d.total)}</td>
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


