package codes.malukimuthusi.hackathon.appIntro

import android.os.Bundle
import androidx.fragment.app.Fragment
import codes.malukimuthusi.hackathon.R
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class MyCustomAppIntro : AppIntro() {

    val screen1 = AppIntroFragment.newInstance(
            title = "Welcome",
            description = "Choose Destination by clicking on the Search Button",
            imageDrawable = R.drawable.screenshot_1
    )

    val screen2 = AppIntroFragment.newInstance(
            title = "Welcome",
            description = "Pick a Location by draggin the map to the desired Point, then click Ok.",
            imageDrawable = R.drawable.screenshot_2
    )

    val screen3 = AppIntroFragment.newInstance(
            title = "Welcome",
            description = "You can also search for a place by its name.",
            imageDrawable = R.drawable.screenshot_3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // add slides
        addSlide(screen1)
        addSlide(screen2)
        addSlide(screen3)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }
}
