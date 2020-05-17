package codes.malukimuthusi.hackathon

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainApplication : Application() {
    override fun onCreate() {
        Firebase.database.setPersistenceEnabled(true)

        super.onCreate()
    }
}