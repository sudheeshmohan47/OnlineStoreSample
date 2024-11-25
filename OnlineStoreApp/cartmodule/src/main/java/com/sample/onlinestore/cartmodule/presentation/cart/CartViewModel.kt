package com.sample.onlinestore.cartmodule.presentation.cart

import androidx.lifecycle.viewModelScope
import com.sample.onlinestore.cartmodule.domain.CartUseCase
import com.sample.onlinestore.commonmodule.foundation.base.BaseViewModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

val cartViewModelCreationCallback =
    { factory: CartViewModel.CartViewModelFactory ->
        factory.create()
    }

@HiltViewModel(assistedFactory = CartViewModel.CartViewModelFactory::class)
class CartViewModel @AssistedInject constructor(
    cartUseCase: CartUseCase,
    @Assisted initialScreenState: UiState<CartUiModel>
) : BaseViewModel<UiState<CartUiModel>, CartAction, CartEvent>(
    initialScreenState
) {

    // Manager class for managing ViewModel business logic
    private val cartViewModelManager = CartViewModelManager(
        cartUseCase,
        viewModelScope,
        ::sendState,
        ::sendEvent
    )

    @AssistedFactory
    interface CartViewModelFactory {
        fun create(
            initialScreenState: UiState<CartUiModel> = UiState.Loading(
                CartUiModel()
            )
        ): CartViewModel
    }

    override fun reduce(
        currentState: UiState<CartUiModel>,
        action: CartAction
    ): UiState<CartUiModel> {
        return handleReducerAction(currentState = currentState, action = action)
    }

    @SuppressWarnings("UnusedParameter")
    private fun handleReducerAction(
        action: CartAction,
        currentState: UiState<CartUiModel>
    ): UiState<CartUiModel> {
        var returnState = currentState

        return returnState
    }

    override fun runSideEffect(
        action: CartAction,
        currentState: UiState<CartUiModel>
    ) {
        when (action) {
            is CartAction.OnClickProduct -> {
                sendEvent(CartEvent.LoadProductDetailScreen(action.productId))
            }

            is CartAction.RefreshData -> {
                cartViewModelManager.fetchCartData(uiState.value)
            }

            is CartAction.RemoveItemFromCart -> {
                cartViewModelManager.removeItemFromCart(currentState, action.productId)
            }

            CartAction.OnClickBackNavigation -> {
                sendEvent(CartEvent.BackToPreviousScreen)
            }
        }
    }
}
