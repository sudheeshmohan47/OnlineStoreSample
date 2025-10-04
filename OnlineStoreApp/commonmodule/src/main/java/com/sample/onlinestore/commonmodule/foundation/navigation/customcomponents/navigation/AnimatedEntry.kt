package com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen

/**
 * Adds an animated entry to the Nav3 EntryProvider with default slide-in and slide-out animations.
 *
 * This extension for [EntryProviderBuilder<BaseScreen>] creates a navigation entry
 * with vertical slide animations for enter, exit, pop, and predictive pop transitions.
 * It automatically generates a stable content key using [simpleContentKey] to support
 * state restoration and proper recomposition.
 *
 * @param T The type of the navigation key. Must extend [BaseScreen].
 * @param duration The duration of the slide animation in milliseconds. Defaults to 500ms.
 * @param content The composable content associated with this navigation entry.
 * @param metadata Optional metadata for the entry. By default, it includes vertical
 *                 slide animations for enter, exit, pop, and predictive pop transitions.
 */
inline fun <reified T : NavKey> EntryProviderBuilder<NavKey>.animatedEntry(
    duration: Int = 500,
    metadata: Map<String, Any> = NavDisplay.transitionSpec {
        slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(duration)
        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
    } + NavDisplay.popTransitionSpec {
        EnterTransition.None togetherWith slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(duration)
        )
    } + NavDisplay.predictivePopTransitionSpec {
        EnterTransition.None togetherWith slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(duration)
        )
    },
    noinline content: @Composable (T) -> Unit
    ) {
    addEntryProvider(T::class, { simpleContentKey(it) }, metadata, content)
}

/**
 * Generates a stable key for a NavEntry from the key object.
 */
fun <T : Any> simpleContentKey(key: T): Any {
    // For simple screens, just use class name + hashCode
    return key::class.qualifiedName + "_" + key.hashCode()
}
