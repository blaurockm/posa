
<h1>Periodikadisposition ${p.name}</h1>

<p>von ${p.publisher}
<p>für den ${dispoDate.toString("dd.MM.yy")}
<p>Bestellte Menge ${p.quantity}

<h2>auszuliefernder Artikel ${art.id?c}</h2>

<form action="subscr/updatearticle">
Name <input type="text" value="${art.name}" size=50> <br>
Preis Gesamt Brutto <input type="text" value = "${art.brutto}"> € <br>
       7% Brutto <input type="text" value = "${art.brutto_half}"> €   prozentualer Anteil <input type="text" value = "${art.halfPercentage}"> <br>
	   19% Brutto <input type="text" value = "${art.brutto_full}"> € <br> 

Zählung <input type="text" value="${art.count}"> <br>
Erscheinungsdatum ${art.timest.toString("dd.MM.yy")} <br>

<button>Änderungen Speichern</button> <button>Neuer Artikel</button>	   
</form>

<h2>betroffene Abos</h2>


<ul>

<table class="table table-striped">
	<thead>
		<tr>
			<th>AboNr</th>
			<th>Abonnent</th>
			<th>Menge</th>
			<th>LS</th>
			<th>Rech?</th>
			<th>Versandart</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
<#list subscriptions as sub>
		<tr>
			<td>${sub.id?c}</td>
			<td>${kunde(sub)} ${sub.deliveryInfo1!""}, ${sub.deliveryInfo2!""}</td>
			<td>${sub.quantity}</td>
			<td>${sub.needsDeliveryNote?c}</td>
			<td>${sub.needsInvoice?c}</td>
			<td></td>
			<td>
			<#assign deli = delivery(sub, art)!"hh" >
			<#if deli != "hh" >
   			   <a href="#subscrDelivery/${deli.id?c}">see</a>
			<#else>
			   <button id="create${sub.id}">anlegen</button>
			 <script>
   	   	 	   $( "#create${sub.id}" ).click(function() {
   	    		 $.getJSON("/subscr/deliverycreate/${sub.id?c}/${art.id?c}/${dispoDate}", function() {console.log( "deliverycreate success" );})
   	    		 $( "#create${sub.id}" ).hide();
				});
			 </script>
			</#if>
			 </td>
		</tr>
</#list>
	</tbody>
</table>


</ul>