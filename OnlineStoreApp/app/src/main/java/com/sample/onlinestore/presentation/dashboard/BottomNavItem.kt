package com.sample.onlinestore.presentation.dashboard

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sample.onlinestore.R
import kotlinx.serialization.Serializable
import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import com.sample.onlinestore.commonmodule.foundation.base.getBaseRoute
import com.sample.onlinestore.foundation.navigation.OnlineStoreScreen

@kotlinx.serialization.Serializable
open class BottomNavItem(
    @DrawableRes val image: Int = R.drawable.ic_dashboard_home,
    @StringRes val labelResource: Int = R.string.label_bottomnav_home
) :
    BaseScreen() {
    @Serializable
    object Home : BottomNavItem(
        R.drawable.ic_bottomnav_home,
        R.string.label_bottomnav_home
    )

    @Serializable
    object Categories : BottomNavItem(
        R.drawable.ic_bottomnav_favourite,
        R.string.label_bottomnav_categories
    )

    @Serializable
    object Wishlist : BottomNavItem(
        R.drawable.ic_bottomnav_post_request,
        R.string.label_bottomnav_wishlist
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
    OnlineStoreScreen.Splash.getBaseRoute()
)
