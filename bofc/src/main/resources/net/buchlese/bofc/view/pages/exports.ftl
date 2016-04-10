
<table class="table table-striped">
	<thead>
		<tr>
			<th>Export-Nr</th>
			<th>Export-Datum</th>
			<th>von</th>
			<th>bis</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list exports as exp>
		<tr>
			<td>${exp.key}</td>
			<td>${exp.execDate.toString("dd.MM.")}</td>
   <td>${exp.from.toString("EEEE dd.MM.yyyy")}</td>
   <td>${exp.till.toString("EEEE dd.MM.yyyy")}</td>
			<td><a href="/fibu/view/?key=${exp.key}" target="_blank">view</a></td>
		</tr>
		</#list>
	</tbody>
</table>
		