package codes.malukimuthusi.hackathon.repository

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import codes.malukimuthusi.hackathon.dataModel.Fare
import codes.malukimuthusi.hackathon.dataModel.Route
import codes.malukimuthusi.hackathon.dataModel.Sacco
import codes.malukimuthusi.hackathon.webService.OpenTripPlannerService
import codes.malukimuthusi.hackathon.webService.Stop
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import kotlin.math.cos

interface Repo {
    fun getRoutes(): List<Route>
    fun getSaccos(): List<Sacco>
    fun getFare()
}

object Repository {

    // perform geocode
    suspend fun fetchAddress(latLng: LatLng, context: Context): String {
        val geocode = Geocoder(context)
        try {
            val addresses = withContext(Dispatchers.IO) {
                geocode.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1
                )
            }
            return if (addresses.size > 0) {
                val fetchedAdd = addresses.first()
                val straddr = StringBuilder()
                for (i in 0..fetchedAdd.maxAddressLineIndex) {
                    straddr.append(fetchedAdd.getAddressLine(i)).append(" ")
                }
                straddr.toString()
            } else {
                "Searching Current Address"
            }

        } catch (e: Exception) {
            Timber.e(e, "failed to get address")
            return "Searching Current Address"
        }
    }

    /*
    * Get all the routes.
    *
    * Routes are fetched from the remote database, and local cache.
    *
    * */
    fun getRoutes(childEventListener: ChildEventListener) {

        val routesPath = Firebase.database.reference.child("Routes")
        routesPath.addChildEventListener(childEventListener)
    }

    // get sacco's in the database
    fun getSaccos(saccoList: MutableLiveData<MutableMap<String, Sacco>>) {
        val saccosPath = Firebase.database.reference.child("saccos")
        val valueEventListener =
            FetchSaccoEventListener(
                saccoList
            )
        saccosPath.addValueEventListener(valueEventListener)
    }

    /*
    * Get the saccos of a given route.
    *
    * e.g when given id for ```Ruaka-Ruiru Road``` get all the sacco's that operate on this road
    *
    * */
    fun getListOfSacco(
        routeId: String,
        saccosInRouteEventListener: SaccosInRouteEventListener
    ) {
        val saccosInRouteRef =
            Firebase.database.reference.child("Routes").child(routeId).child("saccos")

        // check if the route has sacco's. If the route has no Saccos return null
        if (saccosInRouteRef.key != null) {
            saccosInRouteRef.addValueEventListener(saccosInRouteEventListener)
        }
    }

    /*
    * fetch a single sacco item from database.
    *
    * when given @param saccoId get sacco with that ID.
    *
    * */
    fun getSingleSacco(
        saccoID: String,
        eventListener: SingleSaccoValueEventListener
    ) {

        Firebase.database.reference.child("saccos").child(saccoID)
            .addValueEventListener(eventListener)

    }

    /*
    * Fetch current average fare of a given route
    * */
    fun getAverageFareOfRoute(route: Route) {
        // get the sacco's operating on this route
        if (route.saccos.isNullOrEmpty()) {
            Timber.d("%s: has no Saccos", route.name)
            return
        }
        val allSaccos = MutableLiveData<MutableMap<String, Sacco>>()
        getSaccos(allSaccos)

        var filteredMap = mutableListOf<Sacco>()

        allSaccos.value?.let {
//            filteredMap = (it.filter { it.key in route.saccos.keys }).values.toMutableList()
        }


        // get current fare from each sacco

        // observe the sacco's mutable live data list as sacco's are fetched from database
        val faresOfSacco = mutableListOf<Int>()

        filteredMap.forEach {
            try {
                val fare =
                    getCurrentFareFromSacco(
                        it.fare
                    )
                Timber.d("Fare for sacco: %s is %d", it.name, fare)
                faresOfSacco.add(fare)
            } catch (e: Exception) {
                Timber.e(e, "Current fare not set for: %s", it.name)
                filteredMap.remove(it)
            }
        }
        // get average of those fares.
        val average = faresOfSacco.average()
        Timber.d("Everage fare for route: %s is %f", route.name, average)
    }


    // fetch current fare from a sacco.
    fun getCurrentFareFromSacco(fare: Fare?): Int {
        val tz = TimeZone.getTimeZone("Africa/Nairobi")
        val calendarInstance = Calendar.getInstance(tz)
        val hourOfDay = calendarInstance.get(Calendar.HOUR_OF_DAY)
        val minutes = calendarInstance.get(Calendar.MINUTE)
        val hourAndMinutes = hourOfDay * 60 + minutes

        return when (hourAndMinutes) {
            in 300..360 -> fare?.five_six
                ?: throw Exception("Fare Not set for this period!!")
            in 360..420 -> fare?.six_seven
                ?: throw Exception("Fare Not set for this period!!")
            in 420..480 -> fare?.seven_eight
                ?: throw Exception("Fare Not set for this period!!")
            in 480..540 -> fare?.eight_nine
                ?: throw Exception("Fare Not set for this period!!")
            in 540..600 -> fare?.nine_ten
                ?: throw Exception("Fare Not set for this period!!")
            in 600..660 -> fare?.ten_eleven
                ?: throw Exception("Fare Not set for this period!!")
            in 660..720 -> fare?.eleven_twelve
                ?: throw Exception("Fare Not set for this period!!")
            in 720..780 -> fare?.twelve_thirteen
                ?: throw Exception("Fare Not set for this period!!")
            in 780..840 -> fare?.thirteen_fourteen
                ?: throw Exception("Fare Not set for this period!!")
            in 840..900 -> fare?.fourteen_fiveteen
                ?: throw Exception("Fare Not set for this period!!")
            in 900..960 -> fare?.fiveteen_sixteen
                ?: throw Exception("Fare Not set for this period!!")
            in 960..1020 -> fare?.sixteen_seventeen
                ?: throw Exception("Fare Not set for this period!!")
            in 1020..1080 -> fare?.seventeen_eighteen
                ?: throw Exception("Fare Not set for this period!!")
            in 1080..1140 -> fare?.eighteen_nineteen
                ?: throw Exception("Fare Not set for this period!!")
            in 1140..1200 -> fare?.nineteen_twenty
                ?: throw Exception("Fare Not set for this period!!")
            in 1200..1260 -> fare?.twenty_twentyOne
                ?: throw Exception("Fare Not set for this period!!")
            in 1260..1320 -> fare?.twentyone_twentytwo
                ?: throw Exception("Fare Not set for this period!!")
            in 1320..1380 -> fare?.twentytwo_twentythree
                ?: throw Exception("Fare Not set for this period!!")
            else -> 0
        }
    }

    // return list of stops near given point
    suspend fun nearByStopPoints(lat: Double, log: Double): List<Stop>? {

        val maxLat: Double
        val maxLon: Double
        val minLat: Double
        val minLon: Double
        val radius = 200.0

        val radiusEarth = 6378000.0 //meters

        val offset = 400.0
        val negativeOffset = -400.0

        val mapValues = mutableMapOf<String, Double>()

        // Return stop points within a radius of 200 meters
        maxLat = lat + (offset / radiusEarth) * (180.0 / Math.PI)
        minLat = lat + (negativeOffset / radiusEarth) * (180.0 / Math.PI)

        maxLon =
            log + (offset / radiusEarth) * (180.0 / Math.PI) / cos(lat * Math.PI / 180.0)
        minLon =
            log + (negativeOffset / radiusEarth) * (180.0 / Math.PI) / cos(lat * Math.PI / 180.0)

        // reference https://stackoverflow.com/questions/7477003/calculating-new-longitude-latitude-from-old-n-meters

        return withContext(Dispatchers.IO) {
            OpenTripPlannerService.otpServices.getListOfStops(
                lat,
                log,
                maxLat,
                minLat,
                maxLon,
                minLon,
                radius
            )

        }
    }

    // get a single stop point
    suspend fun getStop(stopID: String): Stop = OpenTripPlannerService.otpServices.givenStop(stopID)

    // get all the routes
    suspend fun allRoutes() = OpenTripPlannerService.otpServices.getAllRoutes()

    // plan trip
    suspend fun planTrip(values: Map<String, String>?) = withContext(Dispatchers.IO) {
        OpenTripPlannerService.otpServices.planTrip(values)
    }
}

// fetch a single sacco item from the database
class SingleSaccoValueEventListener(
    val listOfSacco: MutableList<Sacco>,
    val saccoList: MutableLiveData<MutableList<Sacco>>
) : ValueEventListener {

    override fun onCancelled(p0: DatabaseError) {
        Timber.e(p0.toException())

    }

    override fun onDataChange(p0: DataSnapshot) {
        p0.getValue<Sacco>()?.let {
            listOfSacco.add(it)
            saccoList.value = listOfSacco
        }
    }
}

// fetch all the saccos in the given route
class SaccosInRouteEventListener(
    val saccoList: MutableLiveData<MutableList<Sacco>>
) : ValueEventListener {

    val listOfSacco = mutableListOf<Sacco>()


    override fun onCancelled(p0: DatabaseError) {
        Timber.e(p0.toException())

    }

    override fun onDataChange(p0: DataSnapshot) {


    }
}

// event listener to fetch all the sacco's in database
class FetchSaccoEventListener(private val saccoList: MutableLiveData<MutableMap<String, Sacco>>) :
    ValueEventListener {
    private val allSacco = mutableMapOf<String, Sacco>()
    override fun onCancelled(p0: DatabaseError) {
        Timber.e(p0.toException())
    }

    override fun onDataChange(p0: DataSnapshot) {
        for (i in p0.children) {
            val sacco = i.getValue<Sacco>()
            val key = i.key
            sacco?.let { addSacco ->
                key?.let {
                    addSacco.key = it
                    allSacco[it] = addSacco
                    saccoList.value = allSacco
                }
            }
        }
    }
}

