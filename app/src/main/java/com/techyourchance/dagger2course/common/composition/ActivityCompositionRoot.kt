package com.techyourchance.dagger2course.common.composition

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.techyourchance.dagger2course.screens.common.ScreensNavigator

// ActivityCompositionRoot contains all the services app needs in Activity & Fragment Level
class ActivityCompositionRoot(val activity: AppCompatActivity,
                              private val appCompositionRoot: AppCompositionRoot) {
    val screensNavigator by lazy {
        ScreensNavigator(activity)
    }
    val application get() = appCompositionRoot.application
    val layoutInflater: LayoutInflater get() = LayoutInflater.from(activity)
    val fragmentManager get() = activity.supportFragmentManager
    val stackoverflowApi get() = appCompositionRoot.stackoverflowApi
}