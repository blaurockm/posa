<div class="container">
<h1>Periodikum <a href="#" class="editable" data-type="text" data-name="product.name">${p.name}</a>
<a href="#subscrDispo/${p.id?c}" class="btn btn-primary">Dispo</a>
<a href="#subscrCustAdd" class="btn btn-default">Neuer Abonnent</a>
<a href="#subscrSubscrAdd/0/${p.id?c}" class="btn btn-default">Neues Abo</a>
<#if articles?size == 0>
<a href="/subscr/subscrarticlecreate/${p.id?c}" class="btn btn-default">Initial-Artikel</a>
</#if>
</h1>

<h3>von <a href="#" class="editable" data-type="text" data-name="product.publisher">${p.publisher}</a> </h3>
<div class="row">
<div class="col-md-2">Abkürzung</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.abbrev">${p.abbrev}</a></div>
</div>
<div class="row">
<div class="col-md-2">Ausgabenzähler</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.count">${p.count!"1"}</a></div>
</div>
<div class="row">
<div class="col-md-2">Bestellte Menge</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.quantity">${p.quantity!"1"}</a></div>
</div>
<div class="row">
<div class="col-md-2">Periodizität</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.period">${p.period}</a></div>
</div>
<div class="row">
<div class="col-md-2">Rechnungstellung</div>
<div class="col-md-2"><a href="#" id="paytype"></a></div>
</div>
<div class="row">
<div class="col-md-2">Artikel-Namensmuster</div>
<div class="col-md-6"><a href="#" class="editable" data-type="text" data-name="product.namePattern">${p.namePattern}</a></div>
</div>
<div class="row">
<div class="col-md-2">Rechnung bis</div>
<div class="col-md-2"><a href="#" class="editable" data-type="date" data-format="dd.mm.yyyy" data-mode="popup" data-name="product.lastInterval">${(p.lastInterval.toString("dd.MM.YYYY"))!}</a></div>
</div>
<div class="row">
<div class="col-md-2">Proz Print-Anteil</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.halfPercent">${p.halfPercentage!"1"}</a></div>
</div>
<div class="row">
<div class="col-md-2">Zahlung pro Lieferung</div>
<div class="col-md-2"><input type="checkbox" ${p.payPerDelivery?string('checked','')} onChange="switchCheckbox('product.payPerDelivery', this.checked)"></div>
</div>

<div class="row">
<div class="col-md-2">Bezug seit</div>
<div class="col-md-2"><a href="#" class="editable" data-type="date" data-format="dd.mm.yyyy" data-mode="popup" data-name="product.startDate">${(p.startDate.toString("dd.MM.YYYY"))!}</a></div>
</div>
<div class="row">
<div class="col-md-2">Bezug bis</div>
<div class="col-md-2"><a href="#" class="editable" data-type="date" data-format="dd.mm.yyyy" data-mode="popup" data-name="product.endDate">${(p.endDate.toString("dd.MM.YYYY"))!}</a></div>
</div>
<div class="row">
<div class="col-md-2">Letzte Lieferung</div>
<div class="col-md-2">${(p.lastDelivery.toString("dd.MM.yy"))!}</div>
</div>


<ul class="nav nav-tabs">
  <li class="active"><a data-toggle="tab" href="#abos">Abonnements</a></li>
  <li><a data-toggle="tab" href="#artikel">Artikel</a></li>
  <li><a data-toggle="tab" href="#intervalle">Intervalle</a></li>
  <li><a data-toggle="tab" href="#rechnungen">Rechnungen</a></li>
</ul>

<div class="tab-content">
  <div id="abos" class="tab-pane fade in active">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Abonummer</th>
					<th>Abonnent</th>
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
						<td>${kunde(s)}  <a href="#subscriber/${s.subscriberId?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></td>
						<td>${(s.startDate.toString("dd.MM.yyyy"))!}</td>
					    <td>${s.shipmentType.text}</td>
					    <td>${s.paymentType.text}</td>
					    <td>${willBeSettled(s)?string("ja","")}</td>					
					</tr>
				</#list>
			</tbody>
		</table>
  </div>
  <div id="artikel" class="tab-pane fade">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Artikelnummer</th>
					<th>Name</th>
					<th>Erscheinungstag</th>
				</tr>
			</thead>
			<tbody>
				<#list articles as a>
					<tr>
						<td>${a.id}</td>
						<td>${a.name}</td>
						<td>${(a.erschTag.toString("dd.MM.yyyy"))!}</td>
					</tr>	
				</#list>
			</tbody>
		</table>
  </div>
  <div id="intervalle" class="tab-pane fade">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Intervallnummer</th>
					<th>Name</th>
					<th>Start</th>
					<th>Ende</th>
					<th>Typ</th>
				</tr>
			</thead>
			<tbody>
				<#list intervals as a>
					<tr>
						<td>${a.id}</td>
						<td>${a.name}</td>
						<td>${(a.startDate.toString("dd.MM.yyyy"))!}</td>
						<td>${(a.endDate.toString("dd.MM.yyyy"))!}</td>
						<td>${a.intervalType}</td>
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
			</tbody>
		</table>
  </div>
</div>


</div>
<script>
	$('.editable').editable({
	    url: '/subscr/update', pk:'${p.id?c}'
	});
    var switchCheckbox = function(fieldname, val) {
   	 $.post("/subscr/update", { pk: '${p.id?c}', name: fieldname, value : val});
    }
	$('#paytype').editable({
	    url: '/subscr/update', pk:'${p.id?c}',
		type : "select",
		value : "${p.intervalType!}",
		name : "product.intervalType",
		showbuttons:false,
		source : paytypesList
	});

</script>

