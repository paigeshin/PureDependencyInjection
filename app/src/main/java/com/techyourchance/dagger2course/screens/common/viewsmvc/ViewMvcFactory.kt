package com.techyourchance.dagger2course.screens.common.viewsmvc

import android.view.LayoutInflater
import android.view.ViewGroup
import com.techyourchance.dagger2course.screens.questionslist.QuestionListViewMvc

class ViewMvcFactory(private val layoutInflater: LayoutInflater) {
    fun newQuestionsListViewMvc(parent: ViewGroup?): QuestionListViewMvc {
        return QuestionListViewMvc(layoutInflater, parent)
    }
}