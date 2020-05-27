package codes.malukimuthusi.hackathon.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import codes.malukimuthusi.hackathon.data.Route
import codes.malukimuthusi.hackathon.data.RouteListener

class HomeFragmentViewModel : ViewModel() {

    private val _updateUI = MutableLiveData<MutableList<Route>>()
    val allRoutes = mutableListOf<Route>()

    val updateUI: LiveData<MutableList<Route>>
        get() = _updateUI

    val listenerObject =
        RouteListener(_updateUI, allRoutes)


}