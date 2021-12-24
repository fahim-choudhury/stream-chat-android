package io.getstream.chat.android.compose.livestream.sample.ui.messages

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import io.getstream.chat.android.client.models.Attachment
import io.getstream.chat.android.common.state.ValidationError
import io.getstream.chat.android.compose.R
import io.getstream.chat.android.compose.ui.components.composer.CoolDownIndicator
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun SendButton(
    value: String,
    coolDownTime: Int,
    attachments: List<Attachment>,
    validationErrors: List<ValidationError>,
    onSendMessage: (String, List<Attachment>) -> Unit,
) {
    val isInputValid = (value.isNotEmpty() || attachments.isNotEmpty()) && validationErrors.isEmpty()

    if (coolDownTime > 0) {
        CoolDownIndicator(coolDownTime = coolDownTime)
    } else {
        IconButton(
            enabled = isInputValid,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.stream_compose_ic_send),
                    contentDescription = stringResource(id = R.string.stream_compose_send_message),
                    tint = if (isInputValid) ChatTheme.colors.primaryAccent else ChatTheme.colors.textLowEmphasis
                )
            },
            onClick = {
                if (isInputValid) {
                    onSendMessage(value, attachments)
                }
            }
        )
    }
}
