package no.usn.bop3000.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.plugin.locationcomponent.location
import no.usn.bop3000.ui.components.LocationViewModel

@Composable
fun TrailScreen(viewModel: LocationViewModel = viewModel()) {
    val locationState by viewModel.userLocation.collectAsState()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startLocationUpdates()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                viewModel.getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.startLocationUpdates()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val trailPoints = listOf(
        Point.fromLngLat(9.063836, 59.411445), // Bø-parken
        Point.fromLngLat(5.3243, 60.3921),  // Bergen
        Point.fromLngLat(10.3951, 63.4305)  // Trondheim
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.Center)
        ) {
            AndroidView(factory = { context ->
                MapView(context).apply {
                    mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->

                        // Aktiver brukerens posisjon
                        val locationPlugin = location
                        locationPlugin.updateSettings {
                            enabled = true
                            pulsingEnabled = true
                        }

                        // Lag en GeoJSON-kilde med punktene
                        val featureList = trailPoints.map { point ->
                            Feature.fromGeometry(point)
                        }
                        val source = geoJsonSource("trail-source") {
                            featureCollection(FeatureCollection.fromFeatures(featureList))
                        }
                        style.addSource(source)

                        // Legg til et sirkel-lag for å vise punktene
                        style.addLayer(
                            circleLayer("trail-layer", "trail-source") {
                                circleColor("#FF8C00") // Rød prikk
                                circleRadius(6.0) // Størrelse på prikken
                                circleOpacity(0.8)
                            }
                        )
                    }
                }
            }, update = { mapView ->
                locationState?.let { location ->
                    mapView.mapboxMap.setCamera(
                        CameraOptions.Builder()
                            .center(Point.fromLngLat(location.longitude, location.latitude))
                            .zoom(15.0)
                            .build()
                    )
                }
            })
        }
    }
}
