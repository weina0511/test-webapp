

{
	"items": [<#list contacts as item>{
		"link":"${item.link}",
		"picture":"${item.picture_small}",
		"name":"${item.name}",
		"headline":"${item.headline}",
		"id":"${item.id}"
	}<#if item_has_next>,</#if></#list>]
}
