package codes.malukimuthusi.hackathon.startPoint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import codes.malukimuthusi.hackathon.R
import codes.malukimuthusi.hackathon.adapters.LegClickListener
import codes.malukimuthusi.hackathon.adapters.SingleTransitLegAdapter
import codes.malukimuthusi.hackathon.databinding.FragmentDirectionsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM10 = "param1"
private const val ARG_PARAM20 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DirectionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DirectionsFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentDirectionsBinding
    private lateinit var map: GoogleMap
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM10)
            param2 = it.getString(ARG_PARAM20)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDirectionsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_Location) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val adapter = SingleTransitLegAdapter(LegClickListener { leg ->
            sharedViewModel.leg = leg
            val action =
                DirectionsFragmentDirections.actionDirectionsFragmentToSaccosRouteFragment()
            findNavController().navigate(action)
        })
        adapter.submitList(sharedViewModel.transitLegs)



        binding.recyclerView.adapter = adapter

        // return ViewGroup
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DirectionsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DirectionsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM10, param1)
                    putString(ARG_PARAM20, param2)
                }
            }
    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0 ?: return
        val dottedPatern = listOf(Dot(), Gap(2f))


        val tripStart =
            LatLng(sharedViewModel.tripPlan.from!!.lat!!, sharedViewModel.tripPlan.from!!.lon!!)
        val fromMarkerOptions = MarkerOptions()
            .position(
                LatLng(
                    sharedViewModel.tripPlan.from!!.lat!!,
                    sharedViewModel.tripPlan.from!!.lon!!
                )
            )
            .icon(
                bitmapDescriptorFromVector(
                    requireContext(),
                    R.drawable.ic_place_blue_24dp
                )
            )
        val toMarkerOptions = MarkerOptions()
            .position(
                LatLng(
                    sharedViewModel.tripPlan.to!!.lat!!,
                    sharedViewModel.tripPlan.to!!.lon!!
                )
            )
            .icon(
                bitmapDescriptorFromVector(
                    requireContext(),
                    R.drawable.ic_place_red_24dp
                )
            )
        map.addMarker(fromMarkerOptions)
        map.addMarker(toMarkerOptions)
        val tripTo =
            LatLng(sharedViewModel.tripPlan.to!!.lat!!, sharedViewModel.tripPlan.to!!.lon!!)
        val bound = LatLngBounds.Builder()
            .include(tripStart)
            .include(tripTo)
            .build()
        val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(bound, 100)
        map.animateCamera(cameraUpdateFactory)

        for (leg in sharedViewModel.selectedItinerary.legs!!) {

            if (leg.transitLeg!!) {
                val points = PolyUtil.decode(leg.legGeometry!!.points)
                map.addPolyline(
                    PolylineOptions()
                        .color(R.color.purple_700)
                        .addAll(points)
                )
            } else {
                val points = PolyUtil.decode(leg.legGeometry!!.points)
                map.addPolyline(
                    PolylineOptions()
                        .color(R.color.green_500)
                        .pattern(dottedPatern)
                        .addAll(points)
                )

            }
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}
