package codes.malukimuthusi.hackathon.adapters

import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.malukimuthusi.hackathon.R
import codes.malukimuthusi.hackathon.databinding.SearchResultListBinding
import codes.malukimuthusi.hackathon.webService.Itinerary
import codes.malukimuthusi.hackathon.webService.Leg

class SearchResultsAdapter(private val itineraryClickListener: ItineraryClickListener) :
    ListAdapter<Itinerary, SearchResultAdapterViewHolder>(ItineraryDIFF) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultAdapterViewHolder {
        return SearchResultAdapterViewHolder.Instance(parent, itineraryClickListener)
    }

    override fun onBindViewHolder(holder: SearchResultAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SearchResultAdapterViewHolder private constructor(
    private val binding: SearchResultListBinding,
    private val itineraryClickListener: ItineraryClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    private fun dp(dp: Float): Int {
        val end = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            binding.root.context.resources.displayMetrics
        ).toInt()
        return end
    }


    fun bind(itinerary: Itinerary) {

        binding.itinerary = itinerary
        binding.clickListener = itineraryClickListener
        for (leg in itinerary.legs!!) {
            // first Leg
            if (leg == itinerary.legs.first()) {
                if (leg.transitLeg!!) {
                    firstTransit(true, leg)
                } else {
                    firstTransit(false, leg)
                }
            } else
                if (leg == itinerary.legs.last()) {
                    if (leg.transitLeg!!) {
                        firstTransit(true, leg)
                    } else {
                        firstTransit(false, leg)
                    }
                } else {
                    if (leg.transitLeg!!) {
                        middLeg(true, leg)
                    } else {
                        middLeg(false, leg)
                    }
                }

        }
    }

    // intermediate legs
    private fun middLeg(transitType: Boolean, leg: Leg) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.marginStart = dp(4f)
        params.marginEnd = dp(4f)
        val expandMore = ImageView(binding.root.context)
        expandMore.setImageResource(R.drawable.ic_expand_more_black_24dp)
        expandMore.animate().rotation(-90f)
        binding.legs.addView(expandMore, binding.legs.childCount)
        if (transitType) {
            val image = ImageView(binding.root.context)
            image.setImageResource(R.drawable.bus)
            image.setPadding(dp(4f), dp(4f), dp(0f), dp(4f))
            image.layoutParams = params
            binding.legs.addView(image, binding.legs.childCount)

            val textView = TextView(binding.root.context)
            textView.text = leg.routeShortName
            textView.setTypeface(textView.typeface, Typeface.BOLD)
            textView.setPadding(dp(1f))
            binding.legs.addView(textView, binding.legs.childCount)

        } else {
            val imageView = ImageView(binding.root.context)
            imageView.setImageResource(R.drawable.walk)
            imageView.setPadding(dp(4f))
            imageView.layoutParams = params
            binding.legs.addView(imageView, binding.legs.childCount)

            val textView = TextView(binding.root.context)
            val duration = leg.duration!! / 60
            textView.text = "${duration} min"
            binding.legs.addView(textView, binding.legs.childCount)

        }
        val expandMore2 = ImageView(binding.root.context)
        expandMore2.setImageResource(R.drawable.ic_expand_more_black_24dp)
        expandMore2.animate().rotation(-90f)
        binding.legs.addView(expandMore2, binding.legs.childCount)
    }

    // create image of transit and attach
    private fun firstTransit(transitType: Boolean, leg: Leg) {
        val params = ViewGroup.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        if (transitType) {
            val image = ImageView(binding.root.context)
            image.setImageResource(R.drawable.bus)
            image.layoutParams = params
            binding.legs.addView(image, binding.legs.childCount)

            val textView = TextView(binding.root.context)
            textView.text = leg.routeShortName
            textView.setTypeface(textView.typeface, Typeface.BOLD)
            textView.setPadding(dp(1f))
            binding.legs.addView(textView, binding.legs.childCount)
        } else {
            val imageView = ImageView(binding.root.context)
            imageView.setImageResource(R.drawable.walk)
            imageView.layoutParams = params
            binding.legs.addView(imageView, binding.legs.childCount)

            val textView = TextView(binding.root.context)
            val duration = leg.duration!! / 60
            textView.text = "${duration} min"
            binding.legs.addView(textView, binding.legs.childCount)
        }
    }

    companion object {
        fun Instance(
            parentView: ViewGroup,
            itineraryClickListener: ItineraryClickListener
        ): SearchResultAdapterViewHolder {
            val inflater = LayoutInflater.from(parentView.context)
            val binding = SearchResultListBinding.inflate(inflater, parentView, false)
            return SearchResultAdapterViewHolder(binding, itineraryClickListener)
        }
    }
}

object ItineraryDIFF : DiffUtil.ItemCallback<Itinerary>() {
    override fun areItemsTheSame(oldItem: Itinerary, newItem: Itinerary): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Itinerary, newItem: Itinerary): Boolean {
        return oldItem == newItem
    }
}

class ItineraryClickListener(val clickListener: (itinerary: Itinerary) -> Unit) {
    fun onClick(itinerary: Itinerary) = clickListener(itinerary)
}