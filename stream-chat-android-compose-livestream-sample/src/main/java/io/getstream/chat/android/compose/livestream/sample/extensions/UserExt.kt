package io.getstream.chat.android.compose.livestream.sample.extensions

import androidx.compose.ui.graphics.Color
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.livestream.sample.R

val User.color: Color?
    get() {
        val colorInt = this.extraData[EXTRA_COLOR] as? Int

        return if (colorInt != null)
            Color(colorInt)
        else null
    }

val User.badges: List<Int>
    get() {
        val badgeList = (this.extraData[EXTRA_BADGES] as? List<*>)?.filterIsInstance<String>()

        return badgeList?.map { badge ->
            when (badge) {
                "worker" -> R.drawable.ic_gear
                "love" -> R.drawable.ic_heart
                else -> R.drawable.ic_info
            }
        } ?: listOf()
    }

const val EXTRA_COLOR = "EXTRA_COLOR"
const val EXTRA_BADGES = "EXTRA_BADGES"
