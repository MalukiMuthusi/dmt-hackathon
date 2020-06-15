package codes.malukimuthusi.hackathon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.dataModel.Sacco
import codes.malukimuthusi.hackathon.databinding.SaccosListBinding

class SaccosForListAdapter : ListAdapter<Sacco, SaccosListViewHolder>(SaccoDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaccosListViewHolder {
        return SaccosListViewHolder.instance(parent)
    }

    override fun onBindViewHolder(holder: SaccosListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SaccosListViewHolder private constructor(private val binding: SaccosListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(sacco: Sacco) {
        binding.sacco = sacco
        binding.executePendingBindings()
    }

    companion object {
        fun instance(parent: ViewGroup): SaccosListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SaccosListBinding.inflate(inflater, parent, false)
            return SaccosListViewHolder(binding)
        }
    }
}

private object SaccoDiff : DiffUtil.ItemCallback<Sacco>() {
    override fun areItemsTheSame(oldItem: Sacco, newItem: Sacco): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Sacco, newItem: Sacco): Boolean {
        return oldItem == newItem
    }
}