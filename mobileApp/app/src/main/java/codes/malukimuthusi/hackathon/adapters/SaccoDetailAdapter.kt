package codes.malukimuthusi.hackathon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.dataModel.Sacco
import codes.malukimuthusi.hackathon.databinding.SaccoDetailsBinding


class SaccoDetailViewHolder private constructor(val binding: SaccoDetailsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(saccoDetails: Sacco) {
        binding.sacco = saccoDetails
    }

    companion object {
        fun from(parent: ViewGroup): SaccoDetailViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SaccoDetailsBinding.inflate(layoutInflater, parent, false)
            return SaccoDetailViewHolder(
                binding
            )
        }
    }

}

