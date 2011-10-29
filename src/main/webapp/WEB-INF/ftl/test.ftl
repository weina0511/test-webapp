<!DOCTYPE HTML>

<#macro nav cur>
	<#assign list=["status","contacts","groups"]/>
</#macro>

<html>
<head>
	<meta charset="UTF-8">
	<title>app</title>
	<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0rc2/jquery.mobile-1.0rc2.min.css" />
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.4.min.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.0rc2/jquery.mobile-1.0rc2.min.js"></script>				
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
</head>
<body>
	<!-- 
	<a data-transition="slide" data-rel="dialog" data-role="button" href="#dialog" data-theme="c" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-c"><span aria-hidden="true" class="ui-btn-inner ui-btn-corner-all"><span class="ui-btn-text">slide</span></span></a>
	 -->
	<div data-role="page" id="status">
		<div data-role="head">
			<div data-role="navbar">
				<ul>
					<li><a data-transition="slide" data-rel="status" href="#status" class="ui-btn-active">Status</a></li>
					<li><a data-transition="slide" data-rel="contacts" href="#contacts">contacts</a></li>
					<li><a data-transition="slide" data-rel="groups" href="#groups">groups</a></li>
				</ul>
			</div>
		</div>
		<div data-role="content" class="margin">
			<ul data-role="listview" data-inset="true" id="statusList">
				<#list feedlist.data as item>
				<li>
					<p class="username" link="${item.link}">--${item.name}</p>
					<p class="message">${item.label} ${item.message}</p>
					<p class="date">${item.updated_time?string("yyyy-MM-dd")}</p>
				</li>
				</#list>
			</ul>
			<form action="/app/status" method="POST">
				<h2>Post a status</h2>
				<textarea id="message" name="message"></textarea>
				<p><input type="submit" value="POST" class="submit" data-role="button" data-inline="true"></p>
			</form>
		</div>
	</div>
	<div data-role="page" id="contacts">
		<div data-role="head">
			<div data-role="navbar">
				<ul>
					<li><a data-transition="slide" data-rel="status" href="#status" data-direction="reverse">Status</a></li>
					<li><a data-transition="slide" data-rel="contacts" href="#contacts" class="ui-btn-active">contacts</a></li>
					<li><a data-transition="slide" data-rel="groups" href="#groups">groups</a></li>
				</ul>
			</div>
		</div>
		<div data-role="content" class="margin">
			<p>a contact</p>
			<a href="#dialog" data-rel="dialog" data-transition="pop">Open dialog</a>
		</div>
	</div>
	<div data-role="page" id="groups">
		<div data-role="head">
			<div data-role="navbar">
				<ul>
					<li><a data-transition="slide" data-rel="status" href="#status" data-direction="reverse">Status</a></li>
					<li><a data-transition="slide" data-rel="contacts" href="#contacts" data-direction="reverse">contacts</a></li>
					<li><a data-transition="slide" data-rel="groups" href="#groups" class="ui-btn-active">groups</a></li>
				</ul>
			</div>
		</div>
		<div data-role="content" class="margin">groups</div>
	</div>
	<div data-role="page" id="dialog">
		
	</div>
	
	<script type="text/javascript">
		var getLocation=function(position){
			console.log(position.coords)
//			var url=['http://maps.google.com/maps?q=',position.coords.latitude,',',position.coords.longitude].join('');
			$.ajax({
				url:'http://maps.googleapis.com/maps/api/geocode/json',
				type:'GET',
				data:{
					latlng:[position.coords.latitude,position.coords.longitude].join(','),
					sensor:true
				},
				dataType:'jsonp',
				success: function(data){
	//				console.log(data[data.length-2].formatted_address)
				}
			})
		}
		function handleLocationError(error) {
		      switch (error.code) {
		        case 0:
		        updateStatus("There was an error while retrieving your location: " +
		                      error.message);
		break;
		        case 1:
		        updateStatus("The user prevented this page from retrieving a location.");
		        break;
		        case 2:
		        updateStatus("The browser was unable to determine your location: " +
		                      error.message);
		break;
		        case 3:
		        updateStatus("The browser timed out before retrieving the location.");
		        break;
		} }
		navigator.geolocation.getCurrentPosition(getLocation);
	</script>
	
</body>
</html>