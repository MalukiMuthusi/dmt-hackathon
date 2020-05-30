package codes.malukimuthusi.hackathon

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import codes.malukimuthusi.hackathon.adapters.AllRoutesAdapter
import codes.malukimuthusi.hackathon.databinding.ActivityRoutesMapsBinding
import codes.malukimuthusi.hackathon.viewModels.RouteMapsViewModel
import codes.malukimuthusi.hackathon.webService.Route
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

class RoutesMapsActivity : AppCompatActivity(), OnMapReadyCallback,
    EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private val viewModel by viewModels<RouteMapsViewModel>()
    private lateinit var binding: ActivityRoutesMapsBinding
    private var allRoutes: List<Route>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_routes_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        val adapter = AllRoutesAdapter()

        viewModel.fetchAllRoutes()



        binding.recyclerView.adapter = adapter

        viewModel.routesLiveData.observe(this, Observer {
            adapter.submitList(it)
        })
        adapter.submitList(allRoutes)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Sydney and move the camera
        try {
            getLocation()
        } catch (e: Exception) {
            Timber.e("Failed to ge Location")
        }

        // set style
        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
        )
    }

    @AfterPermissionGranted(RC_FINELOCATIONPERMS)
    private fun getLocation() {
        if (haslocationpermission()) {
            val locationResult = fusedLocationProvider.lastLocation
            locationResult.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    lastKnownLocation = task.result
                    try {
                        val newPlace = lastKnownLocation?.latitude?.let {
                            lastKnownLocation?.longitude?.let { it1 ->
                                LatLng(
                                    it,
                                    it1
                                )
                            }
                        }
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                newPlace, 16f
                            )
                        )
                    } catch (e: java.lang.Exception) {
                        Timber.e("Returned Null Location")
                        throw e
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
    private fun haslocationpermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (haslocationpermission()) {
                getLocation()
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Timber.d("Permissons granted %s", perms.toString())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // easy permission handles the request
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onRationaleDenied(requestCode: Int) {
    }

    override fun onRationaleAccepted(requestCode: Int) {
    }
}
