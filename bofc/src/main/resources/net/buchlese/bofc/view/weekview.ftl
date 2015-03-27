<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>Wochenübersicht</title>

</head>
<body>
	<h1>Woche ${week.week} vom ${localDate(week.firstDay)} bis zum ${localDate(week.lastDay)}</h1>
	<h3>es gab insgesamt ${week.ticketCount} Belege</h3>

	<div style="width: 50%; float: left; background: red">
		<div id="pieFan" style="margin: 0pt"></div>
	</div>
	<div style="width: 50%; clear: right; float: right; background: green">
		<b>Warengruppen</b>
	</div>
	<div id="details" style="width: 50%; clear: left; float: left; background: blue">
		<b>Details</b>
		<table width="90%">
			<tr>
				<td width="50%">Bar</td>
				<td id="cashEUR" style="text-align: right"></td>
			</tr>
			<tr>
				<td>TeleCash</td>
				<td id="teleEUR" style="text-align: right"></td>
			</tr>
			<tr>
				<td>Warenverkauf</td>
				<td id="goodsoutEUR" style="text-align: right"></td>
			</tr>
			<tr>
				<td>Auszahlungen</td>
				<td id="payoutEUR" style="text-align: right"></td>
			</tr>
			<tr>
				<td>Einzahlungen</td>
				<td id="payinEUR" style="text-align: right"></td>
			</tr>
			<tr>
				<td>Rechnungen bez.</td>
				<td id="invPayedEUR" style="text-align: right"></td>
			</tr>
			<tr>
				<td>Gutscheine eingelöst</td>
				<td id="tradeinEUR" style="text-align: right"></td>
			</tr>
			<tr>
				<td>Gutscheine verkauft</td>
				<td id="tradeoutEUR" style="text-align: right"></td>
			</tr>
		</table>
	</div>
	<div style="width: 50%; clear: right; float: right; background: gray">
		<b>Ertrag</b>
		<table width="90%">
			<tr>
				<td width="50%">Umsatz</td>
				<td id="umsatzEUR" style="text-align: right">${money(week.revenue)}</td>
			</tr>
			<tr>
				<td>Ertrag</td>
				<td id="profitEUR" style="text-align: right">${money(week.profit)}</td>
			</tr>
			<tr>
				<td>Ertrag letzte Woche</td>
				<td id="profitLastWeekEUR" style="text-align: right"></td>
			</tr>
			<tr>
				<td>Ertrag diesen Monat</td>
				<td id="profitThisMonthEUR" style="text-align: right"></td>
			</tr>
		</table>
	</div>
	
	<div style="width: 100%; clear: left; float: left;">
	<ol>
		<li>Kassenanfangsbestände unstimmig.
		<li>Problem bei folgenden Kassenberichten:
	</ol>
	</div>

</body>
</html>
