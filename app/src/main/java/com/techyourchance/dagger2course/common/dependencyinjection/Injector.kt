package com.techyourchance.dagger2course.common.dependencyinjection

import com.techyourchance.dagger2course.questions.FetchQuestionDetailsUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionsUseCase
import com.techyourchance.dagger2course.screens.common.ScreensNavigator
import com.techyourchance.dagger2course.screens.common.dialogs.DialogsNavigator
import com.techyourchance.dagger2course.screens.common.viewsmvc.ViewMvcFactory
import java.lang.reflect.Field

class Injector(private val compositionRoot: PresentationCompositionRoot) {

    fun inject(client: Any) {
        for(field in getAllFields(client)){
            if(inAnnotatedForInjection(field)) {
                injectField(client, field)
            }
        }
    }
    private fun getAllFields(client: Any): Array<out Field> {
        val clientClass = client::class.java
        return clientClass.declaredFields
    }

    private fun inAnnotatedForInjection(field: Field): Boolean {
        val fieldAnnotations = field.annotations
        for(annotation in fieldAnnotations) {
            if(annotation is Service) {
                return true
            }
        }
        return false
    }

    private fun injectField(client: Any, field: Field) {
        val isAccessibleInitially = field.isAccessible
        field.isAccessible = true
        field.set(client, getServiceForClass(field.type))
        field.isAccessible = isAccessibleInitially
    }

    private fun getServiceForClass(type: Class<*>): Any {
        when(type) {
            DialogsNavigator::class.java -> {
                return compositionRoot.dialogsNavigator
            }
            ScreensNavigator::class.java -> {
                return compositionRoot.screensNavigator
            }
            FetchQuestionsUseCase::class.java -> {
                return compositionRoot.fetchQuestionsUseCase
            }
            FetchQuestionDetailsUseCase::class.java -> {
                return compositionRoot.fetchQuestionDetailUseCase
            }
            ViewMvcFactory::class.java -> {
                return compositionRoot.viewMvcFactory
            }
            else -> {
                throw Exception("Unsupported Service Type: $type")
            }
        }
    }

}