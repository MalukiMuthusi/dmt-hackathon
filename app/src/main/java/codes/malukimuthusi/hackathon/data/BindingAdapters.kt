package codes.malukimuthusi.hackathon.data

import android.widget.TextView
import androidx.databinding.BindingAdapter
import codes.malukimuthusi.hackathon.R
import java.util.*
import java.util.Calendar.HOUR_OF_DAY

@BindingAdapter("fare_value")
fun fareAdapterr(text: TextView, fareAmout: Int?) {
    text.text = fareAmout?.toString() ?: (0).toString()
}

@BindingAdapter("transit_start")
fun startAdapter(text: TextView, start: String?) {
    text.text = start ?: "UnKnown"
}

@BindingAdapter("transit_fare")
fun currentFare(text: TextView, fare: Fare?) {
    text.text = "Unknown"
    fare?.let {
        val tz = TimeZone.getTimeZone("Africa/Nairobi")
        val currentTime = Calendar.getInstance(tz).get(HOUR_OF_DAY)
        text.text = (when (currentTime) {
            in 5..6 -> fare.fiveToSix
            in 6..7 -> fare.sixToSeven
            in 7..8 -> fare.sevenToEight
            in 9..10 -> fare.eightToNine
            in 10..11 -> fare.nineToTen
            in 11..12 -> fare.tenToEleven
            in 12..13 -> fare.elevenToTwelve
            in 13..14 -> fare.twelveToThirteen
            in 14..15 -> fare.thirteenToFourteen
            in 15..16 -> fare.fourteenToFiveteen
            in 16..17 -> fare.fiveteenToSixteen
            in 17..18 -> fare.sixteenToSeventeen
            in 18..19 -> fare.seventeenToEighteen
            in 19..20 -> fare.eighteenToNineteen
            in 20..21 -> fare.nineteenToTwenty
            in 21..22 -> fare.twentyTotwentyone
            in 22..23 -> fare.twentyoneToTwentytwo
            else -> 0
        }).toString()
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