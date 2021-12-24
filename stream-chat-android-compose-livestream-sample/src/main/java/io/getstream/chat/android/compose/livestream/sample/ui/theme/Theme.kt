package io.getstream.chat.android.compose.livestream.sample.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.getstream.sdk.chat.utils.DateFormatter
import io.getstream.chat.android.compose.ui.attachments.AttachmentFactory
import io.getstream.chat.android.compose.ui.attachments.StreamAttachmentFactories
import io.getstream.chat.android.compose.ui.filepreview.AttachmentPreviewHandler
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamColors
import io.getstream.chat.android.compose.ui.theme.StreamDimens
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.ui.theme.StreamTypography
import io.getstream.chat.android.compose.ui.util.ChannelNameFormatter
import io.getstream.chat.android.compose.ui.util.DefaultReactionTypes
import io.getstream.chat.android.compose.ui.util.MessageAlignmentProvider
import io.getstream.chat.android.compose.ui.util.MessagePreviewFormatter

/***
 * You can use our ChatTheme only for the parts of your app that are using
 * Stream's Compose Chat SDK, or you can leverage our ChatTheme and apply
 * it to your whole app for a quick setup, uniformity and great ease of use
 * */
@Composable
fun LiveStreamAppTheme(
    isInDarkMode: Boolean = isSystemInDarkTheme(),
    colors: StreamColors = if (isInDarkMode) StreamColors.defaultDarkColors() else StreamColors.defaultColors(),
    dimens: StreamDimens = StreamDimens.defaultDimens(),
    typography: StreamTypography = StreamTypography.defaultTypography(),
    shapes: StreamShapes = Shapes,
    attachmentFactories: List<AttachmentFactory> = StreamAttachmentFactories.defaultFactories(),
    attachmentPreviewHandlers: List<AttachmentPreviewHandler> = AttachmentPreviewHandler.defaultAttachmentHandlers(
        LocalContext.current
    ),
    reactionTypes: Map<String, Int> = DefaultReactionTypes.defaultReactionTypes(),
    dateFormatter: DateFormatter = DateFormatter.from(LocalContext.current),
    channelNameFormatter: ChannelNameFormatter = ChannelNameFormatter.defaultFormatter(LocalContext.current),
    messagePreviewFormatter: MessagePreviewFormatter = MessagePreviewFormatter.defaultFormatter(
        context = LocalContext.current,
        typography = typography,
        attachmentFactories = attachmentFactories
    ),
    messageAlignmentProvider: MessageAlignmentProvider = MessageAlignmentProvider.defaultMessageAlignmentProvider(),
    content: @Composable () -> Unit,
) {
    ChatTheme(
        isInDarkMode = isInDarkMode,
        colors = colors,
        dimens = dimens,
        typography = typography,
        shapes = shapes,
        attachmentFactories = attachmentFactories,
        attachmentPreviewHandlers = attachmentPreviewHandlers,
        reactionTypes = reactionTypes,
        dateFormatter = dateFormatter,
        channelNameFormatter = channelNameFormatter,
        messagePreviewFormatter = messagePreviewFormatter,
        messageAlignmentProvider = messageAlignmentProvider,
        content = content
    )
}
