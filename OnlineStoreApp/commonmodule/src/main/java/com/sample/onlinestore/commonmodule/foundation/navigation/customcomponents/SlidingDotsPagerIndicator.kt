package com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

const val defaultCornerRadius = 12f

private fun DrawScope.drawIndicator(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    radius: CornerRadius,
    color: Color
) {
    val rect = RoundRect(
        x,
        y - height / 2,
        x + width,
        y + height / 2,
        radius
    )
    val path = Path().apply { addRoundRect(rect) }
    drawPath(path = path, color = color)
}

// To get scroll offset
@OptIn(ExperimentalFoundationApi::class)
val PagerState.pageOffset: Float
    get() = this.currentPage + this.currentPageOffsetFraction

// To get scrolled offset from snap position
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlidingDotsPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    circleSpacing: Dp = 10.dp, // Spacing between circles
    width: Dp = 8.dp, // Width of each circle
    height: Dp = 8.dp, // Height of each circle
    activeLineWidth: Dp = 30.dp, // Width of the active line
    radius: CornerRadius = CornerRadius(defaultCornerRadius), // Radius of each circle
    defaultColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    selectedColor: Color = MaterialTheme.colorScheme.primary
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.width(width = activeLineWidth * 2)) {
            val spacing = circleSpacing.toPx()
            val dotWidth = width.toPx()
            val dotHeight = height.toPx()

            val activeDotWidth = activeLineWidth.toPx()
            var x = 0f
            val y = center.y

            repeat(pagerState.pageCount) { i ->
                val posOffset = pagerState.pageOffset
                val dotOffset = posOffset % 1
                val current = posOffset.toInt()

                val factor = (dotOffset * (activeDotWidth - dotWidth))

                val calculatedWidth = when {
                    i == current -> activeDotWidth - factor
                    i - 1 == current || (i == 0 && posOffset > pagerState.pageCount - 1) -> dotWidth + factor
                    else -> dotWidth
                }

                drawIndicator(
                    x,
                    y,
                    calculatedWidth,
                    dotHeight,
                    radius,
                    color = if (i == pagerState.currentPage) selectedColor else defaultColor
                )
                x += calculatedWidth + spacing
            }
        }
    }
}
