package com.matek3022.vkmessagernextgen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.matek3022.vkmessagernextgen.utils.PreferencesManager

class StartActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LOGOUT = "logout"

        fun getIntent(context: Context, logout: Boolean = false, clearStack: Boolean = false): Intent {
            val intent = Intent(context, StartActivity::class.java)
            intent.putExtra(EXTRA_LOGOUT, logout)
            if (clearStack) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        if (PreferencesManager.getToken() === "") {
            startActivity(LoginActivity.getIntent(this@StartActivity, intent.getBooleanExtra(EXTRA_LOGOUT, true)))
            this@StartActivity.finish()
        } else {
            startActivity(Intent(this, BaseActivity::class.java))
            this@StartActivity.finish()
        }
    }
}
