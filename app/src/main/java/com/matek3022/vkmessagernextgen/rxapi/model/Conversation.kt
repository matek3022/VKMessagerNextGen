package com.matek3022.vkmessagernextgen.rxapi.model

import com.google.gson.annotations.SerializedName

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
data class Conversation(
    val peer: Peer,
    @SerializedName("in_read") val lastInboxMessageIdRead: Int,
    @SerializedName("out_read") val lastOutboxMessageIdRead: Int,
    @SerializedName("unread_count") val unreadMessageCount: Int,
    @SerializedName("chat_setting") val chatSettings: ChatSettings? = null
) {
    data class Peer(
        val id: Int,
        val type: String,
        @SerializedName("local_id") val localId: Int)

    data class ChatSettings(
        @SerializedName("members_count") val membersCount: Int,
        val title: String,
        val photo: Photo
    ) {
        data class Photo(
            @SerializedName("photo_50") val photoUrl50: String,
            @SerializedName("photo_100") val photoUrl100: String,
            @SerializedName("photo_200") val photoUrl200: String
        )
    }
}