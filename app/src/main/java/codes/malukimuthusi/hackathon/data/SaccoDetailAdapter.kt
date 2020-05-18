package codes.malukimuthusi.hackathon.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.databinding.SaccoDetailsBinding


class SaccoDetailAdapter : ListAdapter<Sacco, SaccoDetailViewHolder>(Diif) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaccoDetailViewHolder {
        return SaccoDetailViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SaccoDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SaccoDetailViewHolder private constructor(val binding: SaccoDetailsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(saccoDetails: Sacco) {
        binding.sacco = saccoDetails
    }

    companion object {
        fun from(parent: ViewGroup): SaccoDetailViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SaccoDetailsBinding.inflate(layoutInflater, parent, false)
            return SaccoDetailViewHolder(binding)
        }
    }

}

object Diif : DiffUtil.ItemCallback<Sacco>() {
    override fun areItemsTheSame(oldItem: Sacco, newItem: Sacco): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Sacco, newItem: Sacco): Boolean {
        return oldItem == newItem
    }

}
