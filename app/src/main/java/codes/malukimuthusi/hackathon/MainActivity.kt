package codes.malukimuthusi.hackathon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val SIGNED_IN = "signedin"
private const val RC_SIGN_IN = 100

class MainActivity : AppCompatActivity() {
    //    val TAG = this.applicationContext.packageName
    var authenticated: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        authenticated = savedInstanceState?.getBoolean(SIGNED_IN)


        if (authenticated == false) {
//            requestSignin()
        }


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

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        authenticated?.let { outState.putBoolean(SIGNED_IN, authenticated!!) }
    }

}
