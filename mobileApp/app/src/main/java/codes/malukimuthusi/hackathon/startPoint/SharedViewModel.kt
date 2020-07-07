package codes.malukimuthusi.hackathon.startPoint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codes.malukimuthusi.hackathon.dataModel.Sacco
import codes.malukimuthusi.hackathon.webService.Itinerary
import codes.malukimuthusi.hackathon.webService.Leg
import codes.malukimuthusi.hackathon.webService.TripPlan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.random.Random

class SharedViewModel : ViewModel() {
    var destinationString: String = "unknown"
    var startPointString: String = "unknown"

    var startPoint: LatLng? = null
    var destination: LatLng? = null
    lateinit var leg: Leg
    private val db = Firebase.database.reference
    private val saccosRef = db.child("saccos")
    private val routesRef = db.child("Routes")
    private val saccoList = mutableListOf<Sacco>()
    private var _saccoListMLD = MutableLiveData<List<Sacco>>()

    val saccoListLD: LiveData<List<Sacco>>
        get() = _saccoListMLD

    lateinit var tripPlan: TripPlan

    lateinit var selectedItinerary: Itinerary

    val transitLegs: List<Leg>
        get() = selectedItinerary.legs!!.filter {
            it.transitLeg!!
        }

    // update UI
    private var _updateUi = MutableLiveData<Int>()
    val updateUi: LiveData<Int>
        get() = _updateUi

    fun fetchSaccos(routeId: String) {
        // fetch the Sacco's ID from the Route.
        routesRef.child(routeId).child("saccos").addValueEventListener(SaccosInRouteVEL())
    }

    inner class SaccosInRouteVEL : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Timber.e(p0.toException())
        }

        override fun onDataChange(p0: DataSnapshot) {
            // fetch sacco from its id
            for (saccosEntry in p0.children) {
                val key = saccosEntry.key
                if (key != null) {
                    saccosRef.child(key).addValueEventListener(SaccoVEL())
                } else {
                    Timber.e("No error")
                }
            }
        }
    }

    inner class SaccoVEL : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Timber.e(p0.toException(), "Request Cancelled")
        }

        // fetch value of each sacco
        override fun onDataChange(p0: DataSnapshot) {
            val sacco = p0.getValue<Sacco>()
            if (sacco != null) {
                if (!saccoList.contains(sacco)) {
                    saccoList.add(sacco)
                    _saccoListMLD.postValue(saccoList)
                    // notify to update UI.
                    _updateUi.value = Random.nextInt(0, 1000)
                }
            } else {
                Timber.e("Null sacco returned")
            }
        }
    }

    // geocode
    fun geocode() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

            }
        }

    }

}