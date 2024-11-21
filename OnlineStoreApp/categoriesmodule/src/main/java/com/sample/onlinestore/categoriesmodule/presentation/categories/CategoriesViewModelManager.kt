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
        // Launching coroutine in the IO dispatcher for background work (network/database operations)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Send loading state to UI while fetching the categories
                sendState(UiState.Loading(currentState.data))
                // Fetch categories from the use case
                categoriesUseCase.fetchCategories { isSuccessFul, domainResponse ->
                    if (isSuccessFul) {
                        domainResponse.data?.let {
                            // If data fetch is successful, update the UI state with the new categories
                            sendState(
                                UiState.Result(
                                    currentState.data?.copy(
                                        categories = it,
                                        // Set swipe refreshing to false after data is fetched
                                        isSwipeRefreshing = false,
                                        isInitialLoadingCompleted = true
                                    )
                                )
                            )
                        }
                    }
                }
            } catch (exception: DomainException) {
                // Handle exceptions and update the UI with appropriate error messages
                handleException(exception, currentState)
            }
        }
    }

    /**
     * Handles toggling the selection state of a category.
     * It updates the selected state and saves the selected categories to the database.
     *
     * @param categoryItem The category that was selected/deselected.
     * @param currentState The current UI state to update with the new list of categories.
     */
    fun handleToggleCategoriesSelection(
        categoryItem: CategoryItem,
        currentState: UiState<CategoriesUiModel>
    ) {
        // Get the current categories list from the UI state
        val currentCategories = currentState.data?.categories ?: emptyList()

        // Update the selected state of the clicked category
        val updatedCategories = currentCategories.map {
            if (it.category == categoryItem.category) {
                it.copy(isSelected = !it.isSelected) // Toggle the selection state
            } else {
                it
            }
        }
        // Create a new UI state with the updated list of categories
        val updatedUiState = currentState.data?.copy(
            categories = updatedCategories
        )

        // Launch a coroutine to update the database in the background
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Save to the database only the categories that are selected
                categoriesUseCase.updateSelectedCategories(updatedCategories.filter { it.isSelected })
                // Update the UI state with the new list of categories
                sendState(UiState.Result(updatedUiState))
            } catch (exception: DomainException) {
                handleException(exception, currentState)
            }
        }
    }

    /**
     * Handles exceptions that occur during fetching categories or handling category updates.
     * Based on the exception type, it updates the UI with the appropriate error message or action.
     *
     * @param exception The exception to handle.
     * @param currentState The current UI state to maintain while handling the exception.
     */
    private fun handleException(
        exception: DomainException,
        currentState: UiState<CategoriesUiModel>
    ) {
        // Set Swipe refresh as false during error state
        val updatedUiState =
            currentState.data?.copy(isSwipeRefreshing = false, isInitialLoadingCompleted = true)

        when (exception) {
            is UnauthorizedException -> {
                // If unauthorized, send the updated state and possibly trigger session end
                sendState(UiState.Result(updatedUiState))
                // sendEvent(ProductsListingEvent.EndUserSession) // Handle session expiration if necessary
            }

            else -> {
                // Map the exception to a user-friendly error message
                val errorMessage = mapErrorMessage(exception)
                // Send the updated UI state along with the error message to the UI
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
