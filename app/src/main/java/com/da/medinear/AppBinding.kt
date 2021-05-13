package com.da.medinear

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.da.medinear.utils.DialogUtils
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat

object AppBinding {
    @JvmStatic
    @BindingAdapter("avatar")
    fun setAvatar(view: ImageView, url: String?) {
        Glide
            .with(view)
            .load(url)
            .error(R.drawable.ic_avatar)
            .placeholder(R.drawable.ic_avatar)
            .circleCrop()
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("clinicAvatar")
    fun setClinicAvatar(view: ImageView, url: String?) {
        Glide
            .with(view)
            .load(url)
            .error(R.drawable.clinic)
            .placeholder(R.drawable.clinic)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("time")
    fun setTime(view: TextView, time: Long?) {
        time?.apply {
            val format = SimpleDateFormat("HH:mm")
            view.text = format.format(time)
        }
    }

    @JvmStatic
    @BindingAdapter("open", "close")
    fun setTime(view: TextView, open: Long?, close: Long?) {
        val format = SimpleDateFormat("HH:mm")
        view.text = format.format(open) + "  -  " + format.format(close)
    }

    fun uploadImage(uri: Uri, root: String, callback: (success: String?, error: String?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val riversRef = storageRef.child("$root/${uri.lastPathSegment}")
        riversRef.putFile(uri)
            .addOnFailureListener {
                callback(null, it.message)
                DialogUtils.dismissProgressDialog()
            }
            .addOnSuccessListener { it ->
                it.storage.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback(it.result?.toString(), null)
                    } else {
                        callback(null, "Upload Fail")
                    }
                    DialogUtils.dismissProgressDialog()
                }
            }
    }
}