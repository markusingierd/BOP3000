package no.usn.bop3000.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
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

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .height(500.dp)
                .width(350.dp)
                .align(Alignment.Center)
        ) {
            AndroidView(factory = { context ->
                MapView(context).apply {
                    mapboxMap.loadStyle(Style.MAPBOX_STREETS) {
                        val locationPlugin = location
                        locationPlugin.updateSettings {
                            enabled = true
                            pulsingEnabled = true
                        }

                        locationState?.let { location ->
                            mapboxMap.setCamera(
                                CameraOptions.Builder()
                                    .center(Point.fromLngLat(location.longitude, location.latitude))
                                    .zoom(12.0)
                                    .build()
                            )
                        }
                    }
                }
            })
        }
    }
}