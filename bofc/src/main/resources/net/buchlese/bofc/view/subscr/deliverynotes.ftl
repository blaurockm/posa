<div class="container">

<h1>Lieferscheine</h1>
<table class="table table-striped">
	<thead>
		<tr>
			<th>Nr</th>
			<th>Datum</th>
			<th>Kunde</th>
			<th>LieferVon</th>
			<th>LieferBis</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list deliveryNotes as inv>
		<tr>
			<td>${inv.number}</td>
			<td>${(inv.date)!}</td>
			<td>${inv.name1!}</td>
			<td>${(inv.deliveryFrom)!}</td>
			<td>${(inv.deliveryTill)!}</td>
			<td><a href="/subscr/getdeliverynote/${inv.number}"  data-toggle="tooltip" title="PDF" class="btn btn-default btn-sm" target="_blank"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
			 </td>
		</tr>
		</#list>
	</tbody>
</table>


</div>
	

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
});

</script>

	