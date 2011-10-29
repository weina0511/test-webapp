<!DOCTYPE HTML>
<html lang="en">
	<head>
		<title>common</title>
	</head>
	<body>
		<div>
			<a href="tel:18601286311">tel</a>
			<p id="status">HTML5 Geolocation is <strong>not</strong> supported in your browser.</p>
			<h2>Current Position:</h2>
			<table border="1">
			<tr>
			<th width="40" scope="col">Latitude</th>
			<td width="114" id="latitude">?</td>
			</tr>
			<tr>
			<td> Longitude</td>
			<td id="longitude">?</td>
			</tr>
			<tr>
			<td>Accuracy</td>
			<td id="accuracy">?</td>
			</tr>
			<tr>
			<td>Last Timestamp</td>
			<td id="timestamp">?</td>
			</tr>
			</table>
			<h4 id="currDist">Current distance traveled: 0.0 km</h4>
			<h4 id="totalDist">Total distance traveled: 0.0 km</h4>
		</div>
		<script type="text/javascript">
		    var totalDistance = 0.0;
		    var lastLat;
		    var lastLong;
		    function toRadians(degree) {
		      return degree * Math.PI / 180;
		}
		    function distance(latitude1, longitude1, latitude2, longitude2) {
		      // R is the radius of the earth in kilometers
		      var R = 6371;
		  var deltaLatitude = toRadians(latitude2-latitude1);
		  var deltaLongitude = toRadians(longitude2-longitude1);
		  latitude1 = toRadians(latitude1);
		  latitude2 = toRadians(latitude2);
		  var a = Math.sin(deltaLatitude/2) *
		  Math.sin(deltaLatitude/2) +
		  Math.cos(latitude1) *
		  Math.cos(latitude2) *
		  Math.sin(deltaLongitude/2) *
		  Math.sin(deltaLongitude/2);
		  var c = 2 * Math.atan2(Math.sqrt(a),
		  Math.sqrt(1-a));
		  var d = R * c;
		  return d;
		}
		function updateStatus(message) {
		    document.getElementById("status").innerHTML = message;
		}
		function loadDemo() {
		    if(navigator.geolocation) {
		        updateStatus("HTML5 Geolocation is supported in your browser.");
		        navigator.geolocation.watchPosition(updateLocation,
		                                            handleLocationError,
		                                            {maximumAge:20000});
		} }
		window.addEventListener("load", loadDemo, true);
		function updateLocation(position) {
		    var latitude = position.coords.latitude;
		    var longitude = position.coords.longitude;
		    var accuracy = position.coords.accuracy;
		    var timestamp = position.timestamp;
		    document.getElementById("latitude").innerHTML = latitude;
		    document.getElementById("longitude").innerHTML = longitude;
		    document.getElementById("accuracy").innerHTML = accuracy;
		    document.getElementById("timestamp").innerHTML = timestamp;
			var map=document.createElement('iframe');
			document.body.appendChild(map)
			map.height=map.width='100';
			map.src='http://ditu.google.cn/maps?q='+latitude+','+longitude;
		    // sanity test... don't calculate distance if accuracy
		    // value too large
		    if (accuracy >= 500) {
		        updateStatus("Need more accurate values to calculate distance.");
				return; 
			}
		        // calculate distance
		        if ((lastLat !== null) && (lastLong !== null)) {
		            var currentDistance = distance(latitude, longitude, lastLat, lastLong);
		            document.getElementById("currDist").innerHTML =
		              "Current distance traveled: " +
		               currentDistance.toFixed(4) + " km";
		            totalDistance += currentDistance;
		            document.getElementById("totalDist").innerHTML =
		              "Total distance traveled: " + totalDistance.toFixed(4) + " km";
		}
		        lastLat = latitude;
		        lastLong = longitude;
		        updateStatus("Location successfully updated.");
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
		</script>
		<#list feedlist as feed>
		---------
		userName：${feed.userName};
		senderName：${feed.senderName};
		message:${feed.message};
		senderLink:${feed.senderLink};
		update_time:${feed.updated_time};
		
		</#list>
		
	</body>
</html>