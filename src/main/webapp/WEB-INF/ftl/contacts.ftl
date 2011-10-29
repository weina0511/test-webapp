<#list contacts.data as item>
	<div class="box">
		<a href="${item.link}" class="media"><img src="${item.picture_small}" alt="${item.name}"></a>
		<div class="content">
			<p class="username"><a href="${item.link}">${item.name}</a></p>
			<p class="headline">${item.headline}</p>
		</div>		
	</div>
</#list>
	
