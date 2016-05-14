<form id="entryForm" action="/subscr/customerCreate" method="POST" class="form-horizontal">

  <div class="form-group">
    <label for="pointid" class="col-sm-3 control-label">Ladengeschäft</label>
    <div class="col-sm-8">
       <p class="form-control-static">Schramberg</p>
    </div>
  </div>

  <div class="form-group">
    <label for="subscriberId" class="col-sm-3 control-label">Geschäftspartner</label>
    <div class="col-sm-8">
       <p class="form-control-static">${subscriber.name!}</p>
	 <!--  <input type="text" class="form-control" id="subscriberId" placeholder="Kundennummer"> -->
	</div>
  </div>

  <div class="form-group">
    <label for="productId" class="col-sm-3 control-label">Periodika</label>
    <div class="col-sm-8">
       <input type="text" class="form-control" id="productId" placeholder="Abkü Periodika">
    </div>   
  </div>

  <div class="form-group">
    <label for="quantity" class="col-sm-3 control-label">Exemplare</label>
    <div class="col-sm-8">
       <input type="number" class="form-control" id="quantity" placeholder="1">
    </div>   
  </div>

  <div class="form-group">
    <label for="deliveryInfo1" class="col-sm-3 control-label">Lieferhinweis</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" id="deliveryInfo1" placeholder="">
	</div>    
  </div>

  <div class="form-group">
    <label for="deliveryInfo2" class="col-sm-3 control-label">Lieferhinweis 2</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" id="deliveryInfo2" placeholder="">
	</div>    
  </div>

  <div class="form-group">
    <label for="deliveryInfo2" class="col-sm-3 control-label">Versandart</label>
    <div class="col-sm-8">
       <select id="shipmentType" class="form-control">
       <option value="MAIL">Per Post</option>
       <option value="PUBLISHER">Zustellung durch Verlag</option>
       <option value="PICKUP">Abholung durch Kunde</option>
       <option value="DELIVERY" selected>Belieferung durch Buchlese</option>
       </select>
    </div>
  </div>

  <div class="form-group">
    <label for="shipmentCost" class="col-sm-3 control-label">Versandkosten</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" id="shipmentCost" placeholder="">
	</div>    
  </div>

  <div class="form-group">
    <label for="deliveryAddress.line1" class="col-sm-3 control-label">Lieferadresse Zeile 1</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" id="deliveryAddress.line1" placeholder="zeile1">
	</div>    
  </div>
  
  <div class="form-group">
    <label for="deliveryAddress.line1" class="col-sm-3 control-label">Lieferadresse Zeile 1</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" id="deliveryAddress.line1" placeholder="zeile1">
	</div>    
  </div>

  <div class="form-group">
    <label for="deliveryAddress.line2" class="col-sm-3 control-label">Lieferadresse Zeile 2</label>
    <div class="col-sm-8">
    <input type="text" class="form-control" id="deliveryAddress.line2" placeholder="">
	</div>    
  </div>
  
  <div class="form-group">
    <label for="deliveryAddress.line3" class="col-sm-3 control-label">Lieferadresse Zeile 3</label>
    <div class="col-sm-8">
    <input type="text" class="form-control" id="deliveryAddress.line3" placeholder="">
	</div>    
  </div>

  <div class="form-group">
    <label for="deliveryAddress.street" class="col-sm-3 control-label">Lieferadresse Strasse</label>
    <div class="col-sm-8">
    <input type="text" class="form-control" id="deliveryAddress.street" placeholder="Strasse">
	</div>    
  </div>


  <div class="form-group">
    <label for="deliveryAddress.postalcode" class="col-sm-3 control-label">Lieferadresse Plz</label>
    <div class="col-sm-2">
       <input type="text" class="form-control" id="deliveryAddress.postalcode" placeholder="Plz">
	</div>       
    <label for="deliveryAddress.city" class="col-sm-1 control-label"> Ort</label>
    <div class="col-sm-5">
      <input type="text" class="form-control" id="deliveryAddress.city" placeholder="Ort">
    </div>  
  </div>


</form>
