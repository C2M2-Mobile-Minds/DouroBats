package pt.dourobats.app.features.login.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme

@Composable
internal fun OtpInputField(
    code: String,
    onCodeChanged: (String) -> Unit,
    isError: Boolean = false,
    length: Int = 6,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(code) {
        if (code.length == length) {
            keyboardController?.hide()
        }
    }

    // Delay past AnimatedContent's default animation (~300ms) before requesting focus.
    // BringIntoViewRequester traverses ancestor nodes asynchronously; if any ancestor is
    // still animating (unplaced), the traversal crashes. 400ms guarantees the transition
    // is complete. size(1.dp)+alpha(0f) avoids a zero-area layout rect calculation bug.
    LaunchedEffect(Unit) {
        delay(400)
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            // Tap anywhere on the row to re-open keyboard if it was dismissed
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                focusRequester.requestFocus()
                keyboardController?.show()
            },
    ) {
        BasicTextField(
            value = code,
            onValueChange = { if (it.length <= length) onCodeChanged(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier
                .focusRequester(focusRequester)
                .size(1.dp)
                .alpha(0f),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(length) { index ->
                val char = code.getOrNull(index)?.toString() ?: ""
                val isFocused = code.length == index
                val borderColor = when {
                    isError -> MaterialTheme.colorScheme.error
                    isFocused -> Color.White
                    else -> Color.White.copy(alpha = 0.35f)
                }
                val borderWidth = if (isFocused || isError) 2.dp else 1.dp

                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(borderWidth, borderColor),
                    color = Color.White.copy(alpha = if (isFocused) 0.20f else 0.12f),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OtpInputFieldEmptyPreview() {
    AppTheme { OtpInputField(code = "", onCodeChanged = {}) }
}

@Preview(showBackground = true)
@Composable
private fun OtpInputFieldPartialPreview() {
    AppTheme { OtpInputField(code = "123", onCodeChanged = {}) }
}

@Preview(showBackground = true)
@Composable
private fun OtpInputFieldFullPreview() {
    AppTheme { OtpInputField(code = "123456", onCodeChanged = {}) }
}

@Preview(showBackground = true)
@Composable
private fun OtpInputFieldErrorPreview() {
    AppTheme { OtpInputField(code = "123456", onCodeChanged = {}, isError = true) }
}

