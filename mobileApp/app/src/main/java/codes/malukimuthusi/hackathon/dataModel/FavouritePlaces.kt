package codes.malukimuthusi.hackathon.dataModel

import codes.malukimuthusi.hackathon.R

data class Favorite(
    val icon: Int = HOME,
    val placeTag: String = "Home",
    val placeName: String = "add Place"
)

const val WORK = R.drawable.ic_work_black_24dp
const val HOME = R.drawable.ic_home_black_24dp

val favouritePlaceEx = listOf<Favorite>(
    Favorite(),
    Favorite(WORK, "Work")
)