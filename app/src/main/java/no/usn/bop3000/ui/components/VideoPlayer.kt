package no.usn.bop3000.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoPlayer(videoResId: Int) {
    val context = LocalContext.current
    val videoView = remember { VideoView(context) }

    DisposableEffect(Unit) {
        val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener {
            it.isLooping = true
            videoView.start()
        }

        onDispose {
            videoView.stopPlayback()
        }
    }

    AndroidView(
        factory = { videoView },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
