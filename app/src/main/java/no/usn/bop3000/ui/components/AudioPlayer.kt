package no.usn.bop3000.ui.components

import android.media.MediaPlayer
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButtonDefaults

@Composable
fun AudioPlayer(audioResId: Int) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, audioResId) }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        mediaPlayer.start()
        isPlaying = true
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    fun togglePlayback() {
        if (isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
        isPlaying = !isPlaying
    }

    fun refreshPlayback() {
        mediaPlayer.seekTo(0)
        if (!isPlaying) {
            mediaPlayer.start()
            isPlaying = true
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { togglePlayback() },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )        ) {
            if (isPlaying) {
                Text(
                    "II",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            } else {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Play")
            }
        }
        IconButton(
            onClick = { refreshPlayback() },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface // Sets color for the icon
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Refresh"
            )
        }
    }
}
