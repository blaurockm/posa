<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<link href="dojox/mobile/themes/android/android.css" rel="stylesheet"></link>
<title>Buchlese</title>
<link rel="stylesheet" href="/assets/posa/mobile.css" />

</head>
<body>
	<div id="umsatz" data-dojo-type="dojox.mobile.SwapView">
		<div data-dojo-type="dojox/mobile/Heading" data-dojo-props="label:'Buchlese ${posName?html}'"></div>
		<dl data-dojo-type="dojox.mobile.RoundRect">
			<dt>
				<span style="font-size: 200%; font-weight: bold">Umsatz</span>
			<dt>
			<dd>
				<p style="text-align: right">
					<span id="umsatzEUR" style="font-size: 200%">400,00</span>
				</p>
			</dd>
			<dt>
				<b>Tagesstatistik <span id="balanceDate"></span></b>
			</dt>
			<p>
				Erster Beleg <span id="firstTicket"></span> um <span
					id="firstTicketDate"></span><br /> Letzer Beleg <span
					id="lastTicket"></span> um <span id="lastTicketDate"></span>
			</p>
			<p>
				insgesamt <span id="ticketCount"></span> Belege
			</p>
		</dl>
	</div>
	<div id="details" data-dojo-type="dojox.mobile.SwapView">
		<h1 data-dojo-type="dojox.mobile.Heading">Buchlese
			${posName?html}</h1>
		<div data-dojo-type="dojox.mobile.RoundRect">
			<b>Details</b>
			<table width="95%">
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
					<td>Geschätzer Gewinn</td>
					<td id="profitEUR" style="text-align: right"></td>
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
					<td id="invPayedEUR" style="text-align:right"></td>
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
			<span style="font-size: 80%">
				<p>
				<table width="90%" id="gutscheineInGrp">
				</table>
				<p>
				<table width="90%" id="gutscheineOutGrp">
				</table>
			</span>
		</div>
	</div>
	<div id="chart" data-dojo-type="dojox.mobile.SwapView">
		<h1 data-dojo-type="dojox.mobile.Heading">Buchlese ${posName?html}</h1>
		<div data-dojo-type="dojox.mobile.RoundRect">
			<div id="pieFan" style="margin: 0pt"></div>
		</div>
	</div>
	<div id="compare" data-dojo-type="dojox.mobile.SwapView">
		<h1 data-dojo-type="dojox.mobile.Heading">Buchlese ${posName?html}</h1>
		<div data-dojo-type="dojox.mobile.RoundRect">
			<table width="95%">
				<tr>
					<td width="50%">Umsatz diese Woche</td>
					<td id="thisweekrevenueEUR" style="text-align: right"></td>
				</tr>
				<tr>
					<td>Umsatz letzte Woche</td>
					<td id="lastweekrevenueEUR" style="text-align: right"></td>
				</tr>
				<tr>
					<td>Umsatz diesen Monat</td>
					<td id="thismonthrevenueEUR" style="text-align: right"></td>
				</tr>
				<tr>
					<td>ges. Gewinn diese Woche</td>
					<td id="thisweekprofitEUR" style="text-align: right"></td>
				</tr>
				<tr>
					<td>ges. Gewinn letzte Woche</td>
					<td id="lastweekprofitEUR" style="text-align: right"></td>
				</tr>
				<tr>
					<td>ges. Gewinn diesen Monat</td>
					<td id="thismonthprofitEUR" style="text-align: right"></td>
				</tr>
			</table>
		</div>
	</div>

	<!-- provide config via dojoConfig global -->
	<script>
		var dojoConfig = (function() {
			var base = location.href.split("/");
			base.pop();
			base = base.join("/");

			return {
				async : true,
				isDebug : true,
				parseOnLoad : true,
				packages : [ {
					name : "posa",
					location : "/assets/posa"
				} ],
			};
		})();
	</script>
	<!-- load dojo -->
	<script src="//ajax.googleapis.com/ajax/libs/dojo/1.10.1/dojo/dojo.js"></script>
	<script>
		require([ "posa/mobile", "dojox/mobile", "dojox/mobile/parser",
				"dojox/mobile/SwapView", "dojox/mobile/compat", "dojox/mobile/deviceTheme",
				"dojo/domReady!" ], function(demoApp) {
			demoApp.init();
		});
	</script>
</body>
</html>