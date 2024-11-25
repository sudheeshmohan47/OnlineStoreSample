package com.sample.wishlistmodule.presentation.wishlist

import androidx.lifecycle.viewModelScope
import com.sample.onlinestore.commonmodule.foundation.base.BaseViewModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.wishlistmodule.domain.WishlistUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

val wishlistViewModelCreationCallback =
    { factory: WishlistViewModel.WishlistViewModelFactory ->
        factory.create()
    }

@HiltViewModel(assistedFactory = WishlistViewModel.WishlistViewModelFactory::class)
class WishlistViewModel @AssistedInject constructor(
    wishlistUseCase: WishlistUseCase,
    @Assisted initialScreenState: UiState<WishlistUiModel>
) : BaseViewModel<UiState<WishlistUiModel>, WishlistAction, WishlistEvent>(
    initialScreenState
) {

    // Manager class for managing viewmodel business logic
    private val wishlistViewModelManager =
        WishlistViewModelManager(
            wishlistUseCase,
            viewModelScope,
            ::sendState,
            ::sendEvent
        )

    @AssistedFactory
    interface WishlistViewModelFactory {
        fun create(
            initialScreenState: UiState<WishlistUiModel> = UiState.Result(
                WishlistUiModel()
            )
        ): WishlistViewModel
    }

    override fun reduce(
        currentState: UiState<WishlistUiModel>,
        action: WishlistAction
    ): UiState<WishlistUiModel> {
        return handleReducerAction(currentState = currentState, action = action)
    }

    private fun handleReducerAction(
        action: WishlistAction,
        currentState: UiState<WishlistUiModel>
    ): UiState<WishlistUiModel> {

        var returnState = currentState
        when (action) {
            // Add specific Wishlist actions here
            else -> Unit
        }
        return returnState
    }

    override fun runSideEffect(
        action: WishlistAction,
        currentState: UiState<WishlistUiModel>
    ) {
        when (action) {
            is WishlistAction.OnClickWishListedItem -> {
                sendEvent(WishlistEvent.LoadProductDetailScreen(action.productId))
            }

            is WishlistAction.RefreshData -> {
                wishlistViewModelManager.fetchWishlistData(uiState.value)
            }

            is WishlistAction.OnClickRemoveFromWishlist -> {
                wishlistViewModelManager.removeFromWishlist(
                    uiState.value,
                    action.productId
                )
            }

            is WishlistAction.OnClickCart -> {
                sendEvent(WishlistEvent.GotoCartScreen)
            }

            is WishlistAction.OnClickMoveToCart -> {
                wishlistViewModelManager.moveToCart(uiState.value, action.productId)
            }

            else -> Unit
        }
    }
}
