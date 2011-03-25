<%@ page isELIgnored="false" trimDirectiveWhitespaces="true"
	contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<script type="text/javascript"
	src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="assets/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript">
	var map;
	var count=0;
	var point;
	var points;
	
	function drawPath(points) {
		var path = new google.maps.Polyline({
			path : points,
			strokeColor : "#FF0000",
			strokeOpacity : 1.0,
			strokeWeight : 2
		});
		path.setMap(map);
	}
	
	function drawPaths() {
		<c:forEach var="pathId" items="${pathIds}">
			<c:set var="points" value="${paths[pathId]}" scope="request" />
			count = 0;
			points = new Array();
			<c:forEach var="point" items="${points}">
				point = new google.maps.LatLng(${point.latitude},${point.longitude});
				new google.maps.Marker({
					position : point,
					map : map,
					title : "${pathId} - Count [" + count + "]"
				});
				points[count]=point;
				count++;
			</c:forEach>
			drawPath(points);
		</c:forEach>
	}

	$(document).ready(
			function() {
				var mapOptions = {
					zoom : 4,
					center : new google.maps.LatLng(38.822591,-97.558594)
				};

				map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
				map.setMapTypeId(google.maps.MapTypeId.ROADMAP);
				
				drawPaths();
			});
</script>
</head>
<body>
<div id="map_canvas" style="width: 100%; height: 400px;"></div>
<c:forEach var="pathId" items="${pathIds}">
	<h1>${pathId}</h1>
	<c:set var="points" value="${paths[pathId]}" scope="request" />
	<ul>
		<c:forEach var="point" items="${points}">
			<li>${point.latitude}, ${point.longitude}</li>
		</c:forEach>
	</ul>
	<hr/>
</c:forEach>
</body>
</html>