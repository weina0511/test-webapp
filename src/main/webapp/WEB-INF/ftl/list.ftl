<#if data.type=='status'>
	<ul>
		<#list data as item>
		<li>
			<p class="username">${item.userName}</p>
			<p class="message">${item.message}</p>
			<p class="date">${item.date}</p>
		</li>
		</#list>
	</ul>
<#else>
	<#list data as item>
	<div class="box">
		<a href="${item.url}" class="media"><img src="${item.portrait}" alt="${item.name}"></a>
		<div class="content">
			<p class="username"><a href="${item.url}">${item.userName}</a></p>
			<p class="headline">${item.headline}</p>
		</div>		
	</div>
	</#list>
</#if>
