package io.getstream.chat.android.compose.livestream.sample.extensions

import io.getstream.chat.android.client.models.Channel

val Channel.streamerAvatarLink: String?
    get() = this.extraData[EXTRA_STREAMER_AVATAR_LINK] as? String

val Channel.streamerName: String?
    get() = this.extraData[EXTRA_STREAMER_NAME] as? String

val Channel.streamPreviewLink: String?
    get() = this.extraData[EXTRA_STREAM_PREVIEW_LINK] as? String

const val EXTRA_STREAMER_AVATAR_LINK = "EXTRA_STREAMER_AVATAR_LINK"
const val EXTRA_STREAMER_NAME = "EXTRA_STREAMER_NAME"
const val EXTRA_STREAM_PREVIEW_LINK = "EXTRA_STREAM_PREVIEW_LINK"
