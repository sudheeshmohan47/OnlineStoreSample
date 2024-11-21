package com.sample.onlinestore.categoriesmodule.foundation.navigation

import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import kotlinx.serialization.Serializable

// Sealed class for different screens in the navigation
@Serializable
sealed class CategoriesScreens : BaseScreen()
