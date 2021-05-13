package com.da.medinear.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.da.medinear.App
import com.da.medinear.databinding.FragmentHomeBinding
import com.da.medinear.model.Clinic
import com.da.medinear.model.User
import com.da.medinear.ui.main.MainActivity
import com.da.medinear.ui.main.clinic.ClinicActivity
import com.da.medinear.ui.main.clinic_detail.ClinicDetailActivity
import com.da.medinear.utils.ShareUtils

class HomeFragment : Fragment(), HomeListener, SearchView.OnQueryTextListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ClinicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ClinicAdapter(layoutInflater, this)
        binding.listener = this
        binding.adapter = adapter
        (context?.applicationContext as App).data.observe(viewLifecycleOwner) {
            adapter.data = it
        }
        binding.search.setOnQueryTextListener(this)
        ShareUtils(requireContext()).getValue(ShareUtils.KEY_USER, User::class.java)?.apply {
            binding.isAdmin = role == 1
        }
    }

    override fun onAddClicked() {
        val intent = Intent(context, ClinicActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClinicClicked(item: Clinic) {
        val intent = Intent(context, ClinicDetailActivity::class.java)
        intent.putExtra(Clinic::class.java.name, item)
        startActivity(intent)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return true
    }

    override fun onAddressItemClicked(item: Clinic) {
        (activity as MainActivity).showMap(item)
    }
}