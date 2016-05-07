
<h1>heutige Lieferungen</h1> <a href="/subscr/deliveraddresslist/" target="_blank">Adressliste</a>
<table class="table table-striped">
	<thead>
		<tr>
			<th>Kunde</th>
			<th>Artikel</th>
			<th>Menge</th>
			<th>Versandart</th>
			<th align="right">Betrag</th>
			<th>Berechnet</th>
			
			<th>Aktionen</th>
		</tr>
	</thead>
	<tbody>
		<#list deliveries as d>
		<tr>
			<td>${kunde(d)}</td>
			<td>${artikelbez(d)}</td>
			<td>${d.quantity}</td>
			<td></td>
			<td align="right">${money(d.total)}</td>
			<td>${d.payed?c}</td>
			<td><a href="#subscrDelivery/${d.id?c}">see</a>
			<a href="/subscr/delivnote/${d.id?c}">LS</a></td>
		</tr>
		</#list>
	</tbody>
</table>

<h1>Periodika</h1>
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
			<td><a href="#subscrDispo/${p.id}">dispo</a> <a href="#subscrProduct/${p.id}">details</a> </td>
		</tr>
		</#list>
	</tbody>
</table>

		