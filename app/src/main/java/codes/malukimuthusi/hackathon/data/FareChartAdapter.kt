package codes.malukimuthusi.hackathon.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.databinding.SingleChartBinding

class FareChartAdapter(val clicklistener: FareChartListener) :
    ListAdapter<AvarageFare, ChartViewHolder>(
        DIFFCALLBACK
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        return ChartViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        holder.bind(getItem(position), clicklistener)
    }
}

class ChartViewHolder private constructor(
    private val binding: SingleChartBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(avarageFare: AvarageFare, clicklistener: FareChartListener) {
        binding.chartFare = avarageFare
        binding.clicklistener = clicklistener
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

class FareChartListener(val clicklistener: () -> Unit) {
    fun onClick() = clicklistener()
}

