package com.sample.onlinestore.productsmodule.presentation.productdetails.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.sample.designsystem.components.OnlineStoreButton
import com.sample.designsystem.components.OnlineStoreTopAppBar
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.R
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsAction
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsUiModel

private const val ProductDetailsImageAspectRatio = 1.3f
private const val ProductTitleMaxLines = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsTopAppBarSection(
    productDetailsUiState: UiState<ProductDetailsUiModel>,
    onAction: (ProductDetailsAction) -> Unit,
    topAppBarState: TopAppBarState,
    modifier: Modifier = Modifier,
) {
    val productTitle = productDetailsUiState.data?.product?.name.orEmpty()
    OnlineStoreTopAppBar(
        modifier = modifier,
        topAppBarState = topAppBarState,
        title = productTitle,
        titleStyle = MaterialTheme.typography.titleLarge,
        titleFontWeight = FontWeight.Normal,
        titleColor = MaterialTheme.colorScheme.onBackground,
        displayBackNavigation = true,
        titleAlignment = TextAlign.Center,
        backgroundColor = MaterialTheme.colorScheme.background,
        onBackNavigationClicked = {
            onAction(ProductDetailsAction.OnClickBackNavigation)
        }
    )
}

@Composable
fun ProductsDetailsScreenContent(
    productDetailsUiState: UiState<ProductDetailsUiModel>,
    onAction: (ProductDetailsAction) -> Unit,
    modifier: Modifier = Modifier,
    productDetailsListState: LazyListState = rememberLazyListState()
) {

    ProductDetails(
        productDetailsUiState = productDetailsUiState,
        modifier = modifier,
        onAction = onAction,
        productDetailsListState = productDetailsListState
    )
}

@Composable
fun ProductDetails(
    productDetailsUiState: UiState<ProductDetailsUiModel>,
    modifier: Modifier = Modifier,
    onAction: (ProductDetailsAction) -> Unit,
    productDetailsListState: LazyListState = rememberLazyListState()
) {
    val productItem = productDetailsUiState.data?.product ?: ProductItem()
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f), state = productDetailsListState) {
                item {
                    ProductDetailUIContent(productItem = productItem, onAction = onAction)
                }
            }
            CartButton(
                modifier = Modifier
                    .padding(OnlineStoreSpacing.MEDIUM.dp()),
                productItem = productItem,
                enableButton = productDetailsUiState is UiState.Result,
            ) {
                onAction(it)
            }
        }
    }
}

@Composable
private fun ProductDetailUIContent(
    productItem: ProductItem,
    modifier: Modifier = Modifier, onAction: (ProductDetailsAction) -> Unit
) {
    val price = productItem.price?.let {
        "Rs. $it"
    } ?: ""

    Column(modifier.fillMaxSize()) {
        // Main image
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ProductDetailsImageAspectRatio),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(OnlineStoreSpacing.MEDIUM.dp()),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Title
                Text(
                    text = productItem.name.orEmpty(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = ProductTitleMaxLines,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(OnlineStoreSpacing.SMALL.dp()))
                Text(
                    text = price,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(OnlineStoreSpacing.SMALL.dp()))
                Text(
                    text = productItem.description.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
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

@Composable
private fun FavouriteSection(
    productItem: ProductItem,
    onAction: (ProductDetailsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier
            .clip(CircleShape)
            .size(OnlineStoreSpacing.LARGE.dp())
            .clickable {
                onAction(ProductDetailsAction.OnClickFavourite)
            },
        imageVector = if (productItem.isWishListed) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = "Collection Options",
        tint = if (productItem.isWishListed) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun CartButton(
    enableButton: Boolean,
    productItem: ProductItem,
    modifier: Modifier = Modifier,
    onAction: (ProductDetailsAction) -> Unit
) {
    val isItemAddedToCart = productItem.isAddedToCart
    val buttonTitle =
        if (isItemAddedToCart) stringResource(R.string.label_goto_cart) else stringResource(
            R.string.label_add_to_cart
        )
    OnlineStoreButton(
        modifier = modifier,
        label = buttonTitle,
        enabled = enableButton
    ) {
        onAction(ProductDetailsAction.OnClickAddToCart)
    }

}
