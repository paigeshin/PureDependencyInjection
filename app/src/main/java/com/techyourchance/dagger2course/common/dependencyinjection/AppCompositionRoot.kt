package com.techyourchance.dagger2course.common.dependencyinjection

import android.app.Application
import androidx.annotation.UiThread
import com.techyourchance.dagger2course.Constants
import com.techyourchance.dagger2course.networking.StackoverflowApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// AppCompositionRoot contains all the services app needs in Application Level
@UiThread
class AppCompositionRoot(val application: Application) {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
    // init stackoverflow API
    val stackoverflowApi: StackoverflowApi by lazy {
        retrofit.create(StackoverflowApi::class.java)
    }
}