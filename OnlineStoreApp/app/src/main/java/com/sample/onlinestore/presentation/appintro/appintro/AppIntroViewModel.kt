package com.sample.onlinestore.presentation.appintro.appintro

import androidx.lifecycle.viewModelScope
import com.sample.datastoragemodule.domain.PreferenceManagerRepository
import com.sample.onlinestore.R
import com.sample.onlinestore.commonmodule.foundation.base.BaseViewModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

val appIntroViewModelCreationCallback = { factory: AppIntroViewModel.AppIntroViewModelFactory ->
    factory.create()
}

@HiltViewModel(assistedFactory = AppIntroViewModel.AppIntroViewModelFactory::class)
class AppIntroViewModel @AssistedInject constructor(
    private val preferenceManagerRepository: PreferenceManagerRepository,
    @Assisted initialScreenState: UiState<AppIntroUiModel> = UiState.Loading(AppIntroUiModel())
) : BaseViewModel<UiState<AppIntroUiModel>, AppIntroAction, AppIntroEvent>(initialScreenState) {

    init {
        sendAction(AppIntroAction.LoadIntroPages)
    }

    @AssistedFactory
    interface AppIntroViewModelFactory {
        fun create(initialScreenState: UiState<AppIntroUiModel> = UiState.Loading(AppIntroUiModel())): AppIntroViewModel
    }

    /**
     * This function is for handling various ui data changes in compose
     */
    override fun reduce(
        currentState: UiState<AppIntroUiModel>,
        action: AppIntroAction
    ): UiState<AppIntroUiModel> {
        var returnState = currentState
        when (action) {
            is AppIntroAction.LoadIntroPages -> {
                if (currentState is UiState.Loading<AppIntroUiModel>) {
                    returnState =
                        UiState.Result(currentState.data?.copy(appIntroPages = getOnBoardPages()))
                }
            }

            else -> Unit
        }

        return returnState
    }

    /**
     * This function is used for handling events like navigate to next screen,
     * get results from backend and show something wtc
     */
    override fun runSideEffect(action: AppIntroAction, currentState: UiState<AppIntroUiModel>) {
        when (action) {
            is AppIntroAction.LoadNextScreen -> {
                // Save app intro finished status to preference
                saveAppIntroStatus()
                sendEvent(AppIntroEvent.LoadNextScreen)
            }

            else -> Unit
        }
    }

    private fun saveAppIntroStatus() {
        viewModelScope.launch {
            preferenceManagerRepository.saveAppIntroFinishedStatus(true)
        }
    }

    // Returns the list of pages for the horizontal pager
    private fun getOnBoardPages(): List<AppIntroPage> {
        return listOf(
            AppIntroPage(
                imageId = R.drawable.img_app_intro_1,
                titleResId = R.string.app_intro_title_1,
                subTitleResId = R.string.app_intro_subtitle_1,
            ),
            AppIntroPage(
                imageId = R.drawable.img_app_intro_2,
                titleResId = R.string.app_intro_title_2,
                subTitleResId = R.string.app_intro_subtitle_2,
            ),
            AppIntroPage(
                imageId = R.drawable.img_app_intro_3,
                titleResId = R.string.app_intro_title_3,
                subTitleResId = R.string.app_intro_subtitle_3,
            )
        )
    }
}
