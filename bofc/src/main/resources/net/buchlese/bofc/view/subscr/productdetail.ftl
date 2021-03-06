<div class="container">
<h1>Periodikum <a href="#" class="editable" data-type="text" data-name="product.name">${p.name}</a>

<a href="#subscrCustAdd" class="btn btn-default">Neuer Abonnent</a>
<a href="#subscrSubscrAdd/0/${p.id?c}" class="btn btn-default">Neues Abo</a>
</h1>

<h3>von <a href="#" class="editable" data-type="text" data-name="product.publisher">${p.publisher}</a> </h3>
<div class="row">
<div class="col-md-2">Abkürzung</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.abbrev">${p.abbrev}</a></div>
</div>
<div class="row">
<div class="col-md-2">ISSN</div>
<div class="col-md-8"><a href="#" class="editable" data-type="text" data-name="product.issn">${p.issn!}</a></div>
<#if p.issn! != "" >
<div class="col-md-2"><a href="http://dispatch.opac.dnb.de/DB=1.1/CMD?ACT=SRCHA&IKT=8&TRM=${p.issn}" target="_blank">ZDB Opac<span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></div>
</#if>
</div>
<div class="row">
<div class="col-md-2">URL zum Verlag</div>
<div class="col-md-8"><a href="#" class="editable" data-type="text" data-name="product.url">${p.url!}</a></div>
<#if p.url! != "" >
<div class="col-md-2"><a href="${p.url}" target="_blank">website<span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></div>
</#if>
</div>

<p></p>

<div class="row">
<div class="col-md-4">
<div class="panel panel-info">
  <div class="panel-heading">
     Lieferinformationen <a href="#subscrDispo/${p.id?c}" class="btn btn-info text-right">Dispo</a>
  </div>
  <div class="panel-body">
     Einstellungen für die Lieferscheine an unsere Kunden
  </div>
  <dl class="dl-horizontal">
    <dt>Ausgabenzähler</dt><dd><a href="#" class="editable" data-type="text" data-name="product.count">${p.count!"1"}</a></dd>
    <dt>Artikel-Namensmuster</dt><dd><a href="#" class="editable" data-type="text" data-name="product.namePattern">${p.namePattern}</a></dd>
  </dl>
  <div class="panel-footer">
	Letzte Lieferung  ${(lastArticle.name)!} am ${(lastArticle.erschTag)!}
  </div>
  </div>
</div>

<div class="col-md-4">
<div class="panel panel-info">
  <div class="panel-heading">
     Weiterberechnung  <a href="#subscrIntervalDispo/${p.id?c}" class="btn btn-info text-right">Abrechnen</a>
  </div>
  <div class="panel-body">
     Einstellungen für die Weiterberechnung an unsere Kunden
  </div>
  <dl class="dl-horizontal">
    <dt>Zahlung pro Lieferung</dt><dd><input type="checkbox" ${p.payPerDelivery?string('checked','')} onChange="switchCheckbox('product.payPerDelivery', this.checked)"></dd>
    <dt>Intervall Namensmuster</dt><dd><a href="#" class="editable" data-type="text" data-name="product.intervalPattern">${p.intervalPattern!}</a></dd>
    <dt>Rechnungstellung</dt><dd><a href="#" id="paytype"></a></dd>
    <dt>Rechnung bis</dt><dd><a href="#" class="editable" data-type="date" data-format="dd.mm.yyyy" data-mode="popup" data-name="product.lastInterval">${(p.lastInterval)!}</a></dd>
    <dt>Proz Print-Anteil</dt><dd><a href="#" class="editable" data-type="text" data-name="product.halfPercent">${p.halfPercentage!"1"}</a></dd>
  </dl>
  <div class="panel-footer">
	Letzte Zahlzeit ${(lastInterval.name)!}
  </div>
</div>
</div>

<div class="col-md-4">
<div class="panel panel-info">
  <div class="panel-heading">
     Buchleseabo
  </div>
  <div class="panel-body">
     Infos über das Abo bei unserem Lieferanten
  </div>
  <dl class="dl-horizontal">
    <dt>Bestellte Menge</dt><dd><a href="#" class="editable" data-type="text" data-name="product.quantity">${p.quantity!"1"}</a></dd>
    <dt>Bezug seit</dt><dd><a href="#" class="editable" data-type="date" data-format="dd.mm.yyyy" data-mode="popup" data-name="product.startDate">${(p.startDate)!}</a></dd>
    <dt>Bezug bis</dt><dd><a href="#" class="editable" data-type="date" data-format="dd.mm.yyyy" data-mode="popup" data-name="product.endDate">${(p.endDate)!}</a></dd>
    <dt>Memo</dt><dd><a href="#" class="editable" data-type="textarea" data-mode="popup" data-name="product.memo">${p.memo!}</a></dd>
  </dl>
  <div class="panel-footer">
  </div>
</div>
</div>

</div>




<ul class="nav nav-tabs">
  <li class="active"><a data-toggle="tab" href="#abos">Abonnements</a></li>
  <li><a data-toggle="tab" href="#invabos">Beendete Abos</a></li>
  <li><a data-toggle="tab" href="#artikel">Artikel</a></li>
  <li><a data-toggle="tab" href="#intervalle">Intervalle</a></li>
</ul>

<div class="tab-content">
  <div id="abos" class="tab-pane fade in active">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Abonummer</th>
					<th>Abonnent</th>
					<th>Seit</th>
					<th>Menge</th>
					<th>Versandart</th>
					<th>Zahlweise</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list subscriptions as s>
					<tr>
						<td>${s.id} <a href="#subscription/${s.id?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></td>
						<td>${kunde(s)}  <a href="#subscriber/${s.subscriber.id?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></td>
						<td>${(s.startDate)!}</td>
						<td>${s.quantity}</td>
					    <td>${s.shipmentType.text}</td>
					    <td>${s.paymentType.text}</td>
					</tr>
				</#list>
			</tbody>
		</table>
  </div>
  <div id="invabos" class="tab-pane fade">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Abonummer</th>
					<th>Abonnent</th>
					<th>Seit</th>
					<th>Bis</th>
					<th>Versandart</th>
					<th>Zahlweise</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list invalidSubscriptions as s>
					<tr>
						<td>${s.id} <a href="#subscription/${s.id?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></td>
						<td>${kunde(s)}  <a href="#subscriber/${s.subscriber.id?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></td>
						<td>${(s.startDate)!}</td>
						<td>${(s.endDate)!}</td>
					    <td>${s.shipmentType.text}</td>
					    <td>${s.paymentType.text}</td>
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
					<th>Preis</th>
					<th>Erscheinungstag</th>
				</tr>
			</thead>
			<tbody>
				<#list articles as a>
					<tr>
						<td>${a.id}</td>
						<td>${a.name}</td>
						<td align="right">${money(a.brutto)}</td>
						<td>${(a.erschTag)!}</td>
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
					<th>Preis</th>
					<th>Typ</th>
				</tr>
			</thead>
			<tbody>
				<#list intervals as a>
					<tr>
						<td>${a.id}</td>
						<td>${a.name}</td>
						<td>${(a.startDate)!}</td>
						<td>${(a.endDate)!}</td>
						<td align="right">${money(a.brutto)}</td>
						<td>${a.intervalType.text}</td>
					</tr>	
				</#list>
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

