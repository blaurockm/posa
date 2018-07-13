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
${d.deliveryDate}
</div>
</div>

<div class="row">
<div class="col-md-2">
an
</div>
<div class="col-md-5">
${d.subscriber.name!}
</div>
</div>

<div class="row">
<div class="col-md-2">
wie
</div>
<div class="col-md-5">
${sub.shipmentType.text}
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

<#if sub.paymentType != 'EACHDELIVERY'>
<div class="row">
<div class="col-md-2">
bezahlt
</div>
<div class="col-md-2">
<input type="checkbox" ${d.payed?string('checked','')} onChange="switchCheckbox('delivery.payed', this.checked)">
</div>
</div>
<div class="row">
<div class="col-md-2">
auf Lieferschein
</div>
<div class="col-md-2">
<input type="checkbox" ${d.slipped?string('checked','')} onChange="switchCheckbox('delivery.slipped', this.checked)">
</div>
</div>
<#else>
<div class="row">
<div class="col-md-5">
<#if d.payed >
  auf Rechnung ${d.invoiceNumber!}
<#else>
  noch nicht berechnet.
</#if>
</div>
</div>
</#if>
<div class="row">
<div class="col-md-5">
<#if d.slipped >
  auf Lieferschein <a href="/subscr/deliverynote/${d.id?c}"  target="_blank"> ${d.slipNumber!} </a>
  
</#if>
</div>
</div>


<div class="row">
   <h3 class="card-title">Preis</h3>
	<div class="col-md-5">
    <ul class="list-group list-group-flush">
      <li class="list-group-item">Gesamt Brutto <a href="#" id="total" class="editablemoney" data-type="text"  data-name="delivery.total"  data-title="Gesamtbetrag">${d.total?c}</a></li>
      <li class="list-group-item">7% Brutto <a href="#" id="totalHalf" class="editablemoney" data-type="text"  data-name="delivery.totalHalf"  data-title="Betrag Print-Anteil" >${d.totalHalf?c}</a></li>
      <li class="list-group-item">19% Brutto <a href="#" id="totalFull" class="editablemoney" data-type="text"  data-name="delivery.totalFull"  data-title="Betrag Online-Anteil"  >${d.totalFull?c}</a> </li>
	</ul>
   </div>
</div>
<div class="row">
   <div class="col-md-5">
     Versandkosten <a href="#" class="editablemoney" data-type="text"  data-name="delivery.shipmentCost"  data-title="Gesamtbetrag">${d.shipmentCost?c}</a>
   </div>
</div>

</div>
<script>
$('.editable').editable({
    url: '/subscr/update', pk:'${d.id?c}',
});
var switchCheckbox = function(fieldname, val) {
  	 $.post("/subscr/update", { pk: '${d.id?c}', name: fieldname, value : val});
   }

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
