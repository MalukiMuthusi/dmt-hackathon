package codes.malukimuthusi.hackathon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.databinding.SingleTransitLegBinding
import codes.malukimuthusi.hackathon.webService.Leg

class SingleTransitLegAdapter(private val legClickListener: LegClickListener) :
    ListAdapter<Leg, SingleTranstLegViewHolder>(LegDIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleTranstLegViewHolder {
        return SingleTranstLegViewHolder.Instance(parent, legClickListener)
    }

    override fun onBindViewHolder(holder: SingleTranstLegViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SingleTranstLegViewHolder private constructor(
    private val binding: SingleTransitLegBinding, private val
    legClickListener: LegClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(leg: Leg) {
        binding.leg = leg
        binding.clickListener = legClickListener
        binding.executePendingBindings()
    }

    companion object {
        fun Instance(
            parent: ViewGroup,
            legClickListener: LegClickListener
        ): SingleTranstLegViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SingleTransitLegBinding.inflate(inflater, parent, false)
            return SingleTranstLegViewHolder(binding, legClickListener)
        }
    }
}

object LegDIFF : DiffUtil.ItemCallback<Leg>() {
    override fun areItemsTheSame(oldItem: Leg, newItem: Leg): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Leg, newItem: Leg): Boolean {
        return oldItem == newItem
    }
}

class LegClickListener(val clickListener: (leg: Leg) -> Unit) {
    fun onClick(leg: Leg) = clickListener(leg)
}