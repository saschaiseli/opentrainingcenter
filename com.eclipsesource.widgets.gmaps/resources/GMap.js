///////////////////////////////
// API - to be called from java

window.init = function( center, zoom, typeId, points ) {
  var options = {
    disableDefaultUI : false,
    zoom : zoom,
    center : new google.maps.LatLng( center[ 0 ], center[ 1 ] ),
    mapTypeId : typeId
  };
  var parent = document.getElementById( "map_canvas" );
  window.gmap = new google.maps.Map( parent, options );
  
  var theRoute = [];
  for(i=0;i<points.length;i++){
	  theRoute[i]=new google.maps.LatLng(points[i][0], points[i][1]);
  }

  routeAsPoligon = new google.maps.Polygon({
	    paths: theRoute,
	    strokeColor: "#FF0000",
	    strokeOpacity: 0.5,
	    strokeWeight: 5,
	    fillColor: "#FF0000",
	    fillOpacity: 0.0
	  });

  routeAsPoligon.setMap(gmap);
  _registerEventListener();
};

window.gotoAddress = function( address ) {
  geocoder.geocode( { "address" : address }, window._handleAddressResolved );
};

window.resolveAddress = function() {
  var req = { "location" : gmap.getCenter() };
  geocoder.geocode( req, window._handleLocationResolved );
};

window.setCenter = function( center ) {  
  window._blockEvents = true;
  gmap.panTo( new google.maps.LatLng( center[ 0 ], center[ 1 ] ) );
  window._blockEvents = false;
}

window.setZoom = function( zoom ) {
  window._blockEvents = true;
  gmap.setZoom( zoom );
  window._blockEvents = false;
}


window.setType = function( type ) {
  gmap.setMapTypeId( type );
}

window.addMarker = function( name ) {
  var marker = new google.maps.Marker( {
    position : gmap.getCenter(),
    title : name,
    draggable : true
  } );
  marker.setMap( gmap );
  var infowindow = new google.maps.InfoWindow( {
      content : name,
      disableAutoPan : true
  } );
  google.maps.event.addListener( marker, "click", function() {
    infowindow.open( gmap, marker );
  } );
}

////////////
// Internals

// Note: Using "_" to mark as non-public API.

window._blockEvents = false;

window._registerEventListener = function(){
  //The actual "center_changed" event can't be easily used because it can create
  //a lot of events (resulting in requests) while dragging. 
  google.maps.event.addListener( gmap, "dragend", function() {
    _handleBoundsChanged();
  } );
  google.maps.event.addListener( gmap, "zoom_changed", function() {
    _handleBoundsChanged();
  } );
};

window._handleAddressResolved = function( results, status ) {
  // NOTE: This function is called asynchronously (i.e. not from within java)
  if( status == google.maps.GeocoderStatus.OK && results[ 0 ] ) {
    var newBounds = results[ 0 ].geometry.viewport;
    gmap.fitBounds( newBounds );
  }
};

window._handleLocationResolved = function( results, status ) {
  if( status == google.maps.GeocoderStatus.OK && results[ 0 ] ) {
    onAddressResolved( results[ 0 ].formatted_address );
  }
};

window._handleBoundsChanged = function() {
  // The script that gets executed from java might create change-events that 
  // call a browser-function. That would be a bad idea (unnecessary traffic, 
  // risk of recursion and buggy in SWT), therefore the "blockEvents" flag:
  if( !_blockEvents ) {
    // BrowserFunction:
    onBoundsChanged( gmap.getCenter().lat(), 
                     gmap.getCenter().lng(), 
                     gmap.getZoom() );
  }
};
