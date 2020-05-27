package codes.malukimuthusi.hackathon.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import codes.malukimuthusi.hackathon.data.Repository
import codes.malukimuthusi.hackathon.webService.Stop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsActivityViewModel : ViewModel() {
    var stopId = ""
    val stop: LiveData<Stop> = liveData(Dispatchers.IO) {
        val retriedStop =
            Repository.getStop(stopId)
        emit(retriedStop)
    }

    var markerLat: Double = 0.0
    var markerLon: Double = 0.0
    val searchingStops = false
    var returnedList: List<Stop>? = listOf()

    val nearByStops: LiveData<List<Stop>?> = liveData(Dispatchers.IO) {
        returnedList = Repository.nearByStopPoints(markerLat, markerLon)
        emit(returnedList)
    }
    val viewModel = viewModelScope.launch(Dispatchers.IO) {
        val e = Repository.nearByStopPoints(markerLat, markerLon)

    }
}