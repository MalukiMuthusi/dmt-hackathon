package codes.malukimuthusi.hackathon

import android.util.Log
import androidx.lifecycle.ViewModel
import codes.malukimuthusi.hackathon.data.Route
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class HomeFragmentViewModel : ViewModel() {
    val myDbRef = Firebase.database.getReference("Routes")

    //    var _routes = MutableLiveData<MutableList<Route>>()
    val normalList = mutableListOf<Route>()
//    val routes: LiveData<MutableList<Route>>
//        get() = _routes

    val routesListener = object : ChildEventListener {
        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val routeAdded = p0.getValue<Route>()


            routeAdded?.let {
                if (it !in normalList) normalList.add(it)

            }

            Log.e("maluki", p1 ?: "nothing")
        }

        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }


        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            val routeChanged = p0.getValue<Route>()
            val index = normalList.indexOf(routeChanged)
            if (routeChanged != null) {
                normalList[index] = routeChanged
            }

        }

        override fun onChildRemoved(p0: DataSnapshot) {
            normalList.remove(p0.getValue<Route>())
        }

        fun replaceChanged(route: Route, changeRoute: Route): Route {
            if (route == changeRoute)
                return changeRoute
            return route
        }
    }
}