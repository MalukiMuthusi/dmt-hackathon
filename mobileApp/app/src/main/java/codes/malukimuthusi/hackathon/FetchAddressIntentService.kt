package codes.malukimuthusi.hackathon

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import java.io.IOException
import java.util.*

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FETCHADRESS = "codes.malukimuthusi.hackathon.action.ACTION_FETCHADRESS"
private const val ACTION_BAZ = "codes.malukimuthusi.hackathon.action.BAZ"

// TODO: Rename parameters
const val LOCATION_DATA_EXTRA = "codes.malukimuthusi.hackathon.extra.LOCATION_DATA_EXTRA"
const val RESULT_DATA_KEY = "codes.malukimuthusi.hackathon.extra.RESULT_DATA_KEY"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class FetchAddressIntentService : IntentService("FetchAddressIntentService") {
    private lateinit var resultReceiver: ResultReceiver

    override fun onHandleIntent(intent: Intent?) {


        val location = intent!!.getParcelableExtra<Location>(LOCATION_DATA_EXTRA)

        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses = mutableListOf<Address?>()
        try {
            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude, 1
            )
        } catch (e: IOException) {
            // network not available
        } catch (e: IllegalArgumentException) {
            //invalid latitude and longitude
        }

        if (addresses.isNullOrEmpty()) {
            deliverResult(FAILURE_RESULT, "error message")
        } else {
            val address = addresses.first()
            val addressFragments = mutableListOf<String>()

            val index = address!!.maxAddressLineIndex
            for (x in 0..index) {
                addressFragments.add(address.getAddressLine(x))
            }
            deliverResult(
                SUCCESS_RESULT, addressFragments.toString()
            )
        }


    }

    private fun deliverResult(resultCode: Int, message: String) {
        val bundle = Bundle()
        bundle.putString(RESULT_DATA_KEY, message)
        resultReceiver.send(resultCode, bundle)

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo(param1: String, param2: String) {
        TODO("Handle action Foo")
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionBaz(param1: String, param2: String) {
        TODO("Handle action Baz")
    }

    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, FetchAddressIntentService::class.java).apply {
                action = "one"

            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionBaz(context: Context, param1: String, param2: String) {
            val intent = Intent(context, FetchAddressIntentService::class.java).apply {
                action = ACTION_BAZ

            }
            context.startService(intent)
        }
    }
}
