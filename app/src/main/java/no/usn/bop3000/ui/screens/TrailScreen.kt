package no.usn.bop3000.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailScreen(navController: NavController, viewModel: LocationViewModel = viewModel()) {
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

    // Liste med trail-punkter og tilhørende informasjon
    val trailPointsInfo = listOf(
        PointInfo(
            point = Point.fromLngLat(9.063836, 59.411445),
            title = stringResource(id = R.string.point_1_title),
            description = stringResource(id = R.string.point_1_description),
            imageResId = R.drawable.gullbringimg
        ),
        PointInfo(
            point = Point.fromLngLat(9.064038, 59.412280),
            title = stringResource(id = R.string.point_2_title),
            description = stringResource(id = R.string.point_2_description),
            imageResId = R.drawable.gullbringimg
        ),
        PointInfo(
            point = Point.fromLngLat(9.0611, 59.4122),
            title = stringResource(id = R.string.point_3_title),
            description = stringResource(id = R.string.point_3_description),
            imageResId = R.drawable.gullbringimg
        )
    )

    var currentPointInfo by remember { mutableStateOf<PointInfo?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.headliner),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("home")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        val mapHeight = if (currentPointInfo == null) {
            1500.dp
        } else {
            500.dp
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mapHeight)
                    .padding(16.dp)
            ) {
                AndroidView(factory = { context ->
                    MapView(context).apply {
                        mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->
                            val locationPlugin = location
                            locationPlugin.updateSettings {
                                enabled = true
                                pulsingEnabled = true
                            }

                            val featureList = trailPointsInfo.map { pointInfo ->
                                Feature.fromGeometry(pointInfo.point)
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

                        // Finn nærmeste punkt
                        val nearestPoint = trailPointsInfo.find { pointInfo ->
                            isUserNearPoint(userLocation, pointInfo.point)
                        }

                        // Oppdater informasjon om punktet
                        currentPointInfo = nearestPoint
                    }
                })
            }

            currentPointInfo?.let { pointInfo ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
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
                            painter = rememberAsyncImagePainter(model = pointInfo.imageResId),
                            contentDescription = "Image at point",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(top = 8.dp)
                        )
                        pointInfo.videoUrl?.let {
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
}

data class PointInfo(
    val point: Point,
    val title: String,
    val description: String,
    val imageResId: Int,
    val videoUrl: String? = null
)
