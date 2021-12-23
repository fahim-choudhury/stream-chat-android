package io.getstream.chat.android.compose.livestream.sample.app

import android.app.Application
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.livedata.ChatDomain

class LivestreamSampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupStreamSdk()
        connectUser()
    }

    private fun setupStreamSdk() {
        val client = ChatClient.Builder("cpv4bsuedrft", applicationContext)
            .logLevel(ChatLogLevel.ALL)
            .build()
        ChatDomain.Builder(client, applicationContext)
            .userPresenceEnabled()
            .build()
    }

    private fun connectUser() {
        ChatClient.instance().connectUser(
            user = User(
                id = "marintolic",
                extraData = mutableMapOf(
                    "name" to "Marin Tolic",
                ),
            ),
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoibWFyaW50b2xpYyJ9.PcvGyED4_8jlC3fIzUiFU5sPNYydV35oyv_SOhnGP-8"
        ).enqueue()
    }
}
