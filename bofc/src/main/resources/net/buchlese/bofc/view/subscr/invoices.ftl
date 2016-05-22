<div class="container">

<h1>Rechnungen</h1>
<table class="table table-striped">
	<thead>
		<tr>
			<th>Rechnr</th>
			<th>Datum</th>
			<th>Kunde</th>
			<th>LieferVon</th>
			<th>LieferBis</th>
			<th>Betrag</th>
			<th>Storniert?</th>
			<th>Gedruckt?</th>
			<th>Sammel?</th>
			<th>bezahlt?</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list invoices as inv>
		<tr>
			<td>${inv.number}</td>
			<td>${(inv.date.toString("dd.MM.yy"))!}</td>
			<td>${inv.name1!}</td>
			<td>${(inv.deliveryFrom.toString("dd.MM.yy"))!}</td>
			<td>${(inv.deliveryTill.toString("dd.MM.yy"))!}</td>
			<td>${money(inv.amount)}</td>
			<td>${inv.cancelled?string("storniert","")}</td>
			<td>${inv.printed?string("gedruckt","")}</td>
			<td>${inv.collective?string("sammel","")}</td>
			<td>${inv.payed?string("bezahlt","")}</td>
			<td><a href="/subscr/invoiceView/${inv.number}" class="btn btn-default btn-sm" target="_blank"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
                <#if inv.cancelled == false>			
                <a href="#invoiceCancel/${inv.number}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
                </#if>
			 </td>
		</tr>
		</#list>
	</tbody>
</table>


<h1>unbestätigte Rechnungen</h1>
<table class="table table-striped">
	<thead>
		<tr>
			<th>Rechnr</th>
			<th>Datum</th>
			<th>Kunde</th>
			<th>LieferVon</th>
			<th>LieferBis</th>
			<th>Betrag</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list tempInvoices as inv>
		<tr>
			<td>${inv.number}</td>
			<td>${(inv.date.toString("dd.MM.yy"))!}</td>
			<td>${inv.name1!}</td>
			<td>${(inv.deliveryFrom.toString("dd.MM.yy"))!}</td>
			<td>${(inv.deliveryTill.toString("dd.MM.yy"))!}</td>
			<td>${money(inv.amount)}</td>
			<td><a href="#invoiceRecord/${inv.number}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span></a>
                <a href="/subscr/invoiceView/${inv.number}" class="btn btn-default btn-sm" target="_blank"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
                <a href="#invoiceCancel/${inv.number}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
			 </td>
		</tr>
		</#list>
	</tbody>
</table>


</div>		