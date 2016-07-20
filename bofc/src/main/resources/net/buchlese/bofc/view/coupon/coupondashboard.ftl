
<div class="row">
<div class="col-md-6">
<h1>Dashboard</h1>
</div>
<ul class="nav nav-tabs">
  <li class="active"><a data-toggle="tab" href="#heutigeRech">unberechnete externe Gutscheine<span class="label label-pill label-default">${coupons?size}</span></a></li>
  <li><a data-toggle="tab" href="#rechnungen">unbestätigte Rechnungen <span class="label label-pill label-default">${invoices?size}</span></a></li>
</ul>

<div class="tab-content">
<div id="heutigeRech" class="tab-pane fade in active">
<table class="table table-striped">
	<thead>
		<tr>
			<th>Kunde</th>
			<th align="right">Betrag</th>
			<th>Datum</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list coupons as d>
		<tr>
			<td>${d.subscriberName!} ${abo(d).deliveryInfo1!} ${abo(d).deliveryInfo2!}</td>
			<td align="right">${money(d.total)}</td>
			<td>${(d.deliveryDate.toString("dd.MM.yy"))!}</td>
		</tr>
		</#list>
	</tbody>
</table>
</div>
  <div id="rechnungen" class="tab-pane fade">
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
		<#list invoices as inv>
		<tr>
			<td>${inv.number}</td>
			<td>${(inv.date.toString("dd.MM.yy"))!}</td>
			<td>${inv.name1!}</td>
			<td>${(inv.deliveryFrom.toString("dd.MM.yy"))!}</td>
			<td>${(inv.deliveryTill.toString("dd.MM.yy"))!}</td>
			<td>${money(inv.amount)}</td>
			<td><a href="#invoiceRecord/${inv.number}" data-toggle="tooltip" title="Fakturieren" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span></a>
                <a href="/subscr/invoiceView/${inv.number}" data-toggle="tooltip" title="Anschauen" class="btn btn-default btn-sm" target="_blank"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
                <a href="#invoiceCancel/${inv.number}" data-toggle="tooltip" title="löschen" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
			 </td>
		</tr>
		</#list>
	</tbody>
</table>
  </div>
</div>	<!-- end nav-content -->	
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
});

</script>

