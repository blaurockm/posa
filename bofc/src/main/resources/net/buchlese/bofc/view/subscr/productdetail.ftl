
<h1>Periodikadetails ${p.name}</h1>

<p>von ${p.publisher}
<p>Letzte Lieferung ${p.lastDelivery.toString("dd.MM.yy")}
<p>Bezug seit ${p.startDate.toString("dd.MM.yy")}
<p>Bestellte Menge ${p.quantity}

<h3>Abos</h3>
<ul>
<#list subscriptions as sub>

<script>
$( "#button${sub.id}" ).click(function() {
  $( "#delivs${sub.id}" ).toggle();
});
</script>

<li> Abo Nr ${sub.id},  Anzahl ${sub.quantity}, ${kunde(sub)} seit ${sub.startDate.toString("dd.MM.yy")} 
<button id="button${sub.id}">Alle Lieferungen</button>

<div id="delivs${sub.id}" style="display:none">
<table class="table table-striped">
	<thead>
		<tr>
			<th>Lieferdatum</th>
			<th>Artikel</th>
			<th>Rech</th>
			<th>LS</th>
			<th>bezahlt?</th>
		</tr>
	</thead>
	<tbody>
		<#list deliveries(sub) as d>
		<tr>
			<td>${d.deliveryDate.toString("dd.MM.yy")}</td>
			<td>${artikelbez(d)}</td>
			<td>${money(d.article.brutto)}</td>
			<td><a href="/subscr/dispo/${p.id}">dispo</a> </td>
			<td><a href="/subscr/product/${p.id}">details</a> </td>
		</tr>
		</#list>
	</tbody>
</table>
</div>

</#list>
</ul>