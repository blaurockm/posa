<div class="container">

<div class="row">
<div class="col-md-10"></div>
<div class="col-md-2"><a href="#subscrCustAdd" class="btn btn-primary">Neuer Abonnent</a></div>
</div>

<h1>Abonnenten</h1>
<table class="table table-striped">
	<thead>
		<tr>
			<th>Name</th>
			<th># Abos</th>
			<th>letze Rechnung</th>
			<th>Letzte Lieferung</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list subscribers as s>
		<tr>
			<td>${s.name}</td>
			<td></td>
			<td></td>
			<td></td>
			<td><a href="#subscriber/${s.id?c}">details</a> </td>
		</tr>
		</#list>
	</tbody>
</table>
</div>		