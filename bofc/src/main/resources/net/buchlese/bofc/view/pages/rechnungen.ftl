
<table class="table table-striped">
	<thead>
		<tr>
			<th>RechNummer</th>
			<th>Kunde</th>
			<th>Vorgang</th>
			<th align="right">KdNr</th>
			<th align="right">Debitor</th>
			<th>Datum</th>
			<th align="right">Betrag</th>
			<th align="right">bez?</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list invoices as inv>
		<tr>
			<td>${inv.number}</td>
			<td>${inv.name1}</td>
			<td>${inv.actionum}</td>
			<td align="right">(${inv.customerId})</td>
			<td align="right">${inv.debitorId}</td>
			<td><#if inv.date??>${inv.date.toString("dd.MM.yyyy")}<#else>kein Datum!</#if></td>
			<td align="right">${money(inv.amount)}</td>
			<td align="right">${money(inv.payed)}</td>
		</tr>
		</#list>
	</tbody>
</table>
		