package io.getstream.chat.android.compose.livestream.sample.model.mocks

import androidx.compose.ui.graphics.Color
import io.getstream.chat.android.compose.livestream.sample.R
import io.getstream.chat.android.compose.livestream.sample.model.Reward

fun mockRewards(): List<Reward> = listOf<Reward>(
    Reward(
        color = Color.Cyan,
        name = "Being a super duper sport",
        icon = R.drawable.reward_ic,
        tokenAmount = 100
    ),
    Reward(
        color = Color.Magenta,
        name = "Just being your awesome self",
        icon = R.drawable.reward_ic,
        tokenAmount = 200
    ),
    Reward(
        color = Color.Blue,
        name = "Making great spaghetti",
        icon = R.drawable.reward_ic,
        tokenAmount = 125
    ),
    Reward(
        color = Color.Green,
        name = "Knowing the names of all of the seven dwarves",
        icon = R.drawable.reward_ic,
        tokenAmount = 250
    ),
)
