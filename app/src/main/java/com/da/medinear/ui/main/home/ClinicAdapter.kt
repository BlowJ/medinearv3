package com.da.medinear.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.da.medinear.App
import com.da.medinear.databinding.ItemClinicBinding
import com.da.medinear.model.Clinic

class ClinicAdapter(private val inflater: LayoutInflater, private val listener: HomeListener) : RecyclerView.Adapter<ClinicHolder>(), Filterable {

    var data: ArrayList<Clinic> = arrayListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicHolder {
        return ClinicHolder(ItemClinicBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ClinicHolder, position: Int) {
        holder.binding.item = data[position]
        holder.binding.listener = listener
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ClinicFilter(private val all: List<Clinic>, private val adapter: ClinicAdapter) : Filter() {
        override fun performFiltering(key: CharSequence?): FilterResults {
            val values = key.toString().split("-")
            val search = values.getOrNull(0) ?: ""
            val from = (values.getOrNull(1) ?: "0").toFloat()
            val to = (values.getOrNull(2) ?: "5").toFloat()
            val arr = all.filter {
                (it.name?.contains(search, true) ?: false) && it.getStarAvg() >= from && it.getStarAvg() <= to
            }
            return FilterResults().apply {
                count = arr.size
                this.values = arr
            }
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {
            adapter.data = results?.values as ArrayList<Clinic>
        }

    }

    override fun getFilter(): Filter {
        val all = (inflater.context.applicationContext as App).data.value
        return ClinicFilter(all?.toList() ?: listOf(), this)
    }
}

class ClinicHolder(var binding: ItemClinicBinding) : RecyclerView.ViewHolder(binding.root)