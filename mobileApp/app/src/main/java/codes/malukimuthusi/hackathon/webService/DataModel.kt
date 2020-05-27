package codes.malukimuthusi.hackathon.webService

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
    val agency: Agency? = Agency(),
    val shortname: String? = "",
    val longName: String? = "",
    val type: Int? = 3,
    val routeBikesAllowed: Int? = 0,
    val sortOrder: Int? = -999,
    val eligibilityRestricted: Int? = -999,
    val sortOrderSet: Boolean? = false

)

data class Agency(
    val id: String? = "",
    val name: String? = "",
    val url: String? = "",
    val timezone: String? = "",
    val lang: String? = "en",
    val phone: String? = ""
)

data class Pattern(
    val id: String? = "",
    val desc: String? = "",
    val stops: List<Stop>? = listOf(),
    val trips: List<Trip> = listOf()
)

data class Trip(
    val id: String? = "",
    val route: Route?,
    val serviceId: String?,
    val tripHeadsign: String?,
    val directionId: String?,
    val shapeId: String?,
    val wheelchairAccessible: Int?,
    val tripBikesAllowed: Int?,
    val bikesAllowed: Int?,
    val drtAdvanceBookMin: Double?
)

data class Geometry(
    val points: String? = "",
    val length: Int? = 0
)

data class Time(
    val stopId: String? = "",
    val stopIndex: Int? = 0,
    val stopCount: Int? = 0,
    val scheduledArrival: Int? = 0,
    val scheduledDeparture: Int? = 0,
    val realtimeArrival: Int? = 0,
    val realtimeDeparture: Int? = 0,
    val arrivalDelay: Int? = 0,
    val departureDelay: Int? = 0,
    val timepoint: Boolean? = true,
    val realtime: Boolean? = false,
    val realtimeState: String? = "",
    val serviceDay: Int? = 0,
    val tripId: String? = "",
    val headsign: String? = "",
    val continuousPickup: Int? = -999,
    val continuousDropOff: Int? = -999,
    val serviceAreaRadius: Double? = 0.0
)

data class StopTime(
    val pattern: Pattern?,
    val times: List<Time>?
)

data class Transfer(
    val toStopId: String?,
    val distance: Double?
)
