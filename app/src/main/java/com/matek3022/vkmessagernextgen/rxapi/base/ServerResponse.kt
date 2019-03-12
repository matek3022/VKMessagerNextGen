package com.matek3022.vkmessagernextgen.rxapi.base

import com.google.gson.annotations.SerializedName

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
class ServerResponse<T> {
    val response: T? = null
    val error: Error? = null

    data class Error(@SerializedName("error_code") val errorCode: Int,
                     @SerializedName("error_msg") val errorMsg: String)
}