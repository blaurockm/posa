
<div class="container">
<div class="row">
	<div class="col-md-12">
		<h2>Commands noch zu tun</h2>
		<ul class="list-group">
			<#list cmdsToDo as cmd>
			<li class="list-group-item">${cmd.pointId} ${cmd.action}
			</li> </#list>
		</ul>
	</div>
	</div>
	<hr>
<div class="row">
	<div class="col-md-12">
		<h2>Erledigte Kommandos</h2>
		<ul class="list-group">
			<#list cmdsDone as cmd>
			<li class="list-group-item">${cmd.pointId} ${cmd.action} <#if cmd.result??>${cmd.result}<#else> noch nix</#if>
			</li> </#list>
		</ul>
	</div>
	</div>
</div>
