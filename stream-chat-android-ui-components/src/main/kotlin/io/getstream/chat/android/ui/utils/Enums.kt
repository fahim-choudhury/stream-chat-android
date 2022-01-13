package io.getstream.chat.android.ui.utils

public enum class ChatUserRole(public val value: String) {
    USER("user"),
    CHANNEL_MEMBER("channel_member"),
    CHANNEL_MODERATOR("channel_moderator"),
    MODERATOR("moderator"),
    ADMIN("admin");

    public companion object {
        public fun fromStringValue(value: String?): ChatUserRole {
            return try {
                if (value != null) {
                    values().first { it.value.equals(value, true) }
                } else {
                    USER
                }
            } catch (e: NoSuchElementException) {
                USER
            }
        }
    }
}
