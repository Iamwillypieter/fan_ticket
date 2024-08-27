package com.example.fanticket.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fanticket.MainActivity
import com.example.fanticket.R
import com.example.fanticket.login.LoginActivity
import com.example.fanticket.register.RegisterActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //Timer For SplashScreen
        val splashTime: Long = 2000

        //Timer For move to LoginActivity
        val timer = object : Thread() {
            override fun run() {
                try {
                    sleep(splashTime)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
        timer.start()
    }
}