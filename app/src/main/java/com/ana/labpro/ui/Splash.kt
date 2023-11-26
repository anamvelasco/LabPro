package com.ana.labpro.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import com.ana.labpro.R
import com.ana.labpro.ui.login.LoginActivity

class Splash : AppCompatActivity() {

    private val splashTimeOut: Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val rootView = window.decorView
        rootView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                rootView.viewTreeObserver.removeOnPreDrawListener(this)
                rootView.postDelayed({
                    val intent = Intent(this@Splash, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }, splashTimeOut)
                return true
            }
        }
        )
    }
}