package com.da.medinear.ui.main.clinic_detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.da.medinear.R
import com.da.medinear.databinding.ActivityClinicDetailBinding
import com.da.medinear.model.Clinic
import com.da.medinear.model.Rating
import com.da.medinear.model.User
import com.da.medinear.ui.main.clinic.ClinicActivity
import com.da.medinear.utils.Constance
import com.da.medinear.utils.ShareUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.max

class ClinicDetailActivity : AppCompatActivity(), ClinicDetailListener, ValueEventListener {

    private val viewModel: ClinicDetailViewModel by viewModels()
    private lateinit var binding: ActivityClinicDetailBinding
    private val dialog = RatingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_clinic_detail)
        viewModel.clinic = intent.getSerializableExtra(Clinic::class.java.name) as Clinic
        binding.viewModel = viewModel
        binding.listener = this
        binding.adapter = RatingAdapter(layoutInflater)
        binding.rb.setOnTouchListener { _, _ ->
            if (dialog.isAdded) return@setOnTouchListener true
            dialog.show(viewModel.clinic, supportFragmentManager)
            return@setOnTouchListener true
        }
        FirebaseDatabase.getInstance().getReference(Constance.FIREBASE_CLINIC)
            .child(viewModel.clinic.id.toString())
            .child(Constance.FIREBASE_RATING)
            .addValueEventListener(this)

        ShareUtils(this).getValue(ShareUtils.KEY_USER, User::class.java)?.apply {
            binding.isAdmin = role == 1
        }
    }

    override fun onEditClicked() {
        val intent = Intent(this, ClinicActivity::class.java)
        intent.putExtra(Clinic::class.java.name, viewModel.clinic)
        startActivity(intent)
        finish()
    }

    override fun onDeleteClicked() {
        FirebaseDatabase.getInstance().getReference(Constance.FIREBASE_CLINIC).child(viewModel.clinic.id.toString())
            .removeValue()
            .addOnSuccessListener {
                finish()
            }
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        val data = arrayListOf<Rating>()
        var rating = 0f
        snapshot.children.forEach {
            it.getValue(Rating::class.java)?.apply {
                data.add(this)
                rating += this.star ?: 0f
            }
        }
        binding.star = rating / max(data.size, 1)
        binding.adapter?.data = data
    }

    override fun onCancelled(error: DatabaseError) {

    }
}