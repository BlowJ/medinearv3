package com.da.medinear.ui.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.da.medinear.databinding.UiWindowClinicBinding
import com.da.medinear.model.Clinic
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapClinicInfoDialog : BottomSheetDialogFragment() {

    private lateinit var clinic: Clinic
    private var distance: Float = 0f
    private lateinit var binding: UiWindowClinicBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UiWindowClinicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clinic = clinic
        binding.distance = distance
    }

    fun show(clinic: Clinic, distance: Float, fragmentManager: FragmentManager) {
        this.clinic = clinic
        this.distance = distance
        super.show(fragmentManager, null)
    }
}