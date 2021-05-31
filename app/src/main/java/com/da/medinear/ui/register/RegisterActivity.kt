package com.da.medinear.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.da.medinear.R
import com.da.medinear.databinding.ActivityRegisterBinding
import com.da.medinear.utils.DialogUtils

class RegisterActivity : AppCompatActivity(), RegisterListener {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.viewModel = viewModel
        binding.listener = this

        // show thông báo đăng ký thất bại
        viewModel.error.observe(this) {
            DialogUtils.dismissProgressDialog()
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        // show thông báo thành công
        viewModel.success.observe(this) {
            DialogUtils.dismissProgressDialog()
            Toast.makeText(this, "Register success", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    /**
     * Thực hiện đăng ký tài khoản
     * */
    override fun onRegisterClicked() {
        DialogUtils.showProgressDialog(this)
        viewModel.register()
    }

    override fun onBackClicked() {
        finish()
    }
}