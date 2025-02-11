package no.usn.bop3000.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip

@Composable
fun TrailScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        MapboxMap(
            modifier = Modifier
                .width(300.dp)
                .height(400.dp)
                .clip(RoundedCornerShape(16.dp)),
            mapViewportState = rememberMapViewportState {
                setCameraOptions {
                    zoom(5.0)
                    center(Point.fromLngLat(-98.0, 39.5))
                }
            }
        )
    }
}

