package com.matek3022.vkmessagernextgen.utils

import android.content.Context
import android.content.SharedPreferences
import com.matek3022.vkmessagernextgen.App
import java.lang.ref.WeakReference

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
object PreferencesManager {
    private var TOKEN = "token"
    private var SETTINGS = "mysettings"
    private var USERID = "uid"
    private var USERGSON = "uidgson"
    
    private var tokenPrefs = App.instance.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
    private var settingsPref = App.instance.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
    private var userIdPref = App.instance.getSharedPreferences(USERID, Context.MODE_PRIVATE)
    private var userGsonPref = App.instance.getSharedPreferences(USERGSON, Context.MODE_PRIVATE)
    
    private val CRYPT_KEY = "cryptKey"
    private val IS_CRYPT = "isCrypt"

    fun getUserID(): Int {
        return userIdPref.getInt("uid_int", 0)
    }

    fun getUserGson(): String {
        return userGsonPref.getString("uidgson_string", "")
    }

    fun getToken(): String {
        return tokenPrefs.getString("token_string", "")
    }

    fun getSettingPhotoUserOn(): Boolean {
        return settingsPref.getBoolean("photouserOn", true)
    }

    fun getSettingPhotoChatOn(): Boolean {
        return settingsPref.getBoolean("photochatOn", true)
    }

    fun getSettingOnline(): Boolean {
        return settingsPref.getBoolean("onlineOn", true)
    }

    fun getCryptKey(): String {
        return settingsPref.getString(CRYPT_KEY, "")
    }

    fun getCryptKeyById(id: Int): String {
        return settingsPref.getString(CRYPT_KEY + id, "")
    }

    fun getIsCryptById(id: Int): Boolean {
        return settingsPref.getBoolean(IS_CRYPT + id, false)
    }

    fun getCryptString(): String {
        return settingsPref.getString("cryptString", "")
    }

    fun getDecryptString(): String {
        return settingsPref.getString("decryptString", "")
    }

    fun setUserID(uid: Int) {
        val editor = userIdPref.edit()
        editor.putInt("uid_int", uid)
        editor.apply()
    }

    fun setUserGson(gson: String) {
        val editor = userGsonPref.edit()
        editor.putString("uidgson_string", gson)
        editor.apply()
    }

    fun setToken(token: String) {
        val editor = tokenPrefs.edit()
        editor.putString("token_string", token)
        editor.apply()
    }

    fun setSettingPhotoUserOn(isChecked: Boolean) {
        val editor = settingsPref.edit()
        editor.putBoolean("photouserOn", isChecked)
        editor.apply()
    }

    fun setSettingPhotoChatOn(isChecked: Boolean) {
        val editor = settingsPref.edit()
        editor.putBoolean("photochatOn", isChecked)
        editor.apply()
    }

    fun setSettingOnline(isChecked: Boolean) {
        val editor = settingsPref.edit()
        editor.putBoolean("onlineOn", isChecked)
        editor.apply()
    }

    fun setCryptKey(key: String) {
        val editor = settingsPref.edit()
        editor.putString(CRYPT_KEY, key)
        editor.apply()
    }

    fun setCryptKeyById(id: Int, key: String) {
        val editor = settingsPref.edit()
        editor.putString(CRYPT_KEY + id, key)
        editor.apply()
    }

    fun setIsCryptById(id: Int, isCrypt: Boolean) {
        val editor = settingsPref.edit()
        editor.putBoolean(IS_CRYPT + id, isCrypt)
        editor.apply()
    }

    fun setCryptString(string: String) {
        val editor = settingsPref.edit()
        editor.putString("cryptString", string)
        editor.apply()
    }

    fun setDecryptString(string: String) {
        val editor = settingsPref.edit()
        editor.putString("decryptString", string)
        editor.apply()
    }
}