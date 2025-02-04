package io.getstream.chat.android.compose.ui.messages.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.common.state.MessageMode
import io.getstream.chat.android.compose.R
import io.getstream.chat.android.compose.previewdata.PreviewChannelData
import io.getstream.chat.android.compose.previewdata.PreviewUserData
import io.getstream.chat.android.compose.ui.components.BackButton
import io.getstream.chat.android.compose.ui.components.NetworkLoadingIndicator
import io.getstream.chat.android.compose.ui.components.TypingIndicator
import io.getstream.chat.android.compose.ui.components.avatar.ChannelAvatar
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.util.getMembersStatusText
import io.getstream.chat.android.offline.model.ConnectionState

/**
 * A clean, decoupled UI element that doesn't rely on ViewModels or our custom architecture setup.
 * This allows the user to fully govern how the [MessageListHeader] behaves, by passing in all the
 * data that's required to display it and drive its actions, as well as customize the slot APIs.
 *
 * @param channel Channel info to display.
 * @param currentUser The current user, required for different UI states.
 * @param modifier Modifier for styling.
 * @param typingUsers The list of typing users.
 * @param messageMode The current message mode, that changes the header content, if we're in a Thread.
 * @param connectionState The state of WS connection used to switch between the subtitle and the network loading view.
 * @param color The color of the header.
 * @param shape The shape of the header.
 * @param elevation The elevation of the header.
 * @param onBackPressed Handler that propagates the back button click event.
 * @param onHeaderActionClick Action handler when the user taps on the header action.
 * @param leadingContent The content shown at the start of the header, by default a [BackButton].
 * @param centerContent The content shown in the middle of the header and represents the core information, by default
 * [DefaultMessageListHeaderCenterContent].
 * @param trailingContent The content shown at the end of the header, by default a [ChannelAvatar].
 */
@Composable
public fun MessageListHeader(
    channel: Channel,
    currentUser: User?,
    modifier: Modifier = Modifier,
    typingUsers: List<User> = emptyList(),
    messageMode: MessageMode = MessageMode.Normal,
    connectionState: ConnectionState = ConnectionState.CONNECTED,
    color: Color = ChatTheme.colors.barsBackground,
    shape: Shape = ChatTheme.shapes.header,
    elevation: Dp = ChatTheme.dimens.headerElevation,
    onBackPressed: () -> Unit = {},
    onHeaderActionClick: (Channel) -> Unit = {},
    leadingContent: @Composable RowScope.() -> Unit = {
        DefaultMessageListHeaderLeadingContent(onBackPressed = onBackPressed)
    },
    centerContent: @Composable RowScope.() -> Unit = {
        DefaultMessageListHeaderCenterContent(
            modifier = Modifier.weight(1f),
            channel = channel,
            currentUser = currentUser,
            typingUsers = typingUsers,
            messageMode = messageMode,
            onHeaderActionClick = onHeaderActionClick,
            connectionState = connectionState
        )
    },
    trailingContent: @Composable RowScope.() -> Unit = {
        DefaultMessageListHeaderTrailingContent(
            channel = channel,
            currentUser = currentUser
        )
    },
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        elevation = elevation,
        color = color,
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            leadingContent()

            centerContent()

            trailingContent()
        }
    }
}

/**
 * Represents the leading content of [MessageListHeader]. By default shows a back button.
 *
 * @param onBackPressed Handler that propagates the back button click event.
 */
@Composable
internal fun DefaultMessageListHeaderLeadingContent(onBackPressed: () -> Unit) {
    BackButton(
        modifier = Modifier
            .size(36.dp),
        imageVector = Icons.Default.ArrowBack,
        onBackPressed = onBackPressed,
    )
}

/**
 * Represents the center content of [MessageListHeader]. By default shows header title, that handles
 * if we should show a loading view for network, or the channel information.
 *
 * @param channel The channel used for the title information.
 * @param modifier Modifier for styling.
 * @param typingUsers The list of typing users.
 * @param messageMode Currently active message mode, used to define the title information.
 * @param onHeaderActionClick Handler for when the user taps on the header content.
 * @param connectionState A flag that governs if we show the subtitle or the network loading view.
 */
@Composable
public fun DefaultMessageListHeaderCenterContent(
    channel: Channel,
    currentUser: User?,
    modifier: Modifier = Modifier,
    typingUsers: List<User> = emptyList(),
    messageMode: MessageMode = MessageMode.Normal,
    onHeaderActionClick: (Channel) -> Unit = {},
    connectionState: ConnectionState = ConnectionState.CONNECTED,
) {
    val title = when (messageMode) {
        MessageMode.Normal -> ChatTheme.channelNameFormatter.formatChannelName(channel, currentUser)
        is MessageMode.MessageThread -> stringResource(id = R.string.stream_compose_thread_title)
    }

    val subtitle = when (messageMode) {
        MessageMode.Normal -> channel.getMembersStatusText(LocalContext.current, currentUser)
        is MessageMode.MessageThread -> stringResource(
            R.string.stream_compose_thread_subtitle,
            ChatTheme.channelNameFormatter.formatChannelName(channel, currentUser)
        )
    }

    Column(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onHeaderActionClick(channel) }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = ChatTheme.typography.title3Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = ChatTheme.colors.textHighEmphasis,
        )

        val subtitleTextColor = ChatTheme.colors.textLowEmphasis
        val subtitleTextStyle = ChatTheme.typography.footnote

        if (connectionState == ConnectionState.CONNECTED) {
            if (typingUsers.isEmpty()) {
                Text(
                    text = subtitle,
                    color = subtitleTextColor,
                    style = subtitleTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            } else {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val typingUsersText = LocalContext.current.resources.getQuantityString(
                        R.plurals.stream_compose_message_list_header_typing_users,
                        typingUsers.size,
                        typingUsers.first().name,
                        typingUsers.size - 1
                    )

                    TypingIndicator()

                    Text(
                        text = typingUsersText,
                        color = subtitleTextColor,
                        style = subtitleTextStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        } else {
            NetworkLoadingIndicator(
                modifier = Modifier.wrapContentHeight(),
                spinnerSize = 12.dp,
                textColor = subtitleTextColor,
                textStyle = subtitleTextStyle
            )
        }
    }
}

/**
 * Represents the trailing content of [MessageListHeader]. By default shows the channel avatar.
 *
 * @param channel The channel used to display the avatar.
 * @param currentUser The current user. Used for choosing which avatar to display.
 * */
@Composable
internal fun DefaultMessageListHeaderTrailingContent(channel: Channel, currentUser: User?) {
    ChannelAvatar(
        modifier = Modifier.size(40.dp),
        channel = channel,
        currentUser = currentUser,
        contentDescription = channel.name,
    )
}

@Preview(name = "MessageListHeader Preview (Connected)")
@Composable
private fun MessageListHeaderConnectedPreview() {
    ChatTheme {
        MessageListHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            channel = PreviewChannelData.channelWithImage,
            currentUser = PreviewUserData.user1
        )
    }
}

@Preview(name = "MessageListHeader Preview (Connecting)")
@Composable
private fun MessageListHeaderConnectingPreview() {
    ChatTheme {
        MessageListHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            channel = PreviewChannelData.channelWithImage,
            currentUser = PreviewUserData.user1,
            connectionState = ConnectionState.CONNECTING
        )
    }
}

@Preview(name = "MessageListHeader Preview (Offline)")
@Composable
private fun MessageListHeaderOfflinePreview() {
    ChatTheme {
        MessageListHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            channel = PreviewChannelData.channelWithImage,
            currentUser = PreviewUserData.user1,
            connectionState = ConnectionState.OFFLINE
        )
    }
}

@Preview(name = "MessageListHeader Preview (User Typing)")
@Composable
private fun MessageListHeaderUserTypingPreview() {
    ChatTheme {
        MessageListHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            channel = PreviewChannelData.channelWithImage,
            currentUser = PreviewUserData.user1,
            typingUsers = listOf(PreviewUserData.user2)
        )
    }
}

@Preview(name = "MessageListHeader Preview (Many Members)")
@Composable
private fun MessageListHeaderManyMembersPreview() {
    ChatTheme {
        MessageListHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            channel = PreviewChannelData.channelWithManyMembers,
            currentUser = PreviewUserData.user1,
        )
    }
}
