package io.getstream.chat.android.compose.livestream.sample.ui.messages

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import io.getstream.chat.android.compose.livestream.sample.extensions.badges
import io.getstream.chat.android.compose.livestream.sample.extensions.color
import io.getstream.chat.android.compose.state.messages.list.MessageItemState

@Composable
fun MessageItem(
    messageItemState: MessageItemState,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        messageItemState.currentUser?.badges?.forEach { badgeDrawableRes ->
            key(badgeDrawableRes) {
                Icon(painter = painterResource(id = badgeDrawableRes), contentDescription = null)
            }
        }
        Text(
            color = messageItemState.message.user.color ?: Color.Gray,
            text = "${messageItemState.message.user.name}: "
        )
        Text(text = messageItemState.message.text)
    }
}
