package codes.malukimuthusi.hackathon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.databinding.FirstBusStopBinding
import codes.malukimuthusi.hackathon.databinding.LastBusStopListBinding
import codes.malukimuthusi.hackathon.databinding.MidBusStopListBinding
import codes.malukimuthusi.hackathon.databinding.StopsListTitleBinding
import codes.malukimuthusi.hackathon.webService.Place

class BusStopListAdapter : ListAdapter<Place, RecyclerView.ViewHolder>(PlaceDIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> BusStopTitleViewHolder.instance(parent) // title viewHolder
            1 -> FirstBusStopViewHolder.instance(parent) // first bus stop
            2 -> MidBusStopViewHolder.instance(parent) // mid bus stop
            3 -> LastBusStopViewHolder.instance(parent) // last bus stop
            else -> throw Throwable("No viewHolder")
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0 // title, dummy item.
            1 -> 1 // first bus stop
            (itemCount - 1) -> 3 // last bus stop
            else -> 2 // mid bus stop
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {

            // first bus stop
            1 -> (holder as FirstBusStopViewHolder).bind(getItem(position))

            // last bus stop
            (itemCount - 1) -> (holder as LastBusStopViewHolder).bind(getItem(position))

            // title
            0 -> (holder as BusStopTitleViewHolder).bind()

            // mid bus stop
            else -> (holder as MidBusStopViewHolder).bind(getItem(position))
        }
    }
}

class MidBusStopViewHolder private constructor(private val binding: MidBusStopListBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(place: Place) {
        binding.place = place
        binding.executePendingBindings()
    }

    companion object {
        fun instance(parent: ViewGroup): MidBusStopViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = MidBusStopListBinding.inflate(inflater, parent, false)
            return MidBusStopViewHolder(binding)
        }
    }
}

class LastBusStopViewHolder private constructor(private val binding: LastBusStopListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(place: Place) {
        binding.place = place
        binding.executePendingBindings()
    }

    companion object {
        fun instance(parent: ViewGroup): LastBusStopViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = LastBusStopListBinding.inflate(inflater, parent, false)
            return LastBusStopViewHolder(binding)
        }
    }
}

class FirstBusStopViewHolder private constructor(private val binding: FirstBusStopBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(place: Place) {
        binding.place = place
        binding.executePendingBindings()
    }

    companion object {
        fun instance(parent: ViewGroup): FirstBusStopViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = FirstBusStopBinding.inflate(inflater, parent, false)
            return FirstBusStopViewHolder(binding)
        }
    }
}

class BusStopTitleViewHolder private constructor(binding: StopsListTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind() {

    }

    companion object {
        fun instance(parent: ViewGroup): BusStopTitleViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = StopsListTitleBinding.inflate(inflater, parent, false)
            return BusStopTitleViewHolder(binding)
        }
    }
}

private object PlaceDIFF : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}