package com.sample.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp

@Composable
fun ShimmerEffect(
    modifier: Modifier,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
) {
    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 1.0f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )

    Box(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }
}

/**
 * Lazy grid displaying items with a shimmer effect.
 * The item shape (e.g., square, rounded) is passed dynamically.
 */
@Composable
fun LazyGridWithShimmerEffect(
    modifier: Modifier = Modifier,
    itemShape: Shape = RoundedCornerShape(OnlineStoreSpacing.SMALL.dp()),
    itemCount: Int = 10,
    gridColumns: Int = 2,
    gridItemAspectRatio: Float = 1.0f
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(OnlineStoreSpacing.MEDIUM.dp())
    ) {
        items(itemCount) {
            Card(
                modifier = Modifier
                    .padding(OnlineStoreSpacing.SMALL.dp())
                    .fillMaxSize(),
                shape = itemShape,
                elevation = CardDefaults.cardElevation(OnlineStoreSpacing.EXTRA_SMALL.dp())
            ) {
                // Apply shimmer effect inside each grid item
                ShimmerEffect(
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(gridItemAspectRatio)
                        .clip(RoundedCornerShape(OnlineStoreSpacing.SMALL.dp()))
                        .background(Color.LightGray)
                )
            }
        }
    }
}
