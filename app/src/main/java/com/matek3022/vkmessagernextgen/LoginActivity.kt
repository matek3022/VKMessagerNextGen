package com.matek3022.vkmessagernextgen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.matek3022.vkmessagernextgen.StartActivity.Companion.EXTRA_LOGOUT
import com.matek3022.vkmessagernextgen.utils.PreferencesManager
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context, logout: Boolean): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(EXTRA_LOGOUT, logout)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val extras = intent.extras
        var logout = false
        if (extras != null) {
            logout = extras.getBoolean(EXTRA_LOGOUT, false)
        }
        setContentView(R.layout.activity_login)

        val web = findViewById<WebView>(R.id.webView)
        web.settings.javaScriptEnabled = true
        if (logout) {
            CookieSyncManager.createInstance(web.context).sync()
            val man = CookieManager.getInstance()
            man.removeAllCookie()
        }
        web.settings.javaScriptCanOpenWindowsAutomatically = true
        web.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (url.startsWith("https://oauth.vk.com/blank.html")) {
                    doneWithThis(url)
                }
            }
        }
        //8388607
        val url = "https://oauth.vk.com/authorize?" +
                "client_id=" + 5658788 + "&" +
                "scope=" + 6274271 + "&" +
                "redirect_uri=" + "https://oauth.vk.com/blank.html" + "&" +
                "display=touch&" +
                "v=" + "5.92" + "&" +
                "response_type=token"
        web.loadUrl(url)
        web.visibility = View.VISIBLE
    }

    fun doneWithThis(url: String) {
        val token = extract(url, "access_token=(.*?)&")
        val uid = Integer.parseInt(extract(url, "user_id=(\\d*)")!!)

        PreferencesManager.setToken(token ?: "")
        PreferencesManager.setUserID(uid)

        goNext()
    }

    fun extract(from: String, patt: String): String? {
        val ptrn = Pattern.compile(patt)
        val mtch = ptrn.matcher(from)
        return if (!mtch.find()) null else mtch.toMatchResult().group(1)
    }

    fun goNext() {
        this@LoginActivity.finish()
        startActivity(Intent(this, BaseActivity::class.java))
    }
}
