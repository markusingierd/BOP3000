package no.usn.bop3000.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import coil.compose.rememberAsyncImagePainter
import no.usn.bop3000.ui.components.isUserNearPoint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import no.usn.bop3000.R

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
        Point.fromLngLat(9.063836, 59.411445),
        Point.fromLngLat(9.064038, 59.412280),
        Point.fromLngLat(9.0611, 59.4122)
    )

    val trailPointsInfo = listOf(
        PointInfo(
            point = Point.fromLngLat(9.063836, 59.411445),
            title = "Punkt 1",
            description = "Dette er beskrivelsen for punkt 1.",
            imageResId = R.drawable.gullbringimg
        ),
        PointInfo(
            point = Point.fromLngLat(9.064038, 59.412280),
            title = "Punkt 2",
            description = "Dette er beskrivelsen for punkt 2.",
            imageResId = R.drawable.gullbringimg
        ),
        PointInfo(
            point = Point.fromLngLat(9.0611, 59.4122),
            title = "Punkt 3",
            description = "Dette er beskrivelsen for punkt 3.",
            imageResId = R.drawable.gullbringimg
        )
    )

    var currentPointInfo by remember { mutableStateOf<PointInfo?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.Center)
        ) {
            AndroidView(factory = { context ->
                MapView(context).apply {
                    mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->
                        val locationPlugin = location
                        locationPlugin.updateSettings {
                            enabled = true
                            pulsingEnabled = true
                        }

                        val featureList = trailPoints.map { point ->
                            Feature.fromGeometry(point)
                        }
                        val source = geoJsonSource("trail-source") {
                            featureCollection(FeatureCollection.fromFeatures(featureList))
                        }
                        style.addSource(source)

                        style.addLayer(
                            circleLayer("trail-layer", "trail-source") {
                                circleColor("#FF8C00")
                                circleRadius(6.0)
                                circleOpacity(0.8)
                            }
                        )
                    }
                }
            }, update = { mapView ->
                locationState?.let { userLocation ->
                    mapView.mapboxMap.setCamera(
                        CameraOptions.Builder()
                            .center(Point.fromLngLat(userLocation.longitude, userLocation.latitude))
                            .zoom(15.0)
                            .build()
                    )

                    trailPointsInfo.forEach { pointInfo ->
                        if (isUserNearPoint(userLocation, pointInfo.point)) {
                            currentPointInfo = pointInfo
                        }
                    }
                }
            })
        }

        currentPointInfo?.let { pointInfo ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color.White.copy(alpha = 0.8f),
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = pointInfo.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = pointInfo.description,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = pointInfo.imageResId),
                        contentDescription = "Image at point",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(top = 8.dp)
                    )
                    pointInfo.videoUrl?.let {
                        // Display video button if video URL is available
                        Button(
                            onClick = { /* Open video */ },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Se Video")
                        }
                    }
                }
            }
        }
    }
}

data class PointInfo(
    val point: Point,
    val title: String,
    val description: String,
    val imageResId: Int,
    val videoUrl: String? = null
)
