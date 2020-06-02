package codes.malukimuthusi.hackathon.startPoint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import codes.malukimuthusi.hackathon.R
import codes.malukimuthusi.hackathon.databinding.FragmentSearchBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import pub.devrel.easypermissions.EasyPermissions
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
    EasyPermissions.RationaleCallbacks {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSearchBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: SearchFragmentViewModel by viewModels()
    private var navigate by Delegates.notNull<Boolean>()

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
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.toolBar.setupWithNavController(navController, appBarConfiguration)
        binding.startPlace.text = sharedViewModel.startPoint.toString()
        binding.toPlace.text = sharedViewModel.destination.toString()

        // event for when start place button is clicked
        binding.startPlace.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragment2ToPlanTripFragment(TO)
            findNavController().navigate(action)
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
                val options = mutableMapOf<String, String>()
                options["toPlace"] =
                    "${sharedViewModel.destination!!.latitude},${sharedViewModel.destination!!.longitude}"
                options["fromPlace"] =
                    "${sharedViewModel.startPoint!!.latitude},${sharedViewModel.startPoint!!.longitude}"
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
}
