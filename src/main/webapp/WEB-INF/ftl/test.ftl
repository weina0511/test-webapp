<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<meta charset="UTF-8">
	<title>app</title>
	<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<script type="text/javascript" src="./jquery-1.6.4.min.js"></script>
	<script type="text/javascript" src="./jquery.mobile-1.0rc2.min.js"></script>

	
	<link rel="stylesheet" type="text/css" href="./jquery.mobile.structure-1.0rc2.min.css" media="all">
	<style type="text/css">
		#hd li{display:inline-block;width:33%;}
		
		.box{}
		.box .media{float:left;}
		.box .img{display:block;}
		.box .content{display:table-cell;}
		.box .content:after{content:'X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X ';height:0;overflow:hidden;clear:both;}
		
	</style>
</head>
<body>
	<a data-transition="slide" data-rel="dialog" data-role="button" href="#dialog" data-theme="c" class="ui-btn ui-btn-corner-all ui-shadow ui-btn-up-c"><span aria-hidden="true" class="ui-btn-inner ui-btn-corner-all"><span class="ui-btn-text">slide</span></span></a>
	<div id="doc">
		<div id="hd">
			<ul><li><a href="#" data-transition="slide">Status</a></li><li>Contacts</li><li>Groups</li></ul>	
		</div>
		<div id="bd">
			<div id="status">
			<ul>
				<#list feedlist as feed>
				<li>
					<p class="username" link="${feed.senderLink}">${feed.senderName}</p>
					<p class="message">${feed.message}</p>
					<p class="date">${feed.updated_time}</p>
				</li>
				</#list>
			</ul>
				<form action="#">
					<textarea id=""></textarea>
					<p>post a status</p>
					<p><input type="submit" value="POST" class="submit"></p>
				</form>
			</div>
			
			<!-- user's contacts -->
			<div id="contacts"></div>
			
			<!-- user's groups -->
			<div id="groups"></div>
			
			
			<!-- user's card -->
			<div class="card"></div>
		</div>
	</div>
	<script type="text/javascript">
		var box='<div class="box"><a href="{0}" class="media"><img src="{1}" alt=""></a><div class="content"><p class="username"><a href="{0}">{2}</a></p><p class="headline">{3}</p></div>';
		var card='<div class="box"><a href="{0}" class="media"><img src="{1}" alt=""></a><div class="content"><p class="username"><a href="{0}">{2}</a></p><p class="headline">{3}</p></div>';
		var MessageFormat = function(str){
		    var args = [].slice.call(arguments,1);
		    return str.replace(/\{([^}])\}/g,function(index,value){
		        return args[value];
		    })
		};
		$('#hd span').bind('click',function(){
			var name=this.innerHTML;
			if($('#'+name).html()==''){
				$.ajax({
					url:'/getid',
					dataType:'json',
					success: function(data){
						MessageFormat(box,data.url,data.smallPortrait,data.userName,data.headline)
					}
				})
			}
		})
		/*
		var getLocation=function(position){
			var url=['http://maps.google.com/maps?q=',position.coords.latitude,',',position.coords.longitude].join('');
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
		*/
	</script>
</body>
</html>