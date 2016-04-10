
<div class="container">
	<div class="col-md-12">
		<h2>Ausgangsrechnungen</h2>
		<ul class="list-group">
			<#list invoices as inv>
			<li class="list-group-item">${inv.number} ${inv.name1}
				(${inv.customerId}) ${money(inv.amount)} vom <#if
				inv.date??>${inv.date.toString("dd.MM.yyyy")}<#else> kein
				Datum?</#if> <span class="badge">${inv.debitorId}</span>
			</li> </#list>
		</ul>
	</div>
</div>
