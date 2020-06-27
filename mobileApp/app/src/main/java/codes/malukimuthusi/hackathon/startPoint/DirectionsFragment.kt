package codes.malukimuthusi.hackathon.startPoint

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
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.LineManager
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.mapboxsdk.utils.ColorUtils

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM10 = "param1"
//private const val ARG_PARAM20 = "param2"

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
    private lateinit var map: MapboxMap
    private val sharedViewModel: SharedViewModel by activityViewModels()


    private lateinit var lineString: MutableList<LatLng>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDirectionsBinding.inflate(inflater, container, false)

        // recycler view adapter
        val adapter = SingleTransitLegAdapter(LegClickListener { leg ->
            sharedViewModel.leg = leg
            val action =
                DirectionsFragmentDirections.actionDirectionsFragmentToSaccosRouteFragment()
            findNavController().navigate(action)
        })
        adapter.submitList(sharedViewModel.transitLegs)
        with(binding) {
            lifecycleOwner = this@DirectionsFragment
            recyclerView.adapter = adapter

            // mapbox
            with(mapView) {
                onCreate(savedInstanceState)
                getMapAsync(this@DirectionsFragment)
            }
        }

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
//                    putString(ARG_PARAM10, param1)
//                    putString(ARG_PARAM20, param2)
                }
            }

        private const val LOCATION_MARKER = "location_marker"

    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap

        map.setStyle(Style.MAPBOX_STREETS) { style ->

            addPointsMarkers(style)
            // put marker at start of trip

            // put marker at the end of trip

            // update camera to be within the the path

            val latLngBounds = LatLngBounds.Builder()
            var bounding: MutableList<Point>
            var latLngList: List<LatLng>
            val symbolManager = SymbolManager(binding.mapView, map, style)
            with(symbolManager) {
                iconAllowOverlap = true
                textAllowOverlap = true
            }

            val symbolOptions = SymbolOptions()
                .withLatLng(sharedViewModel.destination)
                .withIconImage(LOCATION_MARKER)
                .withIconSize(2f)
                .withIconColor(
                    ColorUtils.colorToRgbaString(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.purple_700
                        )
                    )
                )
            symbolManager.create(symbolOptions)


            for (leg in sharedViewModel.selectedItinerary.legs!!) {
                bounding = PolylineUtils.decode(leg.legGeometry!!.points!!, 5)
                latLngList = bounding.map {
                    LatLng(it.latitude(), it.longitude())
                }

                // put marker at start of leg and at end of the leg

                latLngBounds.includes(latLngList)

                if (leg.transitLeg!!) {
                    // draw the leg
                    val lineManager = LineManager(binding.mapView, map, style)
                    val lineManagerOptions = LineOptions()
                        .withGeometry(LineString.fromPolyline(leg.legGeometry.points!!, 5))
                        .withLineWidth(2.0f)
                    lineManager.create(lineManagerOptions)
                } else {

                    val lineManager = LineManager(binding.mapView, map, style)
                    val lineManagerOptions = LineOptions()
                        .withLineWidth(5.0f)
                        .withLinePattern("airfield-11")
                        .withGeometry(LineString.fromPolyline(leg.legGeometry.points!!, 5))
                    lineManager.create(lineManagerOptions)

                    // this leg has mode WALK, draw path with dotted steps
                }
            }
            val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 30)
            map.easeCamera(cameraUpdateFactory)
        }

    }

    private fun addPointsMarkers(style: Style) {
        BitmapUtils.getBitmapFromDrawable(requireActivity().getDrawable(R.drawable.ic_location_on_black_24dp))
            ?.let {
                style.addImage(
                    LOCATION_MARKER,
                    it, true
                )
            }
    }

    // map box lifecycle methods.
    // There is a cleaner way to do this!!
    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()

    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}
