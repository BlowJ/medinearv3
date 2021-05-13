package com.da.medinear.ui.main.clinic

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.da.medinear.AppBinding
import com.da.medinear.model.Clinic
import com.da.medinear.model.ClinicLocation
import com.da.medinear.utils.Constance
import com.google.firebase.database.FirebaseDatabase

class ClinicViewModel : ViewModel() {
    var name: String? = null
    var phone: String? = null
    var address = MutableLiveData<ClinicLocation>()
    var open = MutableLiveData<Long>()
    var close = MutableLiveData<Long>()
    var avatar = MutableLiveData<String>()

    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()
    var clinic = Clinic()

    fun save() {
        if (name.isNullOrBlank() || address.value == null || phone.isNullOrBlank() || open.value == null || close.value == null || avatar.value == null) {
            error.postValue("Data empty")
            return
        }
        uploadImage {
            clinic.avatar = it
            clinic.close = close.value
            clinic.open = open.value
            clinic.location = address.value
            clinic.name = name
            clinic.phone = phone
            FirebaseDatabase.getInstance().getReference(Constance.FIREBASE_CLINIC).child(clinic.id.toString()).setValue(clinic)
            this@ClinicViewModel.success.postValue(true)
        }
    }

    private fun uploadImage(callback: (avatar: String) -> Unit) {
        if (avatar.value == clinic.avatar) {
            callback(clinic.avatar ?: "")
            return
        }
        AppBinding.uploadImage(Uri.parse(avatar.value),"clinics") { success, error ->
            error?.apply {
                this@ClinicViewModel.error.postValue(this)
            }
            success?.apply {
                callback(this)
            }
        }
    }
}