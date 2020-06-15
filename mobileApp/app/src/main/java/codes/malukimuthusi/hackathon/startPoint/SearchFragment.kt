package codes.malukimuthusi.hackathon.startPoint

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import codes.malukimuthusi.hackathon.R
import codes.malukimuthusi.hackathon.databinding.FragmentSearchBinding
import codes.malukimuthusi.hackathon.repository.Repository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.util.*
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment(), OnMapReadyCallback,
    EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks, AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSearchBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: SearchFragmentViewModel by viewModels()
    private var navigate by Delegates.notNull<Boolean>()
    val options = mutableMapOf<String, String>()


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
        // autocomplete places
        val fields = listOf(Place.Field.ID, Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        ).build(requireContext())
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        val navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.toolBar.setupWithNavController(navController, appBarConfiguration)
        lifecycleScope.launch {
            binding.startPlace.text =
                Repository.fetchAddress(sharedViewModel.startPoint!!, requireContext())
            binding.toPlace.text =
                Repository.fetchAddress(sharedViewModel.destination!!, requireContext())
        }


        ArrayAdapter.createFromResource(
            requireContext(), R.array.depart_options, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }


        // event for when start place button is clicked
        binding.startPlace.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragment2ToPlanTripFragment(TO)
//            findNavController().navigate(action)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        // event for when start place button is clicked
        binding.toPlace.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragment2ToPlanTripFragment(TO)
            findNavController().navigate(action)
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

        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            AUTOCOMPLETE_REQUEST_CODE -> {
                if (requestCode == RESULT_OK) {
                    val place = data?.let { Autocomplete.getPlaceFromIntent(it) }
                    Timber.e("%s", place?.name ?: "no name")
                }
                if (requestCode == AutocompleteActivity.RESULT_CANCELED) {
                    val status = data?.let { Autocomplete.getStatusFromIntent(it) }
                    Timber.e(status?.statusMessage)
                }
                if (resultCode == RESULT_CANCELED) {
                    Timber.e("User Cancelled")
                }

            }
            else -> super.onActivityResult(requestCode, resultCode, data)
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
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map ?: return
        if (sharedViewModel.destination != null) {
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(sharedViewModel.destination, 12f)
            )
            googleMap.addMarker(
                MarkerOptions()
                    .position(sharedViewModel.destination!!)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_75pct))
            )
        }
        if (sharedViewModel.startPoint != null) {
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(sharedViewModel.startPoint, 12f)
            )
            googleMap.addMarker(
                MarkerOptions()
                    .position(sharedViewModel.startPoint!!)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_75pct))
            )
        }
        if (sharedViewModel.startPoint != null && sharedViewModel.destination != null) {
            val points = listOf(sharedViewModel.destination!!, sharedViewModel.startPoint!!)
            val bound = LatLngBounds.Builder()
                .include(sharedViewModel.startPoint)
                .include(sharedViewModel.destination)
                .build()
            val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(bound, 100)
            googleMap.animateCamera(cameraUpdateFactory)
            googleMap.addPolyline(
                PolylineOptions()
                    .addAll(points)
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == 1) {
            TimePickerFragment().show(requireActivity().supportFragmentManager, "timePicker")
        }
    }

    inner class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            val time = hourOfDay * 60 * 60 + minute * 60
            options["time"] = "$time"
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
}
