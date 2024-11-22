package com.sample.onlinestore.productsmodule.presentation.productslisting

import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.exception.UnauthorizedException
import com.sample.onlinestore.commonmodule.domain.exception.mapErrorMessage
import com.sample.onlinestore.commonmodule.domain.model.Message
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel manager for handling product listing, wishlist actions, and UI state updates.
 * Manages fetching products, adding/removing from wishlist, and error handling.
 *
 * @param productsUseCase The use case for fetching product data and interacting with the wishlist.
 * @param viewModelScope The CoroutineScope for launching background tasks.
 * @param sendState A function to update the UI state with [UiState] containing [ProductsListingUiModel].
 */
class ProductListingViewModelManager(
    private val productsUseCase: ProductsUseCase,
    private val viewModelScope: CoroutineScope,
    private val sendState: (UiState<ProductsListingUiModel>) -> Unit,
    private val sendEvent: (ProductsListingEvent) -> Unit
) {
    /**
     * Fetches the product data and updates the UI state.
     *
     * @param currentState The current state of the UI to be updated.
     */
    fun fetchProductsData(currentState: UiState<ProductsListingUiModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendState(UiState.Loading(currentState.data))
                productsUseCase.getProducts { isSuccessFul, domainResponse ->
                    if (isSuccessFul) {
                        domainResponse.data?.let {
                            sendState(
                                UiState.Result(
                                    currentState.data?.copy(
                                        products = it,
                                        isSwipeRefreshing = false,
                                        isInitialLoadingCompleted = true
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

    /**
     * Adds or removes a product from the wishlist and updates the UI optimistically.
     *
     * @param currentState The current UI state that contains the list of products.
     * @param originalProduct The product to be added or removed from the wishlist.
     */
    fun addOrRemoveProductToWishList(
        currentState: UiState<ProductsListingUiModel>,
        originalProduct: ProductItem
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val isAdding = !originalProduct.isWishListed
            val updatedProduct = originalProduct.copy(isWishListed = isAdding)

            // Optimistically update UI
            updateProductInList(currentState, updatedProduct)

            if (isAdding) {
                productsUseCase.addToWishlist(originalProduct.productId) { isSuccess ->
                    handleWishListApiResponse(currentState, isSuccess, originalProduct)
                }
            } else {
                productsUseCase.removeFromWishlist(originalProduct.productId) { isSuccess ->
                    handleWishListApiResponse(currentState, isSuccess, originalProduct)
                }
            }
        }
    }

    /**
     * Handles the response from adding/removing product to/from the wishlist.
     * If the API call fails, it reverts the UI to its previous state.
     *
     * @param currentState The current UI state containing the list of products.
     * @param isSuccess Indicates whether the wishlist API operation was successful.
     * @param originalProduct The original product before the wishlist operation.
     */
    private fun handleWishListApiResponse(
        currentState: UiState<ProductsListingUiModel>,
        isSuccess: Boolean,
        originalProduct: ProductItem
    ) {
        if (!isSuccess) {
            // if wishlist api fails revert back to original state
            updateProductInList(currentState, originalProduct) // Revert on failure
            sendState(
                UiState.Result(
                    currentState.data,
                    Message(com.sample.wishlistmodule.R.string.error_failed_to_add_wishlist)
                )
            )
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
        currentState: UiState<ProductsListingUiModel>
    ) {

        val updatedUiState =
            currentState.data?.copy(isSwipeRefreshing = false, isInitialLoadingCompleted = true)
        when (exception) {
            is UnauthorizedException -> {
                sendState(UiState.Result(updatedUiState))
                // sendEvent(ProductsListingEvent.EndUserSession)
            }

            else -> {
                val errorMessage = mapErrorMessage(exception)
                sendState(
                    UiState.Result(
                        updatedUiState
                    )
                )
                sendEvent(
                    ProductsListingEvent.ShowMessage(
                        Message(
                            errorMessage.messageResId,
                            errorMessage.message
                        )
                    )
                )
            }
        }
    }

    /**
     * Updates a specific product in the list of products in the UI state.
     *
     * @param currentState The current state containing the list of products.
     * @param product The updated product to be reflected in the UI state.
     */
    private fun updateProductInList(
        currentState: UiState<ProductsListingUiModel>,
        product: ProductItem
    ) {
        val updatedList = currentState.data?.products?.map {
            if (it.productId == product.productId) product else it
        }.orEmpty()
        sendState(UiState.Result(currentState.data?.copy(products = updatedList)))
    }
}
