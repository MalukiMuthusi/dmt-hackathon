package codes.malukimuthusi.hackathon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import codes.malukimuthusi.hackathon.databinding.ActivityMainBinding
import codes.malukimuthusi.hackathon.startPoint.StartActivity
import com.firebase.ui.auth.AuthUI
import timber.log.Timber

private const val SIGNED_IN = "signedin"
private const val RC_SIGN_IN = 100

class MainActivity : AppCompatActivity() {
    //    val TAG = this.applicationContext.packageName
    var authenticated: Boolean? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.navigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.openMaps -> {
                    navigateToMapsActivity()
                    true
                }
                R.id.addSacco -> {
                    navigateToAddSaccoActivity()
                    true
                }
                R.id.routes -> {
                    navigateToAllRoutes()
                    true
                }
                R.id.planTrip -> {
                    navigateToPlanTrip()
                    true
                }
                R.id.startPoint -> {
                    val intent = Intent(this, StartActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToPlanTrip() {
        val intent = Intent(this, PlanTripMapsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAllRoutes() {
        val intent = Intent(this, RoutesMapsActivity::class.java)
        startActivity(intent)
    }


    // navigate to mapa activity
    private fun navigateToMapsActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    // navigate to activity for adding a new sacco entry.
    private fun navigateToAddSaccoActivity() {
        Timber.d("Function to open maps activity called")
        val intent = Intent(this, NewSacco::class.java)
        startActivity(intent)
    }

    private fun requestSignin() {
        val authenticater = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(authenticater)
                .build(),
            RC_SIGN_IN
        )

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        authenticated?.let { outState.putBoolean(SIGNED_IN, authenticated!!) }
    }

}
