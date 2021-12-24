package io.getstream.chat.android.compose.livestream.sample.ui.channels

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun CenterContentStreamerInfo(
    streamerAvatarImage: String?,
    streamerName: String?,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement
    ) {
        Image(
            modifier = Modifier
                .padding(4.dp)
                .background(shape = CircleShape, color = Color.Transparent)
                .height(20.dp)
                .aspectRatio(1f),
            painter = rememberImagePainter(data = streamerAvatarImage), contentDescription = null
        )

        if (streamerName != null)
            Text(
                modifier = Modifier.wrapContentSize(),
                text = streamerName,
                style = ChatTheme.typography.bodyBold, fontSize = 12.sp
            )
    }
}
