<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="utf-8" />
    <title>Statusanzeige Backoffice-App</title>
<style type="text/css">
.week {
   border:1pt solid black;
}

.wotagH {
   border:1pt solid red;
}

.wotag {
   border: 1pt solid green;
}

.valueField {
   padding: 5pt;
} 
</style>
</head>
<body>
    <script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
    <script>
$(function() {

    function displayPosState(pos) {
	    var view = posView.clone().appendTo("#placeholder");
		view.find("#posId").find("span.value").html(pos.pointid);
		view.find("#posRevenue").find("span.value").html(pos.revenue.formatMoney());
		view.find("#posProfit").find("span.value").html(pos.profit.formatMoney());
		var akt = new Date(pos.timest)
		view.find("#posLastSeen").find("span.value").html(akt.toLocaleTimeString() + " " + akt.toLocaleDateString());
	}
	
      function showPosStates(data) {
        for (var i in data) {
		    displayPosState(data[i]);
        }

	  }
	  
	  var posView = $("#posStateView");
	  
      $.getJSON('posState').done(showPosStates);
	  
});
    </script> 
	<script>
	Number.prototype.formatMoney = function(c, d, t){
	var n = this, 
	    c = isNaN(c = Math.abs(c)) ? 2 : c, 
	    d = d == undefined ? "," : d, 
	    t = t == undefined ? "." : t, 
	    s = n < 0 ? "-" : "", 
	    i = parseInt(n = Math.abs(+(n/100.0) || 0).toFixed(c)) + "", 
	    j = (j = i.length) > 3 ? j % 3 : 0;
	   return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "") + " €";
	 };
 </script>
    <div id="placeholder"></div>
	<hr style="clear:both">
	<div id="posStateView" style="clear:left; border:1px solid black; float:left;">
	  <div id="posId" style="float:left; padding:6pt;"><span style="font-size:300%" class="value">1</span></div>
	  <div style="float:left;">
	  <div id="posRevenue" class="valueField" style="float:left">Umsatz <span class="value">0,00</span></div>
	  <div id="posProfit" class="valueField" style="float:left">Rohertrag <span class="value">0,00</span></div>
	  <div id="posLastSeen" class="valueField" style="float:right">Aktuell <span class="value">14:55 29.12.2015</span></div>
	  <div id="posCashBalances" style="clear:left; float:left; position:relative; display:block;">
	  <div style="float:left;">
	  <table class="week">
	    <tr><th class="wotagH">Mo</th><th class="wotagH">Di</th><th class="wotagH">Mi</th><th class="wotagH">Do</th><th class="wotagH">Fr</th><th class="wotagH">Sa</th><th class="wotagH">So</th></tr>
		<tr><td class="wotag"></td><td class="wotag"></td><td class="wotag"></td><td class="wotag"></td><td class="wotag"></td><td class="wotag"></td><td class="wotag"></td></tr>
	  </table></div>
	  <div style="float:left;">
	  <table class="week">
	    <tr><th class="wotagH">Mo</th><th class="wotagH">Di</th><th class="wotagH">Mi</th><th class="wotagH">Do</th><th class="wotagH">Fr</th><th class="wotagH">Sa</th><th class="wotagH">So</th></tr>
		<tr><td class="wotag"></td><td class="wotag"></td><td class="wotag"></td><td class="wotag"></td><td class="wotag"></td><td class="wotag"></td><td class="wotag"></td></tr>
	  </table></div>
	
	  </div> 
	  </div>
	</div>
</body>
</html>