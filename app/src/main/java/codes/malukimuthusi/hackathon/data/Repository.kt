package codes.malukimuthusi.hackathon.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import codes.malukimuthusi.hackathon.data.Repository.getSingleSacco
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

interface Repo {
    fun getRoutes(): List<Route>
    fun getSaccos(): List<Sacco>
    fun getFare()
}

object Repository {
    private val dbRef = Firebase.database.reference

    /*
    * Get all the routes.
    *
    * Routes are fetched from the remote database, and local cache.
    *
    * */
    fun getRoutes(childEventListener: ChildEventListener) {

        val routesPath = dbRef.child("Routes")
        routesPath.addChildEventListener(childEventListener)
    }

    /*
    * Get the saccos of a given route.
    *
    * e.g when given id for ```Ruaka-Ruiru Road``` get all the sacco's that operate on this road
    *
    * */
    fun getListOfSacco(
        routeId: String,
        saccosInRouteEventListener: SaccosInRouteEventListener
    ) {
        val saccosInRouteRef = dbRef.child("Routes").child(routeId).child("saccos")

        // check if the route has sacco's. If the route has no Saccos return null
        if (saccosInRouteRef.key != null) {
            saccosInRouteRef.addValueEventListener(saccosInRouteEventListener)
        }
    }

    /*
    * fetch a single sacco item from database.
    *
    * when given @param saccoId get sacco with that ID.
    *
    * */
    fun getSingleSacco(
        saccoID: String,
        eventListener: SingleSaccoValueEventListener
    ) {

        dbRef.child("saccos").child(saccoID).addValueEventListener(eventListener)

    }

}

class SingleSaccoValueEventListener(
    val listOfSacco: MutableList<Sacco>,
    val saccoList: MutableLiveData<MutableList<Sacco>>
) : ValueEventListener {

    override fun onCancelled(p0: DatabaseError) {
        Log.e("SingleSaccoValueEL", p0.message)

    }

    override fun onDataChange(p0: DataSnapshot) {
        p0.getValue<Sacco>()?.let {
            listOfSacco.add(it)
            saccoList.value = listOfSacco
        }
    }
}

class SaccosInRouteEventListener(
    val saccoList: MutableLiveData<MutableList<Sacco>>
) : ValueEventListener {

    val listOfSacco = mutableListOf<Sacco>()

    override fun onCancelled(p0: DatabaseError) {
        Log.e("SaccosInRouteEventL", p0.message)

    }

    override fun onDataChange(p0: DataSnapshot) {

        for (saccoId in p0.children) {

            //check if sacco exits, then fetch the sacco
            saccoId.key?.let {
                val singleSaccoValueListener = SingleSaccoValueEventListener(listOfSacco, saccoList)
                getSingleSacco(it, singleSaccoValueListener)
            }
        }

    }
}

