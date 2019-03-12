package com.matek3022.vkmessagernextgen.rxapi.model

import com.google.gson.annotations.SerializedName

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
data class ResultGetConv(
    val count: Int,
    val items: List<Item>,
    @SerializedName("unread_count") val unreadCount: Int,
    val profiles: List<User>
) {
    data class Item(val conversation: Conversation,
                    @SerializedName("last_message") val lastMessage: Message,
                    var user: User? = null)

    fun processUserToItem() {
        items.forEach { item ->
            item.user = profiles.find { it.id == item.conversation.peer.id }
        }
    }
}