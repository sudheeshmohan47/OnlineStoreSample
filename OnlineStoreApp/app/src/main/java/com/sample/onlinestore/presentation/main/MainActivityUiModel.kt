package com.sample.onlinestore.presentation.main

import com.sample.onlinestore.commonmodule.foundation.base.Action
import com.sample.onlinestore.commonmodule.foundation.base.Event

data class MainActivityUiModel(
    val showAppExitDialog: Boolean = false,
    val selectedItemIndex: Int = 0
)

sealed class MainActivityAction : Action {
    data class ShowAppExitDialog(val showAppExitDialog: Boolean) : MainActivityAction()
    data class OnNavigationSelected(val selectedItemIndex: Int) : MainActivityAction()
}

sealed class MainActivityEvent : Event
