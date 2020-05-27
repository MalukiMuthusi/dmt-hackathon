package codes.malukimuthusi.hackathon.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import codes.malukimuthusi.hackathon.repository.Repository
import codes.malukimuthusi.hackathon.webService.Route
import kotlinx.coroutines.Dispatchers

class RouteMapsViewModel : ViewModel() {
    private lateinit var _routesLive: LiveData<List<Route>?>

    fun fetchAllRoutes() {
        _routesLive = liveData(Dispatchers.IO) {
            val routes = Repository.allRoutes()
            emit(routes)
        }
    }


    val routesLiveData: LiveData<List<Route>?>
        get() = _routesLive
}