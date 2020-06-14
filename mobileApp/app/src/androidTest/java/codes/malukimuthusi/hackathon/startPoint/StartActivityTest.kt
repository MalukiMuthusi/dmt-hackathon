package codes.malukimuthusi.hackathon.startPoint

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import codes.malukimuthusi.hackathon.R
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class StartActivityTest {

    @get:Rule
    var activityRule = ActivityTestRule(StartActivity::class.java)

    // before method

    @Test
    fun search_button_is_displayed() {

        // click search button
        onView(
            allOf(withId(R.id.buttonQ))
        ).perform(click())

        // choose a destination
        val mapLocation = onView(withId(R.id.okButton))
        mapLocation.perform(click())

        val searchPath = onView(withId(R.id.searchButton))
        searchPath.perform(click())
    }


}