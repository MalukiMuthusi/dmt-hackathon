package codes.malukimuthusi.hackathon.routes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import codes.malukimuthusi.hackathon.R
import codes.malukimuthusi.hackathon.databinding.ActivityAllRoutesBinding

class AllRoutesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllRoutesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_all_routes
        )
    }
}
