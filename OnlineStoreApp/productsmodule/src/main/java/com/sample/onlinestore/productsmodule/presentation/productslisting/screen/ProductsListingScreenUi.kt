package com.sample.onlinestore.productsmodule.presentation.productslisting.screen

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.sample.designsystem.components.LazyGridWithShimmerEffect
import com.sample.designsystem.components.OnlineStoreTopAppBar
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.R
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingAction
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingUiModel

private const val ProductListingItemWidthPercentage = 0.40f
private const val ProductListingItemAspectRatio = 1.3f
private const val ProductListingGridColumnCount = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsListingTopAppBarSection(
    topAppBarState: TopAppBarState,
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
        backgroundColor = MaterialTheme.colorScheme.background
    )
}

@Composable
fun ProductsListingScreenContent(
    productsListingUiState: UiState<ProductsListingUiModel>,
    screenWidth: Dp,
    onAction: (ProductsListingAction) -> Unit,
    modifier: Modifier = Modifier,
    productListState: LazyGridState = rememberLazyGridState(),
    shimmerEffectGridState: LazyGridState = rememberLazyGridState()
) {
    productsListingUiState.data?.let { productsUiModel ->
        val products = productsUiModel.products
        val isInitialLoadingCompleted = productsUiModel.isInitialLoadingCompleted

        if (!isInitialLoadingCompleted && productsListingUiState is UiState.Loading) {
            LazyGridWithShimmerEffect(lazyGridState = shimmerEffectGridState)
        } else {
            LazyVerticalGrid(
                state = productListState,
                modifier = modifier,
                columns = GridCells.Fixed(ProductListingGridColumnCount),
                contentPadding = PaddingValues(vertical = OnlineStoreSpacing.SMALL.dp()),
                verticalArrangement = Arrangement.spacedBy(OnlineStoreSpacing.EXTRA_SMALL.dp()),
                horizontalArrangement = Arrangement.spacedBy(OnlineStoreSpacing.EXTRA_SMALL.dp())
            ) {
                items(products) { productItem ->
                    ProductsListingItem(
                        productItem = productItem,
                        onAction = onAction,
                        screenWidth = screenWidth,
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@Composable
fun ProductsListingItem(
    productItem: ProductItem,
    screenWidth: Dp,
    modifier: Modifier = Modifier,
    onAction: (ProductsListingAction) -> Unit
) {
    Card(
        modifier = modifier
            .width(screenWidth * ProductListingItemWidthPercentage)
            .padding(OnlineStoreSpacing.EXTRA_SMALL.dp())
            .clickable {
                onAction(ProductsListingAction.OnClickProduct(productItem))
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
                            style = MaterialTheme.typography.bodySmall,
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
        contentDescription = "Collection Options",
        tint = if (productItem.isWishListed) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
    )
}
