package codes.malukimuthusi.hackathon

import androidx.lifecycle.ViewModel
import codes.malukimuthusi.hackathon.data.Route
import codes.malukimuthusi.hackathon.data.Sacco
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class NewSaccoFragmentViewModel : ViewModel() {
    val routeList: MutableList<Route> = mutableListOf()
    val db = Firebase.database.reference
    var farePickHours: Int? = null
    var fareLowHours: Int? = null

    fun createSacco(sacco: Sacco, roadKey: String) {
        val saccoKey = db.child("saccos").push().key
        if (saccoKey == null) {
            Timber.e("Failed to get key for new Sacco Entry")
        } else {
            val saccoValue = sacco.toMap()
            val childUpdates = HashMap<String, Any>()
            childUpdates["/Routes/" + roadKey + "/saccos"] = mapOf(saccoKey to true)
            childUpdates["/saccos/" + saccoKey] = saccoValue
            Timber.d(
                "Creating New Sacco in Remote DB. roadKey: %s, saccoKey: %s",
                roadKey,
                saccoKey
            )
            val uploadTast = db.updateChildren(childUpdates)
            if (uploadTast.isSuccessful) {
                Timber.d("uploaded Sacco")
            }
            if (uploadTast.isCanceled) {
                Timber.e("Did not upload sacco!!")
            }
            if (uploadTast.isComplete) {
                Timber.d("Task is complete")
            }
        }

    }

    // static objects and methods
    companion object {
        val pickHoursList = mutableListOf<String>()

        fun doSomethingWhenChecked(
            checked: Boolean,
            time: String,
            pickHoursList: MutableList<String>
        ) {
            if (checked) {
                pickHoursList.add(time)
            } else {
                pickHoursList.remove(time)
            }
        }
    }
}