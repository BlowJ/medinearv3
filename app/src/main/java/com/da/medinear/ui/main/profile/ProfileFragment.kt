package com.da.medinear.ui.main.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.da.medinear.AppBinding
import com.da.medinear.databinding.FragmentProfileBinding
import com.da.medinear.model.User
import com.da.medinear.ui.login.LoginActivity
import com.da.medinear.utils.DialogUtils
import com.da.medinear.utils.ShareUtils

class ProfileFragment : Fragment(), ProfileListener {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.apply {
                context?.apply {
                    DialogUtils.showProgressDialog(this)
                }
                AppBinding.uploadImage(this,"images") { success, error ->
                    error?.apply {
                        viewModel.error.postValue(this)
                    }
                    success?.apply {
                        viewModel.avatar = this
                        binding.viewModel = viewModel
                    }
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = ShareUtils(view.context).getValue(ShareUtils.KEY_USER, User::class.java)
        viewModel.name = user?.name
        viewModel.userName = user?.userName
        viewModel.password = user?.password
        viewModel.avatar = user?.avatar
        binding.listener = this
        binding.viewModel = viewModel

        viewModel.error.observe(viewLifecycleOwner) {
            DialogUtils.dismissProgressDialog()
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

        viewModel.success.observe(viewLifecycleOwner) {
            DialogUtils.dismissProgressDialog()
            Toast.makeText(context, "Update success", Toast.LENGTH_LONG).show()
            context?.apply {
                ShareUtils(this).setValue(viewModel.user, ShareUtils.KEY_USER)
            }
        }
    }

    override fun onLogoutClicked() {
        context?.apply {
            ShareUtils(this).logout()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

    }

    override fun onUpdateAvatarClicked() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultLauncher.launch(Intent.createChooser(intent, "Pick image"))
    }
}