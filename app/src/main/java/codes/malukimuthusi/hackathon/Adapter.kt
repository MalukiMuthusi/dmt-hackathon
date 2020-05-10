package codes.malukimuthusi.hackathon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.data.AvarageFare
import codes.malukimuthusi.hackathon.databinding.SingleChartBinding

class FareChartAdapter : ListAdapter<AvarageFare, ChartViewHolder>(DIFFCALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        return ChartViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ChartViewHolder private constructor(private val binding: SingleChartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(avarageFare: AvarageFare) {
        binding.chartFare = avarageFare
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ChartViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SingleChartBinding.inflate(layoutInflater, parent, false)
            return ChartViewHolder(binding)
        }
    }

}

object DIFFCALLBACK : DiffUtil.ItemCallback<AvarageFare>() {


    override fun areItemsTheSame(oldItem: AvarageFare, newItem: AvarageFare): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: AvarageFare, newItem: AvarageFare): Boolean {
        return oldItem == newItem
    }


}

