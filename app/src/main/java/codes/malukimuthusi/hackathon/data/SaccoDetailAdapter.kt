package codes.malukimuthusi.hackathon.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.databinding.SaccoDetailsBinding


class SaccoDetailAdapter : ListAdapter<SaccoDetails, SaccoDetailViewHolder>(Diif) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaccoDetailViewHolder {
        return SaccoDetailViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SaccoDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SaccoDetailViewHolder private constructor(val binding: SaccoDetailsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(saccoDetails: SaccoDetails) {
        binding.saccodetailsC = saccoDetails
    }

    companion object {
        fun from(parent: ViewGroup): SaccoDetailViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SaccoDetailsBinding.inflate(layoutInflater, parent, false)
            return SaccoDetailViewHolder(binding)
        }
    }

}

object Diif : DiffUtil.ItemCallback<SaccoDetails>() {
    override fun areItemsTheSame(oldItem: SaccoDetails, newItem: SaccoDetails): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: SaccoDetails, newItem: SaccoDetails): Boolean {
        return oldItem == newItem
    }

}
