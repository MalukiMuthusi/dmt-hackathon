package codes.malukimuthusi.hackathon.data

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("fare")
fun fareAdapter(text: TextView, fareAmout: Int) {
    text.text = fareAmout.toString()
}