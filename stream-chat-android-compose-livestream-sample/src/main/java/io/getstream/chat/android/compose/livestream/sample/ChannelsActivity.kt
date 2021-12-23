package io.getstream.chat.android.compose.livestream.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import io.getstream.chat.android.compose.livestream.sample.ui.theme.StreamchatandroidTheme
import io.getstream.chat.android.compose.ui.channels.list.ChannelList
import io.getstream.chat.android.compose.ui.theme.ChatTheme

class ChannelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StreamchatandroidTheme {
                ChatTheme {
                    ChannelList(onChannelClick = {
                        startActivity(MessagesActivity.getIntent(this, it.cid))
                    })
                }
            }
        }
    }
}
