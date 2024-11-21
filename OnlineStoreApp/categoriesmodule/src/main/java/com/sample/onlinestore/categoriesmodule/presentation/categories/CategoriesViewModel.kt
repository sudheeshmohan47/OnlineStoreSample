package com.sample.onlinestore.categoriesmodule.presentation.categories

import androidx.lifecycle.viewModelScope
import com.sample.onlinestore.categoriesmodule.domain.CategoriesUseCase
import com.sample.onlinestore.categoriesmodule.domain.model.CategoryItem
import com.sample.onlinestore.commonmodule.foundation.base.BaseViewModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber

val categoriesViewModelCreationCallback =
    { factory: CategoriesViewModel.CategoriesViewModelFactory ->
        factory.create()
    }

@HiltViewModel(assistedFactory = CategoriesViewModel.CategoriesViewModelFactory::class)
class CategoriesViewModel @AssistedInject constructor(
    categoriesUseCase: CategoriesUseCase,
    @Assisted initialScreenState: UiState<CategoriesUiModel>
) : BaseViewModel<UiState<CategoriesUiModel>, CategoriesAction, CategoriesEvent>(
    initialScreenState
) {

    // Manager class for managing viewmodel business logic
    private val categoriesViewModelManager = CategoriesViewModelManager(
        categoriesUseCase,
        viewModelScope,
        ::sendState,
    )

    @AssistedFactory
    interface CategoriesViewModelFactory {
        fun create(
            initialScreenState: UiState<CategoriesUiModel> = UiState.Result(
                CategoriesUiModel()
            )
        ): CategoriesViewModel
    }

    override fun reduce(
        currentState: UiState<CategoriesUiModel>,
        action: CategoriesAction
    ): UiState<CategoriesUiModel> {
        return handleReducerAction(currentState = currentState, action = action)
    }

    private fun handleReducerAction(
        action: CategoriesAction,
        currentState: UiState<CategoriesUiModel>
    ): UiState<CategoriesUiModel> {

        var returnState = currentState
        when (action) {

            is CategoriesAction.SetSwipeRefreshingStatus -> {
                returnState = UiState.Result(
                    currentState.data?.copy(
                        isSwipeRefreshing = action.isRefreshing
                    )
                )
            }
            else -> Unit
        }
        return returnState
    }

    override fun runSideEffect(
        action: CategoriesAction,
        currentState: UiState<CategoriesUiModel>
    ) {
        when (action) {

            is CategoriesAction.RefreshData -> {
                categoriesViewModelManager.fetchCategoriesData(uiState.value)
            }

            is CategoriesAction.ToggleCategoriesSelection -> {
                categoriesViewModelManager.handleToggleCategoriesSelection(
                    categoryItem = action.categoryItem,
                    currentState = currentState
                )
            }

            else -> Unit
        }
    }
}
