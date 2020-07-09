package codes.malukimuthusi.hackathon.startPoint

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import codes.malukimuthusi.hackathon.R
import codes.malukimuthusi.hackathon.databinding.ActivityStartBinding
import codes.malukimuthusi.hackathon.newSacco.NewSacco
import timber.log.Timber

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)

        val navController = findNavController(R.id.navHost)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawer)
        binding.navigation.setupWithNavController(navController)

        binding.navigation.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.addSacco -> {
                    navigateToAddSaccoActivity()
                    true
                }
                else -> false
            }
        }


    }

    // navigate to activity for adding a new sacco entry.
    private fun navigateToAddSaccoActivity() {
        Timber.d("Function to open maps activity called")
        val intent = Intent(this, NewSacco::class.java)
        startActivity(intent)
    }
}
