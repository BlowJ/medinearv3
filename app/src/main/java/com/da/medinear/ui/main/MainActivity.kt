package com.da.medinear.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.da.medinear.R
import com.da.medinear.databinding.ActivityMainBinding
import com.da.medinear.model.Clinic
import com.da.medinear.ui.main.home.HomeFragment
import com.da.medinear.ui.main.map.MapFragment
import com.da.medinear.ui.main.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.nav.setOnNavigationItemSelectedListener(this)
        selectFragment(HomeFragment())
    }

    /**
     *  Show màn hình tương ứng khi click vào bottom dưới màn hình
     * */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fm = when(item.itemId) {
            R.id.nav_map -> MapFragment()
            R.id.nav_profile -> ProfileFragment()
            else -> HomeFragment()
        }
        selectFragment(fm)
        return true
    }

    private fun selectFragment(fm: Fragment) {
        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.container, fm)
        t.commit()
    }

    /**
     * Show màn hình map và tự động focus vào vị trí bệnh viện vừa chọn
     * */
    fun showMap(item: Clinic) {
        val fm = MapFragment()
        fm.arguments = Bundle().apply {
            putSerializable(Clinic::class.java.name, item)
        }
        selectFragment(fm)
    }
}