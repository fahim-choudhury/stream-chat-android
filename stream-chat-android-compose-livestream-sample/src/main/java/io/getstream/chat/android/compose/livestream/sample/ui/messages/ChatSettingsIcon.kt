package io.getstream.chat.android.compose.livestream.sample.ui.messages

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import io.getstream.chat.android.compose.livestream.sample.R

@Composable
fun ChatSettingsIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_dots),
            contentDescription = LocalContext.current.getString(R.string.accessibility_open_chat_settings)
        )
    }
}
