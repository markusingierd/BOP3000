package no.usn.bop3000.ui.components

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AudioPlayer(audioResId: Int? = null, audioUri: Uri? = null) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var isPrepared by remember { mutableStateOf(false) }

    DisposableEffect(audioResId, audioUri) {
        try {
            mediaPlayer = when {
                audioResId != null -> MediaPlayer.create(context, audioResId)
                audioUri != null -> MediaPlayer().apply {
                    setDataSource(context, audioUri)
                    prepare()
                }
                else -> null
            }

            mediaPlayer?.setOnPreparedListener {
                isPrepared = true
                it.start()
                isPlaying = true
            }

            if (mediaPlayer?.isPlaying == false && isPrepared) {
                mediaPlayer?.start()
                isPlaying = true
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Feil ved oppretting av MediaPlayer", e)
        }

        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    fun togglePlayback() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlaying = false
            } else {
                it.start()
                isPlaying = true
            }
        }
    }

    fun refreshPlayback() {
        mediaPlayer?.let {
            it.seekTo(0)
            if (!it.isPlaying) {
                it.start()
                isPlaying = true
            }
        }
    }

    if (mediaPlayer != null) {
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
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play"
                    )
                }
            }
            IconButton(
                onClick = { refreshPlayback() },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh"
                )
            }
        }
    } else {
        Text("Ingen lyd tilgjengelig", color = MaterialTheme.colorScheme.onSurface)
    }
}