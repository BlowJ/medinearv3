package com.da.medinear.ui.main.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.da.medinear.App
import com.da.medinear.R
import com.da.medinear.databinding.FragmentMapBinding
import com.da.medinear.model.Clinic
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.text.DecimalFormat

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {

    private lateinit var mMap: GoogleMap
    private val viewModel: MapViewModel by viewModels()
    private lateinit var binding: FragmentMapBinding
    private var myLocation: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (checkPermission()) {
            loadMap()
        } else {
            // xin quyền đề hiển thị map
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 0
            )
        }
    }

    private fun loadMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    /**
     * Kiểm tra quyền đã được cấp hay chưa
     * */
    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        initMap()
        // vẽ các điểm bệnh viên lên trên map
        (context?.applicationContext as App).data.observe(viewLifecycleOwner) { it ->
            it.forEach {
                mMap.addMarker(
                    MarkerOptions()
                        .position(
                            LatLng(
                                it.location?.latitude ?: 0.0,
                                it.location?.longitude ?: 0.0
                            )
                        )
                        .title(it.name)
                )?.apply {
                    tag = it
                }
            }
        }
    }

    private fun initMap() {
        // setting hiển thị map
        if (checkPermission()) {
            mMap.isMyLocationEnabled = true
        }
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        // thực hiện nắng nghe khi vị trí của người dùng thay đổi
        val manager = context?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        manager?.apply {
            requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this@MapFragment)
            requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this@MapFragment)
        }
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
     * Xử lý khi click vào marker thì sẽ show thông tin chi tiết của bệnh viện
     * */
    override fun onMarkerClick(marker: Marker): Boolean {
        (marker.tag as? Clinic)?.apply {
            val l = Location("")
            l.latitude = this.location?.latitude ?: 0.0
            l.longitude = this.location?.longitude ?: 0.0
            // tính toán khoảng cách từ vị trí cvuar người dùng đến bệnh viện
            val distance = (myLocation?.distanceTo(l) ?: 0f) / 1000f
            val df = DecimalFormat("#.##")
            MapClinicInfoDialog().show(this, df.format(distance).toFloat(), childFragmentManager)
            return true
        }
        return false
    }

    override fun onLocationChanged(location: Location) {
        if (myLocation == null) {
            val clinic = arguments?.getSerializable(Clinic::class.java.name) as? Clinic
            val ln = if (clinic == null) {
                LatLng(
                    location.latitude,
                    location.longitude
                )
            } else {
                LatLng(
                    clinic.location?.latitude ?: 0.0,
                    clinic.location?.longitude ?: 0.0,
                )
            }
            mMap.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(ln, 15f)
                )
            )
        }
        myLocation = location
    }
}