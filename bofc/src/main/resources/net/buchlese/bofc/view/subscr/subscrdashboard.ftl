<div class="container">

<div class="row">
<div class="col-md-6">
<h1>heutige Lieferungen</h1>
</div>
<div class="col-md-offset-4 col-md-2">
 <a href="/subscr/deliveraddresslist/" class="btn btn-primary" target="_blank">Adressprotokoll</a>
 </div>
</div>
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
		<#list deliveries as d>
		<tr>
			<td>${kunde(d)}</td>
			<td>${d.articleName}</td>
			<td>${d.quantity}</td>
			<td></td>
			<td align="right">${money(d.total)}</td>
			<td>${d.payed?c}</td>
			<td><a href="#subscrDelivery/${d.id?c}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
			<a href="/subscr/delivnote/${d.id?c}" class="btn btn-default btn-sm" target="_blank"><span class="glyphicon glyphicon-folder-open" aria-hidden="true" alt="Lieferschein"></span></a></td>
		</tr>
		</#list>
	</tbody>
</table>

<div class="row">
<div class="col-md-6">
<h1>Periodika</h1>
</div>
</div>
<table class="table table-striped">
	<thead>
		<tr>
			<th>Verlag</th>
			<th>Publikation</th>
			<th>Abkü</th>
			<th># Abos</th>
			<th>letzer Eingang</th>
			<th>nächster Eingang</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list products as p>
		<tr>
			<td>${p.publisher}</td>
			<td>${p.name}</td>
			<td>${p.abbrev}</td>
			<td></td>
			<td><#if p.lastDelivery??>${p.lastDelivery.toString("dd.MM.yy")}</#if></td>
			<td><#if p.nextDelivery??>${p.nextDelivery.toString("dd.MM.yy")}</#if></td>
			<td><a href="#subscrDispo/${p.id?c}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-blackboard" aria-hidden="true"></span></a>
			 <a href="#subscrProduct/${p.id?c}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a> </td>
		</tr>
		</#list>
	</tbody>
</table>

<div class="row">
<div class="col-md-offset-8 col-md-2">
 <a href="#subscrProductAdd" class="btn btn-primary">Neues Periodikum anlegen</a>
 </div>
</div>

</div>		