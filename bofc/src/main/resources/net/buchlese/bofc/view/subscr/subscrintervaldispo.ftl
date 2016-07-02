<div class="container">
<h1>Weiterberechnung ${p.name}  <a href="#subscrProduct/${p.id?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></h1>

<div class="row">
<div class="col-md-6">von ${p.publisher}</div>
</div>
<div class="row">
<div class="col-md-6">Bestellte Menge ${p.quantity}</div>
</div>

<div class="row">

<div class="col-md-6">
  <div class="panel panel-info">
    <div class="panel-heading">weiterzuberechnendes Intervall <span id="artid">${art.id?c}</span>
    <#if showArticlePlusEins>
    <a id="articlepluseins" class="btn btn-primary" href="#subscrintervalcreate/${art.productId?c}">Interval +1</a>
    </#if>
    </div>
    <div class="panel-body">
    <dl class="dl-horizontal">
      <dt>Name</dt><dd><a href="#" id="artname" class="editable" data-type="text"  data-name="interval.name"  data-title="Intervallbezeichnung">${art.name}</a></dd>
      <dt>Type</dt><dd><a href="#" id="paytype"></a></dd>
      <dt>Startdatum</dt><dd><a href="#" id="artstart" class="namechangerfield" data-type="date" data-format="dd.mm.yyyy" data-name="interval.startDate"  data-title="Anfang" >${art.startDate.toString("dd.MM.yyyy")}</a></dd>
      <dt>Enddatum</dt><dd><a href="#" id="artend" class="namechangerfield" data-type="date" data-format="dd.mm.yyyy" data-name="interval.endDate"  data-title="Ende" >${art.endDate.toString("dd.MM.yyyy")}</a></dd>
    </dl>
    </div>
    <div class="panel-footer">
	  <ul class="pager">
	    <li class="previous"><a href="#subscrIntervalDispoNav/${p.id?c}/prev/${art.id?c}"><span aria-hidden="true">&larr;</span>früher</a></li>
	    <li class="next"><a href="#subscrIntervalDispoNav/${p.id?c}/next/${art.id?c}">Neuer<span aria-hidden="true">&rarr;</span></a></li>
	  </ul>
    </div>
   </div> 
</div>

<div class="col-md-6">
  <div class="panel panel-info">
    <div class="panel-heading">Preis</span></div>
    <div class="panel-body">
    <dl class="dl-horizontal">
      <dt>Gesamt Brutto</dt><dd> <a href="#" id="brutto" class="editablemoney" data-type="text"  data-name="interval.brutto"  data-title="Gesamtbetrag">${art.brutto?c}</a></dd>
      <dt>7% Brutto</dt><dd> <a href="#" id="brutto_half" class="editablemoney" data-type="text"  data-name="interval.brutto_half"  data-title="Betrag Print-Anteil" >${art.brutto_half?c}</a></dd> 
      <dt>Print-Anteil</dt><dd> <a href="#" id="half_percent" class="percentagefield" data-type="text"  data-name="article.halfPercentage"  data-title="Prozent Print-Anteil" >${art.halfPercentage?c}</a></dd>
      <dt>19% Brutto</dt><dd><a href="#" id="brutto_full" class="editablemoney" data-type="text"  data-name="interval.brutto_full"  data-title="Betrag Online-Anteil"  >${art.brutto_full?c}</a> </dd>
	</dl>
	</div>
   </div>
</div>

</div>


<h2>betroffene Abos</h2>

<table class="table table-striped">
	<thead>
		<tr>
			<th>AboNr</th>
			<th>Abonnent</th>
			<th>Menge</th>
			<th>LS</th>
			<th>Versandart</th>
			<th>Zahlweise</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
<#list subscriptions as sub>
		<tr>
			<td>${sub.id?c} <a href="#subscription/${sub.id?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></td>
			<td>${kunde(sub)} ${sub.deliveryInfo1!""}, ${sub.deliveryInfo2!""} <a href="#subscriber/${sub.subscriberId?c}" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a></td>
			<td>${sub.quantity}</td>
			<td></td>
			<td>${sub.shipmentType.text}</td>
			<td>${sub.paymentType.text}</td>
			<td>
			<#if sub.paymentType != 'EACHDELIVERY' >
			<#assign deli = delivery(sub, art)!"hh" >
			<#if deli != "hh" >
   	  	  	   <a href="#subscrIntervalDelivery/${deli.id?c}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
			<#else>
			   <button id="create${sub.id?c}" class="btn btn-default">anlegen</button>
			 <script>
   	   	 	   $( "#create${sub.id?c}" ).click(function() {
   	    		 $.getJSON("/subscr/intervaldeliverycreate/${sub.id?c}/${art.id?c}/${dispoDate}", function() {console.log( "intervaldeliverycreate success" );})
   	    		 $( "#create${sub.id?c}" ).hide();
				});
			 </script>
			</#if>
			</#if>
			 </td>
		</tr>
</#list>
	</tbody>
</table>
</div>

<script>
    
    $('.editable').editable({
	    url: '/subscr/update', pk : '${art.id?c}'
	});
	$('.namechangerfield').editable({
	    url: '/subscr/update', pk : '${art.id?c}',
	    mode : "popup",
		success:function(res, newValue) {
	        if(!res.success) return res.msg;
	        $("#artname").editable('setValue',res.name, false);
	    }
    });
	$('.percentagefield').editable({
	    url: '/subscr/update', pk : '${art.id?c}',
	    mode : "popup",
	    display : function(value, jsonresponse) {
	    	$(this).text(value * 100 + " %");
	    },
		success:function(res, newValue) {
	        if(!res.success) return res.msg;
	        $("#brutto_half").editable('setValue',res.bruttoHalf, true);
	        $("#brutto_full").editable('setValue',res.bruttoFull, true);
	    }
    });
	$('.editablemoney').editable({
	    url: '/subscr/update', pk : '${art.id?c}',
	    mode : "popup",
	    display : function(value, jsonresponse) {
	    	$(this).text(formatMoney(value));
	    },
	    success: function(res, newValue) {
	        if(!res.success) return res.msg;
	        $("#brutto").editable('setValue',res.brutto, true);
	        $("#brutto_half").editable('setValue',res.bruttoHalf, true);
	        $("#brutto_full").editable('setValue',res.bruttoFull, true);
	        $("#half_percent").editable('setValue',res.halfPercentage, false);
	    },
	    validate: function(value) {
	        if($.trim(value) == '') {
	            return 'This field is required';
	        }
	    }
	});
	$('#paytype').editable({
	    url: '/subscr/update', pk:'${art.id?c}',
		type : "select",
		value : "${art.intervalType!}",
		name : "interval.intervalType",
		showbuttons:false,
		success:function(res, newValue) {
	        if(!res.success) return res.msg;
	        $("#artname").editable('setValue',res.name, false);
	        $("#artend").editable('setValue',res.endDate, true);
	    },
		source : paytypesList
	});
    
</script>
