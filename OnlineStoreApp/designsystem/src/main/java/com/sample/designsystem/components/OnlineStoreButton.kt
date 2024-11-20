package com.sample.designsystem.components

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sample.designsystem.foundation.OnlineStorePercentage
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp

enum class OnlineStoreButtonSizeVariant {
    SMALL, MEDIUM, LARGE
}

enum class OnlineStoreButtonVariant {
    PRIMARY,
    SECONDARY,
    NAKED,
    OUTLINED
}

val ButtonHeight = 48.dp
const val DisabledButtonAlpha = 0.7F
val RoundedCornerShapeSize = OnlineStoreSpacing.SMALL.dp()

@Composable
fun OnlineStoreButton(
    label: String,
    modifier: Modifier = Modifier,
    variant: OnlineStoreButtonVariant = OnlineStoreButtonVariant.PRIMARY,
    buttonHeight: Dp = ButtonHeight,
    buttonBackground: Color? = null,
    buttonTextColor: Color? = null,
    buttonBorderColor: Color = MaterialTheme.colorScheme.primary,
    buttonCornerShapeSize: Dp = RoundedCornerShapeSize,
    buttonStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleSmall,
    buttonFontFamily: FontFamily? = FontFamily.Default,
    buttonFontWeight: FontWeight = FontWeight.Medium,
    isLoadingEnabled: Boolean = false,
    spinningLoaderSize: Dp = 24.dp,
    spinningLoaderDashesCount: Int = 8,
    spinningLoaderDashesColor: Color = MaterialTheme.colorScheme.primaryContainer,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = getBorderColorWithAlpha(variant, buttonBorderColor, enabled),
                    shape = RoundedCornerShape(buttonCornerShapeSize)
                ),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonBackground ?: getButtonBackgroundColor(variant),
                disabledContainerColor = (buttonBackground ?: getButtonBackgroundColor(variant))
                    .copy(alpha = DisabledButtonAlpha)
            ),
            shape = RoundedCornerShape(buttonCornerShapeSize),
            onClick = { if (!isLoadingEnabled) onClick() }, // Disable click when loading in progress
        ) {
            if (isLoadingEnabled) {
                OnlineStoreProgressIndicator(
                    modifier = Modifier.size(spinningLoaderSize),
                    dashCount = spinningLoaderDashesCount,
                    dashColor = spinningLoaderDashesColor,
                    dashTrailingColor = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = label,
                    style = buttonStyle,
                    fontFamily = buttonFontFamily,
                    fontWeight = buttonFontWeight,
                    color = (buttonTextColor ?: getButtonTextColor(variant)).copy(
                        alpha = if (!enabled) DisabledButtonAlpha else 1F
                    )
                )
            }
        }
    }
}

@Composable
fun getButtonBackgroundColor(variant: OnlineStoreButtonVariant): Color {
    return when (variant) {
        OnlineStoreButtonVariant.PRIMARY -> {
            MaterialTheme.colorScheme.primary
        }

        OnlineStoreButtonVariant.SECONDARY -> {
            MaterialTheme.colorScheme.primary.copy(alpha = OnlineStorePercentage.PERCENT_10.value)
        }

        OnlineStoreButtonVariant.OUTLINED -> {
            MaterialTheme.colorScheme.background
        }

        else -> MaterialTheme.colorScheme.primary
    }
}

@Composable
fun getButtonTextColor(variant: OnlineStoreButtonVariant): Color {
    return when (variant) {
        OnlineStoreButtonVariant.PRIMARY -> {
            MaterialTheme.colorScheme.onPrimary
        }

        OnlineStoreButtonVariant.SECONDARY -> {
            MaterialTheme.colorScheme.primary
        }

        OnlineStoreButtonVariant.OUTLINED -> {
            MaterialTheme.colorScheme.primary
        }

        else -> MaterialTheme.colorScheme.primary
    }
}

fun getBorderColorWithAlpha(
    variant: OnlineStoreButtonVariant,
    borderColor: Color,
    enabled: Boolean
): Color {
    return if (variant == OnlineStoreButtonVariant.SECONDARY) {
        Color.Transparent
    } else {
        borderColor.copy(alpha = if (!enabled) DisabledButtonAlpha else 1F)
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_XL)
@Composable
private fun PreviewOnlineStoreButton() {
    MaterialTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            OnlineStoreButton(
                label = "Primary",
                variant = OnlineStoreButtonVariant.PRIMARY,
                modifier = Modifier.weight(1f),
                onClick = {
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            OnlineStoreButton(
                label = "Outlined",
                variant = OnlineStoreButtonVariant.OUTLINED,
                modifier = Modifier.weight(1f),
                onClick = {
                }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_XL)
@Composable
private fun PreviewOnlineStoreButtonDark() {
    PreviewOnlineStoreButton()
}
