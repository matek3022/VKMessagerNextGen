package com.matek3022.vkmessagernextgen.rxapi

import com.matek3022.vkmessagernextgen.rxapi.base.ServerResponse
import com.matek3022.vkmessagernextgen.rxapi.result.ResultGetConversation
import com.matek3022.vkmessagernextgen.rxapi.result.ResultGetMessages
import com.matek3022.vkmessagernextgen.utils.PreferencesManager
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
interface VKApi {
    @GET("messages.getConversations?v=5.92&fields=photo_200_orig,online")
    fun getDialogs(
        @Query("access_token") access_token: String = PreferencesManager.getToken(),
        @Query("count") count: Int = 20,
        @Query("offset") offset: Int = 0
    ): Call<ServerResponse<ResultGetConversation>>

    @GET("messages.getHistory?v=5.92")
    fun getMessages(
        @Query("access_token") access_token: String = PreferencesManager.getToken(),
        @Query("count") count: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("user_id") userId: Int
    ): Call<ServerResponse<ResultGetMessages>>
}