package codes.malukimuthusi.hackathon.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import codes.malukimuthusi.hackathon.dataModel.Route

class RouteMapsViewModel : ViewModel() {

    var routesLive = MutableLiveData<List<Route>?>()
}