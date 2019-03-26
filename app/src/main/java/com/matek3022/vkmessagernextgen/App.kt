package com.matek3022.vkmessagernextgen

import android.app.Application
import com.matek3022.vkmessagernextgen.rxapi.VKApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
class App: Application() {

    companion object {
        lateinit var instance: App
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.vk.com/method/")
        .client(OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .build()
    val service = retrofit.create(VKApi::class.java)

    override fun onCreate() {
        super.onCreate()
        App.instance = this
    }
}