package codes.malukimuthusi.hackathon.startPoint

import androidx.lifecycle.ViewModel
import codes.malukimuthusi.hackathon.webService.TripPlan
import com.google.android.gms.maps.model.LatLng

class SharedViewModel : ViewModel() {
    var startPoint: LatLng? = null
    var destination: LatLng? = null

    lateinit var tripPlan: TripPlan
}