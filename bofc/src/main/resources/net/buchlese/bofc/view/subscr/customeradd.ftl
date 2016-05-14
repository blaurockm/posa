<form id="entryForm" action="/subscr/customerCreate" method="POST" class="form-horizontal">

  <div class="form-group">
    <label for="pointid" class="col-sm-3 control-label">Ladengeschäft</label>
    <div class="col-sm-8">
       <select id="pointid" class="form-control">
       <option value="1">Dornhan</option>
       <option value="2">Sulz</option>
       <option value="3" selected>Schramberg</option>
       </select>
    </div>
  </div>

  <div class="form-group">
    <label for="name" class="col-sm-3 control-label">Geschäftspartner Name</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" id="name" placeholder="Name">
	</div>
  </div>

  <div class="form-group">
    <label for="customerId" class="col-sm-3 control-label">Kundenummer (Libras)</label>
    <div class="col-sm-8">
       <input type="text" class="form-control" id="customerId" placeholder="librasnummer">
    </div>   
  </div>
  <div class="form-group">
    <div class="col-sm-offset-3 col-sm-8">
    <div class="checkbox">
      <label>
         <input type="checkbox" id="collectiveInvoice" value="collIinvoice">  Sammelrechnung gewünscht
      </label>
    </div>
    </div>
  </div>  
  <div class="form-group">
    <div class="col-sm-offset-3 col-sm-8">
	  <div class="checkbox">
	    <label>
	      <input type="checkbox" id="needsDeliveryNote" value="deliveryNote"> Lieferschein benötigt
	    </label>
	  </div>
    </div>
  </div>  
  
  <div class="form-group">
    <label for="invoiceAddress.line1" class="col-sm-3 control-label">Rechnungsadresse Zeile 1</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" id="invoiceAddress.line1" placeholder="zeile1">
	</div>    
  </div>

  <div class="form-group">
    <label for="invoiceAddress.line2" class="col-sm-3 control-label">Rechnungsadresse Zeile 2</label>
    <div class="col-sm-8">
    <input type="text" class="form-control" id="invoiceAddress.line2" placeholder="">
	</div>    
  </div>
  
  <div class="form-group">
    <label for="invoiceAddress.line3" class="col-sm-3 control-label">Rechnungsadresse Zeile 3</label>
    <div class="col-sm-8">
    <input type="text" class="form-control" id="invoiceAddress.line3" placeholder="">
	</div>    
  </div>

  <div class="form-group">
    <label for="invoiceAddress.street" class="col-sm-3 control-label">Rechnungsadresse Strasse</label>
    <div class="col-sm-8">
    <input type="text" class="form-control" id="invoiceAddress.street" placeholder="Strasse">
	</div>    
  </div>


  <div class="form-group">
    <label for="invoiceAddress.postalcode" class="col-sm-3 control-label">Rechnungsadresse Plz</label>
    <div class="col-sm-2">
       <input type="text" class="form-control" id="invoiceAddress.postalcode" placeholder="Plz">
	</div>       
    <label for="invoiceAddress.city" class="col-sm-1 control-label"> Ort</label>
    <div class="col-sm-5">
      <input type="text" class="form-control" id="invoiceAddress.city" placeholder="Ort">
    </div>  
  </div>


</form>
