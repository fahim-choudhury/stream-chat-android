package io.getstream.chat.android.compose.livestream.sample.ui.reward

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.chat.android.compose.livestream.sample.R
import io.getstream.chat.android.compose.livestream.sample.model.Reward
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun RewardItem(
    reward: Reward,
    modifier: Modifier = Modifier,
    onRewardSelected: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .aspectRatio(0.8f)
                .fillMaxWidth()
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    enabled = true,
                    onClick = onRewardSelected
                ),
            backgroundColor = reward.color
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .padding(10.dp),
                    painter = painterResource(id = reward.icon),
                    tint = Color.White,
                    contentDescription = LocalContext.current.getString(
                        R.string.accessibility_reward_name, reward.name
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(IntrinsicSize.Min),
                    color = ChatTheme.colors.overlay,
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        Icon(
                            modifier = Modifier
                                .fillMaxHeight(0.55f)
                                .aspectRatio(1f)
                                .align(Alignment.CenterVertically),
                            painter = painterResource(id = R.drawable.reward_ic),
                            tint = Color.White,
                            contentDescription = LocalContext.current.getString(
                                R.string.accessibility_give_reward,
                                reward.name,
                                reward.tokenAmount
                            )
                        )
                        Text(
                            modifier = Modifier
                                .padding(2.dp),
                            text = reward.tokenAmount.toString(),
                            style = ChatTheme.typography.bodyBold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
        Text(
            text = reward.name,
            style = ChatTheme.typography.body,
            textAlign = TextAlign.Center,
            color = Color.DarkGray
        )
    }
}

@Preview(showBackground = true, name = "RewardItem Preview")
@Composable
private fun RewardItemPreview() {
    ChatTheme {
        RewardItem(
            reward = Reward(
                name = "Super duper awesome award",
                icon = R.drawable.reward_ic,
                tokenAmount = 100
            )
        )
    }
}
