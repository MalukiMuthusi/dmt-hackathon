package codes.malukimuthusi.hackathon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.dataModel.Sacco
import codes.malukimuthusi.hackathon.databinding.SaccosRouteListBinding

class SaccoListAdapter : ListAdapter<Sacco, SaccoListViewHolder>(SaccoDIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaccoListViewHolder {
        return SaccoListViewHolder.Instance(parent)
    }

    override fun onBindViewHolder(holder: SaccoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SaccoListViewHolder private constructor(private val binding: SaccosRouteListBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(sacco: Sacco) {
        binding.sacco = sacco
    }

    companion object {
        fun Instance(parent: ViewGroup): SaccoListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SaccosRouteListBinding.inflate(inflater, parent, false)
            return SaccoListViewHolder(binding)
        }
    }
}

