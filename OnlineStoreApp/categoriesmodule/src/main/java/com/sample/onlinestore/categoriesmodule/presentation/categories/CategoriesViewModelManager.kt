package com.sample.onlinestore.categoriesmodule.presentation.categories

import com.sample.onlinestore.categoriesmodule.domain.CategoriesUseCase
import com.sample.onlinestore.categoriesmodule.domain.model.CategoryItem
import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.exception.UnauthorizedException
import com.sample.onlinestore.commonmodule.domain.exception.mapErrorMessage
import com.sample.onlinestore.commonmodule.domain.model.ErrorMessage
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModelManager for managing category-related operations and UI state.
 * It handles fetching category data, toggling category selection, and updating the UI state accordingly.
 *
 * @param categoriesUseCase Use case for fetching and updating categories data.
 * @param viewModelScope The scope in which to launch the coroutine for background tasks.
 * @param sendState A function to send updated UI states to the UI layer.
 */
class CategoriesViewModelManager(
    private val categoriesUseCase: CategoriesUseCase,
    private val viewModelScope: CoroutineScope,
    private val sendState: (UiState<CategoriesUiModel>) -> Unit
) {
    /**
     * Fetches category data from the use case and updates the UI state.
     * It handles the loading, success, and error states.
     *
     * @param currentState The current UI state to maintain certain values during the loading phase.
     */
    fun fetchCategoriesData(currentState: UiState<CategoriesUiModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendState(UiState.Loading(currentState.data))
                categoriesUseCase.fetchCategories { isSuccessFul, domainResponse ->
                    if (isSuccessFul) {
                        domainResponse.data?.let {
                            sendState(
                                UiState.Result(
                                    currentState.data?.copy(
                                        categories = it,
                                        isSwipeRefreshing = false
                                    )
                                )
                            )
                        }
                    }
                }
            } catch (exception: DomainException) {
                handleException(exception, currentState)
            }
        }
    }

    fun handleToggleCategoriesSelection(
        categoryItem: CategoryItem,
        currentState: UiState<CategoriesUiModel>
    ) {
        val currentCategories = currentState.data?.categories ?: emptyList()

        // Update the selected state of the clicked category
        val updatedCategories = currentCategories.map {
            if (it.category == categoryItem.category) {
                it.copy(isSelected = !it.isSelected)
            } else {
                it
            }
        }
        // Update the UI state with the new list of categories
        val updatedUiState = currentState.data?.copy(
            categories = updatedCategories
        )
        // Update the database with the new selected categories
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Save to Database only the selected items
                categoriesUseCase.updateSelectedCategories(updatedCategories.filter { it.isSelected })
                sendState(UiState.Result(updatedUiState))
            } catch (exception: Exception) {
                // Revert to current state in case of error
                sendState(currentState)
            }
        }
    }


    /**
     * Handles exceptions thrown during the product fetching or wishlist operations.
     * Displays an appropriate error message based on the exception type.
     *
     * @param exception The caught exception to be handled.
     * @param currentState The current state of the UI.
     */
    private fun handleException(
        exception: DomainException,
        currentState: UiState<CategoriesUiModel>
    ) {

        val updatedUiState = currentState.data?.copy(isSwipeRefreshing = false)
        when (exception) {
            is UnauthorizedException -> {
                sendState(UiState.Result(updatedUiState))
                // sendEvent(ProductsListingEvent.EndUserSession)
            }

            else -> {
                val errorMessage = mapErrorMessage(exception)
                sendState(
                    UiState.Result(
                        updatedUiState,
                        ErrorMessage(errorMessage.messageResId, errorMessage.message)
                    )
                )
            }
        }
    }
}
