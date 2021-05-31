package com.da.medinear.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.da.medinear.model.User
import com.da.medinear.utils.Constance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginViewModel : ViewModel(), ValueEventListener {

    private val reference = FirebaseDatabase.getInstance().getReference(Constance.FIREBASE_USER)
    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    var userName: String? = null
    var password: String? = null
    var user: User? = null

    fun login() {
        if (userName.isNullOrBlank() || password.isNullOrBlank()) {
            error.postValue("Data is blank")
            return
        }
        // thực hiện truy vấn login với tài khoản vừa nhập
        reference.child(userName ?: "").addValueEventListener(this)
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
            // nếu tài khoản vcaf mật khẩu hợp lệ thì thông báo thành công
            user = snapshot.getValue(User::class.java)
            if (user?.password == password) {
                success.postValue(true)
                return
            }
        }
        error.postValue("User name or password incorrect")
    }

    override fun onCancelled(error: DatabaseError) {

    }
}