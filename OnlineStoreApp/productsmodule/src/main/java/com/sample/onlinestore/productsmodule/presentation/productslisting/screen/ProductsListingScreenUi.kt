package com.sample.onlinestore.productsmodule.presentation.productslisting.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.sample.designsystem.components.EmptyTextPlaceHolder
import com.sample.designsystem.components.LazyGridWithShimmerEffect
import com.sample.designsystem.components.OnlineStoreRoundedEdgedCard
import com.sample.designsystem.components.OnlineStoreTopAppBar
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.R
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingAction
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingUiModel
import com.sample.onlinestore.productsmodule.util.ProductTestTags

private const val ProductListingItemAspectRatio = 1.3f
private const val ProductListingGridColumnCount = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsListingTopAppBarSection(
    topAppBarState: TopAppBarState,
    onAction: (ProductsListingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnlineStoreTopAppBar(
        modifier = modifier,
        topAppBarState = topAppBarState,
        title = stringResource(R.string.title_home),
        titleStyle = MaterialTheme.typography.titleLarge,
        titleFontWeight = FontWeight.Normal,
        titleColor = MaterialTheme.colorScheme.onBackground,
        displayBackNavigation = false,
        titleAlignment = TextAlign.Center,
        backgroundColor = MaterialTheme.colorScheme.background,
        customActions = {
            IconButton(
                onClick = {
                    onAction(ProductsListingAction.OnClickCart)
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
fun ProductsListingScreenContent(
    productsListingUiState: UiState<ProductsListingUiModel>,
    onAction: (ProductsListingAction) -> Unit,
    modifier: Modifier = Modifier,
    productListState: LazyGridState = rememberLazyGridState(),
    shimmerEffectGridState: LazyGridState = rememberLazyGridState()
) {
    productsListingUiState.data?.let { productsUiModel ->
        val products = productsUiModel.products
        val isInitialLoadingCompleted = productsUiModel.isInitialLoadingCompleted

        if (!isInitialLoadingCompleted && productsListingUiState is UiState.Loading) {
            LazyGridWithShimmerEffect(
                lazyGridState = shimmerEffectGridState,
                testTag = ProductTestTags.PRODUCT_LISTING_SHIMMER_EFFECT
            )
        } else {
            if (products.isNotEmpty()) {
                LazyVerticalGrid(
                    state = productListState,
                    modifier = modifier.testTag(ProductTestTags.PRODUCT_LISTING_LIST),
                    columns = GridCells.Fixed(ProductListingGridColumnCount),
                    contentPadding = PaddingValues(vertical = OnlineStoreSpacing.SMALL.dp())
                ) {
                    items(products) { productItem ->
                        ProductsListingItem(
                            productItem = productItem,
                            onAction = onAction,
                            modifier = Modifier
                                .animateItem()
                                .padding(OnlineStoreSpacing.SMALL.dp())
                        )
                    }
                }
            } else if (isInitialLoadingCompleted && productsListingUiState is UiState.Result) {
                EmptyTextPlaceHolder(
                    text = stringResource(R.string.label_no_products_found),
                    testTag = ProductTestTags.PRODUCT_LISTING_EMPTY_TEXT
                )
            }
        }
    }
}

@Composable
fun ProductsListingItem(
    productItem: ProductItem,
    modifier: Modifier = Modifier,
    onAction: (ProductsListingAction) -> Unit
) {
    OnlineStoreRoundedEdgedCard(
        modifier = modifier
            .clickable {
                onAction(ProductsListingAction.OnClickProduct(productItem))
            }
            .testTag(ProductTestTags.PRODUCT_LISTING_ITEM)
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
                        .data(productItem.image?.ifEmpty { com.sample.designsystem.R.drawable.placeholder })
                        .crossfade(true)
                        .scale(Scale.FIT)
                        .error(com.sample.designsystem.R.drawable.placeholder)
                        .build(),
                    placeholder = painterResource(com.sample.designsystem.R.drawable.placeholder),
                    contentDescription = "",
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
                            text = productItem.name.orEmpty(),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(OnlineStoreSpacing.EXTRA_SMALL.dp()))

                        Text(
                            text = "Rs.${productItem.price}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.width(OnlineStoreSpacing.SMALL.dp()))
                    FavouriteSection(
                        modifier = Modifier.align(Alignment.Top),
                        productItem = productItem,
                        onAction = onAction
                    )
                }
            }
        }
    }
}

@Composable
fun FavouriteSection(
    productItem: ProductItem,
    onAction: (ProductsListingAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier
            .clip(CircleShape)
            .size(OnlineStoreSpacing.LARGE.dp())
            .clickable {
                onAction(ProductsListingAction.OnClickFavourite(productItem))
            },
        imageVector = if (productItem.isWishListed) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = stringResource(R.string.content_description_product_listing_fav_icon),
        tint = if (productItem.isWishListed) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
    )
}
