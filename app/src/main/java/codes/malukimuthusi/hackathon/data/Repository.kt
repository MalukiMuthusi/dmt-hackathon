package codes.malukimuthusi.hackathon.data

import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import codes.malukimuthusi.hackathon.data.Repository.getSingleSacco
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.util.*

interface Repo {
    fun getRoutes(): List<Route>
    fun getSaccos(): List<Sacco>
    fun getFare()
}

object Repository {
    private val dbRef = Firebase.database.reference

    /*
    * Get all the routes.
    *
    * Routes are fetched from the remote database, and local cache.
    *
    * */
    fun getRoutes(childEventListener: ChildEventListener) {

        val routesPath = dbRef.child("Routes")
        routesPath.addChildEventListener(childEventListener)
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
        val saccosInRouteRef = dbRef.child("Routes").child(routeId).child("saccos")

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

        dbRef.child("saccos").child(saccoID).addValueEventListener(eventListener)

    }

    /*
* Fetch current average fare of a given route
* */
    fun getAverageFareOfRoute(lifecycleOwner: LifecycleOwner, routeId: String, textView: TextView) {
        // get the sacco's operating on this route
        val saccos = MutableLiveData<MutableList<Sacco>>()
        val eventListenerForSaccosInRoute = SaccosInRouteEventListener(saccos)
        getListOfSacco(routeId, eventListenerForSaccosInRoute)

        // get current fare from each sacco

        // observe the sacco's mutable live data list as sacco's are fetched from database
        val faresOfSacco = mutableListOf<Int>()
        saccos.observe(lifecycleOwner, androidx.lifecycle.Observer { saccosList ->
            saccosList.forEach {
                try {
                    val fare = getCurrentFareFromSacco(it)
                    faresOfSacco.add(fare)
                } catch (e: Exception) {
                    Timber.e(e, "Current fare not set for: %s", it.name)
                    saccosList.remove(it)
                }
            }
            // get average of those fares.
            val average = faresOfSacco.average()
            textView.text = average.toInt().toString()
        })
    }

    // fetch current fare from a sacco.
    fun getCurrentFareFromSacco(sacco: Sacco): Int {
        val tz = TimeZone.getTimeZone("Africa/Nairobi")
        val calendarInstance = Calendar.getInstance(tz)
        val hourOfDay = calendarInstance.get(Calendar.HOUR_OF_DAY)
        val minutes = calendarInstance.get(Calendar.MINUTE)

        return when (hourOfDay * minutes) {
            in 300..360 -> sacco.fare?.fiveToSix
                ?: throw Exception("Fare Not set for this period!!")
            in 360..420 -> sacco.fare?.sixToSeven
                ?: throw Exception("Fare Not set for this period!!")
            in 420..480 -> sacco.fare?.sevenToEight
                ?: throw Exception("Fare Not set for this period!!")
            in 480..540 -> sacco.fare?.eightToNine
                ?: throw Exception("Fare Not set for this period!!")
            in 540..600 -> sacco.fare?.nineToTen
                ?: throw Exception("Fare Not set for this period!!")
            in 600..660 -> sacco.fare?.tenToEleven
                ?: throw Exception("Fare Not set for this period!!")
            in 660..720 -> sacco.fare?.elevenToTwelve
                ?: throw Exception("Fare Not set for this period!!")
            in 720..780 -> sacco.fare?.twelveToThirteen
                ?: throw Exception("Fare Not set for this period!!")
            in 780..840 -> sacco.fare?.thirteenToFourteen
                ?: throw Exception("Fare Not set for this period!!")
            in 840..900 -> sacco.fare?.fourteenToFiveteen
                ?: throw Exception("Fare Not set for this period!!")
            in 900..960 -> sacco.fare?.fiveteenToSixteen
                ?: throw Exception("Fare Not set for this period!!")
            in 960..1020 -> sacco.fare?.sixteenToSeventeen
                ?: throw Exception("Fare Not set for this period!!")
            in 1020..1080 -> sacco.fare?.seventeenToEighteen
                ?: throw Exception("Fare Not set for this period!!")
            in 1080..1140 -> sacco.fare?.eighteenToNineteen
                ?: throw Exception("Fare Not set for this period!!")
            in 1140..1200 -> sacco.fare?.nineteenToTwenty
                ?: throw Exception("Fare Not set for this period!!")
            in 1200..1260 -> sacco.fare?.twentyTotwentyone
                ?: throw Exception("Fare Not set for this period!!")
            in 1260..1320 -> sacco.fare?.twentyoneToTwentytwo
                ?: throw Exception("Fare Not set for this period!!")
            in 1320..1380 -> sacco.fare?.twentytwoToTwentythree
                ?: throw Exception("Fare Not set for this period!!")
            else -> 0
        }

    }

}

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

class SaccosInRouteEventListener(
    val saccoList: MutableLiveData<MutableList<Sacco>>
) : ValueEventListener {

    val listOfSacco = mutableListOf<Sacco>()

    override fun onCancelled(p0: DatabaseError) {
        Timber.e(p0.toException())

    }

    override fun onDataChange(p0: DataSnapshot) {

        for (saccoId in p0.children) {

            //check if sacco exits, then fetch the sacco
            saccoId.key?.let {
                val singleSaccoValueListener = SingleSaccoValueEventListener(listOfSacco, saccoList)
                getSingleSacco(it, singleSaccoValueListener)
            }
        }

    }
}

