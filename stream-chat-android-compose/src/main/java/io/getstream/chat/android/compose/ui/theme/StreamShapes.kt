package io.getstream.chat.android.compose.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Contains all the shapes we provide for our components.
 *
 * @param avatar The avatar shape.
 * @param myMessageBubble The bubble that wraps my message content.
 * @param otherMessageBubble The bubble that wraps other people's message content.
 * @param inputField The shape of the input field.
 * @param attachment The shape of attachments.
 * @param imageThumbnail The shape of image thumbnails, shown in selected attachments and image file attachments.
 * @param bottomSheet The shape of components used as bottom sheets.
 * @param suggestionList The shape of suggestion list popup.
 * @param attachmentSiteLabel The shape of the label showing website name over link attachments.
 * @param header The shape of the headers, such as the ones appearing on the Channel or Message screens.
 */
@Immutable
public data class StreamShapes(
    public val avatar: Shape,
    public val myMessageBubble: Shape,
    public val otherMessageBubble: Shape,
    public val inputField: Shape,
    public val attachment: Shape,
    public val imageThumbnail: Shape,
    public val bottomSheet: Shape,
    public val suggestionList: Shape,
    public val attachmentSiteLabel: Shape,
    public val header: Shape
) {
    public companion object {
        /**
         * Builds the default shapes for our theme.
         *
         * @return A [StreamShapes] that holds our default shapes.
         */
        public fun defaultShapes(): StreamShapes = StreamShapes(
            avatar = CircleShape,
            myMessageBubble = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp),
            otherMessageBubble = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp),
            inputField = RoundedCornerShape(24.dp),
            attachment = RoundedCornerShape(16.dp),
            imageThumbnail = RoundedCornerShape(8.dp),
            bottomSheet = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            suggestionList = RoundedCornerShape(16.dp),
            attachmentSiteLabel = RoundedCornerShape(topEnd = 14.dp),
            header = RectangleShape
        )
    }
}
