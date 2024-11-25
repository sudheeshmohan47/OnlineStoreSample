package com.sample.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp

@Composable
fun OnlineStoreRoundedEdgedCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(OnlineStoreSpacing.MEDIUM.dp()),
        colors = CardDefaults.cardColors()
            .copy(containerColor = MaterialTheme.colorScheme.background)
    ) {
        content.invoke()
    }
}