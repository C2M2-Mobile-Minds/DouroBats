package pt.dourobats.app.features.login.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
internal fun OtpInputField(
    code: String,
    onCodeChanged: (String) -> Unit,
    isError: Boolean = false,
    length: Int = 6,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }

    // Delay past AnimatedContent's default animation (~300ms) before requesting focus.
    // BringIntoViewRequester traverses ancestor nodes asynchronously; if any ancestor is
    // still animating (unplaced), the traversal crashes. 400ms guarantees the transition
    // is complete. size(1.dp)+alpha(0f) avoids a zero-area layout rect calculation bug.
    LaunchedEffect(Unit) {
        delay(400)
        focusRequester.requestFocus()
    }

    Box(modifier = modifier.fillMaxWidth()) {
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
                    isFocused -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline
                }
                val borderWidth = if (isFocused || isError) 2.dp else 1.dp

                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .weight(1f),
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(borderWidth, borderColor),
                    color = Color.Transparent,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}
