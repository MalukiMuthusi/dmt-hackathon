package codes.malukimuthusi.hackathon.startPoint

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import codes.malukimuthusi.hackathon.R
import codes.malukimuthusi.hackathon.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.exceptions.MapboxConfigurationException
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.LineManager
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.mapboxsdk.utils.ColorUtils
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
var time = ""
const val AUTOCOMPLETE_REQUEST_CODE = 333


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment(), OnMapReadyCallback,
    AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSearchBinding
    private lateinit var mapboxMap: MapboxMap
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: SearchFragmentViewModel by viewModels()
    private val options = mutableMapOf<String, String>()

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        val navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)


        // set text for start and destination.
        binding.toPlace.text = sharedViewModel.destinationString
        binding.startPlace.text = sharedViewModel.startPointString


        ArrayAdapter.createFromResource(
            requireContext(), R.array.depart_options, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }
        binding.spinner.onItemSelectedListener = this


        // event for when start place button is clicked
        binding.startPlace.setOnClickListener {
            val placeOptions = PlaceOptions.builder()
                .backgroundColor(Color.parseColor("#EEEEEE"))
                .build(PlaceOptions.MODE_CARDS)

            //.bbox(36.213083, -1.864678, 37.48887, -0.759486)

            val intent = PlaceAutocomplete.IntentBuilder()
                .accessToken(
                    Mapbox.getAccessToken()
                        ?: throw MapboxConfigurationException()
                )
                .placeOptions(placeOptions)
                .build(activity)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        // event for when start place button is clicked
        binding.toPlace.setOnClickListener {
            val cameraPosition = CameraPosition.Builder()
                .target(LatLng(-1.2909, 36.8282))
                .zoom(16.0)
                .build()
            val placePickerOptions = PlacePickerOptions.builder()
                .statingCameraPosition(cameraPosition)
                .includeReverseGeocode(false)
                .build()
            val toIntent = PlacePicker.IntentBuilder()
                .accessToken(Mapbox.getAccessToken()!!)
                .placeOptions(placePickerOptions)
                .build(requireActivity())
            startActivityForResult(toIntent, PLACE_SELECTION_REQUEST_CODE)
        }

        // take action when search button is pressed.
        binding.searchButton.setOnClickListener {
            if (sharedViewModel.destination == null) {
                val snackbar =
                    Snackbar.make(binding.parentView, "Select Destonation.", Snackbar.LENGTH_LONG)
                snackbar.show()
            } else if (sharedViewModel.startPoint == null) {
                val snackbar =
                    Snackbar.make(binding.parentView, "Select Start Poimt.", Snackbar.LENGTH_LONG)
                snackbar.show()
            } else {

                options["toPlace"] =
                    "${sharedViewModel.destination!!.latitude},${sharedViewModel.destination!!.longitude}"
                options["fromPlace"] =
                    "${sharedViewModel.startPoint!!.latitude},${sharedViewModel.startPoint!!.longitude}"
                options["showIntermediateStops"] = "true"
                viewModel.searchRoutes(options)
                binding.progressBar2.visibility = View.VISIBLE
            }
        }

        // valid trip was found, take action
        viewModel.tripPlan.observe(viewLifecycleOwner, Observer {
            binding.progressBar2.visibility = View.INVISIBLE
            sharedViewModel.tripPlan = it
        })

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it) {
                val action = SearchFragmentDirections.actionSearchFragment2ToSearchResultsFragment()
                findNavController().navigate(action)
                viewModel.navigate.value = false
            }

        })

        // observe for errors thrown when you search for route
        viewModel.exc.observe(viewLifecycleOwner, Observer {
            binding.progressBar2.visibility = View.INVISIBLE
            val snackbar =
                Snackbar.make(
                    binding.parentView,
                    "Cannot Search Routes. Check Connection.",
                    Snackbar.LENGTH_LONG
                )
            snackbar.show()
        })

        // check errors found from the trip plan result, e.g wrong parameters passed
        viewModel.planTripError.observe(viewLifecycleOwner, Observer {
            binding.progressBar2.visibility = View.INVISIBLE
            val snackbar =
                Snackbar.make(
                    binding.parentView,
                    "${it.message}",
                    Snackbar.LENGTH_LONG
                )
            snackbar.show()
        })

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            val feature = PlaceAutocomplete.getPlace(data)
            sharedViewModel.startPointString = feature.placeName() ?: "Searching Location"
            binding.startPlace.text = sharedViewModel.startPointString
            sharedViewModel.startPoint = LatLng(
                (feature.geometry() as Point).latitude(),
                (feature.geometry() as Point).longitude()
            )
//            Toast.makeText(requireContext(), feature.text(), Toast.LENGTH_LONG).show()

            if (resultCode == RESULT_OK && requestCode == PLACE_SELECTION_REQUEST_CODE) {
                val feature1 = PlacePicker.getPlace(data)
                sharedViewModel.destinationString = feature1?.placeName() ?: "Searching Place"
                binding.toPlace.text = sharedViewModel.destinationString
                sharedViewModel.destination = LatLng(
                    (feature1?.geometry() as Point).latitude(),
                    (feature1.geometry() as Point).longitude()
                )
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }

        private val PLACE_SELECTION_REQUEST_CODE = 56789
        private val MARKER = "marker15"
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onMapReady(map: MapboxMap) {
        mapboxMap = map
        map.setStyle(Style.MAPBOX_STREETS) { mapStyle ->
            addPointsMarkers(mapStyle)

            if (sharedViewModel.destination != null) {
                mapboxMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(sharedViewModel.destination!!, 12.0)
                )

                // add marker
            }
            if (sharedViewModel.startPoint != null) {
                mapboxMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(sharedViewModel.startPoint!!, 12.0)
                )

                // add marker
            }
            if (sharedViewModel.startPoint != null && sharedViewModel.destination != null) {
                val bound = LatLngBounds.Builder()
                    .include(sharedViewModel.startPoint!!)
                    .include(sharedViewModel.destination!!)
                    .build()
                val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(bound, 100)
                mapboxMap.animateCamera(cameraUpdateFactory)

                // draw the path
                val points = listOf<Point>(
                    Point.fromLngLat(
                        sharedViewModel.destination!!.longitude,
                        sharedViewModel.destination!!.latitude
                    ),
                    Point.fromLngLat(
                        sharedViewModel.startPoint!!.longitude,
                        sharedViewModel.startPoint!!.latitude
                    )
                )

                // draw line from start to destination
                val lineManager = LineManager(binding.mapView, mapboxMap, mapStyle)
                val lineOptions = LineOptions()
                    .withGeometry(LineString.fromLngLats(points))
                    .withLineWidth(2f)
                lineManager.create(lineOptions)

                // put marker on start and destination
                val symbolManager = SymbolManager(binding.mapView, mapboxMap, mapStyle)
                val iconColor = ColorUtils.colorToRgbaString(Color.BLUE)
                val destinationMarkerOptions = SymbolOptions()
                    .withIconImage(MARKER)
                    .withIconColor(iconColor)
                    .withIconSize(2f)
                    .withGeometry(
                        Point.fromLngLat(
                            sharedViewModel.destination!!.longitude,
                            sharedViewModel.destination!!.latitude
                        )
                    )
                val startPointMarkerOptions = SymbolOptions()
                    .withIconImage(MARKER)
                    .withIconColor(iconColor)
                    .withGeometry(
                        Point.fromLngLat(
                            sharedViewModel.startPoint!!.longitude,
                            sharedViewModel.startPoint!!.latitude
                        )
                    )
                    .withIconSize(2f)

                symbolManager.create(listOf(destinationMarkerOptions, startPointMarkerOptions))

            }
        }
    }

    private fun addPointsMarkers(style: Style) {
        BitmapUtils.getBitmapFromDrawable(requireActivity().getDrawable(R.drawable.ic_location_on_black_24dp))
            ?.let {
                style.addImage(
                    MARKER,
                    it, true
                )
            }
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            1 -> {
                TimePickerFragment().show(requireActivity().supportFragmentManager, "timePicker")
            }
        }
    }

    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            var am_pm = "am"
            var hour = hourOfDay.toString()
            if (hourOfDay > 12) {
                am_pm = "pm"
                hour = (hourOfDay - 12).toString()
            }
            time = "${hour}:${minute}${am_pm}"

        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(
                activity,
                this,
                hour,
                minute,
                DateFormat.is24HourFormat(activity)
            )
        }
    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(requireActivity(), this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            // Do something with the date chosen by the user
        }
    }
}
