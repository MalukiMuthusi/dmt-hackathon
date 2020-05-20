package codes.malukimuthusi.hackathon.data

import android.widget.TextView
import androidx.databinding.BindingAdapter
import codes.malukimuthusi.hackathon.R

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
        val fare = Repository.getCurrentFareFromSacco(sacco)
        text.text = fare.toString()
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


