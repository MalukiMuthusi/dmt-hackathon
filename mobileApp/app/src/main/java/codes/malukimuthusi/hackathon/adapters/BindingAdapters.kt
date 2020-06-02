package codes.malukimuthusi.hackathon.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import codes.malukimuthusi.hackathon.R
import codes.malukimuthusi.hackathon.dataModel.Fare
import codes.malukimuthusi.hackathon.dataModel.Sacco
import codes.malukimuthusi.hackathon.repository.Repository
import codes.malukimuthusi.hackathon.webService.Route
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import timber.log.Timber

@BindingAdapter("tripDuration")
fun tripDuration(text: TextView, seconds: Long) {
    val minutes = seconds / 1000
    when (minutes) {
        in 0..60 -> {
            text.text = "$minutes mins"
        }
        in 60..(60 * 24) -> {
            val hours = minutes / 60
            val min = minutes % 60
            text.text = "$hours Hrs: $min mns"
        }
        else -> text.text = text.context.getString(R.string.duration_unknow)
    }


}

@BindingAdapter("fare_value")
fun fareAdapterr(text: TextView, fareAmout: Int?) {
    text.text = fareAmout?.toString() ?: (0).toString()
}

@BindingAdapter("transit_start")
fun startAdapter(text: TextView, start: String?) {
    text.text = start ?: "UnKnown"
}

@BindingAdapter("transit_fare")
fun currentFare(text: TextView, sacco: Sacco) {
    try {
        val fare = Repository.getCurrentFareFromSacco(sacco.fare)
        text.text = text.context.getString(R.string.current_fare, fare)
    } catch (e: Exception) {
        text.text = text.context.getString(R.string.fare_unknown)
    }

}

@BindingAdapter("routeStart")
fun routeStartFormatedUI(text: TextView, start: String) {
    text.text = text.context.getString(R.string.start_station, start)
}

@BindingAdapter("routeName")
fun routeNameRouteOverview(text: TextView, name: String) {
    text.text = text.context.getString(R.string.route_name, name)
}

@BindingAdapter("routeEnd")
fun endOfRoute(text: TextView, end: String) {
    text.text = text.context.getString(R.string.end_station, end)
}

@BindingAdapter("averageRouteFare")
fun averageFare(text: TextView, fare: Int?) {
    text.text = text.context.getString(R.string.average_fare_now, fare ?: 0)
}

fun fareForRoute(route: Route): Int {
    var fare = 0
    val routeId = route.id!!.split(":").last()
    // fetch saccos on this route then calculate fare from them
    val dbRef = Firebase.database.getReference("Routes").child(routeId).child("saccos")
    dbRef.addValueEventListener(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Timber.e(p0.toException(), "Error fetching Sacco")
        }

        override fun onDataChange(p0: DataSnapshot) {
            val saccoKeys = p0.getValue<Map<String, Boolean>>()
            fare = fetchFares(saccoKeys)
        }
    })
    return fare
}

fun fetchFares(saccoKeys: Map<String, Boolean>?): Int {
    val fareList = mutableListOf<Int>()

    class SaccoFare : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Timber.e(p0.toException())
        }

        override fun onDataChange(p0: DataSnapshot) {
            val fareObject = p0.getValue<Fare>()
            val fare = Repository.getCurrentFareFromSacco(fareObject)
            fareList.add(fare)
        }
    }
    if (!saccoKeys.isNullOrEmpty()) {
        val dbRef = Firebase.database.getReference("saccos")
        saccoKeys.keys.forEach {
            dbRef.child(it).child("fare").addValueEventListener(SaccoFare())
        }
        return fareList.average().toInt()
    } else {
        Timber.e("Saccos are null")
        return 0
    }

}

@BindingAdapter("favoriteIcon")
fun icon(imageView: ImageView, iconUrl: Int) {
    imageView.setImageResource(iconUrl)
}



