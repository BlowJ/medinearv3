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
            val arr = all.filter {
                it.name?.contains(key.toString(), true) ?: false
            }
            return FilterResults().apply {
                count = arr.size
                values = arr
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