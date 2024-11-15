package com.sample.onlinestore.presentation.dashboard

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sample.onlinestore.R
import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import com.sample.onlinestore.commonmodule.foundation.base.getBaseRoute
import com.sample.onlinestore.foundation.navigation.OnlineStoreScreens
import kotlinx.serialization.Serializable

@Serializable
open class BottomNavItem(
    @DrawableRes val image: Int = R.drawable.ic_bottom_nav_home,
    @StringRes val labelResource: Int = R.string.label_bottom_nav_home
) :
    BaseScreen() {
    @Serializable
    object Home : BottomNavItem(
        R.drawable.ic_bottom_nav_home,
        R.string.label_bottom_nav_home
    )

    @Serializable
    object Categories : BottomNavItem(
        R.drawable.ic_bottom_nav_categories,
        R.string.label_bottom_nav_categories
    )

    @Serializable
    object Wishlist : BottomNavItem(
        R.drawable.ic_bottom_nav_favourite,
        R.string.label_bottom_nav_wishlist
    )

    fun dashBoardScreens(): List<BottomNavItem> {
        return listOf(
            Home, Categories, Wishlist
        )
    }
}

/**
 * Retrieves a list of screen routes that do not include the bottom navigation.
 *
 * This function collects the base routes for various screens in the application,
 * specifically those that are not part of the bottom navigation structure.
 * Each screen's base route is obtained using the `getBaseRoute()` extension function.
 *
 * @return List<String> A list of screen routes excluding any routes associated with the bottom navigation.
 */
fun getScreenRoutesWithoutBottomNav(): List<String> = listOf(
    OnlineStoreScreens.Splash.getBaseRoute(),
    OnlineStoreScreens.AppIntroScreen.getBaseRoute()
)
