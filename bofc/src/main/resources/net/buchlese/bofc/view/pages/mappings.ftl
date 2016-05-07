
<table class="table table-striped">
	<thead>
		<tr>
			<th>Kassen</th>
			<th>Kunde</th>
			<th>Name</th>
			<th>Debitkto</th>
			<th>Anzahl</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list mappings as ma>
		<tr>
			<td>${ma.pointid}</td>
			<td>${ma.customerId?c}</td>
			<td>${ma.name1!"no name"}</td>
			<td align="right" style="padding-left: 10mm">${ma.count}</td>
			<td align="right">
			<a href="#" class="editable" data-type="text" data-pk="${ma.pointid?c}" data-name="${ma.customerId?c}" data-url="/mapping/update" data-title="Debitor-Konto">${ma.debitorId?c}</a>
			<td>
		</tr>
		</#list>
	</tbody>
</table>

<script>
$('.editable').editable();
</script>