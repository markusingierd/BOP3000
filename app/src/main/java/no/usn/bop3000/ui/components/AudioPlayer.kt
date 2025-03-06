package no.usn.bop3000.ui.components

import android.media.MediaPlayer
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun PlayAudioAutomatically(audioResId: Int) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, audioResId) }

    DisposableEffect(Unit) {
        mediaPlayer.start()

        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}

