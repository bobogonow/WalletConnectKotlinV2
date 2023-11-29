@file:JvmSynthetic

package com.walletconnect.android.internal.common.storage.push_messages

import android.database.sqlite.SQLiteException
import com.walletconnect.android.internal.common.model.Tags
import com.walletconnect.android.sdk.storage.data.dao.PushMessageQueries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PushMessagesRepository(private val pushMessageQueries: PushMessageQueries) {

    val notificationTags = listOf(Tags.SESSION_PROPOSE.id, Tags.SESSION_REQUEST.id, Tags.AUTH_REQUEST.id, Tags.NOTIFY_MESSAGE.id)

    private val _arePushNotificationsEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val arePushNotificationsEnabled: StateFlow<Boolean> = _arePushNotificationsEnabled.asStateFlow()

    fun enablePushNotifications() {
        _arePushNotificationsEnabled.value = true
    }

    @Throws(SQLiteException::class)
    suspend fun insertPushMessage(id: String, topic: String, blob: String, tag: Int) {
        pushMessageQueries.insertMessage(id, topic, blob, tag.toLong())

    }

    @Throws(SQLiteException::class)
    suspend fun doesPushMessageExist(id: String): Boolean {
        return pushMessageQueries.doesMessagesExistsByRequestId(id).executeAsOne()
    }

    @Throws(SQLiteException::class)
    suspend fun deletePushMessagesByTopic(topic: String) {
        pushMessageQueries.deleteMessageByTopic(topic)
    }
}