package codes.malukimuthusi.hackathon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import codes.malukimuthusi.hackathon.data.AvarageFare

class HomeViewModel : ViewModel() {

    private val _avarageFareLive = MutableLiveData<Boolean>()

    val avarageFaree: LiveData<Boolean>
        get() = _avarageFareLive

    fun onFareChartClicked() {
        _avarageFareLive.value = true
    }

    fun doneNavigate() {
        _avarageFareLive.value = null
    }

    lateinit var sharedAvarageFare: AvarageFare
}