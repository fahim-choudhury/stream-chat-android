package io.getstream.chat.android.compose.livestream.sample.ui.reward

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import io.getstream.chat.android.compose.livestream.sample.R

@Composable
fun RewardsIntegration(
    rewardCount: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(modifier = modifier) {
        Icon(
            modifier = Modifier
                .wrapContentSize()
                .clickable { onClick() },
            painter = painterResource(id = R.drawable.reward_ic),
            contentDescription = null

        )
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .height(IntrinsicSize.Max)
                .align(Alignment.CenterVertically),
            text = rewardCount.toString(),
            fontSize = TextUnit.Unspecified
        )
    }
}
