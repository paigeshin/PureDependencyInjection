# AOS_Kotlin_dagger2_refactoring_project

# Refactoring Guide

- One Activity => Mvc + Dialog + Network + Navigator
- RootComposition, Application Layer => Initialize global instance
- AppComposition, Activity Layer => Initialize Activity related objects (ScreenNavigators, Dialog)

# Basic Refactoring

### Goals

- Activity ⇒ UserInterface + Network + Dialog + Navigator
- Extra Abstraction needed where there are code duplications ⇒ but be careful not to generalize too much.

### How does the code look after code separation?

```kotlin
private lateinit var viewMvc: QuestionListViewMvc
private lateinit var fetchQuestionsUseCase: FetchQuestionsUseCase
private lateinit var dialogsNavigator: DialogsNavigator
private lateinit var screensNavigator: ScreensNavigator
```

### Look how it seems

- QuestionsListActivity
  - ScreensNavigator ⇒ encapsulates screens management logic
  - QuestionsListViewMvc ⇒ encapsulates UI Logic
  - DialogsNavigator ⇒ encapsulates dialogs management logic
  - FetchQuestionsUseCase ⇒ encapsulates networking logic

### MVC

- QuestionsListActivity ⇒ Controller
- FetchQuestionsUseCase ⇒ Model
- QuestionsListViewMvc ⇒ View

# Law of Demeter

"Only talk to your immediate friends"

⇒ Make sure that it only uses the dependency that it really needs and it doesn't have other dependencies.

- Principle of least knowledge
- Minimize class dependencies
- Makes code more readable and maintainable

# Initialize value when called

```kotlin
@UiThread
class AppCompositionRoot {

    private var _retrofit: Retrofit? = null

    // init retrofit
    private val retrofit = if(_retrofit == null) {
        _retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        _retrofit!!
    } else {
        _retrofit!!
    }

    // init stackoverflow API
    public val stackoverflowApi = retrofit.create(StackoverflowApi::class.java)

    // Object should be stateful
    // When you want to share a object and multiple clients try to access it, that might be harmful.
    // However, if you put get(), this getter function will be invoked and will create a new instance of which questions to use.
    // `Just make sure you put `getter` when you want to make some object sharable between multiple clients.`
    public val fetchQuestionsUseCase get() = FetchQuestionsUseCase(stackoverflowApi)
    public val fetchQuestionDetailUseCase get() = FetchQuestionDetailsUseCase(stackoverflowApi)
}
```

# Using `lazy` keyword

- with global services, you need to use `lazy` keyword.
- when `lazy` is used for global scope ⇒ single instance.
- Whenever you want your services to be global, just use `lazy` keyword.

```kotlin
@UiThread
class AppCompositionRoot {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    // init stackoverflow API
    public val stackoverflowApi: StackoverflowApi by lazy {
        retrofit.create(StackoverflowApi::class.java)
    }

    // Object should be stateful
    // When you want to share a object and multiple clients try to access it, that might be harmful.
    // However, if you put get(), this getter function will be invoked and will create a new instance of which questions to use.
    // `Just make sure you put `getter` when you want to make some object sharable between multiple clients.`
    public val fetchQuestionsUseCase get() = FetchQuestionsUseCase(stackoverflowApi)
    public val fetchQuestionDetailUseCase get() = FetchQuestionDetailsUseCase(stackoverflowApi)
}
```

### `lazy` in different syntax

```kotlin
open class BaseActivity: AppCompatActivity() {
    val compositionRoot by lazy {
        ActivityCompositionRoot(this)
    }
}
```

⇒ In this case, `compositionRoot` which is created with `lazy` keyword, is not a singleton.

⇒ Activities which inherit BaseActivity will have its own `compositionRoot` for each activity.

# AppCompositionRoot

- Retrofit
- Stackoverflow API

# ActivityCompositionRoot

- Network UseCases

# Version Control

v.0.0.1

- Code Separation in `QuestionsListActivity` and `QuestionsDetailActivity`
- Base Class for removing code duplications on `QuestionsListMvc` and `QuestionsDetailMvc`

v.0.0.2

- FetchQuestionsUseCase
- FetchQuestionDetailUseCase

v.0.0.3

- DialogNavigator

v.0.0.4

- ScreenNavigator

v.0.0.5

- Share same retrofit instance using MyApplication

v.0.0.6

- passing stackoverflow api instead of retrofit

v.0.0.7

- Applied `Law of Demeter`

v.0.0.8

- Created app composition class in order to organize code for less dependency

v.0.0.9

- Global Service refactoring using `lazy` keyword and `get()` keyword

v.0.0.10

- Create AppCompositionRoot

AppCompositionRoot (Application Scope) <-> ActivityCompositionRoot(Activity Scope)

v.0.0.11

- refactored to fragment to show reusablility of MVC
