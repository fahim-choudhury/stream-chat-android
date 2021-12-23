package io.getstream.chat.android.compose.livestream.sample.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class Reward(
    val color: Color = Color.Cyan,
    val name: String,
    @DrawableRes
    val icon: Int,
    val tokenAmount: Int
)
