package codes.malukimuthusi.hackathon.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import codes.malukimuthusi.hackathon.dataModel.Sacco
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class SaccosInRouteViewModel : ViewModel() {
    private val db = Firebase.database.reference
    private val saccosRef = db.child("saccos")
    private val routesRef = db.child("Routes")

    private val saccoList = mutableListOf<Sacco>()
    private var _saccoListMLD = MutableLiveData<List<Sacco>>()
    val saccoListLD: LiveData<List<Sacco>>
        get() = _saccoListMLD

    fun fetchSaccos(routeId: String) {
        routesRef.child(routeId).child("saccos").addValueEventListener(SaccosInRouteVEL())
    }

    private inner class SaccosInRouteVEL : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Timber.e(p0.toException())
        }

        override fun onDataChange(p0: DataSnapshot) {
            for (saccosEntry in p0.children) {
                val key = saccosEntry.key
                if (key != null) {
                    saccosRef.child(key).addValueEventListener(SaccoVEL())
                } else {
                    Timber.e("No error")
                }
            }
        }
    }

    private inner class SaccoVEL : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Timber.e(p0.toException(), "Request Cancelled")
        }

        // fetch value of each sacco
        override fun onDataChange(p0: DataSnapshot) {
            val sacco = p0.getValue<Sacco>()
            if (sacco != null) {
                if (!saccoList.contains(sacco)) {
                    saccoList.add(sacco)
                    _saccoListMLD.postValue(saccoList)
                }
            } else {
                Timber.e("Null sacco returned")
            }
        }
    }
}