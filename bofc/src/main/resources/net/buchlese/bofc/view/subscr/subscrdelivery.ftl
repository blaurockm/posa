
<h1>Lieferungsdetails</h1>

<p>Artikel ${d.articleName}
<p>am ${d.deliveryDate.toString("dd.MM.yyyy")}
<p>an ${kunde(d)}

<p>Menge ${d.quantity}

<p>Gesamtpreis brutto ${money(d.total)}

<p>7% brutto ${money(d.totalHalf)}

<p>19% brutto ${money(d.totalFull)}


<#if d.payed >
  <p>auf Rechnung ${d.invoiceNumber!}
<#else>
  <p>noch nicht berechnet.
</#if>



