package com.matek3022.vkmessagernextgen.rxapi.result

import com.google.gson.annotations.SerializedName

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 26.03.19.
 */
class ResultUploadPhotoServer(
    @SerializedName("upload_url") val uploadUrl: String,
    @SerializedName("user_id") val userId: Int
) {
}