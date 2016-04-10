<div class="container">
	<div class="row">
		<div class="col-md-6">
			<h3>FiBu-Export für beliebigen Zeitraum:</h3>
			<form method="POST" action="/cashbalance/fibuexport" target="_blank">
				<p>
					Von <input type="text" name="from" value="20150207" /> <br>
					Bis <input type="text" name="till" value="20150228" /> <br>
					Kasse Dornhan <input type="checkbox" name="kasse" value="1"
						checked="checked" /> Sulz <input type="checkbox" name="kasse"
						value="2" checked="checked" /><br> <input type="submit"
						value="Fibuexport" />
				</p>
			</form>
		</div>

		<div class="col-md-6">
			<h1>Back Office Version 1.0</h1>

			<h3>JVM-Daten</h3>
			<table border=1>
				<tr>
					<td>JVM</td>
					<td>${jvmVersion}</td>
				</tr>
				<tr>
					<td>Memory</td>
					<td>${jvmMemory}</td>
				</tr>
			</table>

			<h3>Performance</h3>
			<table border=1>
				<tr>
					<td>Wochenübersicht</td>
					<td>${weekViewAvg}</td>
				</tr>
				<tr>
					<td>Monatsübersicht</td>
					<td>${monthViewAvg}</td>
				</tr>
			</table>

			<h3>Konfiguration</h3>
			<table border=1>
				<tr>
					<td>Datenbank</td>
					<td>${dbconfig}</td>
				</tr>
			</table>

		</div>
	</div>
</div>
