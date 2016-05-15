<form id="entryForm" action="/subscr/productCreate" method="POST" class="form-horizontal">

  <div class="form-group">
    <label for="abbrev" class="col-sm-3 control-label">Abkürzung</label>
    <div class="col-sm-8">
       <input type="text" class="form-control" name="abbrev" placeholder="Abkü">
    </div>   
  </div>

  <div class="form-group">
    <label for="name" class="col-sm-3 control-label">Bezeichnung</label>
    <div class="col-sm-8">
       <input type="text" class="form-control" name="name" placeholder="Name">
    </div>   
  </div>

  <div class="form-group">
    <label for="publisher" class="col-sm-3 control-label">Verlag</label>
    <div class="col-sm-8">
       <input type="text" class="form-control" name="publisher" placeholder="Verlag">
    </div>   
  </div>

  <div class="form-group">
    <label for="period" class="col-sm-3 control-label">Periodizität in Monaten</label>
    <div class="col-sm-8">
       <input type="text" class="form-control" name="period" placeholder="1">
    </div>   
  </div>

  <div class="form-group">
    <label for="quantity" class="col-sm-3 control-label">Exemplare</label>
    <div class="col-sm-8">
       <input type="number" class="form-control" name="quantity" placeholder="1">
    </div>   
  </div>

  <div class="form-group">
    <label for="namePattern" class="col-sm-3 control-label">Namensmuster</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" name="namePattern" placeholder="Steuergesetze, #. Ergänzungslieferung">
	</div>    
  </div>

  <div class="form-group">
    <label for="count" class="col-sm-3 control-label">Ausgabennummer</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" name="count" placeholder="1">
	</div>    
  </div>

  <div class="form-group">
    <label for="halfPercentage" class="col-sm-3 control-label">Print-Prozentanteil</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" name="halfPercentage" placeholder="0.75">
	</div>    
  </div>


</form>
