
<div class="row">
<div class="col-md-6">
<h1>Dashboard</h1>
</div>
<div class="col-md-offset-4 col-md-2">
 <a href="/subscr/deliveraddresslist/" class="btn btn-primary" target="_blank">Adressprotokoll</a>
 </div>
</div>
<ul class="nav nav-tabs">
  <li class="active"><a data-toggle="tab" href="#heutigeliefer">heutige Lieferungen <span class="label label-pill label-default">${deliveries?size}</span></a></li>
  <li><a data-toggle="tab" href="#periodika">Periodika mit zu erwartenden Lieferungen</a></li>
  <li><a data-toggle="tab" href="#abos">abzurechnende Abos</a></li>
  <li><a data-toggle="tab" href="#rechnungen">unbestätigte Rechnungen <span class="label label-pill label-default">${invoices?size}</span></a></li>
</ul>

<div class="tab-content">
  <div id="heutigeliefer" class="tab-pane fade in active">
<table class="table table-striped">
	<thead>
		<tr>
			<th>Kunde</th>
			<th>Artikel</th>
			<th>Menge</th>
			<th>Versandart</th>
			<th align="right">Betrag</th>
			<th>Berechnet</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list deliveries as d>
		<tr>
			<td>${d.subscriberName} ${abo(d).deliveryInfo1!} ${abo(d).deliveryInfo2!}</td>
			<td>${d.articleName}</td>
			<td>${d.quantity}</td>
			<td>${abo(d).shipmentType.text}</td>
			<td align="right">${money(d.total)}</td>
			<td>${d.payed?string("ja","")}</td>
			<td><a href="#subscrDelivery/${d.id?c}" data-toggle="tooltip" title="Details" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
            <a href="#subscription/${d.subscriptionId?c}" data-toggle="tooltip" title="Abo" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
			<a href="/subscr/deliverynote/${d.id?c}" data-toggle="tooltip" title="Lieferschein" class="btn btn-default btn-sm" target="_blank"><span class="glyphicon glyphicon-folder-open" aria-hidden="true" alt="Lieferschein"></span></a>
			<a href="#deliverydelete/${d.id?c}" data-toggle="tooltip" title="Löschen" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></td>
		</tr>
		</#list>
	</tbody>
</table>
  </div>
  <div id="periodika" class="tab-pane fade">
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
			<td><a href="#subscrDispo/${p.id?c}"  data-toggle="tooltip" title="Lieferungen anlegen" class="btn btn-default btn-sm">Dispo</a>
			 <a href="#subscrProduct/${p.id?c}"  data-toggle="tooltip" title="Details" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a> </td>
		</tr>
		</#list>
	</tbody>
</table>
  </div>
  <div id="abos" class="tab-pane fade">
<table class="table table-striped">
	<thead>
		<tr>
			<th>Abonr</th>
			<th>Periodikum</th>
			<th>Abonnent</th>
			<th>Lieferhinweis</th>
			<th>Zahlweise</th>
			<th>Versandart</th>
			<th>Bezahlt bis</th>
			<th>Letzte Rechnung am</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list subscriptions as s>
		<tr>
			<td>${s.id}</td>
			<td>${product(s)}</td>
			<td>${kunde(s)}</td>
			<td>${s.deliveryInfo1!} ${s.deliveryInfo2!}</td>
			<td>${s.paymentType.text}</td>
			<td>${s.shipmentType.text}</td>
			<td>${(s.payedUntil.toString("dd.MM.yy"))!}</td>
			<td>${(s.lastInvoidDate.toString("dd.MM.yy"))!}</td>
			<td><a href="#subscription/${s.id?c}" data-toggle="tooltip" title="Details" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a> </td>
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

