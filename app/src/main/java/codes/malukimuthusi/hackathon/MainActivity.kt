package codes.malukimuthusi.hackathon

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val TAG = this.applicationContext.packageName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("Sacco")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w(TAG, p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val value = p0.getValue<String>()
            }
        })
    }
}
