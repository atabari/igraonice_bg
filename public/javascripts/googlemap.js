/**
 * Created by User on 12/29/2015.
 */
var marker;

function initMap() {
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 13,
        center: {lat: 43.850572, lng: 18.393421}

    });

    marker = new google.maps.Marker({
        map: map,
        draggable: true,
        animation: google.maps.Animation.DROP,
        position: {lat: 43.850572, lng: 18.393421}
    });
    marker.addListener('click', toggleBounce);
}

function toggleBounce() {
    if (marker.getAnimation() !== null) {
        marker.setAnimation(null);
    } else {
        marker.setAnimation(google.maps.Animation.BOUNCE);
    }
}

var geocoder = new google.maps.Geocoder();

function geocodePosition(pos) {
    geocoder.geocode({
        latLng: pos
    }, function(responses) {
        if (responses && responses.length > 0) {
            updateMarkerAddress(responses[0].formatted_address);
        } else {
            updateMarkerAddress('Cannot determine address at this location.');
        }
    });
}

function updateMarkerStatus(str) {
    document.getElementById('markerStatus').innerHTML = str;
}

function updateMarkerPosition(latLng) {
    document.getElementById('info').innerHTML = [
        latLng.lat(),
        latLng.lng()
    ].join(', ');
    document.getElementById('lat').value = latLng.lat();
    document.getElementById('lng').value = latLng.lng();
}

function updateMarkerAddress(str) {
    document.getElementById('address').innerHTML = str;
}



function initialize() {
    var latLng;
    var x = document.getElementById('lat').value;
    var y = document.getElementById('lng').value;

    latLng = new google.maps.LatLng(x, y);

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 12,
        center: latLng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });

    var marker = new google.maps.Marker({
        position: latLng,
        title: 'Click here to zoom',
        map: map,
        draggable: true
    });

    google.maps.event.addListener(marker,'click',function() {
        map.setZoom(16);
        map.setCenter(marker.getPosition());
        infowindow.open(map, marker);
    });

    //when marker has completed the drag event
    //recenter the circle on the marker.
    google.maps.event.addListener(marker, 'dragend', function() {
        myCircle.setCenter(this.position);
    });

    // Update current position info.
    updateMarkerPosition(latLng);
    geocodePosition(latLng);

    // Add dragging event listeners.
    google.maps.event.addListener(marker, 'dragstart', function() {
        updateMarkerAddress('Dragging...');
    });

    google.maps.event.addListener(marker, 'drag', function() {
        updateMarkerStatus('Dragging...');
        updateMarkerPosition(marker.getPosition());
    });

    google.maps.event.addListener(marker, 'dragend', function() {
        updateMarkerStatus('Drag ended');
        geocodePosition(marker.getPosition());
    });
}
google.maps.event.addDomListener(window, 'load', initialize());