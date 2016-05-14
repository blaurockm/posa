<form id="entryForm" action="/subscr/customerCreate" method="POST" class="form-horizontal">

  <div class="form-group">
    <label for="abbrev" class="col-sm-3 control-label">Abkürzung</label>
    <div class="col-sm-8">
       <input type="text" class="form-control" id="abbrev" placeholder="Abkü">
    </div>   
  </div>

  <div class="form-group">
    <label for="name" class="col-sm-3 control-label">Bezeichnung</label>
    <div class="col-sm-8">
       <input type="text" class="form-control" id="name" placeholder="Name">
    </div>   
  </div>

  <div class="form-group">
    <label for="publisher" class="col-sm-3 control-label">Verlag</label>
    <div class="col-sm-8">
       <input type="text" class="form-control" id="publisher" placeholder="Verlag">
    </div>   
  </div>

  <div class="form-group">
    <label for="period" class="col-sm-3 control-label">Periodizität</label>
    <div class="col-sm-8">
       <input type="text" class="form-control" id="period" placeholder="1M6">
    </div>   
  </div>

  <div class="form-group">
    <label for="quantity" class="col-sm-3 control-label">Exemplare</label>
    <div class="col-sm-8">
       <input type="number" class="form-control" id="quantity" placeholder="1">
    </div>   
  </div>

  <div class="form-group">
    <label for="namePattern" class="col-sm-3 control-label">Namensmuster</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" id="namePattern" placeholder="Der Archivar {number} und {date:yyyy/MM}">
	</div>    
  </div>

  <div class="form-group">
    <label for="halfPercentage" class="col-sm-3 control-label">Print-Prozentanteil</label>
    <div class="col-sm-8">
	    <input type="text" class="form-control" id="halfPercentage" placeholder="0.75">
	</div>    
  </div>


</form>
