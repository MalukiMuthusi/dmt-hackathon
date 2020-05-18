package codes.malukimuthusi.hackathon.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue

class RouteListener(
    val _updateUI: MutableLiveData<MutableList<Route>>,
    val allRoutes: MutableList<Route>
) : ChildEventListener {


    override fun onCancelled(p0: DatabaseError) {
    }

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val newRoute = p0.getValue<Route>()
        newRoute?.let {
            it.key = p0.key!!
            if (it !in allRoutes) allRoutes.add(it)
            Log.e("maluki_list", it.toString())
            _updateUI.value = allRoutes
        }
    }

    override fun onChildRemoved(p0: DataSnapshot) {

    }


}