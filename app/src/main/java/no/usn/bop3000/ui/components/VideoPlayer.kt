package no.usn.bop3000.ui.components

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoPlayer(videoResId: Int) {
    AndroidView(
        factory = { context ->
            VideoView(context).apply {
                val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")
                setVideoURI(videoUri)

                val mediaController = MediaController(context)
                mediaController.setAnchorView(this)
                setMediaController(mediaController)

                start()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
