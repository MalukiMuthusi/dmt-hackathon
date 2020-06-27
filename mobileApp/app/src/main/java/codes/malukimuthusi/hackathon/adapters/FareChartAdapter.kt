package codes.malukimuthusi.hackathon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.dataModel.Route
import codes.malukimuthusi.hackathon.databinding.RouteOverviewTemplateBinding
import timber.log.Timber

class FareChartAdapter(val clicklistener: FareChartListener) :
    ListAdapter<Route, ChartViewHolder>(
        Route.RouteDiff
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        return ChartViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
//        holder.bind(getItem(position))
    }
}

class ChartViewHolder private constructor(
    private val binding: RouteOverviewTemplateBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(routte: Route, clicklistener: FareChartListener) {
        binding.route = routte
        Timber.d("Setting average fare for: %s", routte.name)
        binding.clicklistener = clicklistener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ChartViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RouteOverviewTemplateBinding.inflate(layoutInflater, parent, false)
            return ChartViewHolder(binding)
        }
    }

}

class FareChartListener(val clicklistener: (route: Route) -> Unit) {
    fun onClick(route: Route) = clicklistener(route)
}

