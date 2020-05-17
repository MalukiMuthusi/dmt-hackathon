package codes.malukimuthusi.hackathon.data

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("fare_value")
fun fareAdapterr(text: TextView, fareAmout: Int?) {
    text.text = fareAmout?.toString() ?: (0).toString()
}

@BindingAdapter("transit_start")
fun startAdapter(text: TextView, start: String?) {
    text.text = start ?: "UnKnown"
}