package com.techyourchance.dagger2course.screens.questiondetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.techyourchance.dagger2course.common.dependencyinjection.Service
import com.techyourchance.dagger2course.questions.FetchQuestionDetailsUseCase
import com.techyourchance.dagger2course.screens.common.ScreensNavigator
import com.techyourchance.dagger2course.screens.common.activities.BaseActivity
import com.techyourchance.dagger2course.screens.common.dialogs.DialogsNavigator
import com.techyourchance.dagger2course.screens.common.viewsmvc.ViewMvcFactory
import kotlinx.coroutines.*

class QuestionDetailsActivity : BaseActivity(), QuestionDetailsViewMvc.Listener {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @field:Service
    private lateinit var fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase
    @field:Service
    private lateinit var dialogsNavigator: DialogsNavigator
    @field:Service
    private lateinit var screensNavigator: ScreensNavigator
    @field:Service
    private lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: QuestionDetailsViewMvc
    private lateinit var questionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
        viewMvc = viewMvcFactory.newQuestionDetailsViewMvc(null)
        setContentView(viewMvc.rootView)
        questionId = intent.extras!!.getString(EXTRA_QUESTION_ID)!!
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
        fetchQuestionDetails()
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()
        viewMvc.unregisterListener(this)
    }

    private fun fetchQuestionDetails() {
        coroutineScope.launch {
            viewMvc.showProgressIndication()
            try {
                val result = fetchQuestionDetailsUseCase.fetchQuestion(questionId)
                when(result) {
                    is FetchQuestionDetailsUseCase.Result.Success -> {
                        viewMvc.bindQuestionBody(result.question.body)
                    }
                    is FetchQuestionDetailsUseCase.Result.Failure -> onFetchFailed()
                }
            } finally {
                viewMvc.hideProgressIndication()
            }

        }
    }

    private fun onFetchFailed() {
        dialogsNavigator.showServerErrorDialog()
    }

    override fun onBackClicked() {
        screensNavigator.navigateBack()
    }

    companion object {
        const val EXTRA_QUESTION_ID = "EXTRA_QUESTION_ID"
        fun start(context: Context, questionId: String) {
            val intent = Intent(context, QuestionDetailsActivity::class.java)
            intent.putExtra(EXTRA_QUESTION_ID, questionId)
            context.startActivity(intent)
        }
    }
}