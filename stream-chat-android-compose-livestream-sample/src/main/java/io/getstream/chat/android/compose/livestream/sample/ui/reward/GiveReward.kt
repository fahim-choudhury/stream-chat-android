package io.getstream.chat.android.compose.livestream.sample.ui.reward

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.compose.livestream.sample.R
import io.getstream.chat.android.compose.livestream.sample.model.Reward
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun GiveReward(
    reward: Reward,
    modifier: Modifier,
    onGiveRewardClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = LocalContext.current.getString(R.string.you_are_about_to_give_reward),
            style = ChatTheme.typography.body
        )
        Spacer(modifier = Modifier.height(50.dp))
        Surface(color = reward.color) {
            Icon(
                modifier = Modifier.padding(8.dp),
                painter = painterResource(id = reward.icon),
                contentDescription = LocalContext.current.getString(
                    R.string.accessibility_give_reward,
                    reward.name,
                    reward.tokenAmount
                ),
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        Button(onClick = onGiveRewardClicked) {
            Text(text = LocalContext.current.getString(R.string.got_it), color = reward.color)
        }
    }
}
