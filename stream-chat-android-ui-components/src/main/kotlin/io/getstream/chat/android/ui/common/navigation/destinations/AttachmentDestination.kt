package io.getstream.chat.android.ui.common.navigation.destinations

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import com.getstream.sdk.chat.images.load
import com.getstream.sdk.chat.model.ModelType
import com.getstream.sdk.chat.navigation.destinations.ChatDestination
import com.getstream.sdk.chat.view.activity.AttachmentDocumentActivity
import com.stfalcon.imageviewer.StfalconImageViewer
import io.getstream.chat.android.client.logger.ChatLogger
import io.getstream.chat.android.client.models.Attachment
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.ui.common.R
import io.getstream.chat.android.ui.gallery.AttachmentActivity
import io.getstream.chat.android.ui.gallery.AttachmentMediaActivity

public open class AttachmentDestination(
    public var message: Message,
    public var attachment: Attachment,
    context: Context,
) : ChatDestination(context) {

    private val logger = ChatLogger.get("AttachmentDestination")

    override fun navigate() {
        showAttachment(message, attachment)
    }

    public fun showAttachment(message: Message, attachment: Attachment) {
        if (attachment.type == ModelType.attach_file ||
            attachment.type == ModelType.attach_video ||
            attachment.type == ModelType.attach_audio ||
            attachment.mimeType?.contains(VIDEO_MIME_TYPE_PREFIX) == true
        ) {
            loadFile(attachment)
            return
        }

        var url: String? = null
        var type: String? = attachment.type

        when (attachment.type) {
            ModelType.attach_image -> {
                when {
                    attachment.titleLink != null || attachment.ogUrl != null || attachment.assetUrl != null -> {
                        url = attachment.titleLink ?: attachment.ogUrl ?: attachment.assetUrl
                        type = ModelType.attach_link
                    }
                    attachment.isGif() -> {
                        url = attachment.imageUrl
                        type = ModelType.attach_giphy
                    }
                    else -> {
                        showImageViewer(message, attachment)
                        return
                    }
                }
            }
            ModelType.attach_giphy -> url = attachment.thumbUrl
            ModelType.attach_product -> url = attachment.url
        }

        if (url.isNullOrEmpty()) {
            logger.logE("Wrong URL for attachment. Attachment: $attachment")
            Toast.makeText(
                context,
                context.getString(R.string.stream_ui_message_list_attachment_invalid_url),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val intent = Intent(context, AttachmentActivity::class.java).apply {
            putExtra("type", type)
            putExtra("url", url)
        }
        start(intent)
    }

    private fun loadFile(attachment: Attachment) {
        val mimeType = attachment.mimeType
        val url = attachment.assetUrl
        val type = attachment.type
        val title = attachment.title ?: attachment.name ?: ""

        if (mimeType == null && type == null) {
            ChatLogger.instance.logE("AttachmentDestination", "MimeType is null for url $url")
            Toast.makeText(
                context,
                context.getString(R.string.stream_ui_message_list_attachment_invalid_mime_type, attachment.name),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Media
        when {
            playableContent(mimeType, type) -> {
                val intent = Intent(context, AttachmentMediaActivity::class.java).apply {
                    putExtra(AttachmentMediaActivity.TYPE_KEY, mimeType)
                    putExtra(AttachmentMediaActivity.URL_KEY, url)
                    putExtra(AttachmentMediaActivity.HEADER_TITLE_KEY, title)
                }
                start(intent)
            }
            docMimeType(mimeType) -> {
                val intent = Intent(context, AttachmentDocumentActivity::class.java).apply {
                    putExtra("url", url)
                }
                start(intent)
            }

            else -> {
                ChatLogger.instance.logE(
                    "AttachmentDestination", "Could not load attachment. Mimetype: $mimeType. Type: $type")
                Toast.makeText(
                    context,
                    context.getString(R.string.stream_ui_message_list_attachment_invalid_mime_type, attachment.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun playableContent(mimeType: String?, type: String?): Boolean {
        return mimeType?.contains("audio") == true ||
            mimeType?.contains(VIDEO_MIME_TYPE_PREFIX) == true ||
            mimeType?.contains(MP4_MIME_TYPE) == true ||
            mimeType?.contains(MPEG_MIME_TYPE) == true ||
            mimeType?.contains(QUICKTIME_MIME_TYPE) == true ||
            type == AUDIO_TYPE ||
            type == VIDEO_TYPE
    }

    private fun docMimeType(mimeType: String?): Boolean {
        return mimeType == ModelType.attach_mime_doc ||
            mimeType == ModelType.attach_mime_txt ||
            mimeType == ModelType.attach_mime_pdf ||
            mimeType == ModelType.attach_mime_html ||
            mimeType?.contains("application/vnd") == true
    }

    protected open fun showImageViewer(
        message: Message,
        attachment: Attachment,
    ) {
        val imageUrls: List<String> = message.attachments
            .filter { it.type == ModelType.attach_image && !it.imageUrl.isNullOrEmpty() }
            .mapNotNull(Attachment::imageUrl)

        if (imageUrls.isEmpty()) {
            Toast.makeText(context, "Invalid image(s)!", Toast.LENGTH_SHORT).show()
            return
        }

        val attachmentIndex = message.attachments.indexOf(attachment)

        StfalconImageViewer
            .Builder(context, imageUrls, ImageView::load)
            .withStartPosition(
                if (attachmentIndex in imageUrls.indices) attachmentIndex else 0
            )
            .show()
    }

    private fun Attachment.isGif() = mimeType?.contains("gif") ?: false

    private companion object {
        private const val VIDEO_MIME_TYPE_PREFIX = "video"
        private const val MP4_MIME_TYPE = "mp4"
        private const val MPEG_MIME_TYPE = "audio/mpeg"
        private const val QUICKTIME_MIME_TYPE = "quicktime"
        private const val VIDEO_TYPE = "video"
        private const val AUDIO_TYPE = "audio"
    }
}
