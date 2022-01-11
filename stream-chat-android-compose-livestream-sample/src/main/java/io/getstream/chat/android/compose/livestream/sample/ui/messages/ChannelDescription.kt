package io.getstream.chat.android.compose.livestream.sample.ui.messages

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.compose.livestream.sample.R
import io.getstream.chat.android.compose.livestream.sample.extensions.description
import io.getstream.chat.android.compose.livestream.sample.extensions.streamerAvatarLink
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun ChannelDescription(
    channel: Channel,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    onChannelDescriptionClicked: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        Image(
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
                .background(color = ChatTheme.colors.textLowEmphasis, shape = CircleShape),
            painter = rememberImagePainter(
                data = channel.streamerAvatarLink,
                builder = { transformations(CircleCropTransformation()) }
            ),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .weight(0.4f)
                .verticalScroll(ScrollState(0))
        ) {
            Text(
                text = channel.name,
                style = ChatTheme.typography.bodyBold,
                color = ChatTheme.colors.textLowEmphasis
            )
            Text(
                text = channel.description ?: "",
                style = ChatTheme.typography.body,
                color = ChatTheme.colors.textLowEmphasis
            )
        }

        Button(
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp),
            onClick = onChannelDescriptionClicked
        ) {
            Icon(
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                painter = painterResource(id = R.drawable.ic_heart), contentDescription = null,
                tint = Color.White
            )
            Text(
                text = LocalContext.current.getString(R.string.follow),
                style = ChatTheme.typography.bodyBold,
                color = Color.White
            )
        }
    }
}
