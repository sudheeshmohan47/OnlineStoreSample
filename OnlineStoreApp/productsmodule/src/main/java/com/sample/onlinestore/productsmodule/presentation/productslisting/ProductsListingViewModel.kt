package com.sample.onlinestore.productsmodule.presentation.productslisting

import androidx.lifecycle.viewModelScope
import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.exception.UnauthorizedException
import com.sample.onlinestore.commonmodule.domain.exception.mapErrorMessage
import com.sample.onlinestore.commonmodule.domain.model.ErrorMessage
import com.sample.onlinestore.commonmodule.foundation.base.BaseViewModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.R
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import com.sample.wishlistmodule.domain.WishlistUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val productsListingViewModelCreationCallback =
    { factory: ProductsListingViewModel.ProductsListingViewModelFactory ->
        factory.create()
    }

@HiltViewModel(assistedFactory = ProductsListingViewModel.ProductsListingViewModelFactory::class)
class ProductsListingViewModel @AssistedInject constructor(
    private val productsUseCase: ProductsUseCase,
    private val wishlistUseCase: WishlistUseCase,
    @Assisted initialScreenState: UiState<ProductsListingUiModel>
) : BaseViewModel<UiState<ProductsListingUiModel>, ProductsListingAction, ProductsListingEvent>(
    initialScreenState
) {

    @AssistedFactory
    interface ProductsListingViewModelFactory {
        fun create(
            initialScreenState: UiState<ProductsListingUiModel> = UiState.Result(
                ProductsListingUiModel()
            )
        ): ProductsListingViewModel
    }

    override fun reduce(
        currentState: UiState<ProductsListingUiModel>,
        action: ProductsListingAction
    ): UiState<ProductsListingUiModel> {
        return handleReducerAction(currentState = currentState, action = action)
    }

    private fun handleReducerAction(
        action: ProductsListingAction,
        currentState: UiState<ProductsListingUiModel>
    ): UiState<ProductsListingUiModel> {

        var returnState = currentState
        when (action) {

            is ProductsListingAction.SetSwipeRefreshingStatus -> {
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
        action: ProductsListingAction,
        currentState: UiState<ProductsListingUiModel>
    ) {
        when (action) {
            is ProductsListingAction.OnClickProduct -> {
                sendEvent(ProductsListingEvent.LoadProductDetailScreen(action.product.productId))
            }

            is ProductsListingAction.RefreshData -> {
                fetchProductsData()
            }

            is ProductsListingAction.OnClickFavourite -> {
                addOrRemoveProductToWishList(action.product)
            }

            else -> Unit
        }
    }

    private fun fetchProductsData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendState(UiState.Loading(uiState.value.data))
                productsUseCase.getProducts { isSuccessFul, domainResponse ->
                    if (isSuccessFul) {
                        domainResponse.data?.let {
                            sendState(
                                UiState.Result(
                                    uiState.value.data?.copy(
                                        products = it,
                                        isSwipeRefreshing = false
                                    )
                                )
                            )
                        }
                    }
                }
            } catch (exception: DomainException) {
                handleException(exception, uiState.value)
            }
        }
    }

    private fun addOrRemoveProductToWishList(product: ProductItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val isAdding = !product.isWishListed
            val updatedProduct = product.copy(isWishListed = isAdding)
            updateProductInList(updatedProduct) // Optimistically update UI
            sendState(UiState.Loading(uiState.value.data))

            if (isAdding) {
                wishlistUseCase.addToWishlist(product.productId) { isSuccess ->
                    handleApiResponse(isSuccess, product)
                }
            } else {
                wishlistUseCase.removeFromWishlist(product.productId) { isSuccess, _ ->
                    handleApiResponse(isSuccess, product)
                }
            }
        }
    }

    private fun handleApiResponse(isSuccess: Boolean, originalProduct: ProductItem) {
        if (!isSuccess) {
            updateProductInList(originalProduct) // Revert on failure
            sendState(UiState.Result(
                uiState.value.data,
                ErrorMessage(com.sample.wishlistmodule.R.string.error_failed_to_add_wishlist)
            ))
        }
    }

    private fun handleException(
        exception: DomainException,
        currentState: UiState<ProductsListingUiModel>
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

    // Helper function to update the product in the list
    private fun updateProductInList(product: ProductItem) {
        val updatedList = uiState.value.data?.products?.map {
            if (it.productId == product.productId) product else it
        }.orEmpty()
        sendState(UiState.Result(uiState.value.data?.copy(products = updatedList)))
    }
}
