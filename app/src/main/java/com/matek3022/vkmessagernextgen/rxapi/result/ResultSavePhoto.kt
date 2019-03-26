package com.matek3022.vkmessagernextgen.rxapi.result

import com.google.gson.annotations.SerializedName
import com.matek3022.vkmessagernextgen.rxapi.model.Attachment

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 26.03.19.
 */
class ResultSavePhoto(
    val id: Int,
    val pid: Int,
    val aid: Int,
    @SerializedName("owner_id") val ownerId: Int,
    val sizes: List<Attachment.Photo.Size>

) {
    fun getOriginalUrl() = sizes.find { it.type == "w" }?.src ?: sizes.find { it.type == "z" }?.src
    ?: sizes.find { it.type == "y" }?.src ?: sizes.find { it.type == "x" }?.src
    ?: sizes.find { it.type == "m" }?.src
}