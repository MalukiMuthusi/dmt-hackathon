package codes.malukimuthusi.hackathon.webService

import androidx.recyclerview.widget.DiffUtil

data class Response(
    // A dictionary of the parameters provided in the request that triggered this response.
    @Transient val requestParameters: RequestParameters? = RequestParameters(),
    @Transient val debugOutput: DebugOutPut? = DebugOutPut(),
    @Transient val elevationMetadata: ElevationMetadata? = ElevationMetadata(),
    val plan: TripPlan? = TripPlan(),
    val error: PlannerError? = null
)

data class RequestParameters(
    val fromPlace: String? = "40.714476,-74.005966",
    val toPlace: String? = "40.714476,-74.005966"
)

/*
* Holds information to be included in the REST Response for debugging and profiling purposes.
* startedCalculating is called in the routingContext constructor.
* finishedCalculating and finishedRendering are all called in PlanGenerator.generate().
*  finishedPrecalculating and foundPaths are called in the SPTService implementations.
* */
data class DebugOutPut(
    val precalculationTime: Long? = 0L,
    val pathCalculationTime: Long? = 0L,
    val pathTimes: List<Long>? = listOf(),
    val renderingTime: Long? = 0L,
    val totalTime: Long? = 0L,
    val timedOut: Boolean? = false
)

data class ElevationMetadata(
    val ellipsoidToGeoidDifference: Double? = 0.0,
    val geoidElevation: Boolean? = true
)

data class TripPlan(
    val from: Place? = Place(),
    val to: Place? = Place(),
    val itineraries: List<Itinerary>? = listOf(),
    val date: Long? = 0L

)

data class Place(
    val name: String? = "",
    val stopId: String? = "",
    @Transient val stopCode: String? = "",
    @Transient val platformCode: String? = "",
    val lon: Double? = 0.0,
    val lat: Double? = 0.0,
    val arrival: Long? = 0L,
    val departure: Long? = 0L,
    @Transient val orig: String? = "",
    @Transient val zoneId: String? = "",
    @Transient val stopIndex: Long? = 0L,
    @Transient val stopSequence: Long? = 0L,
    @Transient val vertexType: String? = "",
    @Transient val bikeShareId: String? = "",
    @Transient val boardAlightType: String? = "",
    val flagStopArea: EncodedPolylineBean? = EncodedPolylineBean()
) {
    object PlaceDiff : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }
    }
}

/*
* Distinguish between special ways a passenger may board or alight at a stop.
* The majority of boardings and alightings will be of type "default"
*  -- a regular boarding or alighting at a regular transit stop. Currently,
*  the only non-default types are related to GTFS-Flex,
*  but this pattern can be extended as necessary.*/
enum class BoardAlightType {
    //A regular boarding or alighting at a fixed-route transit stop.
    DEFAULT,

    //A flag-stop boarding or alighting, e.g.
    // flagging the bus down or a passenger asking the bus driver for a drop-off between stops.
    // This is specific to GTFS-Flex.
    FLAG_STOP,

    //A boarding or alighting at which the vehicle deviates from its fixed route to drop off a passenger.
    // This is specific to GTFS-Flex.
    DEVIATED
}

/*
* Type of vertex. (Normal, Bike sharing station, Bike P+R, Transit stop)
*  Mostly used for better localization of bike sharing and P+R station names
* */
enum class VertexType {
    NORMAL,
    BIKESHARE,
    BIKEPARK,
    TRANSIT
}

data class Itinerary(
    val duration: Long? = 0L,
    val startTime: Long? = 0L,
    val endTime: Long? = 0L,
    val walkTime: Long? = 0L,
    val transitTime: Long? = 0L,
    @Transient val waitingTime: Long? = 0L,
    val walkDistance: Double? = 0.0,
    @Transient val walkLimitExceeded: Boolean? = false,
    @Transient val elevationLost: Long? = 0L,
    @Transient val elevationGained: Long? = 0L,
    val transfers: Int? = 0,
    @Transient val fare: Fare? = Fare(),
    val legs: List<Leg>? = listOf(),
    @Transient val tooSloped: Boolean? = false
) {
    // List Adapter Diff
    object IteneraryDiff : DiffUtil.ItemCallback<Itinerary>() {
        override fun areItemsTheSame(oldItem: Itinerary, newItem: Itinerary): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Itinerary, newItem: Itinerary): Boolean {
            return oldItem == newItem
        }
    }
}

data class Fare(
    @Transient val fare: Money? = Money(),
    @Transient val details: FareComponent? = FareComponent()
)

data class FareComponent(
    @Transient val fareId: String? = "",
    @Transient val price: Money? = Money(),
    @Transient val routes: List<String>? = listOf()
)

data class Money(
    @Transient val currency: WrappedCurrency? = WrappedCurrency(),
    @Transient val cents: Int? = 0
)

data class WrappedCurrency(
    @Transient val defaultFractionDigits: Int? = 0,
    @Transient val currencyCode: String? = "",
    @Transient val symbol: String? = "",
    @Transient val currency: String? = ""
)

// One leg of a trip -- that is, a temporally continuous piece of the journey that takes place on a particular vehicle (or on foot).
data class Leg(
    val startTime: Long? = 0L,
    val endTime: Long? = 0L,
    @Transient val departureDelay: Long? = 0L,
    @Transient val arrivalDelay: Long? = 0L,
    @Transient val realTime: Boolean? = false,
    @Transient val isNonExactFrequency: Boolean? = false,
    @Transient val headway: Long? = 0L,
    val distance: Double? = 0.0,
    @Transient val pathway: Boolean? = false,
    val mode: String? = "",
    val route: String? = "",
    @Transient val agencyName: String? = "",
    @Transient val agencyUrl: String? = "",
    @Transient val agencyBrandingUrl: String? = "",
    @Transient val agencyTimeZoneOffset: Long? = 0L,
    @Transient val routeColor: String? = "",
    @Transient val routeType: Int? = 3,
    val routeId: String? = "",
    @Transient val routeTextColor: String? = "",
    @Transient val interlineWithPreviousLeg: Boolean? = false,
    @Transient val tripShortName: String? = "",
    @Transient val tripBlockId: String? = "",
    @Transient val headsign: String? = "",
    @Transient val agencyId: String? = "",
    val tripId: String? = "",
    @Transient val serviceDate: String? = "",
    @Transient val routeBrandingUrl: String? = null,
    val from: Place? = Place(),
    val to: Place? = Place(),
    val intermediateStops: List<Place>? = listOf(),
    val legGeometry: EncodedPolylineBean? = EncodedPolylineBean(),
    @Transient val steps: List<WalkStep>? = listOf(),
    @Transient val alerts: LocalizedAlert? = LocalizedAlert(),
    val routeShortName: String? = "",
    val routeLongName: String? = "",
    @Transient val boardRule: String? = "",
    @Transient val alightRule: String? = "",
    @Transient val rentedBike: Boolean? = false,
    @Transient val callAndRide: Boolean? = false,
    @Transient val flexCallAndRideMaxStartTime: Long? = 0L,
    @Transient val flexCallAndRideMinEndTime: Long? = 0L,
    @Transient val flexDrtAdvanceBookMin: Long? = 0L,
    @Transient val flexDrtPickupMessage: String? = "",
    @Transient val flexDrtDropOffMessage: String? = "",
    @Transient val flexFlagStopPickupMessage: String? = "",
    @Transient val flexFlagStopDropOffMessage: String? = "",
    val transitLeg: Boolean? = false,
    val duration: Long? = 0L

) {
    object LegDiff : DiffUtil.ItemCallback<Leg>() {
        override fun areItemsTheSame(oldItem: Leg, newItem: Leg): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Leg, newItem: Leg): Boolean {
            return oldItem == newItem
        }
    }
}

data class WalkStep(
    @Transient val distance: Long? = 0,
    @Transient val relativeDirection: String? = "",
    @Transient val streetName: String? = "",
    @Transient val absoluteDirection: String? = "",
    @Transient val exit: String? = "",
    @Transient val stayOn: Boolean? = false,
    @Transient val area: Boolean? = false,
    @Transient val bogusName: Boolean? = false,
    @Transient val lon: Double? = 0.0,
    @Transient val lat: Double? = 0.0,
    @Transient val elevation: List<P2OfDouble>? = listOf(),
    @Transient val alerts: List<LocalizedAlert>? = listOf()
)

data class P2OfDouble(
    @Transient val second: String? = "",
    @Transient val first: String? = ""
)

data class LocalizedAlert(
    @Transient val alertHeaderText: String? = "",
    @Transient val alertDescriptionText: String? = "",
    @Transient val alertUrl: String? = "",
    @Transient val effectiveStartDate: Long? = 0L
)

enum class AbsoluteDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWES
}

enum class RelativeDirection {
    DEPART,
    HARD_LEFT,
    LEFT,
    SLIGHTLY_LEFT,
    CONTINUE,
    SLIGHTLY_RIGHT,
    RIGHT,
    HARD_RIGHT,
    CIRCLE_CLOCKWISE,
    CIRCLE_COUNTERCLOCKWISE,
    ELEVATOR,
    UTURN_LEFT,
    UTURN_RIGHT
}

data class FeedScopedId(
    val agencyId: String? = "",
    val id: String = ""
)

data class EncodedPolylineBean(
    val points: String? = "",
    val length: Int? = 0,
    val levels: String? = ""
)

data class PlannerError(
    val id: Int? = 0,
    val msg: String? = "",
    val message: String? = "",
    val missing: List<String>? = listOf(),
    val noPath: Boolean? = false
)

enum class Message {
    PLAN_OK,
    SYSTEM_ERROR,
    GRAPH_UNAVAILABLE,
    OUTSIDE_BOUNDS,
    PATH_NOT_FOUND,
    NO_TRANSIT_TIMES,
    REQUEST_TIMEOUT,
    BOGUS_PARAMETER,
    GEOCODE_FROM_NOT_FOUND,
    GEOCODE_TO_NOT_FOUND,
    GEOCODE_FROM_TO_NOT_FOUND,
    TOO_CLOSE,
    LOCATION_NOT_ACCESSIBLE,
    GEOCODE_FROM_AMBIGUOUS,
    GEOCODE_TO_AMBIGUOUS,
    GEOCODE_FROM_TO_AMBIGUOUS,
    UNDERSPECIFIED_TRIANGLE,
    TRIANGLE_NOT_AFFINE,
    TRIANGLE_OPTIMIZE_TYPE_NOT_SET,
    TRIANGLE_VALUES_NOT_SET
}