package io.getstream.chat.android.compose.livestream.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.livestream.sample.model.mocks.mockRewards
import io.getstream.chat.android.compose.livestream.sample.ui.messages.ChannelDescription
import io.getstream.chat.android.compose.livestream.sample.ui.messages.MessageItem
import io.getstream.chat.android.compose.livestream.sample.ui.player.VideoPlayer
import io.getstream.chat.android.compose.livestream.sample.ui.reward.RewardsContent
import io.getstream.chat.android.compose.livestream.sample.ui.reward.RewardsIntegration
import io.getstream.chat.android.compose.state.messages.list.MessageItemState
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import io.getstream.chat.android.compose.ui.messages.list.MessageList
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

class MessagesActivity : AppCompatActivity() {

    private val channelId = "livestream:livestream-test"

    private val factory by lazy {
        MessagesViewModelFactory(
            context = this,
            channelId = channelId
        )
    }

    private val listViewModel by viewModels<MessageListViewModel>(factoryProducer = { factory })

    private val composerViewModel by viewModels<MessageComposerViewModel>(factoryProducer = { factory })

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isRewardsContentExpanded by remember { mutableStateOf(false) }
            var isChannelDescriptionExpanded by remember { mutableStateOf(false) }

            ChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    setContent {
                        ChatTheme {
                            Scaffold(bottomBar = {
                                Column(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .fillMaxWidth()
                                        .animateContentSize()
                                ) {
                                    MessageComposer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        viewModel = composerViewModel,
                                        onCancelAction = {},
                                        onMentionSelected = {},
                                        onCommandSelected = {},
                                        onCommandsClick = {},
                                        commandPopupContent = {},
                                        onAlsoSendToChannelSelected = {},
                                        integrations = {
                                            RewardsIntegration(
                                                modifier = Modifier
                                                    .padding(horizontal = 8.dp)
                                                    .align(Alignment.CenterVertically),
                                                rewardCount = 1,
                                                onClick = {
                                                    isRewardsContentExpanded =
                                                        !isRewardsContentExpanded
                                                }
                                            )
                                        }
                                    )

                                    val rewardContentHeight =
                                        if (isRewardsContentExpanded) 200.dp else 0.dp

                                    RewardsContent(
                                        channelName = listViewModel.channel.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(rewardContentHeight),
                                        cells = GridCells.Fixed(3),
                                        rewardList = mockRewards(),
                                        onRewardSelected = {}
                                    )
                                }
                            }) { paddingValues ->
                                Column(
                                    Modifier
                                        .fillMaxSize()
                                        .animateContentSize()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(250.dp)
                                            .background(Color.Black),
                                    ) {
                                        // TODO set this up so that the URL is sent through extra data so that we can emulate streaming channels better
                                        VideoPlayer(
                                            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                                            onError = {}
                                        )

                                        Surface(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .align(Alignment.TopEnd)
                                                .clickable {
                                                    isChannelDescriptionExpanded =
                                                        !isChannelDescriptionExpanded
                                                },
                                            color = Color.Black,
                                            shape = CircleShape
                                        ) {
                                            Icon(
                                                modifier = Modifier
                                                    .padding(1.dp),
                                                painter = painterResource(id = R.drawable.ic_info),
                                                contentDescription = null,
                                                tint = Color.White,
                                            )
                                        }
                                    }
                                    ChannelDescription(
                                        modifier = Modifier
                                            .background(ChatTheme.colors.appBackground)
                                            .height(if (isChannelDescriptionExpanded) 50.dp else 0.dp)
                                            .padding(horizontal = 8.dp)
                                            .animateContentSize(),
                                        channel = listViewModel.channel,
                                        currentUser = User()
                                    )

                                    MessageList(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = paddingValues.calculateBottomPadding()),
                                        viewModel = listViewModel,
                                    ) {
                                        if (it is MessageItemState)
                                            MessageItem(messageItemState = it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(context: Context, channelId: String): Intent {
            return Intent(context, MessagesActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}
