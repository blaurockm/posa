<div class="container">

<div class="row">
<div class="col-md-2">
Artikel
</div>
<div class="col-md-5">
${d.articleName!}
</div>
</div>

<div class="row">
<div class="col-md-2">
am 
</div>
<div class="col-md-5">
${d.deliveryDate.toString("dd.MM.yyyy")}
</div>
</div>

<div class="row">
<div class="col-md-2">
an
</div>
<div class="col-md-5">
${kunde(d)}
</div>
</div>

<div class="row">
<div class="col-md-2">
Menge
</div>
<div class="col-md-2">
${d.quantity}
</div>
</div>

<div class="row">
<div class="col-md-5">
<#if d.payed >
  auf Rechnung ${d.invoiceNumber!}
<#else>
  noch nicht berechnet.
</#if>
</div>
</div>

<div class="row">
   <h3 class="card-title">Preis</h3>
	<div class="col-md-5">
    <ul class="list-group list-group-flush">
      <li class="list-group-item">Gesamt Brutto <a href="#" id="total" class="editablemoney" data-type="text"  data-name="article.brutto"  data-title="Gesamtbetrag">${d.total?c}</a></li>
      <li class="list-group-item">7% Brutto <a href="#" id="totalHalf" class="editablemoney" data-type="text"  data-name="article.brutto_half"  data-title="Betrag Print-Anteil" >${d.totalHalf?c}</a></li>
      <li class="list-group-item">19% Brutto <a href="#" id="totalFull" class="editablemoney" data-type="text"  data-name="article.brutto_full"  data-title="Betrag Online-Anteil"  >${d.totalFull?c}</a> </li>
	</ul>
   </div>
</div>

</div>
<script>
$('.editable').editable({
    url: '/subscr/update', pk:'${d.id?c}',
});
$('.editablemoney').editable({
    url: '/subscr/update', pk:'${d.id?c}',
    mode : "popup",
    display : function(value, jsonresponse) {
    	$(this).text(formatMoney(value));
    },
    success: function(res, newValue) {
        if(!res.success) return res.msg;
        $("#total").editable('setValue',res.brutto, true);
        $("#totalHalf").editable('setValue',res.bruttoHalf, true);
        $("#totalFull").editable('setValue',res.bruttoFull, true);
    },
    validate: function(value) {
        if($.trim(value) == '') {
            return 'This field is required';
        }
    }
});
</script>
