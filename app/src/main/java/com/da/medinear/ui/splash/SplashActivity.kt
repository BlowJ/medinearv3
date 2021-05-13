package com.da.medinear.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.da.medinear.R
import com.da.medinear.databinding.ActivitySplashBinding
import com.da.medinear.model.User
import com.da.medinear.ui.login.LoginActivity
import com.da.medinear.ui.main.MainActivity
import com.da.medinear.utils.ShareUtils

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        Handler().postDelayed({
            val intent = if (ShareUtils(this).getValue(ShareUtils.KEY_USER, User::class.java) != null) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000)
    }
}