package com.matek3022.vkmessagernextgen.rxapi.result

import com.google.gson.annotations.SerializedName
import com.matek3022.vkmessagernextgen.rxapi.model.Message

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 13.03.19.
 */
data class ResultGetMessages(val count: Int?,
                             var items: List<Message>,
                             @SerializedName("in_read") val inRead: Int,
                             @SerializedName("out_read") val outRead: Int) {
}