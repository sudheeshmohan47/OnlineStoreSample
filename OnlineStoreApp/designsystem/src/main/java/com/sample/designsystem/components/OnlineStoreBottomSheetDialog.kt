package com.sample.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineStoreBottomSheetDialog(
    title: String,
    description: String,
    negativeButtonLabel: String,
    positiveButtonLabel: String,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState, containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.background,
        dragHandle = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetDefaults.DragHandle()
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(OnlineStoreSpacing.LARGE.dp())
        ) {
            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(OnlineStoreSpacing.MEDIUM.dp()))

            // Description
            Text(
                text = description,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(OnlineStoreSpacing.LARGE.dp()))

            // Button Section
            Row {
                OnlineStoreButton(
                    label = negativeButtonLabel,
                    variant = OnlineStoreButtonVariant.OUTLINED,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onDismiss()
                    }
                )
                Spacer(modifier = Modifier.width(OnlineStoreSpacing.MEDIUM.dp()))
                OnlineStoreButton(
                    label = positiveButtonLabel,
                    variant = OnlineStoreButtonVariant.PRIMARY,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onDismiss()
                        onPositiveButtonClick()
                    }
                )
            }
        }
    }
}
