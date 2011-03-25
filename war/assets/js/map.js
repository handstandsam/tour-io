var map = null;
var marker = null;
var layer = null;
var points = [];
var count = 0;
var seconds = 10;
var path = null;
var ONESECOND = 1000;
var watchid = null;
var startTime = new Date().getTime();
var recordingId = null;
var stopRecordingButton;
var startRecordingButton;
var tourname;

function secondsPast() {
	var nowTime = new Date().getTime();
	return (nowTime - startTime) / 1000;
}

function dolog(dastr) {
	var newone = $(dastr);
	//$('ol').append(newone);
}

function updateMap(lat, lon) {

	var point = new google.maps.LatLng(lat, lon);
	points[count] = point;
	count++;

	path = new google.maps.Polyline({
		path : points,
		strokeColor : "#FF0000",
		strokeOpacity : 1.0,
		strokeWeight : 2
	});

	path.setMap(map);

	var marker = new google.maps.Marker({
		position : point,
		map : map,
		title : "Count [" + count + "]"
	});

	map.setCenter(point);
	map.setZoom(18);
}

function locSuccessWatch(pos) {
	var lat = pos.coords.latitude;
	var lon = pos.coords.longitude;
	var timestamp = new Date(pos.timestamp).getTime();
	dolog('<li><p>[' + secondsPast() + ' Seconds] - ' + lat + ", " + lon
			+ "</p></li>");
	updateMap(lat, lon);

	var newPoint = {
		recordingId : recordingId,
		point : {
			latitude : lat,
			longitude : lon,
			time : timestamp
		}
	};

	var pointJson = JSON.stringify(newPoint);
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/record-point",
		data : pointJson,
		error : function(res) {
			//alert("Error");
		},
		success : function(res) {
			// Point Recorded...
		}
	});
}

function startWatching() {
	var tournameInput = $("input[name=tourname]");
	var categorySelect = $("select[name=category]");
	tourname = tournameInput.val();
	var category = categorySelect.val();
	if (tourname == null || tourname.length == 0) {
		showWarning("Please Enter a Tour Name.");
	} else if (category == null || category.length == 0) {
		showWarning("Please Enter a Category.");
	} else {
		tournameInput.val("");
		drawCreateMap();
		$("#start").hide();
		var stopDiv = $("#stop");
		stopDiv.show();
		stopDiv.find("h1 span.tourname").text(tourname);

		var data = {
			tourname : tourname,
			category : category
		};
		$.ajax({
			type : "POST",
			contentType : "application/json",
			data : JSON.stringify(data),
			url : "/start-recording",
			error : function(res) {
				//alert("Error");
			},
			success : function(res) {
				recordingId = res.id;
				try {
					watchid = navigator.geolocation.watchPosition(
							locSuccessWatch, handleLocError, {
								enableHighAccuracy : true
							});
					console.log("Started: " + watchid);
				} catch (ex) {
					handleLocError(ex);
				}
			}
		});
	}
}

function stopWatching() {
	$('#create-map-canvas').hide();
	$("#stop").hide();
	$("#start").show();
	console.log("Stopping: " + watchid);
	showWarning(tourname + " Tour was Saved!");
	navigator.geolocation.clearWatch(watchid);
}

function handleLocError(error) {
	/*if (error.PERMISSION_DENIED) {
		alert("User denied access!");
	} else if (error.POSITION_UNAVAILABLE) {
		alert("You must be hiding in Area 51!");
	} else if (error.TIMEOUT) {
		alert("hmmm we timed out trying to find where you are hiding!");
	} else {
		alert('another error occurred.');
	}*/
}

function drawCreateMap() {
	$('#create-map-canvas').show();
	var tourMapStyles = [ {
		featureType : "road.local",
		elementType : "geometry",
		stylers : [ {
			hue : "#00ff00"
		}, {
			saturation : 100
		} ]
	}, {
		featureType : "landscape",
		elementType : "geometry",
		stylers : [ {
			lightness : -100
		} ]
	} ];

	var sanfran = new google.maps.LatLng(37.784215,-122.400784);

	var mapOptions = {
		zoom : 10,
		center : sanfran,
		mapTypeControlOptions : {
			mapTypeIds : [ /* google.maps.MapTypeId.ROADMAP, */'tours' ]
		}
	};

	map = new google.maps.Map(document.getElementById("create-map-canvas"),
			mapOptions);

	var styledMapOptions = {
		name : "Tours"
	};

	var tourMapType = new google.maps.StyledMapType(tourMapStyles,
			styledMapOptions);
	map.mapTypes.set('tours', tourMapType);
	map.setMapTypeId('tours');
}

function findTours() {
	var data = {
		category : ""
	};
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/get-tours",
		data : data,
		error : function(res) {
			//alert("Error");
		},
		success : function(res) {
			showTours(res);
		}
	});
}

function showTours(tourMap) {
	var tourUl = $("#tour-list");
	tourUl.children().each().remove();
	for ( var key in tourMap) {
		var tourList = tourMap[key];
		var newCategory = $("<li><div class='category-heading'>" + key + " ("
				+ tourList.length + ")</div></li>");
		var sublist = $("<ul class='sublist'></ul>");
		for ( var i = 0; i < tourList.length; i++) {
			var subtourElem = $("<li><a class='tour-link' href='#/tours/"
					+ tourList[i].id + "'>" + tourList[i].name + "</a></li>");
			sublist.append(subtourElem);
		}
		newCategory.append(sublist);
		tourUl.append(newCategory);
	}

	$("a.tour-link").click(function() {
		var prefix = "#/tours/";
		var href = $(this).attr('href');
		if (href.indexOf(prefix) != -1) {
			var tourId = href.substring(prefix.length, href.length);
			showTour(tourId);
		}
		return false;
	});
}

function showTour(tourId) {
	linkClicked("#tour");
	var tourDiv = $("#tour");
	tourDiv.children().remove();
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/tour?id=" + tourId,
		error : function(res) {
			//alert("Error");
		},
		success : function(tour) {
			var elem = $("<br/><h1>" + tour.name
					+ " ("+tour.points.length+" GPS Points)</h1><br/><div id='tour-map' class='map' style='width:100%;height:200px;'></div>");
			tourDiv.append(elem);
			showMapOfTour(tour);
		}
	});
}

function showMapOfTour(tour) {
	if(tour.points==null){
		showMessage("No Points in Tour.");
	}else {
		var tourMapStyles = [ {
			featureType : "road.local",
			elementType : "geometry",
			stylers : [ {
				hue : "#00ff00"
			}, {
				saturation : 100
			} ]
		}, {
			featureType : "landscape",
			elementType : "geometry",
			stylers : [ {
				lightness : -100
			} ]
		} ];
		
		var firstPoint = tour.points[0];
		var tourMapOptions = {
			zoom : 18,
			center : new google.maps.LatLng(firstPoint.latitude, firstPoint.longitude),
			mapTypeControlOptions : {
				mapTypeIds : [ /* google.maps.MapTypeId.ROADMAP, */'tours' ]
			}
		};
	
		var tourMap = new google.maps.Map(document.getElementById("tour-map"),
				tourMapOptions);
	
		var styledTourMapOptions = {
			name : tour.name
		};
	
		var tourMapType = new google.maps.StyledMapType(tourMapStyles,
				styledTourMapOptions);
		tourMap.mapTypes.set('tours', tourMapType);
		tourMap.setMapTypeId('tours');
		
		var googlePoints = [];
		for ( var i = 0; i < tour.points.length; i++) {
			var currPoint = tour.points[i];
			var googlePoint = new google.maps.LatLng(currPoint.latitude, currPoint.longitude);
			googlePoints[i] = googlePoint;
	
			var newMarker = new google.maps.Marker({
				position : googlePoint,
				map : tourMap,
				title: "A Point"
			});
		}
		
		//Draw path
		var tourPath = new google.maps.Polyline({
			path : googlePoints,
			strokeColor : "#FF0000",
			strokeOpacity : 1.0,
			strokeWeight : 2
		});
		tourPath.setMap(tourMap);

	}
}

function linkClicked(href) {
	var divId = href.substring(1, href.length);

	$('.nav a').each(function(index) {
		var currHref = $(this).attr('href');
		$(currHref).hide();
	});
	$("#" + divId).show();
	if (href == "#findtours") {
		findTours();
	}

}