<!DOCTYPE HTML>
<html>
<head>
	<meta charset="UTF-8">
	<title>app</title>
	<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0rc2/jquery.mobile-1.0rc2.min.css" />
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.4.min.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.0rc2/jquery.mobile-1.0rc2.min.js"></script>				
	<!-- 
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
	
 -->
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
				</ul>
			</div>
		</div>
		<div data-role="content" class="margin">
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
				</ul>
			</div>
		</div>
		<div data-role="content" class="margin">
			<#include 'contacts.ftl'/>
			<p>a contact</p>
			<a href="#dialog" data-rel="dialog" data-transition="pop">Open dialog</a>
		</div>
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
	};
	function handleLocationError(error) {
		switch (error.code) {
		case 0:
			updateStatus("There was an error while retrieving your location: " +error.message);
			break;
		case 1:
			updateStatus("The user prevented this page from retrieving a location.");
			break;
		case 2:
			updateStatus("The browser was unable to determine your location: " +error.message);
			break;
		case 3:
			updateStatus("The browser timed out before retrieving the location.");
			break;
		}
	}
//	navigator.geolocation.getCurrentPosition(getLocation);
	$(function(){
		$.ajax({
			url:'/contacts',
			data:{
				location:'北京'
			},
			success: function(data){
				$('#contacts div[data-role=content]').html(data);
			}
		})
	})
	
	
	
	
	
	
	
	
	var categoryData = {
		animals: {
			name: "Animals",
			description: "All your favorites from aardvarks to zebras.",
			items: [
				{
					name: "Pets",
				},
				{
					name: "Farm Animals",
				},
				{
					name: "Wild Animals",
				}
			]
		},
		colors: {
			name: "Colors",
			description: "Fresh colors from the magic rainbow.",
			items: [
				{
					name: "Blue",
				},
				{
					name: "Green",
				},
				{
					name: "Orange",
				},
				{
					name: "Purple",
				},
				{
					name: "Red",
				},
				{
					name: "Yellow",
				},
				{
					name: "Violet",
				}
			]
		},
		vehicles: {
			name: "Vehicles",
			description: "Everything from cars to planes.",
			items: [
				{
					name: "Cars",
				},
				{
					name: "Planes",
				},
				{
					name: "Construction",
				}
			]
		}
	};
	$(document).bind( "pagebeforechange", function( e, data ) {

		// We only want to handle changePage() calls where the caller is
		// asking us to load a page by URL.
		if ( typeof data.toPage === "string" ) {

			// We are being asked to load a page by URL, but we only
			// want to handle URLs that request the data for a specific
			// category.
			var u = $.mobile.path.parseUrl( data.toPage ),
				re = /^#category-item/;

			if ( u.hash.search(re) !== -1 ) {

				// We're being asked to display the items for a specific category.
				// Call our internal method that builds the content for the category
				// on the fly based on our in-memory category data structure.
				showCategory( u, data.options );

				// Make sure to tell changePage() we've handled this call so it doesn't
				// have to do anything.
				e.preventDefault();
			}
		}
	});
	function showCategory( urlObj, options )
	{
		var categoryName = urlObj.hash.replace( /.*category=/, "" ),

			// Get the object that represents the category we
			// are interested in. Note, that at this point we could
			// instead fire off an ajax request to fetch the data, but
			// for the purposes of this sample, it's already in memory.
			category = categoryData[ categoryName ],

			// The pages we use to display our content are already in
			// the DOM. The id of the page we are going to write our
			// content into is specified in the hash before the '?'.
			pageSelector = urlObj.hash.replace( /\?.*$/, "" );

		if ( category ) {
			// Get the page we are going to dump our content into.
			var $page = $( pageSelector ),

				// Get the header for the page.
				$header = $page.children( ":jqmData(role=header)" ),

				// Get the content area element for the page.
				$content = $page.children( ":jqmData(role=content)" ),

				// The markup we are going to inject into the content
				// area of the page.
				markup = "<p>" + category.description + "</p><ul data-role='listview' data-inset='true'>",

				// The array of items for this category.
				cItems = category.items,

				// The number of items in the category.
				numItems = cItems.length;

			// Generate a list item for each item in the category
			// and add it to our markup.
			for ( var i = 0; i < numItems; i++ ) {
				markup += "<li>" + cItems[i].name + "</li>";
			}
			markup += "</ul>";

			// Find the h1 element in our header and inject the name of
			// the category into it.
			$header.find( "h1" ).html( category.name );

			// Inject the category items markup into the content element.
			$content.html( markup );

			// Pages are lazily enhanced. We call page() on the page
			// element to make sure it is always enhanced before we
			// attempt to enhance the listview markup we just injected.
			// Subsequent calls to page() are ignored since a page/widget
			// can only be enhanced once.
			$page.page();

			// Enhance the listview we just injected.
			$content.find( ":jqmData(role=listview)" ).listview();

			// We don't want the data-url of the page we just modified
			// to be the url that shows up in the browser's location field,
			// so set the dataUrl option to the URL for the category
			// we just loaded.
			options.dataUrl = urlObj.href;

			// Now call changePage() and tell it to switch to
			// the page we just modified.
			$.mobile.changePage( $page, options );
		}
	}
	</script>

</body>
</html>