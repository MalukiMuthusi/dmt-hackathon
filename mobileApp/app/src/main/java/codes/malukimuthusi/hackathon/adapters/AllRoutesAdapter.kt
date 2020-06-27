package codes.malukimuthusi.hackathon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.databinding.AllRoutesListBinding
import codes.malukimuthusi.hackathon.webService.Route

class AllRoutesListAdapter(private val clickListener: RouteClickListener) :
    ListAdapter<Route, AllRoutesViewHolder>(Route.RouteDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllRoutesViewHolder {
        return AllRoutesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AllRoutesViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}

class AllRoutesViewHolder private constructor(
    private val binding: AllRoutesListBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(route: Route, clickListener: RouteClickListener) {
        binding.route = route
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AllRoutesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AllRoutesListBinding.inflate(layoutInflater, parent, false)
            return AllRoutesViewHolder(binding)
        }
    }
}

class RouteClickListener(val clickListener: (String) -> Unit) {
    fun onClick(routeId: String) = clickListener(routeId)
}