package com.da.medinear.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.da.medinear.model.User
import com.da.medinear.utils.Constance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterViewModel : ViewModel(), ValueEventListener {
    var name: String? = null
    var userName: String? = null
    var password: String? = null
    var rePassword: String? = null

    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    private val reference = FirebaseDatabase.getInstance().getReference(Constance.FIREBASE_USER)

    fun register() {
        if (name.isNullOrBlank() || userName.isNullOrBlank() || password.isNullOrBlank() || rePassword.isNullOrBlank()) {
            error.postValue("Data is blank")
            return
        }
        if (password != rePassword) {
            error.postValue("Password not same")
            return
        }
        // kiểm tra user name đã tồn tại hay chưa
        reference.orderByChild("userName").equalTo(userName).addValueEventListener(this)
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        // nếu user name đã tồn tại thông báo lỗi
        if (snapshot.exists()) {
            error.postValue("User name exists")
            return
        }
        reference.removeEventListener(this)
        // Đăng ký thông tin user lên firebase
        val user = User().apply {
            name = this@RegisterViewModel.name
            userName = this@RegisterViewModel.userName
            password = this@RegisterViewModel.password
            role = 0
        }
        reference.child(userName ?: "").setValue(user)
        success.postValue(true)
    }

    override fun onCancelled(error: DatabaseError) {
    }
}