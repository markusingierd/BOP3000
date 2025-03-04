package no.usn.bop3000.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkDefaultButton,
    background = DarkDefaultBackground,
    surface = DarkNavbarBackground,
    onPrimary = DarkProfileUserBackground,
    onBackground = DarkProfileUserBackground,
    onSurface = DarkProfileUserStatisticsBackground,
    error = DarkErrorColor
)

private val LightColorScheme = lightColorScheme(
    primary = DefaultButton,
    background = DefaultBackground,
    surface = NavbarBackground,
    onPrimary = DefaultBackground,
    onBackground = ProfileUserBackground,
    onSurface = ProfileUserStatisticsBackground,
    error = ErrorColor
)

@Composable
fun BopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (!darkTheme) {
            LightColorScheme
        } else {
            DarkColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}