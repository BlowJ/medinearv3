package com.da.medinear.ui.main.clinic_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.da.medinear.databinding.ItemRatingBinding
import com.da.medinear.model.Rating

class RatingAdapter(private val inflater: LayoutInflater) : RecyclerView.Adapter<RatingHolder>(){

    var data: ArrayList<Rating> = arrayListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingHolder {
        return RatingHolder(ItemRatingBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        holder.binding.item = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class RatingHolder(var binding: ItemRatingBinding) : RecyclerView.ViewHolder(binding.root)