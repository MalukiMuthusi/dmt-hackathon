package codes.malukimuthusi.hackathon.adapters

import androidx.recyclerview.widget.DiffUtil
import codes.malukimuthusi.hackathon.dataModel.Sacco

object SaccoDIFF : DiffUtil.ItemCallback<Sacco>() {
    override fun areItemsTheSame(oldItem: Sacco, newItem: Sacco): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Sacco, newItem: Sacco): Boolean {
        return oldItem == newItem
    }
}