package io.getstream.chat.android.compose.livestream.sample.ui.reward

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.compose.livestream.sample.R
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun RewardsIntegration(
    rewardCount: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    IconButton(
        onClick = onClick
    ) {
        Row(modifier = modifier) {
            Icon(
                modifier = Modifier
                    .padding(vertical = 2.dp, horizontal = 4.dp)
                    .height(IntrinsicSize.Max)
                    .aspectRatio(1f),
                painter = painterResource(id = R.drawable.reward_ic),
                tint = ChatTheme.colors.textLowEmphasis,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(IntrinsicSize.Max)
                    .align(Alignment.CenterVertically),
                text = rewardCount.toString(),
                fontSize = TextUnit.Unspecified,
                color = ChatTheme.colors.textLowEmphasis,
                style = ChatTheme.typography.bodyBold,
            )
        }
    }
}
