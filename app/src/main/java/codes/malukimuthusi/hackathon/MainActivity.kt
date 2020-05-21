package codes.malukimuthusi.hackathon

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import codes.malukimuthusi.hackathon.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI

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
            if (it.itemId == R.id.addSacco) {
                navigateToAddSaccoActivity()
                return@setNavigationItemSelectedListener true
            }
            return@setNavigationItemSelectedListener false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addSacco) {
            navigateToAddSaccoActivity()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToAddSaccoActivity() {
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
