package com.matek3022.vkmessagernextgen.rxapi.model

import com.google.gson.annotations.SerializedName

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
data class User(val id: Int,
                @SerializedName("first_name") val firstName: String,
                @SerializedName("last_name") val lastName: String,
                val deactivated: String?,
                @SerializedName("is_closed") val isClosed: Boolean,
                @SerializedName("can_access_closed") val canAccessClosed: Boolean,
                @SerializedName("photo_200_orig") val photoUrlOrig200: String,
                val online: Int) {
}