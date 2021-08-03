package com.techyourchance.dagger2course.screens.common.activities

import androidx.appcompat.app.AppCompatActivity
import com.techyourchance.dagger2course.MyApplication
import com.techyourchance.dagger2course.common.composition.CompositionRoot

open class BaseActivity: AppCompatActivity() {
    private val appCompositionRoot get() = (application as MyApplication).appCompositionRoot
    val compositionRoot by lazy {
        CompositionRoot(this, appCompositionRoot)
    }
}