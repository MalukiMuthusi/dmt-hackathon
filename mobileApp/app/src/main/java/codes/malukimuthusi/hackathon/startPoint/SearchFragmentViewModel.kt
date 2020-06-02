package codes.malukimuthusi.hackathon.startPoint

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codes.malukimuthusi.hackathon.repository.Repository
import codes.malukimuthusi.hackathon.webService.PlannerError
import codes.malukimuthusi.hackathon.webService.TripPlan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SearchFragmentViewModel : ViewModel() {
    private lateinit var exception: java.lang.Exception

    var exc = MutableLiveData<java.lang.Exception>()
    var planTripError = MutableLiveData<PlannerError>()
    var tripPlan = MutableLiveData<TripPlan>()
    var navigate = MutableLiveData<Boolean>()

    fun searchRoutes(parameters: Map<String, String>?) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    val response = Repository.planTrip(parameters)
                    if (response.error == null) {
                        tripPlan.value = response.plan
                        navigate.value = true

                    } else {
                        planTripError.value = response.error

                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error Fetching Trip")
                    exc.value = e
                }

            }
        }
    }
}