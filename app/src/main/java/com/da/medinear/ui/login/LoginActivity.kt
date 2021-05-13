package com.da.medinear.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.da.medinear.ui.main.MainActivity
import com.da.medinear.R
import com.da.medinear.databinding.ActivityLoginBinding
import com.da.medinear.model.User
import com.da.medinear.ui.register.RegisterActivity
import com.da.medinear.utils.DialogUtils
import com.da.medinear.utils.ShareUtils

class LoginActivity : AppCompatActivity(), LoginListener {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var shared: ShareUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shared = ShareUtils(this)
        checkLogin()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.listener = this

        viewModel.error.observe(this) {
            DialogUtils.dismissProgressDialog()
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        viewModel.success.observe(this) {
            DialogUtils.dismissProgressDialog()
            shared.setValue(viewModel.user, ShareUtils.KEY_USER)
            checkLogin()
        }
    }

    private fun checkLogin() {
        if (shared.getValue(ShareUtils.KEY_USER, User::class.java) != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onLoginClicked() {
        DialogUtils.showProgressDialog(this)
        viewModel.login()
    }

    override fun onRegisterClicked() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}