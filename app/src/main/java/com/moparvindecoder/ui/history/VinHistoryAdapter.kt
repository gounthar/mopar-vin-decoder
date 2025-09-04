package com.moparvindecoder.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moparvindecoder.data.local.VinHistoryEntity
import com.moparvindecoder.databinding.ItemVinHistoryBinding

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

    inner class Vh(private val binding: ItemVinHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VinHistoryEntity) {
            binding.tvVin.text = item.vin
            val year = item.year ?: "-"
            val make = item.make ?: "-"
            val model = item.model ?: "-"
            binding.tvYearMakeModel.text = listOf(year, make, model).joinToString(" ")

            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val binding = ItemVinHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Vh(binding)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bind(getItem(position))
    }
}
