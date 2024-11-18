package com.sample.onlinestore.productsmodule.presentation.productslisting.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
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
fun CollectionsScreenContent(
    productsListingUiState: UiState<ProductsListingUiModel>,
    screenWidth: Dp,
    onAction: (ProductsListingAction) -> Unit,
    modifier: Modifier = Modifier
) {
    productsListingUiState.data?.let { collectionsUiModel ->
      val products = collectionsUiModel.products
            LazyVerticalGrid(
                modifier = modifier,
                columns = GridCells.Fixed(CollectionsGridColumnCount),
                contentPadding = PaddingValues(horizontal = OnlineStoreSpacing.MEDIUM.dp()),
                verticalArrangement = Arrangement.spacedBy(OnlineStoreSpacing.EXTRA_SMALL.dp()),
                horizontalArrangement = Arrangement.spacedBy(OnlineStoreSpacing.EXTRA_SMALL.dp())
            ) {
                items(products) {
                    CollectionItem(
                        collectionItem = it,
                        onAction = onAction,
                        screenWidth = screenWidth,
                        selectedOverFlowCollectionId = selectedOverFlowCollectionId
                    )
                }
            }
    }
}

@Composable
fun CollectionItem(
    productItem: ProductItem,
    screenWidth: Dp,
    modifier: Modifier = Modifier,
    onAction: (ProductsListingAction) -> Unit
) {
    Card(
        modifier = modifier
            .width(screenWidth * ProductListingItemWidthPercentage)
            .padding(OnlineStoreSpacing.EXTRA_SMALL.dp()),
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
                .clickable {
                    onAction(ProductsListingAction.OnClickProduct(productItem))
                }
        ) {
            Column {
                // Main image
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(OnlineStoreSpacing.SMALL.dp()))
                        .fillMaxWidth()
                        .aspectRatio(ProductListingItemAspectRatio),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(productItem.image.ifEmpty { R.drawable.placeholder_home })
                        .crossfade(true)
                        .scale(Scale.FIT)
                        .error(R.drawable.placeholder_home)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder_home),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(OnlineStoreSpacing.SMALL.dp()))
                // Title
                Text(
                    text = collectionItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(AtlasSpacing.EXTRASMALL.dp()))
                Text(
                    text = collectionItem.place,
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun BoxScope.FavouriteSection(
    productItem: ProductItem,
    onAction: (ProductsListingAction) -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton (
        modifier = modifier
            .align(Alignment.TopEnd)
            .padding(OnlineStoreSpacing.SMALL.dp()),
        onClick = {
            onAction(ProductsListingAction.OnClickFavourite(productItem))
        }
    ) {
        Icon(
            modifier = Modifier
                .clip(RoundedCornerShape(OnlineStoreSpacing.EXTRASMALL.dp()))
                .size(OnlineStoreSpacing.LARGE.dp())
                .padding(AtlasSpacing.EXTRASMALL.dp())
                .clickable { onAction(CollectionsAction.ToggleOverFlowMenu(collectionItem.id)) },
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Collection Options",
            tint = Color.White
        )
    }
}

@Composable
fun CollectionOverflowMenu(
    collectionId: String,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onAction: (CollectionsAction) -> Unit
) {
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(
            extraSmall = RoundedCornerShape(AtlasSpacing.MEDIUM.dp())
        )
    ) {
        DropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = { onAction(CollectionsAction.ToggleOverFlowMenu(collectionId)) }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.label_remove),
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(AtlasSpacing.MEDIUM.dp()),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                        contentDescription = "Collection Options",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onClick = {
                    onAction(CollectionsAction.OnClickedRemoveCollection(collectionId))
                }
            )
            DropdownMenuItem(
                modifier = Modifier,
                text = {
                    Text(
                        text = stringResource(R.string.ic_share),
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(AtlasSpacing.MEDIUM.dp()),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_share),
                        contentDescription = "Collection Options",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onClick = { onAction(CollectionsAction.OnClickedShareCollection(collectionId)) }
            )
        }
    }
}
