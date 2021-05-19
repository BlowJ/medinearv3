package com.da.medinear.ui.main.clinic

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.da.medinear.R
import com.da.medinear.databinding.ActivityAddClinicBinding
import com.da.medinear.model.Clinic
import com.da.medinear.model.ClinicLocation
import com.da.medinear.ui.main.clinic.map.MapActivity
import com.da.medinear.utils.DialogUtils
import java.util.*

class ClinicActivity : AppCompatActivity(), ClinicListener {

    private lateinit var binding: ActivityAddClinicBinding
    private val viewModel: ClinicViewModel by viewModels()

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data?.hasExtra(ClinicLocation::class.java.name) == true) {
                viewModel.address.postValue(data.getSerializableExtra(ClinicLocation::class.java.name) as ClinicLocation?)
                return@registerForActivityResult
            }
            data?.data?.apply {
                viewModel.avatar.postValue(this.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (intent.getSerializableExtra(Clinic::class.java.name) as? Clinic)?.apply {
            viewModel.avatar.value = avatar
            viewModel.open.value = open
            viewModel.close.value = close
            viewModel.address.value = location
            viewModel.name = name
            viewModel.phone = phone
            viewModel.clinic = this
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_clinic)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.listener = this

        viewModel.error.observe(this) {
            DialogUtils.dismissProgressDialog()
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        viewModel.success.observe(this) {
            DialogUtils.dismissProgressDialog()
            Toast.makeText(this, "Update success", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun openClicked() {
        showTimePicker(viewModel.open)
    }

    override fun closeClicked() {
        showTimePicker(viewModel.close)
    }

    override fun openMapClicked() {
        val intent = Intent(this, MapActivity::class.java)
        resultLauncher.launch(intent)
    }

    override fun openGalleryClicked() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    override fun onSubmitClicked() {
        DialogUtils.showProgressDialog(this)
        viewModel.save()
    }

    private fun showTimePicker(target: MutableLiveData<Long>) {
        val calendar = Calendar.getInstance()
        val picker = TimePickerDialog(this,
            { p0, p1, p2 ->
                calendar.set(Calendar.HOUR_OF_DAY, p1)
                calendar.set(Calendar.MINUTE, p2)
                target.postValue(calendar.timeInMillis)
            }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false)
        picker.show()
    }
}