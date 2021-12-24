package io.getstream.chat.android.compose.livestream.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import io.getstream.chat.android.compose.livestream.sample.ui.theme.LiveStreamAppTheme
import io.getstream.chat.android.compose.ui.channels.list.ChannelList

class ChannelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveStreamAppTheme {
                ChannelList(onChannelClick = {
                    startActivity(MessagesActivity.getIntent(this, it.cid))
                })
            }
        }
    }
}
