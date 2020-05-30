package codes.malukimuthusi.hackathon

import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import codes.malukimuthusi.hackathon.databinding.ActivityPlanTripMapsBinding
import codes.malukimuthusi.hackathon.viewModels.PlanTripsViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

const val REQUEST_CHECK_SETTINGS = 29
const val SUCCESS_RESULT = 1
const val FAILURE_RESULT = 0
const val REQUESTING_LOCATION_UPDATES_KEY = "location"

class PlanTripMapsActivity : AppCompatActivity(), OnMapReadyCallback,
    EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private lateinit var binding: ActivityPlanTripMapsBinding

    // track the start and end point state
    private var startMarkPosition: LatLng? = null
    private var endMarkPosition: LatLng? = null
    private val viewModel by viewModels<PlanTripsViewModel>()

    private lateinit var locationResult: Task<Location>

    private lateinit var resultReceiver: FetchAddressIntentService
    private val locationRequest = LocationRequest()
    private lateinit var locationCallback: LocationCallback

    private var requestingLocationUpdates = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plan_trip_maps)
        binding.lifecycleOwner = this
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        viewModel.toastErrFetching.observe(this, Observer {
            val toast =
                Toast.makeText(this, "Error fetching trip: Check Stack Trace", Toast.LENGTH_LONG)
            toast.show()
        })

        // plot a path
        viewModel.pathPoints.observe(this, Observer { locationPoints ->
            map.addPolyline(
                PolylineOptions()
                    .addAll(locationPoints)
            )
            binding.progressBar.visibility = View.GONE
        })

        // no trip found or wrong parameters.
        viewModel.tripPlanError.observe(this, Observer { error ->
            binding.progressBar.visibility = View.GONE
            if (error.noPath!!) {
                val snackbar =
                    Snackbar.make(binding.parentView, "${error.message}", Snackbar.LENGTH_LONG)
                snackbar.show()
            } else {
                val snackbar =
                    Snackbar.make(binding.parentView, "${error.message}", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        })

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                if (p0 == null) {
                    Timber.e("Error")
                }
            }
        }

    }

    private fun setLocationOptions() {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
    }

    fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) return

    }

    override fun onResume() {
        super.onResume()
        fusedLocationProvider.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun startIntentService() {
        val intent = Intent(this, FetchAddressIntentService::class.java)

        val locationBuilder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val clientSettings = LocationServices.getSettingsClient(this)
        val locationSettingsTask = clientSettings.checkLocationSettings(locationBuilder.build())
        locationSettingsTask.addOnSuccessListener {
            locationResult = fusedLocationProvider.lastLocation
        }
        locationSettingsTask.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Timber.e(sendEx)
                }
            }
        }
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
        getLocation()
        binding.startButton.setOnClickListener {
            // do something
            putStartMark(map.cameraPosition.target)
            binding.pointTracker.setImageResource(R.drawable.marker_flag_end)
        }

        binding.endButton.setOnClickListener {
            //do something
            putEndMark(map.cameraPosition.target)
            binding.pointTracker.visibility = View.GONE
            val mapParameters = mutableMapOf<String, String>()
            mapParameters["fromPlace"] =
                "${startMarkPosition!!.latitude}, ${startMarkPosition!!.longitude}"
            mapParameters["toPlace"] =
                "${endMarkPosition!!.latitude}, ${endMarkPosition!!.longitude}"
            viewModel.planTrip(mapParameters)

            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun putStartMark(place: LatLng) {
        map.addMarker(
            MarkerOptions().position(place)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_flag_start))
        )
        startMarkPosition = map.cameraPosition.target
    }

    private fun putEndMark(place: LatLng) {
        map.addMarker(
            MarkerOptions().position(place)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_flag_end))
        )
        endMarkPosition = map.cameraPosition.target
    }


    @AfterPermissionGranted(RC_FINELOCATIONPERMS)
    private fun getLocation() {
        if (haslocationpermission()) {
            locationResult = fusedLocationProvider.lastLocation
            locationResult.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    lastKnownLocation = task.result
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                lastKnownLocation?.latitude ?: -1.29088401794,
                                lastKnownLocation?.longitude ?: 36.8282432556
                            ), 15f
                        )
                    )

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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates)

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
