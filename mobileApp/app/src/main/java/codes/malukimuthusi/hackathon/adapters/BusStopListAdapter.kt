package codes.malukimuthusi.hackathon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.databinding.FirstBusStopBinding
import codes.malukimuthusi.hackathon.databinding.LastBusStopListBinding
import codes.malukimuthusi.hackathon.databinding.MidBusStopListBinding
import codes.malukimuthusi.hackathon.webService.Place

class BusStopListAdapter : ListAdapter<Place, RecyclerView.ViewHolder>(placeDIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> return FirstBusStopViewHolder.Instance(parent)
            1 -> return LastBusStopViewHolder.Instance(parent)
            else -> return MidBusStopViewHolder.Instance(parent)
        }

    }

    override fun getItemViewType(position: Int): Int {
        // first bus stop
        if (position == 0) {
            return 0
        }
        // last bus stop
        if (position == itemCount - 1) {
            return 1
        } else return 2 // mid bus stop
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // first bus stop
        if (position == 0) {
            return (holder as FirstBusStopViewHolder).bind(getItem(position))
        }
        // last bus stop
        if (position == itemCount - 1) {
            return (holder as LastBusStopViewHolder).bind(getItem(position))
        } else return (holder as MidBusStopViewHolder).bind(getItem(position))
    }
}

class MidBusStopViewHolder private constructor(private val binding: MidBusStopListBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(place: Place) {
        binding.place = place
        binding.executePendingBindings()
    }

    companion object {
        fun Instance(parent: ViewGroup): MidBusStopViewHolder {
            val inflatar = LayoutInflater.from(parent.context)
            val binding = MidBusStopListBinding.inflate(inflatar, parent, false)
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
        fun Instance(parent: ViewGroup): LastBusStopViewHolder {
            val inflatar = LayoutInflater.from(parent.context)
            val binding = LastBusStopListBinding.inflate(inflatar, parent, false)
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
        fun Instance(parent: ViewGroup): FirstBusStopViewHolder {
            val inflatar = LayoutInflater.from(parent.context)
            val binding = FirstBusStopBinding.inflate(inflatar, parent, false)
            return FirstBusStopViewHolder(binding)
        }
    }
}

object placeDIFF : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}