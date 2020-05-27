package codes.malukimuthusi.hackathon

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import codes.malukimuthusi.hackathon.data.Repository
import codes.malukimuthusi.hackathon.databinding.ActivityMapsBinding
import codes.malukimuthusi.hackathon.viewModels.MapsActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

const val RC_FINELOCATIONPERMS = 34

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {


    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private var locationGranted = false
    private var lastKnownLocation: Location? = null

    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsActivityViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        binding.selectLoaction.setOnClickListener {
            val selected = map.cameraPosition.target
            lifecycleScope.launch {
                val x = Repository.nearByStopPoints(selected.latitude, selected.longitude)
                if (x.isNullOrEmpty()) {
                    Timber.e("No nearer stop")
                } else {
                    x.forEach {
                        Timber.d(it.name)
                    }
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

        // set marker to the current location
        getLocation()

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
                    val newPlace = LatLng(
                        lastKnownLocation!!.latitude,
                        lastKnownLocation!!.longitude
                    )
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            newPlace, 16f
                        )
                    )
                } else {
                    Timber.e("Location is Null")
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
