package no.usn.bop3000.ui.components

import com.mapbox.geojson.Point
import android.location.Location

fun isUserNearPoint(userLocation: Location, point: Point): Boolean {
    // Beregn avstand mellom brukerens posisjon og punktet
    val distance = calculateDistance(userLocation.latitude, userLocation.longitude, point.latitude(), point.longitude())
    return distance <= 10 // Sjekk om avstanden er mindre enn eller lik 10 meter
}

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val location1 = Location("provider")
    location1.latitude = lat1
    location1.longitude = lon1

    val location2 = Location("provider")
    location2.latitude = lat2
    location2.longitude = lon2

    return location1.distanceTo(location2).toDouble() // Konverterer Float til Double og returnerer avstand i meter
}
