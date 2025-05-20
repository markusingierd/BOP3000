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
fun VideoPlayer(videoResId: Int? = null, videoUri: Uri? = null) {
    val context = LocalContext.current
    val videoView = remember { VideoView(context) }

    DisposableEffect(videoResId, videoUri) {
        val source: Uri? = when {
            videoResId != null -> Uri.parse("android.resource://${context.packageName}/$videoResId")
            videoUri != null -> videoUri
            else -> null
        }

        source?.let {
            videoView.setVideoURI(it)
            videoView.setOnPreparedListener { player ->
                player.isLooping = true
                videoView.start()
            }
        }

        onDispose {
            videoView.stopPlayback()
        }
    }

    if (videoResId != null || videoUri != null) {
        AndroidView(
            factory = { videoView },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
        ) {
        }
    }
}