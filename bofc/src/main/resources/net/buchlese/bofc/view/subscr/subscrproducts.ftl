<div class="container">


<div class="row">
<div class="col-md-6">
<h1>Periodika</h1>
</div>
<div class="col-md-offset-4 col-md-2">
 <a href="#subscrProductAdd" class="btn btn-primary">Neues Periodikum anlegen</a>
 </div>
</div>
<table class="table table-striped">
	<thead>
		<tr>
			<th>Verlag</th>
			<th>Publikation</th>
			<th>Abkü</th>
			<th># Abos</th>
			<th>Periodizität</th>
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
			<td>${p.period!}</td>
			<td>${(p.lastDelivery.toString("dd.MM.yy"))!}</td>
			<td>${(p.nextDelivery.toString("dd.MM.yy"))!}</td>
			<td><a href="#subscrDispo/${p.id?c}" class="btn btn-default btn-sm">dispo</a>
			 <a href="#subscrProduct/${p.id?c}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a> </td>
		</tr>
		</#list>
	</tbody>
</table>

</div>		