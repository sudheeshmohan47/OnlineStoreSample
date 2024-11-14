package com.sample.onlinestore.presentation.main

import com.sample.onlinestore.commonmodule.foundation.base.BaseViewModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

val mainActivityViewModelCreationCallback =
    { factory: MainActivityViewModel.MainActivityViewModelFactory ->
        factory.create()
    }

@HiltViewModel(assistedFactory = MainActivityViewModel.MainActivityViewModelFactory::class)
class MainActivityViewModel @AssistedInject constructor(
    @Assisted initialScreenState: UiState<MainActivityUiModel>
) : BaseViewModel<UiState<MainActivityUiModel>, MainActivityAction, MainActivityEvent>(
    initialScreenState
) {

    @AssistedFactory
    interface MainActivityViewModelFactory {
        fun create(
            initialScreenState: UiState<MainActivityUiModel> = UiState.Result(
                MainActivityUiModel()
            )
        ): MainActivityViewModel
    }

    override fun reduce(
        currentState: UiState<MainActivityUiModel>,
        action: MainActivityAction
    ): UiState<MainActivityUiModel> {
        return handleReducerAction(currentState = currentState, action = action)
    }

    private fun handleReducerAction(
        action: MainActivityAction,
        currentState: UiState<MainActivityUiModel>
    ): UiState<MainActivityUiModel> {

        var returnState = currentState
        when (action) {
            is MainActivityAction.ShowAppExitDialog -> {
                returnState = UiState.Result(
                    currentState.data?.copy(
                        showAppExitDialog = action.showAppExitDialog
                    )
                )
            }

            is MainActivityAction.OnNavigationSelected -> {
                returnState =
                    UiState.Result(
                        currentState.data?.copy(
                            selectedItemIndex = action.selectedItemIndex
                        )
                    )
            }
        }
        return returnState
    }

    override fun runSideEffect(
        action: MainActivityAction,
        currentState: UiState<MainActivityUiModel>
    ) {
        // Handle Side effects here
    }
}
