package com.sample.onlinestore.categoriesmodule.presentation.categories

import com.sample.onlinestore.categoriesmodule.domain.model.CategoryItem
import com.sample.onlinestore.commonmodule.foundation.base.Action
import com.sample.onlinestore.commonmodule.foundation.base.Event

data class CategoriesUiModel(
    val categories: List<CategoryItem> = emptyList(),
    val isSwipeRefreshing: Boolean = false
)

sealed class CategoriesAction : Action {
    data object RefreshData: CategoriesAction()
    data class ToggleCategoriesSelection(val categoryItem: CategoryItem) : CategoriesAction()
    data class SetSwipeRefreshingStatus(val isRefreshing: Boolean) : CategoriesAction()
}

sealed class CategoriesEvent : Event
