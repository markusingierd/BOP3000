package no.usn.bop3000.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkButton,
    background = DarkBackground,
    surface = DarkInfoBox,
    onPrimary = DarkOnButton,
    onBackground = DarkText,
    onSurface = DarkTextOnInfo,
    error = DarkErrorColor
)

private val LightColorScheme = lightColorScheme(
    primary = Button,
    background = Background,
    surface = InfoBox,
    onPrimary = OnButton,
    onBackground = Text,
    onSurface = TextOnInfo,
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