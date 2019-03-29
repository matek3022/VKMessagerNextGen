package com.matek3022.vkmessagernextgen.rxapi.model

import com.google.gson.annotations.SerializedName

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
/**
 * todo пока без пересланых сообщений
 */
data class Message(
    val id: Int,
    val date: Int,
    @SerializedName("peer_id") val peerId: Int,
    @SerializedName("from_id") val fromId: Int,
    var text: String,
    @SerializedName("random_id") val randomId: Int? = null,
    var attachments: List<Attachment>
)