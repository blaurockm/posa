<div class="container">

<h1>Rechnungen</h1>
<table class="table table-striped">
	<thead>
		<tr>
			<th>Rechnr</th>
			<th>Datum</th>
			<th>Kunde</th>
			<th>LieferVon</th>
			<th>LieferBis</th>
			<th>Betrag</th>
			<th>Storniert?</th>
			<th>Gedruckt?</th>
			<th>Sammel?</th>
			<th>bezahlt?</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list invoices as inv>
		<tr>
			<td>${inv.number}</td>
			<td>${(inv.date)!}</td>
			<td>${inv.name1!}</td>
			<td>${(inv.deliveryFrom)!}</td>
			<td>${(inv.deliveryTill)!}</td>
			<td>${money(inv.amount)}</td>
			<td>${inv.cancelled?string("storniert","")}</td>
			<td>${inv.printed?string("gedruckt","")}</td>
			<td>${inv.collective?string("sammel","")}</td>
			<td>${inv.payed?string("bezahlt","")}</td>
			<td><a href="/subscr/invoiceView/${inv.number}"  data-toggle="tooltip" title="PDF" class="btn btn-default btn-sm" target="_blank"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
                <#if inv.cancelled == false>			
                <a data-href="#invoiceCancel/${inv.number}" data-toggle="modal" data-target="#confirm-cancel" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
                </#if>
			 </td>
		</tr>
		</#list>
	</tbody>
</table>


</div>
	
    <div class="modal fade" id="confirm-cancel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
            
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">Storno bestätigen</h4>
                </div>
            
                <div class="modal-body">
                    <p>Sie möchten eine Rechnung stornieren!</p>
                    <p>Sind Sie sich sicher?</p>
                    <p class="debug-url"></p>
                </div>
                
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Abbrechen</button>
                    <a class="btn btn-danger btn-ok">Stornieren</a>
                </div>
            </div>
        </div>
    </div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
    $('#confirm-cancel').on('show.bs.modal', function(e) {
        $(this).find('.btn-ok').attr('href', $(e.relatedTarget).data('href'));
        
        $('.debug-url').html('Delete URL: <strong>' + $(this).find('.btn-ok').attr('href') + '</strong>');
    });
});

</script>

	