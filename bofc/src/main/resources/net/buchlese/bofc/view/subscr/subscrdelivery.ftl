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
<div class="col-md-2">
Gesamtpreis brutto
</div>
<div class="col-md-2">
${money(d.total)}
</div>
</div>

<div class="row">
<div class="col-md-2">
7% brutto
</div>
<div class="col-md-2">
${money(d.totalHalf)}
</div>
</div>

<div class="row">
<div class="col-md-2">
19% brutto
</div>
<div class="col-md-2">
${money(d.totalFull)}
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

</div>

