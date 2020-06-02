package codes.malukimuthusi.hackathon.startPoint

import androidx.lifecycle.ViewModel
import codes.malukimuthusi.hackathon.webService.Itinerary
import codes.malukimuthusi.hackathon.webService.Leg
import codes.malukimuthusi.hackathon.webService.TripPlan
import com.google.android.gms.maps.model.LatLng

class SharedViewModel : ViewModel() {
    var startPoint: LatLng? = null
    var destination: LatLng? = null

    lateinit var tripPlan: TripPlan

    lateinit var selectedItinerary: Itinerary

    val transitLegs: List<Leg>
        get() = selectedItinerary.legs!!.filter {
            it.transitLeg!!
        }

    lateinit var leg: Leg
}