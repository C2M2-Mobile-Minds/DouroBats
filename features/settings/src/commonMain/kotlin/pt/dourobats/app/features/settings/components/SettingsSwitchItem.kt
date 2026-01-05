package pt.dourobats.app.features.settings.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Settings item with a trailing switch toggle.
 *
 * Used for binary on/off settings like Dark Mode.
 * Provides instant feedback without requiring dialogs.
 *
 * ## Usage
 *
 * ```kotlin
 * SettingsSwitchItem(
 *     icon = "🌙",
 *     title = "Dark Mode",
 *     subtitle = "Enable dark theme",
 *     checked = isDarkMode,
 *     onCheckedChange = { viewModel.setDarkMode(it) }
 * )
 * ```
 *
 * @param icon Emoji icon displayed on the left
 * @param title Setting title
 * @param subtitle Optional description text
 * @param checked Current switch state
 * @param onCheckedChange Callback when switch is toggled
 * @param modifier Optional modifier
 */
@Composable
fun SettingsSwitchItem(
    icon: String,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.width(spacing.standard))

        // Title and subtitle
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Switch
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
