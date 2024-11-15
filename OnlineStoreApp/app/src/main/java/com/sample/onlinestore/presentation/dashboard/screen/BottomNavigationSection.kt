package com.sample.onlinestore.presentation.dashboard.screen

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.utils.getStringFromId
import com.sample.onlinestore.presentation.dashboard.BottomNavItem
import com.sample.onlinestore.presentation.main.MainActivityAction
import com.sample.onlinestore.presentation.main.MainActivityUiModel

@Composable
fun BottomNavigationSection(
    mainUiState: UiState<MainActivityUiModel>,
    onAction: (MainActivityAction) -> Unit,
    modifier: Modifier = Modifier,
    switchToScreen: (BottomNavItem) -> Unit
) {
    val context = LocalContext.current
    val dashBoardScreens = BottomNavItem().dashBoardScreens()

    NavigationBar(
        modifier = modifier.clip(
            RoundedCornerShape(
                topStart = OnlineStoreSpacing.MEDIUM.dp(),
                topEnd = OnlineStoreSpacing.MEDIUM.dp()
            )
        )
    ) {
        // Indexing through the screens
        dashBoardScreens.forEachIndexed { index, navigationItem ->
            val selectedColor =
                if (index == mainUiState.data?.selectedItemIndex)
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            /* val selectedIcon =
                 getIcon(navigationItem, index == dashBoardUiState.data?.selectedItemIndex)
            */
            NavigationBarItem(
                selected = index == mainUiState.data?.selectedItemIndex,
                label = {
                    Text(
                        textAlign = TextAlign.Center,
                        text = getStringFromId(context, navigationItem.labelResource),
                        style = MaterialTheme.typography.labelSmall,
                        minLines = 2,
                        fontWeight = FontWeight.Normal,
                        color = selectedColor
                    )
                },
                icon = {
                    Icon(
                        ImageVector.vectorResource(id = navigationItem.image),
                        contentDescription = "Navigation icon",
                        tint = selectedColor
                    )
                },
                onClick = {
                    if (index != mainUiState.data?.selectedItemIndex) {
                        onAction(MainActivityAction.OnNavigationSelected(index))
                        switchToScreen(navigationItem)
                    }
                }
            )
        }
    }
}
