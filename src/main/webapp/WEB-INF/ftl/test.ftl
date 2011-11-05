<!DOCTYPE HTML>
<html>
<head>
	<meta charset="UTF-8">
	<title>app</title>
	<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0rc2/jquery.mobile-1.0rc2.min.css" />
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.4.min.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.0rc2/jquery.mobile-1.0rc2.min.js"></script>				
	<script type="text/javascript" src="//maps.googleapis.com/maps/api/js?sensor=false"></script>
</head>
<body>
	<div data-role="page" id="status">
		<div data-role="head">
			<div data-role="navbar">
				<ul>
					<li><a data-transition="slide" data-rel="status" href="#status" class="ui-btn-active ui-state-persist">近况</a></li>
					<li><a data-transition="slide" data-rel="contacts" href="#contacts">附近的好友</a></li>
				</ul>
			</div>
		</div>
		<div data-role="content" class="ui-content">
			<form action="/status" method="POST">
				<h2>发布近况</h2>
				<textarea id="message" name="message"></textarea>
				<p class="buttons"><input type="submit" value="发布" class="submit" data-role="button" data-inline="true">&nbsp;<input type="button" value="发布当前位置" class="button" data-role="button" data-inline="true"></p>
			</form>
		
			<ul data-role="listview" data-inset="true" id="statusList">
				<#list feedlist.data as item>
				<li data-id="${item.from.id}" data-url="${item.from.link}">
					<a href="#dialog" data-rel="dialog" class="toCard"><img src="${item.from.picture_small}" alt="${item.name}" class="img"/>
					<h3 class="username" >${item.name}</h3>
					<p class="message">${item.label} ${item.message}</p>
					<p class="date">${item.updated_time?string("yyyy-MM-dd HH:mm")}</p>
					</a>
				</li>
				</#list>
			</ul>
		</div>
	</div>
	<div data-role="page" id="contacts">
		<div data-role="head">
			<div data-role="navbar">
				<ul>
					<li><a data-transition="slide" data-rel="status" href="#status" data-direction="reverse">近况</a></li>
					<li><a data-transition="slide" data-rel="contacts" href="#contacts" class="ui-btn-active ui-state-persist">附近的好友</a></li>
				</ul>
			</div>
		</div>
		<div data-role="content" class="ui-content"></div>
	</div>
	<div data-role="page" id="dialog"><div data-role="head"></div><div data-role="content"></div></div>
	<script type="text/javascript">
	var MF = function(str){
	    var args = [].slice.call(arguments,1);
	    return str.replace(/\{([^}])\}/g,function(index,value){
	        return args[value];
	    });
	};
	
	function initialize(pos) {
		var geocoder = new google.maps.Geocoder(),
		latlng = new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
		geocoder.geocode({'latLng': latlng}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				var cityname=results[0].address_components[results[0].address_components.length-3].long_name;
				$.ajax({
					url:'/contacts?location='+cityname,
					dataType:'json',
					success: function(data){						
						var li='<li data-id="{3}" data-url="${4}"><a class="toCard" href="#dialog" data-transition="pop" data-rel="dialog"><img src="{0}" alt="{1}" class="img"/><h3 class="username" >{1}</h3><p class="headline">{2}</p></a></li>',
						head='<div data-role="head"><div data-role="navbar"><ul><li><a data-transition="slide" data-rel="status" href="#status" data-direction="reverse">近况</a></li><li><a data-transition="slide" data-rel="contacts" href="#contacts" class="ui-btn-active">附近的好友</a></li></ul></div></div>',
						list=['<ul data-role="listview" data-inset="true" id="contactsList">'],
						item=data.items,
						$page=$('#contacts'),
						$header = $page.children( ":jqmData(role=header)" ),
						$content = $page.children( ":jqmData(role=content)" );
						
						for(var i in item){
							list.push(MF(decodeURI(li),item[i].picture,item[i].name,item[i].headline,item[i].id,item[i].link))
						};
						list.push('</ul>');
						
						$header.html('<div data-role="head"><div data-role="navbar"><ul><li><a data-transition="slide" data-rel="status" href="#status" data-direction="reverse">近况</a></li><li><a data-transition="slide" data-rel="contacts" href="#contacts" class="ui-btn-active">附近的好友</a></li></ul></div></div>');
						$content.html(list.join(''));
						$page.page();
						$content.find( ":jqmData(role=listview)" ).listview();
						$('.button').click(function(){
							var temp,str=(temp=results[0].formatted_address).substring(0,temp.indexOf(' ')>0?temp.indexOf(' '):temp.length);
							$('textarea').val(['我在:"',str,'"'].join(''));
						})
					}
				})
				
			} else {
			alert("Geocoder failed due to: " + status);
			}
		});
	};
	if (navigator.geolocation) {
	  navigator.geolocation.getCurrentPosition(initialize);
	} else {
	  error('not supported');
	};
	var showCard=function(){
		var that=this;
		$('body').delegate('a.toCard','click',function(){
			var self=this;
			$.ajax({
				url:'/card?id='+$(this).closest('li').attr('data-id'),
				dataType:'json',
				success: function(data){
					var li=$(self).closest('li'),
						div=$(self).closest('div[data-role=content]').parent(),
						$page=$('#dialog'),
						$content=$page.children( ":jqmData(role=content)" );
					data=$.extend(data,{
						name:li.find('.username').html(),
						picture:li.find('.img').attr('src'),
						link:li.attr('data-url'),
						target:div.attr('id')
					});
					
					var	template=MF(decodeURI('<ul data-role="listview" data-inset="true" data-theme="c"><li><img src="{5}" alt="" class="img"/><h3><a href="{0}">{1}</a></h3><p class="email"><span>邮件：</span><a href="mailto:{2}">{2}</a></p><p class="phone"><span>电话：</span><a href="tel:{3}">{3}</a></p></li></ul><p><a href="#" data-role="button" data-inline="true" data-direction="reverse" data-rel="back">返回</a></p>'),data.link,data.name,data.email[0],data.phone[0],'#'+data.target,data.picture)

					$content.html(template).trigger('create');
					$page.page();
				}
			})
		})
	};
	$('#status').swipeleft(function(){
		$.mobile.changePage( "#contacts", { 
			transition: "slide"
		});
	});
	$('#contacts').swiperight(function(){
		$.mobile.changePage( "#status", {
			transition: "slide",
			reverse:true
		});
	});
	showCard();
</script>
</body>
</html>