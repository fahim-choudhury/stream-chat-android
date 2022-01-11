package io.getstream.chat.android.compose.livestream.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.compose.livestream.sample.extensions.description
import io.getstream.chat.android.compose.livestream.sample.extensions.streamPreviewLink
import io.getstream.chat.android.compose.livestream.sample.extensions.streamerAvatarLink
import io.getstream.chat.android.compose.livestream.sample.extensions.streamerName
import io.getstream.chat.android.compose.livestream.sample.ui.channels.ChannelItemCenterContent
import io.getstream.chat.android.compose.livestream.sample.ui.messages.ChatSettingsIcon
import io.getstream.chat.android.compose.livestream.sample.ui.theme.LiveStreamAppTheme
import io.getstream.chat.android.compose.ui.channels.list.ChannelItem
import io.getstream.chat.android.compose.ui.channels.list.ChannelList
import io.getstream.chat.android.compose.viewmodel.channel.ChannelListViewModel
import io.getstream.chat.android.compose.viewmodel.channel.ChannelViewModelFactory
import io.getstream.chat.android.offline.ChatDomain

class ChannelsActivity : AppCompatActivity() {

    private val factory by lazy {
        ChannelViewModelFactory(
            chatClient = ChatClient.instance(),
            chatDomain = ChatDomain.instance(),
            querySort = QuerySort.desc("last_updated"),
            filters = Filters.and(
                Filters.eq("type", "livestream"),
                Filters.`in`("members", listOf(ChatClient.instance().getCurrentUser()?.id ?: ""))
            )
        )
    }

    private val channelListViewModel by viewModels<ChannelListViewModel>(factoryProducer = { factory })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveStreamAppTheme {
                ChannelList(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = channelListViewModel,
                    itemContent = { channelItemState ->
                        ChannelItem(
                            channelItem = channelItemState,
                            currentUser = channelListViewModel.user.value,
                            onChannelClick = { startActivity(MessagesActivity.getIntent(this, it.cid)) },
                            onChannelLongClick = {

                                channelListViewModel.selectChannel(it)
                            },
                            leadingContent = {
                                Image(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp, horizontal = 12.dp)
                                        .background(Color.DarkGray, RoundedCornerShape(4.dp))
                                        .height(60.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .align(Alignment.CenterVertically)
                                        .aspectRatio(1.5f),
                                    painter = rememberImagePainter(data = channelItemState.channel.streamPreviewLink),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            },
                            centerContent = {
                                ChannelItemCenterContent(
                                    modifier = Modifier
                                        .align(Alignment.Top)
                                        .weight(1f)
                                        .padding(vertical = 8.dp),
                                    streamerAvatarImage = channelItemState.channel.streamerAvatarLink,
                                    streamerName = channelItemState.channel.streamerName,
                                    channelDescription = channelItemState.channel.description
                                )
                            },
                            trailingContent = {
                                ChatSettingsIcon {
                                }
                            }
                        )
                    }
                )
            }
        }
    }
}
