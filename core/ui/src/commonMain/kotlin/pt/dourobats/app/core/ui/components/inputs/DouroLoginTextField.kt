package pt.dourobats.app.core.ui.components.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme

/**
 * Glassmorphism text field for use on dark gradient backgrounds (e.g. Login screen).
 *
 * Design:
 * - Frosted-glass container: `Color.White @ 10%` (unfocused) / `15%` (focused)
 * - White border: full opacity when focused, 30% when idle
 * - White label + input text — max contrast against Midnight Pitch gradient
 * - Error state uses M3 error color (red stands out on both dark and light surfaces)
 *
 * Do NOT use on light (Slate/White) backgrounds — use [DouroTextField] instead.
 *
 * @param value Current text value.
 * @param onValueChange Called on text change.
 * @param label Floating label above/inside the field.
 * @param modifier Optional modifier.
 * @param isError Whether field is in error state.
 * @param supportingText Optional supporting or error text below the field.
 */
@Composable
fun DouroLoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        isError = isError,
        supportingText = supportingText,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White.copy(alpha = 0.85f),
            disabledTextColor = Color.White.copy(alpha = 0.4f),
            cursorColor = Color.White,

            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White.copy(alpha = 0.30f),
            errorBorderColor = MaterialTheme.colorScheme.error,

            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White.copy(alpha = 0.60f),
            errorLabelColor = MaterialTheme.colorScheme.error,

            focusedContainerColor = Color.White.copy(alpha = 0.15f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
            errorContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.10f),

            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White.copy(alpha = 0.60f),
            focusedTrailingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.White.copy(alpha = 0.60f),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun DouroLoginTextFieldPreview() {
    AppTheme {
        DouroLoginTextField(value = "", onValueChange = {}, label = "Email")
    }
}

@Preview(showBackground = true)
@Composable
private fun DouroLoginTextFieldFilledPreview() {
    AppTheme {
        DouroLoginTextField(value = "athlete@dourobats.pt", onValueChange = {}, label = "Email")
    }
}
