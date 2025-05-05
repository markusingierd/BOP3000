package no.usn.bop3000.ui.components

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.font.FontWeight

@Composable
fun VideoPlayer(videoResId: Int) {
    val context = LocalContext.current
    val videoView = remember { VideoView(context) }
    val mediaController = remember { MediaController(context) }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")
        videoView.setVideoURI(videoUri)
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)

        videoView.setOnPreparedListener {
            videoView.start()
            isPlaying = true
        }

        onDispose {
            videoView.stopPlayback()
        }
    }

    fun togglePlayback() {
        if (isPlaying) {
            videoView.pause()
        } else {
            videoView.start()
        }
        isPlaying = !isPlaying
    }

    fun refreshPlayback() {
        videoView.seekTo(0)
        if (!isPlaying) {
            videoView.start()
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
            )
        ) {
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
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
        ) {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh")
        }
    }

    AndroidView(
        factory = { localContext ->
            videoView.apply {
                setVideoURI(Uri.parse("android.resource://${localContext.packageName}/$videoResId"))
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}