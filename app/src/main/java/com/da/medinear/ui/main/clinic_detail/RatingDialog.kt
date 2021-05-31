package com.da.medinear.ui.main.clinic_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.da.medinear.databinding.UiRatingBinding
import com.da.medinear.model.Clinic
import com.da.medinear.model.Rating
import com.da.medinear.model.User
import com.da.medinear.utils.Constance
import com.da.medinear.utils.ShareUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase

class RatingDialog : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var clinic: Clinic
    private lateinit var binding: UiRatingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UiRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imSend.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (binding.edtComment.text.toString().isNullOrBlank()) {
            Toast.makeText(context,  "Comment is empty", Toast.LENGTH_LONG).show()
            return
        }
        // Gửi rating lên firebase
        val user = ShareUtils(requireContext()).getValue(ShareUtils.KEY_USER, User::class.java)
        val rating = Rating()
        rating.comment = binding.edtComment.text.toString()
        rating.star = binding.rd.rating
        rating.userName = user?.userName
        rating.userAvatar = user?.avatar
        FirebaseDatabase.getInstance().getReference(Constance.FIREBASE_CLINIC)
            .child(clinic.id.toString())
            .child(Constance.FIREBASE_RATING)
            .child(rating.userName ?: "")
            .setValue(rating)
            .addOnSuccessListener {
                dismiss()
            }
    }

    fun show(clinic: Clinic, fragmentManager: FragmentManager) {
        this.clinic = clinic
        super.show(fragmentManager, null)
    }
}