package codes.malukimuthusi.hackathon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import codes.malukimuthusi.hackathon.databinding.FragmentDirectionsBinding
import codes.malukimuthusi.hackathon.startPoint.SharedViewModel
import codes.malukimuthusi.hackathon.viewModels.DirectionsFragmentViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
    private val viewModel: DirectionsFragmentViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDirectionsBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


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
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0 ?: return
        val dottedPatern = listOf(Dot(), Gap(2f))

        val lat = viewModel.itinerary.legs!!.first().from!!.lat
        val lon = viewModel.itinerary.legs!!.first().from!!.lon
        val cameraPoint = LatLng(lat!!, lon!!)

        val tripStart =
            LatLng(sharedViewModel.tripPlan.from!!.lat!!, sharedViewModel.tripPlan.from!!.lon!!)
        val tripTo =
            LatLng(sharedViewModel.tripPlan.to!!.lat!!, sharedViewModel.tripPlan.to!!.lon!!)
        val bound = LatLngBounds.Builder()
            .include(tripStart)
            .include(tripTo)
            .build()
        val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(bound, 100)
        map.animateCamera(cameraUpdateFactory)

        for (leg in viewModel.itinerary.legs!!) {

            if (leg.transitLeg!!) {
                val points = PolyUtil.decode(leg.legGeometry!!.points)
                map.addPolyline(
                    PolylineOptions()
                        .color(R.color.indigo_400)
                        .addAll(points)
                )
            } else {
                val points = PolyUtil.decode(leg.legGeometry!!.points)
                map.addPolyline(
                    PolylineOptions()
                        .color(R.color.brown_400)
                        .pattern(dottedPatern)
                        .addAll(points)
                )

            }
        }
    }
}
