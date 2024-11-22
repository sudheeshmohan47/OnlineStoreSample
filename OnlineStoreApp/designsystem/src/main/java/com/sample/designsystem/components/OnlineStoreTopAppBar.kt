package com.sample.designsystem.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineStoreTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    titleAlignment: TextAlign = TextAlign.Start,
    displayBackNavigation: Boolean = false,
    backNavigationIcon: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
    backNavigationIconSize: Dp = 30.dp,
    onBackNavigationClicked: (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    titleFontWeight: FontWeight = FontWeight.Medium,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState),
    shadowElevation: Dp = 3.dp,
) {
    val commonModifier = modifier
        .fillMaxWidth()
        .shadow(elevation = shadowElevation)

    val commonNavigationIcon: @Composable (() -> Unit)? = if (displayBackNavigation) {
        {
            IconButton(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clip(CircleShape),
                onClick = { onBackNavigationClicked?.invoke() }
            ) {
                Icon(
                    imageVector = backNavigationIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(backNavigationIconSize)
                )
            }
        }
    } else {
        null
    }

    val commonTitle: @Composable (() -> Unit) = {
        Text(
            modifier = Modifier.padding(start = if (!displayBackNavigation) 12.dp else 0.dp),
            text = title,
            style = titleStyle,
            fontWeight = titleFontWeight,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = titleAlignment,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    if (titleAlignment == TextAlign.Center) {
        CenterAlignedTopAppBar(
            modifier = commonModifier,
            navigationIcon = commonNavigationIcon ?: {},
            title = commonTitle,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor,
                titleContentColor = titleColor,
            ),
            scrollBehavior = scrollBehavior,
            windowInsets = WindowInsets(
                top = 0.dp,
                bottom = 0.dp
            )
        )
    } else {
        TopAppBar(
            modifier = commonModifier,
            navigationIcon = commonNavigationIcon ?: {},
            title = commonTitle,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor,
                titleContentColor = titleColor,
            ),
            scrollBehavior = scrollBehavior,
            windowInsets = WindowInsets(
                top = 0.dp,
                bottom = 0.dp
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun CustomTopAppBarPreview() {
    OnlineStoreTopAppBar(
        title = "Custom App Bar",
        displayBackNavigation = true,
        titleAlignment = TextAlign.Start,
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomTopAppBarPreviewDark() {
    CustomTopAppBarPreview()
}
