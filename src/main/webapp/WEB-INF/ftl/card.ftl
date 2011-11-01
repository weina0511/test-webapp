{
	<#list card.data as item>
	"email":[<#list item.emails as e>"${e}"<#if item_has_next>,</#if></#list>],
	"phone":[<#list item.phones as p>"${p.number}"<#if item_has_next>,</#if></#list>]
	</#list>
}
