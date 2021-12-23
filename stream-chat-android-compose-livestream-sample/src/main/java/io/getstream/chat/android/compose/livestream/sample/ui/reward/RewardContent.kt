package io.getstream.chat.android.compose.livestream.sample.ui.reward

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.compose.livestream.sample.model.Reward
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@ExperimentalFoundationApi
@Composable
fun RewardsContent(
    channelName: String,
    rewardList: List<Reward>,
    modifier: Modifier = Modifier,
    cells: GridCells,
    onRewardSelected: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = channelName,
            style = ChatTheme.typography.bodyBold
        )

        LazyVerticalGrid(cells = cells) {
            items(rewardList) { reward ->
                key(reward.name) {
                    RewardItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(12.dp),
                        reward = reward,
                        onRewardSelected = onRewardSelected
                    )
                }
            }
        }
    }
}
