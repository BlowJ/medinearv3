package com.da.medinear.ui.main.home

import com.da.medinear.model.Clinic

interface HomeListener {
    fun onAddClicked()
    fun onItemClinicClicked(item: Clinic)
    fun onAddressItemClicked(item: Clinic)
}