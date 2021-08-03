package com.techyourchance.dagger2course.common.composition

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.techyourchance.dagger2course.questions.FetchQuestionDetailsUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionsUseCase
import com.techyourchance.dagger2course.screens.common.ScreensNavigator
import com.techyourchance.dagger2course.screens.common.dialogs.DialogsNavigator

class CompositionRoot(private val activity: AppCompatActivity,
                      private val appCompositionRoot: AppCompositionRoot) {

    val screensNavigator by lazy {
        ScreensNavigator(activity)
    }

    private val fragmentManager get() = activity.supportFragmentManager

    val dialogsNavigator get() = DialogsNavigator(fragmentManager)

    private val stackoverflowApi get() = appCompositionRoot.stackoverflowApi

    // Object should be stateful
    // When you want to share a object and multiple clients try to access it, that might be harmful.
    // However, if you put get(), this getter function will be invoked and will create a new instance of which questions to use.
    // `Just make sure you put `getter` when you want to make some object sharable between multiple clients.`
    val fetchQuestionsUseCase get() = FetchQuestionsUseCase(stackoverflowApi)
    val fetchQuestionDetailUseCase get() = FetchQuestionDetailsUseCase(stackoverflowApi)

}