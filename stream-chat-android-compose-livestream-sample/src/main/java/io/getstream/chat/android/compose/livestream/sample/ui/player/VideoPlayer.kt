package io.getstream.chat.android.compose.livestream.sample.ui.player

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.devbrackets.android.exomedia.listener.OnErrorListener
import com.devbrackets.android.exomedia.listener.OnPreparedListener
import com.devbrackets.android.exomedia.ui.widget.VideoView
import com.devbrackets.android.exomedia.ui.widget.controls.VideoControlsMobile

@Composable
fun VideoPlayer(videoUrl: String, onError: () -> Unit) {
    val context = LocalContext.current

    val videoView = remember {
        VideoView(context).apply {
            isPlaying
            videoControls = VideoControlsMobile(context)
            setOnPreparedListener(
                object : OnPreparedListener {
                    override fun onPrepared() {
                        start()
                    }
                }
            )
            setOnErrorListener(
                object : OnErrorListener {
                    override fun onError(e: Exception?): Boolean {
                        onError()
                        return true
                    }
                }
            )
            setVideoURI(Uri.parse(videoUrl))
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        factory = { videoView }
    )
}
