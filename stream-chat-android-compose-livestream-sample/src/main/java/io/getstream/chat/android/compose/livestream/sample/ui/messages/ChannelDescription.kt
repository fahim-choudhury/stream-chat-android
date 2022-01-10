package io.getstream.chat.android.compose.livestream.sample.ui.messages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.livestream.sample.R
import io.getstream.chat.android.compose.ui.components.avatar.ChannelAvatar
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun ChannelDescription(
    channel: Channel,
    currentUser: User,
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
        ChannelAvatar(
            modifier = Modifier
                .size(24.dp),
            channel = channel,
            currentUser = currentUser
        )

        Column {
            Text(
                text = channel.name,
                style = ChatTheme.typography.bodyBold,
                color = ChatTheme.colors.textLowEmphasis
            )
            Text(
                text = "Channel Description goes here",
                style = ChatTheme.typography.body,
                color = ChatTheme.colors.textLowEmphasis
            )
        }

        Button(onClick = onChannelDescriptionClicked) {
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
