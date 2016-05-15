<form id="entryForm" action="/subscr/subscriptionCreate" method="POST" class="form-horizontal">

  <div class="form-group">
    <label for="pointid" class="col-sm-3 control-label">Ladengeschäft</label>
    <div class="col-sm-8">
       <p class="form-control-static">Schramberg</p>
    </div>
  </div>

  <div class="form-group">
    <label for="subscriberId" class="col-sm-3 control-label">Geschäftspartner</label>
    <div class="col-sm-8">
       <select class="form-control select2 subscriberselect" name="subscriberId">
          <#if (subscriber.id)?? >
          <option value="${subscriber.id?c}" selected>${subscriber.name}</option>
          </#if>
       </select>
	</div>
  </div>

  <div class="form-group">
    <label for="productId" class="col-sm-3 control-label">Periodikum</label>
    <div class="col-sm-8">
       <select class="form-control select2 subscrproductselect" name="productId">
          <#if (subscrProduct.id)?? >
          <option value="${subscrProduct.id?c}" selected>${subscrProduct.name}</option>
          </#if>
       </select>
    </div>   
  </div>

  <div class="form-group">
    <label for="quantity" class="col-sm-3 control-label">Exemplare</label>
    <div class="col-sm-8">
       <input type="number" class="form-control" name="quantity" placeholder="1">
    </div>   
  </div>

  <div class="form-group">
    <label for="deliveryInfo1" class="col-sm-3 control-label">Rechnungs / Lieferhinweis 1</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" name="deliveryInfo1" placeholder="">
	</div>    
  </div>

  <div class="form-group">
    <label for="deliveryInfo2" class="col-sm-3 control-label">Rechnungs / Lieferhinweis 2</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" name="deliveryInfo2" placeholder="">
	</div>    
  </div>

  <div class="form-group">
    <label for="deliveryInfo2" class="col-sm-3 control-label">Zahlweise</label>
    <div class="col-sm-8">
       <select name="paymentType" class="form-control">
       <option value="EACHDELIVERY" selected>Pro Lieferung</option>
       <option value="MONTHLY">Monatlich</option>
       <option value="HALFYEARLY">Halbjährlich</option>
       <option value="YEARLY">Jährlich</option>
       </select>
    </div>
  </div>

  <div class="form-group">
    <label for="shipmentCost" class="col-sm-3 control-label">bezahlt bis</label>
    <div class="col-sm-8">
	    <input type="type" class="form-control" name="payedUntil" placeholder="mm/jj">
	</div>    
  </div>

  <div class="form-group">
    <label for="deliveryInfo2" class="col-sm-3 control-label">Versandart</label>
    <div class="col-sm-8">
       <select name="shipmentType" class="form-control">
       <option value="MAIL">Per Post</option>
       <option value="PUBLISHER">Zustellung durch Verlag</option>
       <option value="PICKUP">Abholung durch Kunde</option>
       <option value="DELIVERY" selected>Belieferung durch Buchlese</option>
       </select>
    </div>
  </div>

<!--  <div class="form-group">
    <label for="shipmentCost" class="col-sm-3 control-label">Versandkosten</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" name="shipmentCost" placeholder="">
	</div>    
  </div>

   <div class="form-group">
    <label for="deliveryAddress.line1" class="col-sm-3 control-label">Lieferadresse Zeile 1</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" name="deliveryAddress.line1" placeholder="zeile1">
	</div>    
  </div>

  <div class="form-group">
    <label for="deliveryAddress.line2" class="col-sm-3 control-label">Lieferadresse Zeile 2</label>
    <div class="col-sm-8">
    <input type="text" class="form-control" name="deliveryAddress.line2" placeholder="">
	</div>    
  </div>
  
  <div class="form-group">
    <label for="deliveryAddress.line3" class="col-sm-3 control-label">Lieferadresse Zeile 3</label>
    <div class="col-sm-8">
    <input type="text" class="form-control" name="deliveryAddress.line3" placeholder="">
	</div>    
  </div>

  <div class="form-group">
    <label for="deliveryAddress.street" class="col-sm-3 control-label">Lieferadresse Strasse</label>
    <div class="col-sm-8">
    <input type="text" class="form-control" name="deliveryAddress.street" placeholder="Strasse">
	</div>    
  </div>


  <div class="form-group">
    <label for="deliveryAddress.postalcode" class="col-sm-3 control-label">Lieferadresse Plz</label>
    <div class="col-sm-2">
       <input type="text" class="form-control" name="deliveryAddress.postalcode" placeholder="Plz">
	</div>       
    <label for="deliveryAddress.city" class="col-sm-1 control-label"> Ort</label>
    <div class="col-sm-5">
      <input type="text" class="form-control" name="deliveryAddress.city" placeholder="Ort">
    </div>  
  </div>
 -->
</form>

<script>
   function formatProduct (product) {
    var markup = "<div class='select2-result-repository clearfix'>" +
      "<div class='select2-result-repository__meta'>" +
        "<div class='select2-result-repository__title'>" + product.abbrev + "</div>";

	markup += "<div class='select2-result-repository__description'>" + product.name + "</div>";

    markup += "</div></div>";

    return markup;
  }

  function formatProductSelection (product) {
	  if (typeof product.name == "undefined") {
		  return product.text;
	  }
    return product.name + " (" + product.id + ")";
  }

  function formatCustomer (subscriber) {
	    var markup = "<div class='select2-result-repository clearfix'>" +
	      "<div class='select2-result-repository__meta'>" +
	        "<div class='select2-result-repository__title'>" + subscriber.customerId + "</div>";

		markup += "<div class='select2-result-repository__description'>" + subscriber.name + "</div>";

	    markup += "</div></div>";

	    return markup;
	  }
  
  function formatCustomerSelection (subscriber) {
	  if (typeof subscriber.name == "undefined") {
		  return subscriber.text;
	  }
	  return subscriber.name + " (" + subscriber.customerId + ")";
  }
  
  $(document).ready(function() {
   $(".subscrproductselect").select2({
	   ajax: {
		    url: "/subscr/queryproduct",
		    dataType: 'json',
		    delay: 250,
		    data: function (params) {
		      return {
		        q: params.term, // search term
		        page: params.page
		      };
		    },
		    processResults: function (data, params) {
		      // parse the results into the format expected by Select2
		      // since we are using custom formatting functions we do not need to
		      // alter the remote JSON data, except to indicate that infinite
		      // scrolling can be used
		      params.page = params.page || 1;
		      return {
		        results: data,
		        pagination: {
		          more: (params.page * 30) < data.length
		        }
		      };
		    },
		    cache: true
		  },
		  escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
		  minimumInputLength: 1,
		  templateResult: formatProduct, 
		  templateSelection: formatProductSelection 
		});
   
   $(".subscriberselect").select2({
       ajax: {
		    url: "/subscr/querycustomers",
		    dataType: 'json',
		    delay: 250,
		    data: function (params) {
		      return {
		        q: params.term, // search term
		        page: params.page
		      };
		    },
		    processResults: function (data, params) {
		      // parse the results into the format expected by Select2
		      // since we are using custom formatting functions we do not need to
		      // alter the remote JSON data, except to indicate that infinite
		      // scrolling can be used
		      params.page = params.page || 1;
		      return {
		        results: data,
		        pagination: {
		          more: (params.page * 30) < data.length
		        }
		      };
		    },
		    cache: true
		  },
		  escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
		  minimumInputLength: 1,
		  templateResult: formatCustomer, 
		  templateSelection: formatCustomerSelection 
		});
	});
   
</script>

