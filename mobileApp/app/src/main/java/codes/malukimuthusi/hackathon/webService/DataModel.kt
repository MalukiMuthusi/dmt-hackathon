package codes.malukimuthusi.hackathon.webService

import androidx.recyclerview.widget.DiffUtil

data class Stop(
    val id: String? = "",
    val name: String? = "",
    val lat: Double? = 0.0,
    val lon: Double? = 0.0,
    val wheelchairBoarding: Int = 0,
    val vehicleType: Int = 0,
    val vehicleTypeSet: Boolean = false
)

data class Route(
    val id: String? = "",
    @Transient val agency: Agency? = null,
    val shortName: String? = "YES",
    val longName: String? = "",
    val type: Int? = 3,
    @Transient val routeBikesAllowed: Int? = 0,
    val sortOrder: Int? = -999,
    val eligibilityRestricted: Int? = -999,
    val sortOrderSet: Boolean? = false,
    var fare: Int? = 0
) {
    object RouteDiff : DiffUtil.ItemCallback<Route>() {
        override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean {
            return oldItem == newItem
        }
    }
}

data class Agency(
    val id: String? = "",
    val name: String? = "",
    val url: String? = "",
    val timezone: String? = "",
    val lang: String? = "en",
    val phone: String? = ""
)

