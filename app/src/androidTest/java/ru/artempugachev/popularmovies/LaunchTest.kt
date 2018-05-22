package ru.artempugachev.popularmovies

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule
import ru.artempugachev.popularmovies.movielist.MovieListActivity

/**
 * Launch app and check if views appear
 */
@RunWith(AndroidJUnit4::class)
class LaunchTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule<MovieListActivity>(MovieListActivity::class.java)

    @Test
    fun views_appear() {
        onView(withId(R.id.main_activity_app_bar_layout)).check(matches(isDisplayed()))
    }
}
