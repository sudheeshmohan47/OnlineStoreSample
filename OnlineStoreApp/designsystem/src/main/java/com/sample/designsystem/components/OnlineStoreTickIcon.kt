package com.sample.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.sample.designsystem.R
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp

private const val TickIconAnimDuration = 100
private const val TickIconAnimInitialScale = 0.5f

@Composable
fun OnlineStoreTickIcon(isSelected: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isSelected,
        enter = scaleIn(
            animationSpec = tween(durationMillis = TickIconAnimDuration),
            initialScale = TickIconAnimInitialScale
        ),
        exit = scaleOut(
            animationSpec = tween(durationMillis = TickIconAnimDuration),
            targetScale = TickIconAnimInitialScale
        )
    ) {
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(OnlineStoreSpacing.MEDIUM_PLUS.dp()),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_tick_green),
            contentDescription = "Selected icon"
        )
    }
}
