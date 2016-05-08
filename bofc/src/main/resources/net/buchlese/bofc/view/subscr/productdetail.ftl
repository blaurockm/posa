
<div class="container">
<h1>Periodikadetails <a href="#" class="editable" data-type="text" data-name="product.publisher">${p.name}</a></h1>

<h3>von <a href="#" class="editable" data-type="text" data-name="product.publisher">${p.publisher}</a> </h3>
<div class="row">
<div class="col-md-2">Abkürzung</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.publisher">${p.abbrev}</a></div>
</div>
<div class="row">
<div class="col-md-2">Ausgabenzähler</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.publisher">${p.count!"1"}</a></div>
</div>
<div class="row">
<div class="col-md-2">Bestellte Menge</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.publisher">${p.quantity!"1"}</a></div>
</div>
<div class="row">
<div class="col-md-2">Periodizität</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.publisher">${p.period}</a></div>
</div>
<div class="row">
<div class="col-md-2">Artikel-Namensmuster</div>
<div class="col-md-6"><a href="#" class="editable" data-type="text" data-name="product.publisher">${p.namePattern}</a></div>
</div>
<div class="row">
<div class="col-md-2">Artikel-Basispreis</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.publisher">${p.baseBrutto!"0"}</a></div>
</div>
<div class="row">
<div class="col-md-2">Proz Print-Anteil</div>
<div class="col-md-2"><a href="#" class="editable" data-type="text" data-name="product.publisher">${p.halfPercentage!"1"}</a></div>
</div>
<div class="row">
<div class="col-md-2">Letzte Lieferung</div>
<div class="col-md-2">${p.lastDelivery.toString("dd.MM.yy")}</div>
</div>
<div class="row">
<div class="col-md-2">Bezug seit</div>
<div class="col-md-2">${p.startDate.toString("dd.MM.yy")}</div>
</div>

<h3>Abos</h3>
<div id="accordion" role="tablist" aria-multiselectable="true">
<#list subscriptions as sub>
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading${sub.id}">
      <h4 class="panel-title">
        <a data-toggle="collapse" data-parent="#accordion" href="#delivs${sub.id}" aria-expanded="false" aria-controls="delivs${sub.id}">
          Abo Nr ${sub.id}, ${kunde(sub)}, ${sub.deliveryInfo1!""}, ${sub.deliveryInfo2!""} seit ${sub.startDate.toString("dd.MM.yy")}
          <span class="label label-pill label-default">${sub.quantity}</span> 
          <a href="#subscription/${sub.id}" class="btn btn-info">Abodetails</a>
        </a>
      </h4>
    </div>
    <div id="delivs${sub.id}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${sub.id}">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Lieferdatum</th>
					<th>Artikel</th>
					<th>Rech</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<#list deliveries(sub) as d>
					<tr>
						<td>${d.deliveryDate.toString("dd.MM.yy")}</td>
						<td>${d.articleName}</td>
						<td>${money(d.article.brutto)}</td>
						<td>  <a href="#subscrDelivery/${d.id?c}">see</a> </td>
					</tr>
				</#list>
			</tbody>
		</table>
    </div>
  </div>
</#list>
</div>  


</div>
<script>
	$('.editable').editable({
	    url: '/subscr/update', pk:'${p.id?c}'
	});
</script>

