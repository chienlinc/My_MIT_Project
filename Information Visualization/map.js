mapboxgl.accessToken = 'pk.eyJ1IjoiamVubnljaGVuMDIxNCIsImEiOiJja3QxY3J4NmQwY2VzMnRueGptMDd2ZHEzIn0.o4eMvK6U6AnfLR6SbNgBLQ';
	var map = new mapboxgl.Map({
  container: 'map',
  style: 'mapbox://styles/jennychen0214/ckt1vi7421hs917mnmg7vu0ra',
	center: [-120.78, 37.09],
  zoom: 7
	});

	map.addControl(
	new MapboxGeocoder({
	accessToken: mapboxgl.accessToken,
	mapboxgl: mapboxgl
	})
	);

  map.addControl(new mapboxgl.NavigationControl());
