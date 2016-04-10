
<div class="container">
	<!-- Example row of columns -->
	<div class="row">
		<div class="col-md-4">
			<h2>Kasse Dornhan</h2>
			<form class="form-horizontal" action="/fibu/export" method="POST" target="_blank">
				<input type="hidden" name="kasse" value="1">
				<div class="form-group">
					<label for="dornhanFrom">Ab Datum</label> <input type="date"
						placeholder="ab" name="from" class="form-control" id="dornhanFrom"
						value="${dornhanFrom}">
				</div>
				<div class="form-group">
					<label for="dornhanTill">Bis Datum</label> <input type="date"
						placeholder="bis" name="till" class="form-control"
						id="dornhanTill" value="${dornhanTill}">
				</div>
				<button type="submit" class="btn btn-success">Export</button>
			</form>
		</div>
		<div class="col-md-4">
			<h2>Kasse Sulz</h2>
			<form class="form-horizontal" action="/fibu/export" method="POST" target="_blank">
				<input type="hidden" name="kasse" value="2">
				<div class="form-group">
					<label for="sulzFrom">Ab Datum</label> <input type="date"
						placeholder="ab" name="from" class="form-control" id="sulzFrom"
						value="${sulzFrom}">
				</div>
				<div class="form-group">
					<label for="sulzTill">Bis Datum</label> <input type="date"
						placeholder="bis" name="till" class="form-control" id="sulzTill"
						value="${sulzTill}">
				</div>
				<button type="submit" class="btn btn-success">Export</button>
			</form>
		</div>
		<div class="col-md-4">
			<h2>Kasse Schramberg</h2>
			<form class="form-horizontal" action="/fibu/export" method="POST" target="_blank">
				<input type="hidden" name="kasse" value="3">
				<div class="form-group">
					<label for="schrambergFrom">Ab Datum</label> <input type="date"
						placeholder="ab" name="from" class="form-control"
						id="schrambergFrom" value="${schrambergFrom}">
				</div>
				<div class="form-group">
					<label for="schrambergTill">Bis Datum</label> <input type="date"
						placeholder="ab" name="till" class="form-control"
						id="schrambergTill" value="${schrambergTill}">
				</div>
				<button type="submit" class="btn btn-success">Export</button>
			</form>
		</div>
	</div>

</div>
