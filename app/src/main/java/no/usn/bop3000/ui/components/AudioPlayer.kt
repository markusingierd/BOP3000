package no.usn.bop3000.ui.components

import android.media.MediaPlayer
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp

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
        Button(onClick = { togglePlayback() }) {
            Text(text = if (isPlaying) "Pause" else "Start")
        }
        Button(onClick = { refreshPlayback() }) {
            Text("Refresh")
        }
    }
}
