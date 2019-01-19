package com.barry.mobiledatausage

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@RunWith(AndroidJUnit4::class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {

    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testApiSuccess() {
        onView(RecyclerViewMatcher(R.id.rv_data_usage).atPosition(0))
            .check(matches(hasDescendant(withText("Year:"))))
            .check(matches(hasDescendant(withText("Volume:"))))

        onView(withId(R.id.rv_data_usage))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(14, click()))
    }

    @Ignore
    @Test
    fun testApi_refreshLayout() {
        onView(withId(R.id.refresh_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_data_usage)).check(matches(isDisplayed()))

        onView(withId(R.id.refresh_layout))
            .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))
    }

    /*
    * Turn off internet
    * */
    @Ignore
    @Test
    fun testApiFailed() {
        onView(withId(R.id.refresh_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_data_usage)).check(matches(isDisplayed()))

        onView(withText("Something went wrong!")).inRoot(RootMatchers.withDecorView(Matchers.not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    /************************ RefreshLayout Helper *****************************/

    private fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController, view: View) {
                action.perform(uiController, view)
            }
        }
    }
}