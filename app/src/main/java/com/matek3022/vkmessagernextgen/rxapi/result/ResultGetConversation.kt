package com.matek3022.vkmessagernextgen.rxapi.result

import com.google.gson.annotations.SerializedName
import com.matek3022.vkmessagernextgen.rxapi.model.Conversation
import com.matek3022.vkmessagernextgen.rxapi.model.Message
import com.matek3022.vkmessagernextgen.rxapi.model.User

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
data class ResultGetConversation(
    val count: Int,
    val items: List<Item>,
    @SerializedName("unread_count") val unreadCount: Int,
    var profiles: List<User>? = null
) {
    data class Item(val conversation: Conversation,
                    @SerializedName("last_message") val lastMessage: Message,
                    var user: User? = null)

    fun processUserToItem() {
        if (profiles == null) profiles = ArrayList()
        items.forEach { item ->
            item.user = profiles?.find { it.id == item.conversation.peer.id }
        }
    }
}