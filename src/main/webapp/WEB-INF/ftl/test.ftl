<!DOCTYPE HTML>
<html>
<head>
	<meta charset="UTF-8">
	<title>app</title>
	<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0rc2/jquery.mobile-1.0rc2.min.css" />
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.4.min.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.0rc2/jquery.mobile-1.0rc2.min.js"></script>				
	<script type="text/javascript" src="http://www.ryanscherf.net/demos/swipe/jquery.jswipe.js"></script>
	<script type="text/javascript" src="//maps.googleapis.com/maps/api/js?sensor=false"></script>
</head>
<body>
	<!-- 
		<a data-transition="slide" data-rel="dialog" data-role="button" href="#dialog" data-theme="c" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-c"><span aria-hidden="true" class="ui-btn-inner ui-btn-corner-all"><span class="ui-btn-text">slide</span></span></a>
	-->
	<div data-role="page" id="status">
		<div data-role="head">
			<div data-role="navbar">
				<ul>
					<li><a data-transition="slide" data-rel="status" href="#status" class="ui-btn-active">近况</a></li>
					<li><a data-transition="slide" data-rel="contacts" href="#contacts">附近的好友</a></li>
				</ul>
			</div>
		</div>
		<div data-role="content" class="ui-content">
			<ul data-role="listview" data-inset="true" id="statusList">
				<#list feedlist.data as item>
				<li>
					<a href="${item.from.link}" target="_blank"><img src="${item.from.picture_small}" alt="${item.name}">
					<h3 class="username" >${item.name}</h3>
					<p class="message">${item.label} ${item.message}</p>
					<p class="date">${item.updated_time?string("yyyy-MM-dd")}</p>
					</a>
				</li>
				</#list>
			</ul>
			<form action="/status" method="POST">
				<h2>发布近况</h2>
				<textarea id="message" name="message"></textarea>
				<p><input type="submit" value="发布" class="submit" data-role="button" data-inline="true"></p>
			</form>
		</div>
	</div>
	<div data-role="page" id="contacts">
		<div data-role="head">
			<div data-role="navbar">
				<ul>
					<li><a data-transition="slide" data-rel="status" href="#status" data-direction="reverse">近况</a></li>
					<li><a data-transition="slide" data-rel="contacts" href="#contacts" class="ui-btn-active">附近的好友</a></li>
				</ul>
			</div>
		</div>
		<div data-role="content" class="ui-content">
			<ul data-role="listview" data-inset="true" id="contactsList">
				<li data-id="{3}" data-url="${4}">
					<a href="#dialog" data-transition="pop" data-rel="dialog"><img src="{0}" alt="{1}" class="img"/>
					<h3 class="username" >{1}</h3>
					<p class="headline">{2}</p>
					</a>
				</li>
			</ul>
		</div>
	</div>
	<div data-role="page" id="dialog">
		<div data-role="content">
		<ul data-role="listview" data-inset="true" data-theme="c"><li>
			<img src="{1}" alt="{2}" class="img"/>
			<h3><a href="{0}">{2}</a></h3>
			<p class="email"><span>邮件：</span><a href="mailto:{3}">{3}</a></p>
			<p class="phone"><span>电话：</span><a href="tel:{4}">{4}</a></p>
		</li></ul>
		<p><a href="#contacts" data-role="button" data-inline="true">返回</a></p>
		</div>
	</div>
	<script type="text/javascript">
	var MessageFormat = function(str){
	    var args = [].slice.call(arguments,1);
	    return str.replace(/\{([^}])\}/g,function(index,value){
	        return args[value];
	    })
	};
	var showCard=function(){
		var that=this;
		$('#contacts').delegate('.ui-content a','click',function(){
			var self=this;
			$.ajax({
				url:'/card?id='+$(this).closest('li').attr('data-id'),
				dataType:'json',
				success: function(data){
					if (!that.template) {
						that.template=$('#dialog').html();
					}
					
					data=$.extend(data,{
						name:$(self).closest('li').find('.username').html(),
						picture:$(self).closest('li').find('.img').attr('src'),
						link:$(self).closest('li').attr('data-url')
					})
					$('#dialog').html(MessageFormat(decodeURI(that.template),data.link,data.picture,data.name,data.email[0],data.phone[0]))
				}
			})
		})
	}
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
						var template=$('#contactsList').html(),list=[],item=data.items;
						for(var i in item){
							list.push(MessageFormat(decodeURI(template),item[i].picture,item[i].name,item[i].headline,item[i].id,item[i].link))
						};
						$('#contactsList').html(list.join(''));
						showCard()
					}
				})
				
			} else {
			alert("Geocoder failed due to: " + status);
			}
		});
	}
	if (navigator.geolocation) {
	  navigator.geolocation.getCurrentPosition(initialize);
	} else {
	  error('not supported');
	}	


</script>
</body>
</html>