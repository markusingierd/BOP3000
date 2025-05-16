// TrailScreen.kt (med støtte for egne punkter med bilde, lyd, video)

package no.usn.bop3000.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
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
import kotlinx.coroutines.launch
import no.usn.bop3000.R
import no.usn.bop3000.ui.components.AudioPlayer
import no.usn.bop3000.ui.components.LocationViewModel
import no.usn.bop3000.ui.components.VideoPlayer
import no.usn.bop3000.ui.components.isUserNearPoint
import androidx.navigation.NavController
import android.net.Uri
import java.io.File

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

    val staticPoints = listOf(
        PointInfo(
            point = Point.fromLngLat(9.061171, 59.410259),
            title = stringResource(id = R.string.point_1_title),
            description = stringResource(id = R.string.point_1_description),
            videoResId = R.raw.gullbringkulturhus,
            audioResId = R.raw.gullbring_kulturhus_aud
        ),
        PointInfo(
            point = Point.fromLngLat(9.059962, 59.409273),
            title = stringResource(id = R.string.point_2_title),
            description = stringResource(id = R.string.point_2_description),
            imageResId = R.drawable.campusbo,
            audioResId = R.raw.usn_campus
        ),
        PointInfo(
            point = Point.fromLngLat(9.061380, 59.411515),
            title = stringResource(id = R.string.point_3_title),
            description = stringResource(id = R.string.point_3_description),
            sliderResId = listOf(R.drawable.bo_hotell_1, R.drawable.bo_hotell_2,R.drawable.bo_hotell_3,R.drawable.bo_hotell_4),
            audioResId = R.raw.bo_hotel
        ),
        PointInfo(
            point = Point.fromLngLat(9.0611, 59.4122),
            title = stringResource(id = R.string.point_4_title),
            description = stringResource(id = R.string.point_4_description),
            sliderResId = listOf(R.drawable.gullbringimg, R.drawable.gullbringto),
            audioResId = R.raw.gullbring
        )
    )

    val dynamicPoints = PointRepository.points.map {
        PointInfo(
            point = Point.fromLngLat(it.longitude, it.latitude),
            title = "Eget punkt",
            description = it.description,
            imageUri = it.imageUri,
            audioUri = it.audioUri,
            videoUri = it.videoUri
        )
    }

    val allPoints = staticPoints + dynamicPoints
    var currentPointInfo by remember { mutableStateOf<PointInfo?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Kulturminner", style = MaterialTheme.typography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("home")
                    }) {
                        Icon(Icons.Filled.Home, contentDescription = "Home", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("pincode?redirectTo=addpoint")
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = "Legg til punkt", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        val mapHeight = if (currentPointInfo == null) 1500.dp else 400.dp

        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Box(
                modifier = Modifier.fillMaxWidth().height(mapHeight).padding(16.dp)
            ) {
                AndroidView(factory = { context ->
                    MapView(context).apply {
                        mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->
                            location.updateSettings {
                                enabled = true
                                pulsingEnabled = true
                            }
                            val featureList = allPoints.map { Feature.fromGeometry(it.point) }
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
                                .zoom(15.3)
                                .build()
                        )
                        currentPointInfo = allPoints.find {
                            isUserNearPoint(userLocation, it.point)
                        }
                    }
                })
            }

            currentPointInfo?.let { pointInfo ->
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp).background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.medium
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Text(pointInfo.title, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            pointInfo.audioResId?.let {
                                AudioPlayer(audioResId = it)
                            } ?: pointInfo.audioUri?.let {
                                AudioPlayer(audioUri = it)
                            }

                            pointInfo.imageResId?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(model = it),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth().height(200.dp)
                                )
                            } ?: pointInfo.imageUri?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth().height(200.dp)
                                )
                            }

                            pointInfo.sliderResId.takeIf { it.isNotEmpty() }?.let {
                                ImageSliderInfo(it)
                            }

                            pointInfo.videoResId?.let {
                                VideoPlayer(videoResId = it)
                            } ?: pointInfo.videoUri?.let {
                                VideoPlayer(videoUri = it)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(pointInfo.description, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageSliderInfo(sliderResId: List<Int>) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        HorizontalPager(count = sliderResId.size, state = pagerState, modifier = Modifier.matchParentSize()) { page ->
            Image(
                painter = painterResource(id = sliderResId[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Left",
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp).size(48.dp)
                .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.55f), shape = CircleShape)
                .clickable {
                    coroutineScope.launch {
                        if (pagerState.currentPage > 0) pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }.padding(12.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        Icon(
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = "Right",
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp).size(48.dp)
                .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.55f), shape = CircleShape)
                .clickable {
                    coroutineScope.launch {
                        if (pagerState.currentPage < sliderResId.size - 1) pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }.padding(12.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

data class PointInfo(
    val point: Point,
    val title: String,
    val description: String,
    val imageResId: Int? = null,
    val sliderResId: List<Int> = emptyList(),
    val audioResId: Int? = null,
    val videoResId: Int? = null,
    val imageUri: Uri? = null,
    val audioUri: Uri? = null,
    val videoUri: Uri? = null
)
