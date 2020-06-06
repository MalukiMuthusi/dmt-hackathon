package codes.malukimuthusi.hackathon.startPoint

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import codes.malukimuthusi.hackathon.R
import codes.malukimuthusi.hackathon.adapters.FavouritePlacesAdapter
import codes.malukimuthusi.hackathon.dataModel.favouritePlaceEx
import codes.malukimuthusi.hackathon.databinding.FragmentWelcomeBinding
import codes.malukimuthusi.hackathon.routes.AllRoutesActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM5 = "param1"
private const val ARG_PARAM6 = "param2"
const val RC_FINELOCATIONPERMS = 109

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
const val TO = 1
const val FROM = 0

class WelcomeFragment : Fragment(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentWelcomeBinding
    private lateinit var recyclerViewAdapter: FavouritePlacesAdapter

    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private val sharedViewModel: SharedViewModel by activityViewModels()


    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM5)
            param2 = it.getString(ARG_PARAM6)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocation()


        recyclerViewAdapter = FavouritePlacesAdapter()
        binding.recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.submitList(favouritePlaceEx)

        val navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolBar.setupWithNavController(navController, appBarConfiguration)


        binding.buttonQ.setOnClickListener {
            val snackbar = Snackbar.make(it, "Navigating", Snackbar.LENGTH_LONG)
            snackbar.show()
            val directions =
                WelcomeFragmentDirections.actionWelcomeFragmentToPlanTripFragment(TO)
            findNavController().navigate(directions)
        }

        binding.bottomNavigation.setOnNavigationItemReselectedListener { }
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.routes -> {
                    val intent = Intent(requireContext(), AllRoutesActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }

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
         * @return A new instance of fragment WelcomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WelcomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM5, param1)
                    putString(ARG_PARAM6, param2)
                }
            }
    }

    @AfterPermissionGranted(RC_FINELOCATIONPERMS)
    private fun getLocation() {
        if (hasLocationPermission()) {
            fusedLocationProvider.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (task.result != null) {
                        sharedViewModel.startPoint =
                            LatLng(task.result!!.latitude, task.result!!.longitude)
                        sharedViewModel.destination =
                            LatLng(task.result!!.latitude, task.result!!.longitude)
                    } else {
                        sharedViewModel.startPoint = LatLng(-1.2873788121, 36.8282264471)
                    }
                } else {
                    Timber.e("Task to get Location Failed")
                }
            }

        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rational_location),
                RC_FINELOCATIONPERMS,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    // check if we have location permission
    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Timber.d("Permissons granted %s", perms.toString())
        getLocation()
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
