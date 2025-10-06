package com.sample.designsystem.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A custom composable function that provides pull-to-refresh functionality using Material3's experimental
 * PullToRefresh API. It wraps content within a refreshable container that triggers the provided `onRefresh`
 * callback when a pull-down gesture is detected.
 *
 * @param isRefreshing Indicates whether a refresh is currently in progress.
 * @param onRefresh Callback to trigger the refresh action when the user pulls down.
 * @param modifier Optional [Modifier] to be applied to the entire pull-to-refresh box.
 * @param pullRefreshState The state object representing the pull-to-refresh state, created with `rememberPullToRefreshState()`.
 * @param spinningIndicatorColor Color for the spinning refresh indicator, defaulting to [MaterialTheme.colorScheme.primary].
 * @param containerColor Background color for the refresh indicator's container, defaulting to [MaterialTheme.colorScheme.surfaceContainerHigh].
 * @param positionalThreshold The vertical distance from top where the refresh indicator stays while spinning, defaulting to 150.dp.
 * @param content The main content to be displayed inside the pull-to-refresh box.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineStorePullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    pullRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    spinningIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    positionalThreshold: Dp = 150.dp,
    testTag: String = "",
    indicatorTestTag: String = "",
    content: @Composable () -> Unit,
) {
    PullToRefreshBox(
        state = pullRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
            .fillMaxSize()
            .testTag(testTag),
        indicator = {
            PullToRefreshDefaults.Indicator(
                modifier = Modifier.align(Alignment.TopCenter).testTag(indicatorTestTag),
                state = pullRefreshState,
                isRefreshing = isRefreshing,
                color = spinningIndicatorColor,
                containerColor = containerColor,
            )
        }
    ) {
        content()
    }
}
