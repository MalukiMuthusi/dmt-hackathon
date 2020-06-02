package codes.malukimuthusi.hackathon.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import codes.malukimuthusi.hackathon.webService.EncodedPolylineBean
import codes.malukimuthusi.hackathon.webService.Itinerary
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

class DirectionsFragmentViewModel : ViewModel() {
    // this value is supposed to be passed as a parameter to this class
    lateinit var itinerary: Itinerary

    //
    var trasitLinePoints = MutableLiveData<List<LatLng>>()

    fun extractLegs() {
        for (leg in itinerary.legs!!) {
            if (leg.transitLeg!!) {
                plotTransitLine(leg.legGeometry!!)
            } else {
                plotWalkPath(leg.legGeometry!!)
            }
        }
    }

    fun plotTransitLine(geometry: EncodedPolylineBean) {
        val points = PolyUtil.decode(geometry.points)
    }

    fun plotWalkPath(geometry: EncodedPolylineBean) {
        val points = PolyUtil.decode(geometry.points)
    }
}