package com.da.medinear.ui.main.clinic.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.da.medinear.R
import com.da.medinear.databinding.ActivityMapBinding
import com.da.medinear.model.ClinicLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception


class MapActivity : AppCompatActivity(), OnMapReadyCallback, SearchView.OnQueryTextListener,
    GoogleMap.OnMapClickListener, MapListener {

    private var marker: Marker? = null
    private var clinicLocal: ClinicLocation? = null

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.search.setOnQueryTextListener(this)
        binding.listener = this
        geocoder = Geocoder(this)

        if (checkPermission()) {
            loadMap()
        } else {
            // Xin quyền truy cập vào map
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 0
            )
        }
    }

    /**
     * Load hiển thị map
     * */
    private fun loadMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    /**
     * Nếu chưa cấp quyền thì xin quyền truy cập vào map
     * */
    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        initMap()
    }

    /**
     * Setup hiển thị cài đặt map
     * */
    private fun initMap() {
        if (checkPermission()) {
            mMap.isMyLocationEnabled = true
        }
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (checkPermission()) {
            loadMap()
        }
    }

    /**
     * Tìm kiếm vị trí khi thưc hiện search
     * */
    override fun onQueryTextSubmit(query: String?): Boolean {
        try {
            val result = geocoder.getFromLocationName(query, 1)
            if (result.size == 0) {
                Toast.makeText(this, "Address not found", Toast.LENGTH_LONG)
                return true
            }
            val location = LatLng(result[0].latitude, result[0].longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        } catch (ex: Exception) {
            Toast.makeText(this, "Address not found", Toast.LENGTH_LONG)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    /**
     * Lấy vị trí khi click vào map
     * */
    override fun onMapClick(ln: LatLng) {
        val result = geocoder.getFromLocation(ln.latitude, ln.longitude, 1)
        if (result.isEmpty()) return
        val name = result[0].getAddressLine(0)
        clinicLocal = ClinicLocation()
        clinicLocal?.latitude = ln.latitude
        clinicLocal?.longitude = ln.longitude
        clinicLocal?.name = name

        marker?.remove()
        marker = mMap.addMarker(
            MarkerOptions()
                .position(ln)
                .title(name)
        )
    }

    /**
     * Trả về vị trí vừa lựa chọn để thực hiện thêm bệnh viện
     * */
    override fun onSubmitLocationClicked() {
        if (clinicLocal == null) {
            Toast.makeText(this, "Location not selected yet", Toast.LENGTH_LONG).show()
            return
        }
        val intent = Intent()
        intent.putExtra(ClinicLocation::class.java.name, clinicLocal)
        setResult(RESULT_OK, intent)
        finish()
    }
}