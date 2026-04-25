package pt.dourobats.app.features.settings.components

import androidx.compose.foundation.layout.Column
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
 * Generic list item with a trailing switch toggle.
 *
 * Isolated, reusable component for binary on/off settings.
 * Can be used in any feature module for toggleable options.
 *
 * ## Features
 * - Icon support (emoji or custom composable)
 * - Title and optional subtitle
 * - Trailing switch
 * - Design tokens for spacing
 * - Instant feedback (no dialogs needed)
 *
 * ## Usage
 *
 * ```kotlin
 * SwitchListItem(
 *     icon = "🔔",
 *     title = "Enable Notifications",
 *     subtitle = "Receive push notifications",
 *     checked = isEnabled,
 *     onCheckedChange = { viewModel.setNotifications(it) }
 * )
 * ```
 *
 * @param icon Icon string (emoji) displayed on the left
 * @param title Item title
 * @param subtitle Optional description text
 * @param checked Current switch state
 * @param onCheckedChange Callback when switch is toggled
 * @param modifier Optional modifier
 * @param enabled Whether the switch is enabled (default: true)
 */
@Composable
fun SwitchListItem(
    icon: String,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.width(spacing.standard))

        Column(
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

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

/**
 * Variant of SwitchListItem with composable icon support.
 *
 * Use this when you need a custom icon instead of emoji.
 *
 * @param icon Composable icon content
 * @param title Item title
 * @param subtitle Optional description text
 * @param checked Current switch state
 * @param onCheckedChange Callback when switch is toggled
 * @param modifier Optional modifier
 * @param enabled Whether the switch is enabled
 */
@Composable
fun SwitchListItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(modifier = Modifier.width(spacing.standard))

        Column(
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

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}
