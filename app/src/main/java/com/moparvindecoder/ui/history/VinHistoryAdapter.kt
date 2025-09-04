package com.moparvindecoder.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moparvindecoder.R
import com.moparvindecoder.data.local.VinHistoryEntity

class VinHistoryAdapter(
    private val onItemClick: (VinHistoryEntity) -> Unit
) : ListAdapter<VinHistoryEntity, VinHistoryAdapter.Vh>(Diff) {

    object Diff : DiffUtil.ItemCallback<VinHistoryEntity>() {
        override fun areItemsTheSame(oldItem: VinHistoryEntity, newItem: VinHistoryEntity): Boolean {
            // vin is unique (indexed)
            return oldItem.vin == newItem.vin
        }

        override fun areContentsTheSame(oldItem: VinHistoryEntity, newItem: VinHistoryEntity): Boolean {
            return oldItem == newItem
        }
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvVin: TextView = itemView.findViewById(R.id.tv_vin)
        private val tvYmm: TextView = itemView.findViewById(R.id.tv_year_make_model)

        fun bind(item: VinHistoryEntity) {
            tvVin.text = item.vin
            val year = item.year ?: "-"
            val make = item.make ?: "-"
            val model = item.model ?: "-"
            tvYmm.text = listOf(year, make, model).joinToString(" ")

            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_vin_history, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bind(getItem(position))
    }
}
