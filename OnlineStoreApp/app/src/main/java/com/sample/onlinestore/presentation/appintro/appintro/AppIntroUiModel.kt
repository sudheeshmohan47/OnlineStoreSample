package com.sample.onlinestore.presentation.appintro.appintro

import com.sample.onlinestore.commonmodule.foundation.base.Action
import com.sample.onlinestore.commonmodule.foundation.base.Event

data class AppIntroUiModel(val appIntroPages: List<AppIntroPage> = emptyList())

sealed class AppIntroAction : Action {
    data object LoadIntroPages : AppIntroAction()
    data object OnNextButtonTapped : AppIntroAction()
}

sealed class AppIntroEvent : Event
