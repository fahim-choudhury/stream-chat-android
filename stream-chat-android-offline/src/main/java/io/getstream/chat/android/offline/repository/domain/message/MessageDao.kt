package io.getstream.chat.android.offline.repository.domain.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.getstream.chat.android.client.utils.SyncStatus
import io.getstream.chat.android.offline.repository.domain.message.attachment.AttachmentEntity
import io.getstream.chat.android.offline.repository.domain.reaction.ReactionEntity
import java.util.Date

@Dao
internal abstract class MessageDao {

    @Transaction
    open suspend fun insert(messageEntities: List<MessageEntity>) {
        upsertMessageInnerEntities(messageEntities.map(MessageEntity::messageInnerEntity))
        deleteAttachments(messageEntities.map { it.messageInnerEntity.id })
        insertAttachments(messageEntities.flatMap(MessageEntity::attachments))
        insertReactions(messageEntities.flatMap { it.latestReactions + it.ownReactions })
    }

    @Transaction
    open fun deleteAttachments(messageIds: List<String>) {
        messageIds.chunked(SQLITE_MAX_VARIABLE_NUMBER).forEach(::deleteAttachmentsChunked)
    }

    @Query("DELETE FROM attachment_inner_entity WHERE messageId in (:messageIds)")
    protected abstract fun deleteAttachmentsChunked(messageIds: List<String>)

    @Transaction
    open suspend fun insert(messageEntity: MessageEntity) = insert(listOf(messageEntity))

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertMessageInnerEntity(messageInnerEntity: MessageInnerEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertMessageInnerEntities(messageInnerEntities: List<MessageInnerEntity>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun updateMessageInnerEntity(messageInnerEntity: MessageInnerEntity)

    @Transaction
    open suspend fun upsertMessageInnerEntity(messageInnerEntity: MessageInnerEntity) {
        val rowId = insertMessageInnerEntity(messageInnerEntity)
        if (rowId == -1L) {
            updateMessageInnerEntity(messageInnerEntity)
        }
    }

    @Transaction
    open suspend fun upsertMessageInnerEntities(messageInnerEntities: List<MessageInnerEntity>) {
        val rowIds = insertMessageInnerEntities(messageInnerEntities)
        val entitiesToUpdate = rowIds.mapIndexedNotNull { index, rowId ->
            if (rowId == -1L) messageInnerEntities[index] else null
        }
        entitiesToUpdate.forEach { updateMessageInnerEntity(it) }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertAttachments(attachmentEntities: List<AttachmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertReactions(reactions: List<ReactionEntity>)

    @Query("SELECT * from stream_chat_message WHERE cid = :cid AND (createdAt > :dateFilter || createdLocallyAt > :dateFilter) ORDER BY CASE WHEN createdAt IS NULL THEN createdLocallyAt ELSE createdAt END ASC LIMIT :limit")
    @Transaction
    abstract suspend fun messagesForChannelNewerThan(
        cid: String,
        limit: Int = 100,
        dateFilter: Date,
    ): List<MessageEntity>

    @Query("SELECT * from stream_chat_message WHERE cid = :cid AND (createdAt >= :dateFilter || createdLocallyAt >= :dateFilter) ORDER BY CASE WHEN createdAt IS NULL THEN createdLocallyAt ELSE createdAt END ASC LIMIT :limit")
    @Transaction
    abstract suspend fun messagesForChannelEqualOrNewerThan(
        cid: String,
        limit: Int = 100,
        dateFilter: Date,
    ): List<MessageEntity>

    @Query("SELECT * from stream_chat_message WHERE cid = :cid AND (createdAt < :dateFilter || createdLocallyAt < :dateFilter) ORDER BY CASE WHEN createdAt IS NULL THEN createdLocallyAt ELSE createdAt END DESC LIMIT :limit")
    @Transaction
    abstract suspend fun messagesForChannelOlderThan(
        cid: String,
        limit: Int = 100,
        dateFilter: Date,
    ): List<MessageEntity>

    @Query("SELECT * from stream_chat_message WHERE cid = :cid AND (createdAt <= :dateFilter || createdLocallyAt <= :dateFilter) ORDER BY CASE WHEN createdAt IS NULL THEN createdLocallyAt ELSE createdAt END DESC LIMIT :limit")
    @Transaction
    abstract suspend fun messagesForChannelEqualOrOlderThan(
        cid: String,
        limit: Int = 100,
        dateFilter: Date,
    ): List<MessageEntity>

    @Query("SELECT * from stream_chat_message WHERE cid = :cid ORDER BY CASE WHEN createdAt IS NULL THEN createdLocallyAt ELSE createdAt END DESC LIMIT :limit")
    @Transaction
    abstract suspend fun messagesForChannel(cid: String, limit: Int = 100): List<MessageEntity>

    @Query("DELETE from stream_chat_message WHERE cid = :cid AND createdAt < :deleteMessagesBefore")
    abstract suspend fun deleteChannelMessagesBefore(cid: String, deleteMessagesBefore: Date)

    @Query("DELETE from stream_chat_message WHERE cid = :cid AND id = :messageId")
    abstract suspend fun deleteMessage(cid: String, messageId: String)

    @Transaction
    open suspend fun select(ids: List<String>): List<MessageEntity> {
        return ids.chunked(SQLITE_MAX_VARIABLE_NUMBER).flatMap { messageIds -> selectChunked(messageIds) }
    }

    @Query("SELECT * FROM stream_chat_message WHERE stream_chat_message.id IN (:ids)")
    @Transaction
    protected abstract suspend fun selectChunked(ids: List<String>): List<MessageEntity>

    @Query("SELECT * FROM stream_chat_message WHERE stream_chat_message.id IN (:id)")
    @Transaction
    abstract suspend fun select(id: String): MessageEntity?

    @Transaction
    open suspend fun selectSyncNeeded(): List<MessageEntity> {
        return selectBySyncStatus(SyncStatus.SYNC_NEEDED)
    }

    @Transaction
    open suspend fun selectWaitForAttachments(): List<MessageEntity> {
        return selectBySyncStatus(SyncStatus.AWAITING_ATTACHMENTS)
    }

    @Query("SELECT * FROM stream_chat_message WHERE stream_chat_message.syncStatus IN (:syncStatus) ORDER BY CASE WHEN createdAt IS NULL THEN createdLocallyAt ELSE createdAt END ASC")
    @Transaction
    protected abstract suspend fun selectBySyncStatus(syncStatus: SyncStatus): List<MessageEntity>

    private companion object {
        private const val SQLITE_MAX_VARIABLE_NUMBER = 999
    }
}
