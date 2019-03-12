package com.matek3022.vkmessagernextgen

import android.app.Application
import com.matek3022.vkmessagernextgen.rxapi.VKApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(VKApi::class.java)

    override fun onCreate() {
        super.onCreate()
        App.instance = this
    }
}