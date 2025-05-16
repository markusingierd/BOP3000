package no.usn.bop3000.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

data class InfoPoint(
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val imageUri: Uri?,
    val videoUri: Uri?,
    val audioUri: Uri?
)

object PointRepository {
    val points = mutableStateListOf<InfoPoint>()
}

@Composable
fun AddPointScreen() {
    val context = LocalContext.current
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var useManualCoordinates by remember { mutableStateOf(false) }
    var manualLat by remember { mutableStateOf("") }
    var manualLon by remember { mutableStateOf("") }
    var coordinates by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var description by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var audioUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }
    val videoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        videoUri = it
    }
    val audioPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        audioUri = it
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (!useManualCoordinates &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            locationClient.lastLocation.addOnSuccessListener {
                it?.let { loc -> coordinates = Pair(loc.latitude, loc.longitude) }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)) {
            Text("Legg til nytt punkt", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = useManualCoordinates,
                    onCheckedChange = {
                        useManualCoordinates = it
                        coordinates = null
                    }
                )
                Text("Skriv inn koordinater manuelt")
            }

            if (useManualCoordinates) {
                OutlinedTextField(
                    value = manualLat,
                    onValueChange = { manualLat = it },
                    label = { Text("Breddegrad (lat)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = manualLon,
                    onValueChange = { manualLon = it },
                    label = { Text("Lengdegrad (lon)") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                coordinates?.let {
                    Text("Koordinater: ${it.first}, ${it.second}")
                } ?: Text("Henter posisjon...")
            }

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Beskrivelse") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))
            Button(onClick = { imagePicker.launch("image/*") }) {
                Text("Velg bilde")
            }
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            Spacer(Modifier.height(8.dp))
            Button(onClick = { videoPicker.launch("video/*") }) {
                Text("Velg video")
            }
            videoUri?.let {
                Text("Video valgt: ${it.lastPathSegment}")
            }

            Spacer(Modifier.height(8.dp))
            Button(onClick = { audioPicker.launch("audio/*") }) {
                Text("Velg lyd")
            }
            audioUri?.let {
                Text("Lyd valgt: ${it.lastPathSegment}")
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                val lat = if (useManualCoordinates) manualLat.toDoubleOrNull() else coordinates?.first
                val lon = if (useManualCoordinates) manualLon.toDoubleOrNull() else coordinates?.second

                if (lat != null && lon != null) {
                    val point = InfoPoint(lat, lon, description, imageUri, videoUri, audioUri)
                    PointRepository.points.add(point)

                    description = ""
                    manualLat = ""
                    manualLon = ""
                    imageUri = null
                    videoUri = null
                    audioUri = null

                    scope.launch {
                        snackbarHostState.showSnackbar("Punkt lagret!")
                    }
                }
            }) {
                Text("Lagre punkt")
            }
        }
    }
}