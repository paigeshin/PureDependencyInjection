package com.techyourchance.dagger2course.common.composition

import com.techyourchance.dagger2course.Constants
import com.techyourchance.dagger2course.networking.StackoverflowApi
import com.techyourchance.dagger2course.questions.FetchQuestionDetailsUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionsUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppCompositionRoot {
    // init retrofit
    private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // init stackoverflow API
    public val stackoverflowApi = retrofit.create(StackoverflowApi::class.java)

    // Object should be stateful
    // When you want to share a object and multiple clients try to access it, that might be harmful.
    // However, if you put get(), this getter function will be invoked and will create a new instance of which questions to use.
    // `Just make sure you put `getter` when you want to make some object sharable between multiple clients.`
    public val fetchQuestionsUseCase get() = FetchQuestionsUseCase(stackoverflowApi)
    public val fetchQuestionDetailUseCase get() = FetchQuestionDetailsUseCase(stackoverflowApi)
}