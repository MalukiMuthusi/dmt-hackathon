package codes.malukimuthusi.hackathon.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import codes.malukimuthusi.hackathon.data.Sacco
import codes.malukimuthusi.hackathon.data.SaccosInRouteEventListener

class SaccoFragmentViewModel : ViewModel() {

    // mutable live data for list of saccos of a given route
    private val _saccoList = MutableLiveData<MutableList<Sacco>>()

    // immutable live data of a list of saccos.
    val saccoList: LiveData<MutableList<Sacco>>
        get() = _saccoList

    // listener for sacco's in a given route.
    val eventListenerForSacco = SaccosInRouteEventListener(_saccoList)

}