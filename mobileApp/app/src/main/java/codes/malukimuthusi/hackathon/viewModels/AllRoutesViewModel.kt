package codes.malukimuthusi.hackathon.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codes.malukimuthusi.hackathon.repository.Repository
import codes.malukimuthusi.hackathon.webService.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AllRoutesViewModel : ViewModel() {
    private var _routesMLD = MutableLiveData<List<Route>>()
    private var routesList = mutableListOf<Route>()
    val routesLD: LiveData<List<Route>>
        get() = _routesMLD
    var loadingState = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<Boolean>()

    fun fetchAllRoutes() {
        loadingState.value = true
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    routesList = Repository.allRoutes() as MutableList<Route>
                    loadingState.value = false
                    _routesMLD.value = routesList
                } catch (e: Exception) {
                    Timber.e(e, "Error When Fetching All routes")
                    errorMessage.value = true
                    loadingState.value = false
                }
            }
        }
    }
}