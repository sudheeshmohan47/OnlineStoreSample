package com.sample.designsystem.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp

@Composable
fun OnlineStoreOutlinedTextField(
    text: String,
    hint: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    onFocusChangeListener: ((Boolean) -> Unit)? = null,
    focusable: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    textFieldShape: Shape = OutlinedTextFieldDefaults.shape,
    focusedIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor: Color = MaterialTheme.colorScheme.outlineVariant,
    containerColor: Color = Color.Transparent,
) {
    var hasFocus by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.onFocusChanged {
            onFocusChangeListener?.invoke(it.isFocused)
            hasFocus = it.isFocused
        }
    ) {
        OutlinedTextField(
            value = text,
            shape = textFieldShape,
            label = if (label.isNotEmpty()) {
                { Text(text = label, maxLines = 1) }
            } else null,
            readOnly = !focusable,
            textStyle = textStyle,
            placeholder = {
                Text(
                    text = hint,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            },
            onValueChange = {
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType, imeAction = ImeAction.Next
            ),
            singleLine = true,
            isError = isError,
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = focusedIndicatorColor,
                unfocusedIndicatorColor = unfocusedIndicatorColor,
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor
            )
        )
        if (isError && !errorMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(OnlineStoreSpacing.EXTRA_SMALL.dp()))
            Text(
                text = errorMessage,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_XL)
@Composable
private fun PreviewOnlineStoreOutlinedTextField() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            OnlineStoreOutlinedTextField(
                text = "34534534",
                hint = "Mobile Number",
                label = "Mobile Number",
                onValueChange = {},
                keyboardType = KeyboardType.Number,
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_XL)
@Composable
private fun PreviewOnlineStoreOutlinedTextFieldDark() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            OnlineStoreOutlinedTextField(
                text = "345345345",
                hint = "Mobile Number",
                label = "Mobile Number",
                onValueChange = {},
                keyboardType = KeyboardType.Number,
            )
        }
    }
}