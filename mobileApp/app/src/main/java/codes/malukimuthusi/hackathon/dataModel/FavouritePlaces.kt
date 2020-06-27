package codes.malukimuthusi.hackathon.dataModel

import androidx.recyclerview.widget.DiffUtil
import codes.malukimuthusi.hackathon.R

data class Favorite(
    val icon: Int = HOME,
    val placeTag: String = "Home",
    val placeName: String = "add Place"
) {
    object FavouriteDiff : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem
        }
    }
}

const val WORK = R.drawable.ic_work_black_24dp
const val HOME = R.drawable.ic_home_black_24dp

val favouritePlaceEx = listOf<Favorite>(
    Favorite(),
    Favorite(WORK, "Work")
)