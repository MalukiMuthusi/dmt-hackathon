package codes.malukimuthusi.hackathon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.dataModel.Favorite
import codes.malukimuthusi.hackathon.databinding.FavouritesBinding

class FavouritePlacesAdapter : ListAdapter<Favorite, FavoritePlaceViewHolder>(FavoriteDIFF) {
    override fun onBindViewHolder(holder: FavoritePlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePlaceViewHolder {
        return FavoritePlaceViewHolder.from(parent)
    }
}

class FavoritePlaceViewHolder private constructor(private val binding: FavouritesBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(favoritePlace: Favorite) {
        binding.place = favoritePlace
        binding.executePendingBindings()
    }

    companion object {
        fun from(parentView: ViewGroup): FavoritePlaceViewHolder {
            val inflater = LayoutInflater.from(parentView.context)
            val binding = FavouritesBinding.inflate(inflater, parentView, false)
            return FavoritePlaceViewHolder(binding)
        }
    }
}

private object FavoriteDIFF : DiffUtil.ItemCallback<Favorite>() {
    override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem == newItem
    }
}