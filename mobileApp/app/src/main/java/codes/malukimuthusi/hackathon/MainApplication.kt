package codes.malukimuthusi.hackathon

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

    }
}