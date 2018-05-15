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
			<th>Publikation</th>
			<th>Verlag</th>
			<th>Abkü</th>
			<th>Periode</th>
			<th>Zahlung weise</th>
			<th>letzer Eingang</th>
			<th>Zahlzeit</th>
			<th>berechnet bis</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list products as p>
		<tr>
			<td><a href="#subscrProduct/${p.id?c}">${p.name}</a></td>
			<td>${p.publisher}</td>
			<td>${p.abbrev}</td>
			<td>${p.period!}</td>
			<td>${p.payPerDelivery?string("pro Lieferung", "pro Zeit")}</td>
			<td>${(p.lastDelivery)!}</td>
			<td>${(p.intervalType.text)!}</td>
			<td>${(p.lastInterval)!}</td>
			<td><a href="#subscrDispo/${p.id?c}" class="btn btn-default btn-sm">dispo</a>
			 <a href="#subscrProduct/${p.id?c}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a> </td>
		</tr>
		</#list>
	</tbody>
</table>

</div>		