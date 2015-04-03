<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>Monatsübersicht</title>
<link rel="stylesheet" href="/assets/bofc/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/assets/bofc/blueprint/print.css" type="text/css" media="print"> 

</head>
<body>
   <div class="container">
     <div class="span-24">
	<h1>Monat ${month.month} vom ${localDate(month.firstDay)} bis zum ${localDate(month.lastDay)}</h1>
	<h3>es gab insgesamt ${month.ticketCount} Belege</h3>
    </div>
    <div class="span-10 prepend-1 append-1">
		<b>Warengruppen</b>
		<table>
		 <#assign keys = articleGroupBalance?keys>
		 <#list keys as key>
		 	<tr>
		 	  <td>${key}</td> <td style="text-align: right">${money(articleGroupBalance[key])}</td>
		 	</tr>
		 </#list>	
		</table>
		<b>Kategorien</b>
		<table>
			<tr>
				<td>Warenverkauf</td>
				<td id="goodsoutEUR" style="text-align: right">${money(month.goodsOut)}</td>
			</tr>
			<tr>
				<td>Gutscheine verkauft</td>
				<td id="tradeoutEUR" style="text-align: right">${money(month.couponTradeOut)}</td>
			</tr>
		</table>
    </div>
    <div class="span-10 prepend-1 append-1 last">
		<b>Ertrag</b>
		<table>
			<tr>
				<td>Umsatz</td>
				<td id="umsatzEUR" style="text-align: right">${money(month.revenue)}</td>
			</tr>
			<tr>
				<td>Ertrag</td>
				<td id="profitEUR" style="text-align: right">${money(month.profit)}</td>
			</tr>
		</table>
		<b>Zahlungsarten</b>
		<table>
			<tr>
				<td>Abschöpfung</td>
				<td id="cashEUR" style="text-align: right">${money(month.absorption)}</td>
			</tr>
			<tr>
				<td>TeleCash</td>
				<td id="teleEUR" style="text-align: right">${money(month.telecash)}</td>
			</tr>
			<tr>
				<td>Bareinnahmen</td>
				<td id="teleEUR" style="text-align: right">${money(month.cash)}</td>
			</tr>
			<tr>
				<td>Gutscheine eingelöst</td>
				<td id="tradeinEUR" style="text-align: right">${money(month.couponTradeIn)}</td>
			</tr>
			<tr>
				<td>Auszahlungen</td>
				<td id="payoutEUR" style="text-align: right">${money(month.cashOutSum)}</td>
			</tr>
			<tr>
				<td>Einzahlungen</td>
				<td id="payinEUR" style="text-align: right">${money(month.cashInSum)}</td>
			</tr>
			<tr>
				<td>Rechnungen bar bez.</td>
				<td id="invPayedEUR" style="text-align: right">${money(month.payedInvoicesSum)}</td>
			</tr>
		</table>	
	</div>
	
	<div class="span-24">
	<ol>
	  <#list month.problems as problem>
		<li class="error"> ${problem} </li>
	  </#list>	
	</ol>
	</div>
</div>
</body>
</html>
