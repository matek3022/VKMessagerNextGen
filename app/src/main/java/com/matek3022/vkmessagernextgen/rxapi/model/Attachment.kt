package com.matek3022.vkmessagernextgen.rxapi.model

import com.google.gson.annotations.SerializedName

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
/**
 * todo Пока реализованы вложения фото
 */
data class Attachment(
    val type: String,
    val photo: Photo?
) {
    data class Photo(val id: Int,
                     @SerializedName("owner_id") val ownerId: Int,
                     @SerializedName("user_id") val userId: Int,
                     val text: String,
                     val date: Int,
                     val width: Int,
                     val height: Int,
                     val sizes: List<Size>) {
        data class Size(val src: String,
                        val width: Int,
                        val height: Int,
                        val type: String)
    }
}