package com.getstream.sdk.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.getstream.sdk.chat.utils.extensions.combineWith
import com.getstream.sdk.chat.utils.extensions.isDirectMessaging
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.enqueue
import io.getstream.chat.android.client.logger.ChatLogger
import io.getstream.chat.android.client.models.Attachment
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Command
import io.getstream.chat.android.client.models.Member
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.core.ExperimentalStreamChatApi
import io.getstream.chat.android.livedata.ChatDomain
import io.getstream.chat.android.offline.extensions.keystroke
import io.getstream.chat.android.offline.extensions.setMessageForReply
import io.getstream.chat.android.offline.extensions.stopTyping
import java.io.File

/**
 * ViewModel class for MessageInputView. Responsible for sending and updating chat messages.
 * Can be bound to the view using the MessageInputViewModel.bindView function.
 * @param cid The full channel id, i.e. "messaging:123".
 * @param chatDomain Entry point for all livedata & offline operations.
 */
public class MessageInputViewModel @JvmOverloads constructor(
    private val cid: String,
    private val chatDomain: ChatDomain = ChatDomain.instance(),
) : ViewModel() {
    private var activeThread = MutableLiveData<Message?>()
    private val _maxMessageLength = MediatorLiveData<Int>()
    private val _commands = MediatorLiveData<List<Command>>()
    private val _members = MediatorLiveData<List<Member>>()
    public val maxMessageLength: LiveData<Int> = _maxMessageLength
    private val _cooldownInterval = MediatorLiveData<Int>()
    public val cooldownInterval: LiveData<Int> = _cooldownInterval
    public val commands: LiveData<List<Command>> = _commands
    public val members: LiveData<List<Member>> = _members
    private val _messageToEdit: MutableLiveData<Message?> = MutableLiveData()
    public val messageToEdit: LiveData<Message?> = _messageToEdit
    private val _repliedMessage: MediatorLiveData<Message?> = MediatorLiveData()
    public val repliedMessage: LiveData<Message?> = _repliedMessage
    private val _isDirectMessage: MediatorLiveData<Boolean> = MediatorLiveData()
    public val isDirectMessage: LiveData<Boolean> = _isDirectMessage
    private val _channel = MediatorLiveData<Channel>()

    private val logger = ChatLogger.get("MessageInputViewModel")

    init {
        _maxMessageLength.value = Int.MAX_VALUE
        _commands.value = emptyList()
        chatDomain.watchChannel(cid, 0).enqueue { channelControllerResult ->
            if (channelControllerResult.isSuccess) {
                val channelController = channelControllerResult.data()
                _channel.addSource(channelController.offlineChannelData) {
                    _channel.value = channelController.toChannel()
                }
                _maxMessageLength.addSource(_channel) { _maxMessageLength.value = it.config.maxMessageLength }
                _cooldownInterval.addSource(_channel) { _cooldownInterval.value = it.cooldown }
                _commands.addSource(_channel) { _commands.value = it.config.commands }
                _isDirectMessage.addSource(
                    _channel.combineWith(chatDomain.user) { channel, _ ->
                        channel?.isDirectMessaging() ?: true
                    }
                ) { _isDirectMessage.value = it }

                _members.addSource(channelController.members) { _members.value = it }
                _repliedMessage.addSource(channelController.repliedMessage) { _repliedMessage.value = it }
            } else {
                val error = channelControllerResult.error()
                logger.logE("Could not watch channel with cid: $cid. Error message: ${error.message}. Cause message: ${error.cause?.message}")
            }
        }
    }

    /**
     * Sets and informs about new active thread.
     */
    public fun setActiveThread(parentMessage: Message) {
        activeThread.postValue(parentMessage)
    }

    public fun getActiveThread(): LiveData<Message?> {
        return activeThread
    }

    private val isThread: Boolean
        get() = activeThread.value != null

    /**
     * Resets currently active thread.
     */
    public fun resetThread() {
        activeThread.postValue(null)
    }

    public fun sendMessage(messageText: String, messageTransformer: Message.() -> Unit = { }) {
        val message = Message(cid = cid, text = messageText)
        activeThread.value?.let { message.parentId = it.id }
        stopTyping()

        chatDomain.sendMessage(message.apply(messageTransformer)).enqueue(
            onError = { chatError ->
                logger.logE("Could not send message with cid: ${message.cid}. Error message: ${chatError.message}. Cause message: ${chatError.cause?.message}")
            }
        )
    }

    public fun sendMessageWithAttachments(
        messageText: String,
        attachmentsWithMimeTypes: List<Pair<File, String?>>,
        messageTransformer: Message.() -> Unit = { },
    ) {
        // Send message should not be cancelled when viewModel.onCleared is called
        val attachments = attachmentsWithMimeTypes.map { (file, mimeType) ->
            Attachment(upload = file, mimeType = mimeType)
        }.toMutableList()

        val message = Message(cid = cid, text = messageText, attachments = attachments).apply(messageTransformer)
        chatDomain.sendMessage(message).enqueue(
            onError = { chatError ->
                logger.logE("Could not send message with cid: ${message.cid}. Error message: ${chatError.message}. Cause message: ${chatError.cause?.message}")
            }
        )
    }

    @ExperimentalStreamChatApi
    public fun sendMessageWithCustomAttachments(
        messageText: String,
        customAttachments: List<Attachment>,
        messageTransformer: Message.() -> Unit = { },
    ) {
        val message = Message(cid = cid, text = messageText, attachments = customAttachments.toMutableList())
            .apply(messageTransformer)
        chatDomain.sendMessage(message).enqueue()
    }

    /**
     * Edit message.
     *
     * @param message The Message sent.
     */
    public fun editMessage(message: Message) {
        stopTyping()
        chatDomain.editMessage(message).enqueue(
            onError = { chatError ->
                logger.logE("Could not edit message with cid: ${message.cid}. Error message: ${chatError.message}. Cause message: ${chatError.cause?.message}")
            }
        )
    }

    /**
     * Sets the message to be edited.
     *
     * @param message The Message to edit.
     */
    public fun postMessageToEdit(message: Message?) {
        _messageToEdit.postValue(message)
    }

    /**
     * First of the typing.start and typing.stop events based on the users keystrokes.
     * Call this on every keystroke.
     */
    @Synchronized
    public fun keystroke() {
        val parentId = activeThread.value?.id
        ChatClient.instance().keystroke(cid, parentId).enqueue(
            onError = { chatError ->
                logger.logE("Could not send keystroke cid: $cid. Error message: ${chatError.message}. Cause message: ${chatError.cause?.message}")
            }
        )
    }

    /**
     * Sets last typing to null and sends the typing.stop event.
     */
    public fun stopTyping() {
        val parentId = activeThread.value?.id
        ChatClient.instance().stopTyping(cid, parentId).enqueue(
            onError = { chatError ->
                logger.logE("Could not send stop typing event with cid: $cid. Error message: ${chatError.message}. Cause message: ${chatError.cause?.message}")
            }
        )
    }

    public fun dismissReply() {
        if (repliedMessage.value != null) {
            ChatClient.instance().setMessageForReply(cid, null).enqueue()
        }
    }
}
