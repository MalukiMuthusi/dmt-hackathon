package codes.malukimuthusi.hackathon.startPoint

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import codes.malukimuthusi.hackathon.R
import codes.malukimuthusi.hackathon.databinding.FragmentPlanTripBinding
import codes.malukimuthusi.hackathon.repository.Repository.fetchAddress
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
const val AUTOCOMPLETE_REQUEST_CODE = 1

/**
 * A simple [Fragment] subclass.
 * Use the [PlanTripFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlanTripFragment : Fragment(), OnMapReadyCallback,
    EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentPlanTripBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var lastKnownLocation: LatLng
    val args: PlanTripFragmentArgs by navArgs()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var geocoder: Geocoder


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
        binding = FragmentPlanTripBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_Location) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())

        val navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolBar.setupWithNavController(navController, appBarConfiguration)


        binding.okButton.setOnClickListener {
            if (args.locationType == TO) {
                sharedViewModel.destination = googleMap.cameraPosition.target

            } else {
                sharedViewModel.startPoint = googleMap.cameraPosition.target
            }
            val action = PlanTripFragmentDirections.actionPlanTripFragmentToSearchFragment2()
            findNavController().navigate(action)
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlanTripFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlanTripFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map ?: return
        if (sharedViewModel.startPoint != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sharedViewModel.startPoint, 15f))
        }

        geocoder = Geocoder(requireContext(), Locale.getDefault())
        googleMap.setOnCameraIdleListener {
            lifecycleScope.launch {
                binding.textView2.text = fetchAddress(
                    googleMap.cameraPosition.target,
                    requireContext()
                )
            }
        }


    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Timber.d("Permissons granted %s", perms.toString())
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }
}
