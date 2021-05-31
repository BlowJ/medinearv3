package com.da.medinear.ui.main.clinic_detail

import android.content.Intent
import android.net.Uri
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
import java.lang.Exception
import kotlin.math.max

class ClinicDetailActivity : AppCompatActivity(), ClinicDetailListener, ValueEventListener {

    private val viewModel: ClinicDetailViewModel by viewModels()
    private lateinit var binding: ActivityClinicDetailBinding
    private val dialog = RatingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_clinic_detail)
        // lấy thông tin clinic để hiển thị lên màn hình
        viewModel.clinic = intent.getSerializableExtra(Clinic::class.java.name) as Clinic
        binding.viewModel = viewModel
        binding.listener = this
        binding.adapter = RatingAdapter(layoutInflater)
        binding.rb.setOnTouchListener { _, _ ->
            // show dialog nhập đánh giá
            if (dialog.isAdded) return@setOnTouchListener true
            dialog.show(viewModel.clinic, supportFragmentManager)
            return@setOnTouchListener true
        }
        // lấy list đánh giá của bệnh viện
        FirebaseDatabase.getInstance().getReference(Constance.FIREBASE_CLINIC)
            .child(viewModel.clinic.id.toString())
            .child(Constance.FIREBASE_RATING)
            .addValueEventListener(this)

        ShareUtils(this).getValue(ShareUtils.KEY_USER, User::class.java)?.apply {
            binding.isAdmin = role == 1
        }
    }

    /**
     * show màn hình edit clinic
     * */
    override fun onEditClicked() {
        val intent = Intent(this, ClinicActivity::class.java)
        intent.putExtra(Clinic::class.java.name, viewModel.clinic)
        startActivity(intent)
        finish()
    }

    /**
     * Mở web thông tin của bệnh viện
     * */
    override fun onMoreInformationClicked(url: String?) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * show map và focus vào vị trí của bệnh viện
     * */
    override fun onAddressItemClicked() {
        val intent = Intent()
        intent.putExtra(Clinic::class.java.name, viewModel.clinic)
        setResult(RESULT_OK, intent)
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
        // Tính tổng rating
        var rating = 0f
        snapshot.children.forEach {
            it.getValue(Rating::class.java)?.apply {
                data.add(this)
                rating += this.star ?: 0f
            }
        }
        // trung bình bằng tổng chia số lượng
        binding.star = rating / max(data.size, 1)
        binding.adapter?.data = data
    }

    override fun onCancelled(error: DatabaseError) {

    }
}