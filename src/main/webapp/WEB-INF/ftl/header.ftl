<#macro nav cur>
	<#assign list=["status","contacts","groups"]/>
	<div data-role="head">
		<div data-role="navbar">
			<ul>
				<#list list as item><li><a data-transition="slide" data-rel="${item}" href="#${item}"<#if item==cur> class="ui-btn-active"</#if>>${item?capitalize}</a></li></#list>
			</ul>
		</div>
	</div>
</#macro>
