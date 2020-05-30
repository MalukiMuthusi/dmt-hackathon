package codes.malukimuthusi.hackathon.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codes.malukimuthusi.hackathon.repository.Repository
import codes.malukimuthusi.hackathon.webService.DebugOutPut
import codes.malukimuthusi.hackathon.webService.PlannerError
import codes.malukimuthusi.hackathon.webService.TripPlan
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.random.Random

class PlanTripsViewModel : ViewModel() {
    private lateinit var errorr: PlannerError
    private lateinit var debugOutPut: DebugOutPut
    private lateinit var plan: TripPlan


    private var _toastErrFetching = MutableLiveData<Int>()
    val toastErrFetching: LiveData<Int>
        get() = _toastErrFetching

    // list of coordinates to plot
    private var _pathPoints = MutableLiveData<List<LatLng>>()
    val pathPoints: LiveData<List<LatLng>>
        get() = _pathPoints

    private var _tripPlanError = MutableLiveData<PlannerError>()
    val tripPlanError: LiveData<PlannerError>
        get() = _tripPlanError

    fun planTrip(parameters: Map<String, String>?) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    val response = Repository.planTrip(parameters)
                    if (response.error == null) {
                        plan = response.plan!!

                        processTripResult(plan)
                    } else {
                        _tripPlanError.value = response.error
                        errorr = response.error
                        Timber.e(
                            "%s%s No path: %s; missing: %s",
                            errorr.msg,
                            errorr.message,
                            errorr.noPath,
                            errorr.missing
                        )
                        Timber.e("Request has an error")
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error Fetching Trip")
                    val randomNumber = Random.nextInt(10)
                    _toastErrFetching.value = randomNumber
                }
            }
        }
    }

    fun processTripResult(plan: TripPlan) {
        val firstWay = plan.itineraries!!.first()
        val legPoints = mutableListOf<LatLng>()
        firstWay.legs!!.forEach { leg ->
            legPoints.addAll(PolyUtil.decode(leg.legGeometry!!.points))
        }
        _pathPoints.value = legPoints

    }
}