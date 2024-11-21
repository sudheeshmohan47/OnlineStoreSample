package com.sample.onlinestore.categoriesmodule.presentation.categories.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sample.designsystem.components.OnlineStoreTickIcon
import com.sample.designsystem.components.OnlineStoreTopAppBar
import com.sample.designsystem.foundation.OnlineStoreSize
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.categoriesmodule.R
import com.sample.onlinestore.categoriesmodule.domain.model.CategoryItem
import com.sample.onlinestore.categoriesmodule.presentation.categories.CategoriesAction
import com.sample.onlinestore.categoriesmodule.presentation.categories.CategoriesUiModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import timber.log.Timber
import java.util.Locale

private const val CategoriesItemWidthPercentage = 0.40f
private const val CategoriesListingItemAspectRatio = 1f
private const val CategoriesListingGridColumnCount = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsListingTopAppBarSection(
    topAppBarState: TopAppBarState,
    modifier: Modifier = Modifier,
) {
    OnlineStoreTopAppBar(
        modifier = modifier,
        topAppBarState = topAppBarState,
        title = stringResource(R.string.title_categories),
        titleStyle = MaterialTheme.typography.titleLarge,
        titleFontWeight = FontWeight.Normal,
        titleColor = MaterialTheme.colorScheme.onBackground,
        displayBackNavigation = false,
        titleAlignment = TextAlign.Center,
        backgroundColor = MaterialTheme.colorScheme.background
    )
}

@Composable
fun CategoriesScreenListingContent(
    categoriesUiState: UiState<CategoriesUiModel>,
    screenWidth: Dp,
    onAction: (CategoriesAction) -> Unit,
    modifier: Modifier = Modifier
) {
    categoriesUiState.data?.let { categoriesUiModel ->
        val categories = categoriesUiModel.categories
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(CategoriesListingGridColumnCount),
            contentPadding = PaddingValues(vertical = OnlineStoreSpacing.SMALL.dp()),
            verticalArrangement = Arrangement.spacedBy(OnlineStoreSpacing.EXTRA_SMALL.dp()),
            horizontalArrangement = Arrangement.spacedBy(OnlineStoreSpacing.EXTRA_SMALL.dp())
        ) {
            items(categories) { categoryItem ->
                CategoriesListingItem(
                    categoryItem = categoryItem,
                    onAction = onAction,
                    screenWidth = screenWidth
                )
            }
        }
    }
}

@Composable
fun CategoriesListingItem(
    categoryItem: CategoryItem,
    screenWidth: Dp,
    modifier: Modifier = Modifier,
    onAction: (CategoriesAction) -> Unit
) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .width(screenWidth * CategoriesItemWidthPercentage)
                .aspectRatio(CategoriesListingItemAspectRatio)
                .padding(OnlineStoreSpacing.EXTRA_SMALL.dp()),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            shape = CircleShape,
            colors = CardDefaults.cardColors()
                .copy(containerColor = MaterialTheme.colorScheme.onTertiary)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .clickable {
                        onAction(CategoriesAction.ToggleCategoriesSelection(categoryItem))
                    }
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(OnlineStoreSpacing.SMALL.dp()),
                    textAlign = TextAlign.Center,
                    text = categoryItem.category.uppercase(Locale.ROOT),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        // Tick icon
        OnlineStoreTickIcon(
            modifier = Modifier
                .size(OnlineStoreSize.SMALL.dp())
                .align(Alignment.TopEnd)
                .padding(
                    top = OnlineStoreSpacing.LARGE.dp(),
                    end = OnlineStoreSpacing.LARGE.dp()
                ),
            isSelected = categoryItem.isSelected
        )
    }
}