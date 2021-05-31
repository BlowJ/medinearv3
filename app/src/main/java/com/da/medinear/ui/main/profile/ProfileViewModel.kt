package com.da.medinear.ui.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.da.medinear.model.User
import com.da.medinear.utils.Constance
import com.google.firebase.database.FirebaseDatabase

class ProfileViewModel : ViewModel() {
    lateinit var user: User
    var name: String? = null
    var userName: String? = null
    var password: String? = null
    var avatar: String? = null
    var role: Int = 0

    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    fun update() {
        if (name.isNullOrBlank() || userName.isNullOrBlank() || password.isNullOrBlank()) {
            error.postValue("Data is blank")
            return
        }
        // update thông tin user lên firebase
        user = User().apply {
            userName = this@ProfileViewModel.userName
            name = this@ProfileViewModel.name
            password = this@ProfileViewModel.password
            avatar = this@ProfileViewModel.avatar
            role = this@ProfileViewModel.role
        }
        FirebaseDatabase.getInstance().getReference(Constance.FIREBASE_USER).child(userName ?: "").setValue(user)
        success.postValue(true)
    }
}