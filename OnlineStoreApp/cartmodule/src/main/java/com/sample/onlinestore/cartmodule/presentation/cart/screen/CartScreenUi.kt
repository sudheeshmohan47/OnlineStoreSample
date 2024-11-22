package com.sample.onlinestore.cartmodule.presentation.cart.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.sample.designsystem.components.OnlineStoreTopAppBar
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.cartmodule.R
import com.sample.onlinestore.cartmodule.domain.model.CartItem
import com.sample.onlinestore.cartmodule.presentation.cart.CartAction
import com.sample.onlinestore.cartmodule.presentation.cart.CartUiModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTopAppBarSection(
    topAppBarState: TopAppBarState,
    onAction: (CartAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnlineStoreTopAppBar(
        modifier = modifier,
        topAppBarState = topAppBarState,
        title = stringResource(R.string.title_cart),
        titleStyle = MaterialTheme.typography.titleLarge,
        titleFontWeight = FontWeight.Normal,
        titleColor = MaterialTheme.colorScheme.onBackground,
        displayBackNavigation = true,
        titleAlignment = TextAlign.Center,
        backgroundColor = MaterialTheme.colorScheme.background,
        onBackNavigationClicked = {
            onAction(CartAction.OnClickBackNavigation)
        }
    )
}

@Composable
fun CartScreenContent(
    cartUiState: UiState<CartUiModel>,
    screenWidth: Dp,
    onAction: (CartAction) -> Unit,
    modifier: Modifier = Modifier,
    cartListState: LazyListState = rememberLazyListState(),
) {
    cartUiState.data?.let { cartUiModel ->
        val cartItems = cartUiModel.cartItems
        LazyColumn(
            state = cartListState,
            modifier = modifier,
            contentPadding = PaddingValues(vertical = OnlineStoreSpacing.SMALL.dp()),
            verticalArrangement = Arrangement.spacedBy(OnlineStoreSpacing.EXTRA_SMALL.dp()),
        ) {
            items(cartItems) { productItem ->
                CartItem(
                    cartItem = productItem,
                    screenWidth = screenWidth,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
fun CartItem(
    cartItem: CartItem,
    screenWidth: Dp,
    onAction: (CartAction) -> Unit,
    modifier: Modifier = Modifier
) {
    // Image size set to 20% of the screen width, making it square
    val imageWidthSize = screenWidth * 0.2f
    Card(
        modifier = modifier
            .padding(OnlineStoreSpacing.EXTRA_SMALL.dp()),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(OnlineStoreSpacing.SMALL.dp()),
        colors = CardDefaults.cardColors()
            .copy(containerColor = MaterialTheme.colorScheme.onTertiary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(100.dp)
        ) {
            // Main image
            AsyncImage(
                modifier = Modifier
                    .width(imageWidthSize)
                    .aspectRatio(1f)
                    .clickable {
                        onAction(CartAction.OnClickCartImage(cartItem.productId))
                    },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cartItem.image.ifEmpty { com.sample.designsystem.R.drawable.placeholder })
                    .crossfade(true)
                    .scale(Scale.FIT)
                    .error(com.sample.designsystem.R.drawable.placeholder)
                    .build(),
                placeholder = painterResource(com.sample.designsystem.R.drawable.placeholder),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(OnlineStoreSpacing.SMALL.dp()))

            // Details Section
            Column(
                modifier = Modifier
                    .padding(vertical = OnlineStoreSpacing.SMALL.dp())
            ) {
                Text(
                    text = cartItem.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Quantity
                Text(
                    text = "Qty: ${cartItem.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = OnlineStoreSpacing.SMALL.dp())
                )

                // Remove from Cart Text (Clickable)
                Text(
                    text = "Remove from Cart",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.clickable {
                        onAction(CartAction.RemoveItemFromCart(cartItem.productId))
                    }
                )
            }
        }
    }
}