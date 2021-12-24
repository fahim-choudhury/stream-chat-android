package io.getstream.chat.android.compose.livestream.sample.ui.channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChannelItemCenterContent(
    streamerAvatarImage: String?,
    streamerName: String?,
    modifier: Modifier = Modifier,
    channelDescription: String? = null,
    channelCategory: String? = null,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement
    ) {
        CenterContentStreamerInfo(
            modifier = Modifier.wrapContentSize(),
            streamerAvatarImage = streamerAvatarImage,
            streamerName = streamerName
        )

        if (channelDescription != null)
            Text(modifier = Modifier.padding(horizontal = 2.dp), text = channelDescription)

        if (channelCategory != null)
            Text(modifier = Modifier.padding(horizontal = 2.dp), text = channelCategory)
    }
}
