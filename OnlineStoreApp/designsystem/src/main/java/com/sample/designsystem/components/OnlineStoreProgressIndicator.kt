package com.sample.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@SuppressWarnings("MagicNumber")
@Composable
fun OnlineStoreProgressIndicator(
    modifier: Modifier = Modifier,
    dashCount: Int = 7,
    dashColor: Color = MaterialTheme.colorScheme.primary,
    dashTrailingColor: Color = MaterialTheme.colorScheme.secondary
) {

    val infiniteTransition = rememberInfiniteTransition(label = "initial transition")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = dashCount.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(dashCount * 80, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "animation angle"
    )

    Canvas(modifier = modifier) {

        val canvasWidth = size.width
        val canvasHeight = size.height

        val width = size.width * .3f
        val height = size.height / 8

        val cornerRadius = width.coerceAtMost(height) / 2

        for (i in 0..360 step 360 / dashCount) {
            rotate(i.toFloat()) {
                drawRoundRect(
                    color = dashColor.copy(alpha = .7f),
                    topLeft = Offset(canvasWidth - width, (canvasHeight - height) / 2),
                    size = Size(width, height),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
        }

        val coefficient = 360f / dashCount
        // Increase dash count divisor to increase the spinning arc size and vice versa
        val dashCountDivisor = 4
        for (i in 1..dashCount - (dashCount / dashCountDivisor)) {
            rotate((angle.toInt() + i) * coefficient) {
                drawRoundRect(
                    color = dashTrailingColor.copy(alpha = (0.2f + 0.2f * i).coerceIn(0f, 1f)),
                    topLeft = Offset(canvasWidth - width, (canvasHeight - height) / 2),
                    size = Size(width, height),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
        }
    }
}

// Centered
@Composable
fun ShowDashedProgressIndicator(
    modifier: Modifier = Modifier,
    spinningLoaderSize: Dp = 46.dp,
    spinningLoaderDashesCount: Int = 8,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        OnlineStoreProgressIndicator(
            modifier = Modifier.size(spinningLoaderSize),
            dashCount = spinningLoaderDashesCount,
            dashColor = MaterialTheme.colorScheme.primary,
            dashTrailingColor = MaterialTheme.colorScheme.surface
        )
    }
}

// Default
@Composable
fun ShowDashedProgressIndicatorDefault(
    modifier: Modifier = Modifier,
    spinningLoaderSize: Dp = 46.dp,
    spinningLoaderDashesCount: Int = 8,
) {
    OnlineStoreProgressIndicator(
        modifier = modifier.size(spinningLoaderSize),
        dashCount = spinningLoaderDashesCount,
        dashColor = MaterialTheme.colorScheme.primary,
        dashTrailingColor = MaterialTheme.colorScheme.surface
    )
}
