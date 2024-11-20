package com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import kotlin.reflect.KType

/**
 * Constants for the duration of the slide animation.
 */
const val SlideAnimationDuration = 500

/**
 * Extension function for [NavGraphBuilder] to create a composable with slide animations for a
 * specific [BaseScreen] subclass.
 *
 * @param content The composable content block with [AnimatedContentScope] and [NavBackStackEntry].
 */
inline fun <reified T : BaseScreen> NavGraphBuilder.slideComposable(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(SlideAnimationDuration)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(SlideAnimationDuration)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(SlideAnimationDuration)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(SlideAnimationDuration)
            )
        },
        content = content
    )
}
