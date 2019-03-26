package com.matek3022.vkmessagernextgen.rxapi

import com.matek3022.vkmessagernextgen.rxapi.base.ServerResponse
import com.matek3022.vkmessagernextgen.rxapi.result.*
import com.matek3022.vkmessagernextgen.utils.PreferencesManager
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.*


/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
interface VKApi {
    @GET("messages.getConversations?v=5.92&extended=1&fields=photo_200,online")
    fun getDialogs(
        @Query("access_token") access_token: String = PreferencesManager.getToken(),
        @Query("count") count: Int = 20,
        @Query("offset") offset: Int = 0
    ): Observable<ServerResponse<ResultGetConversation>>

    @GET("messages.getHistory?v=5.92")
    fun getMessages(
        @Query("access_token") access_token: String = PreferencesManager.getToken(),
        @Query("count") count: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("user_id") userId: Int
    ): Observable<ServerResponse<ResultGetMessages>>

    @GET("photos.getMessagesUploadServer?v=5.92")
    fun getPhotoUploadServer(
        @Query("access_token") access_token: String = PreferencesManager.getToken()
    ): Observable<ServerResponse<ResultUploadPhotoServer>>

    @Multipart
    @POST
    fun uploadPhoto(@Url server: String, @Part("photo") description: RequestBody, @Part file: MultipartBody.Part): Observable<ResultUploadPhoto>

    @GET("photos.saveMessagesPhoto?v=5.92")
    fun savePhoto(
        @Query("access_token") access_token: String = PreferencesManager.getToken(),
        @Query("photo") photo: String,
        @Query("server") server: Int,
        @Query("hash") hash: String
    ): Observable<ServerResponse<List<ResultSavePhoto>>>

    @GET("messages.send?v=5.92")
    fun messageSend(
        @Query("access_token") access_token: String = PreferencesManager.getToken(),
        @Query("message") message: String,
        @Query("random_id") randomId: Int = Random().nextInt(),
        @Query("user_id") userId: Int,
        @Query("attachment") attachment: String? = null
    ): Observable<ServerResponse<Int>>
}