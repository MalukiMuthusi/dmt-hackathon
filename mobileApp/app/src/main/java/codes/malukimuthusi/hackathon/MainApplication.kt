package codes.malukimuthusi.hackathon

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mapbox.mapboxsdk.Mapbox
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        // set up firebase realtime database
        Firebase.database.setPersistenceEnabled(true)

        //set up Timber Logging library
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        Places.initialize(applicationContext, "AIzaSyDhmypbi5Eaw6MO2r4RF54qlOkSt67nwb0")
        Places.createClient(this)

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))
    }

}