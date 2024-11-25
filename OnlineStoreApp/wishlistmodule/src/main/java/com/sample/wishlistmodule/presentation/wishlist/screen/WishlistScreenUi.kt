package com.sample.wishlistmodule.presentation.wishlist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.sample.designsystem.components.OnlineStoreButton
import com.sample.designsystem.components.OnlineStoreButtonVariant
import com.sample.designsystem.components.OnlineStoreTopAppBar
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.wishlistmodule.domain.model.WishlistItem
import com.sample.wishlistmodule.presentation.wishlist.WishlistAction
import com.sample.wishlistmodule.presentation.wishlist.WishlistUiModel

private const val ProductListingItemWidthPercentage = 0.40f
private const val ProductListingItemAspectRatio = 1.3f
private const val ProductListingGridColumnCount = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistTopAppBarSection(
    topAppBarState: TopAppBarState,
    onAction: (WishlistAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnlineStoreTopAppBar(
        modifier = modifier,
        topAppBarState = topAppBarState,
        title = stringResource(com.sample.onlinestore.commonmodule.R.string.label_bottom_nav_wishlist),
        titleStyle = MaterialTheme.typography.titleLarge,
        titleFontWeight = FontWeight.Normal,
        titleColor = MaterialTheme.colorScheme.onBackground,
        displayBackNavigation = false,
        titleAlignment = TextAlign.Center,
        backgroundColor = MaterialTheme.colorScheme.background,
        customActions = {
            IconButton(
                onClick = {
                    onAction(WishlistAction.OnClickCart)
                }
            ) {
                Icon(
                    modifier = Modifier.size(OnlineStoreSpacing.LARGE.dp()),
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
fun WishlistScreenContent(
    wishlistUiState: UiState<WishlistUiModel>,
    screenWidth: Dp,
    onAction: (WishlistAction) -> Unit,
    modifier: Modifier = Modifier,
    wishlistGridState: LazyGridState = rememberLazyGridState(),
) {
    wishlistUiState.data?.let { wishlistUiModel ->
        val wishlistItems = wishlistUiModel.wishListedProducts

        LazyVerticalGrid(
            state = wishlistGridState,
            modifier = modifier,
            columns = GridCells.Fixed(ProductListingGridColumnCount),
            contentPadding = PaddingValues(vertical = OnlineStoreSpacing.SMALL.dp()),
            verticalArrangement = Arrangement.spacedBy(OnlineStoreSpacing.EXTRA_SMALL.dp()),
            horizontalArrangement = Arrangement.spacedBy(OnlineStoreSpacing.EXTRA_SMALL.dp())
        ) {
            items(wishlistItems) { wishlistItem ->
                WishlistItemCard(
                    wishlistItem = wishlistItem,
                    onAction = onAction,
                    screenWidth = screenWidth,
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}

@Composable
fun WishlistItemCard(
    wishlistItem: WishlistItem,
    screenWidth: Dp,
    modifier: Modifier = Modifier,
    onAction: (WishlistAction) -> Unit
) {
    Card(
        modifier = modifier
            .width(screenWidth * ProductListingItemWidthPercentage)
            .padding(OnlineStoreSpacing.EXTRA_SMALL.dp())
            .clickable {
                onAction(WishlistAction.OnClickWishListedItem(wishlistItem.productId))
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(OnlineStoreSpacing.SMALL.dp()),
        colors = CardDefaults.cardColors()
            .copy(containerColor = MaterialTheme.colorScheme.onTertiary)
    ) {
        Box(
            modifier = Modifier
                .padding(OnlineStoreSpacing.SMALL.dp())
        ) {
            Column {
                // Main image
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(OnlineStoreSpacing.SMALL.dp()))
                        .fillMaxWidth()
                        .aspectRatio(ProductListingItemAspectRatio),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(wishlistItem.image.ifEmpty { com.sample.designsystem.R.drawable.placeholder })
                        .crossfade(true)
                        .scale(Scale.FIT)
                        .error(com.sample.designsystem.R.drawable.placeholder)
                        .build(),
                    placeholder = painterResource(com.sample.designsystem.R.drawable.placeholder),
                    contentDescription = "Wishlist item image",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(OnlineStoreSpacing.SMALL.dp()))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Title
                        Text(
                            text = wishlistItem.name.orEmpty(),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(OnlineStoreSpacing.EXTRA_SMALL.dp()))
                        Text(
                            text = "Rs.${wishlistItem.price}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
                Spacer(modifier = Modifier.height(OnlineStoreSpacing.SMALL.dp()))
                OnlineStoreButton(
                    variant = OnlineStoreButtonVariant.SECONDARY,
                    label = stringResource(com.sample.wishlistmodule.R.string.label_move_to_cart),
                    onClick = {
                        onAction(WishlistAction.OnClickMoveToCart(wishlistItem.productId))
                    }
                )
            }
            RemoveItemSection(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(OnlineStoreSpacing.EXTRA_SMALL.dp()),
                wishlistItem = wishlistItem,
                onAction = onAction
            )
        }
    }
}

@Composable
fun RemoveItemSection(
    wishlistItem: WishlistItem,
    onAction: (WishlistAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier
            .padding(OnlineStoreSpacing.EXTRA_SMALL.dp())
            .size(OnlineStoreSpacing.MEDIUM_PLUS.dp())
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                onAction(WishlistAction.OnClickRemoveFromWishlist(wishlistItem.productId))

            },
        imageVector = Icons.Default.Clear,
        contentDescription = "Remove wishlist",
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
