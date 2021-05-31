package com.da.medinear.ui.main.clinic_detail

import com.da.medinear.model.Clinic

interface ClinicDetailListener {
    fun onDeleteClicked()
    fun onEditClicked()
    fun onMoreInformationClicked(url: String?)
    fun onAddressItemClicked()
}