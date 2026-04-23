package pt.dourobats.app.features.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dourobats.features.settings.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.core.model.Theme

/**
 * Dialog for selecting application theme.
 *
 * @param currentTheme Currently selected theme
 * @param onThemeSelected Callback when a theme is selected
 * @param onDismiss Callback when dialog should be dismissed
 * @param modifier Optional modifier for the dialog
 */
@Composable
fun ThemeSelectionDialog(
    currentTheme: Theme,
    onThemeSelected: (Theme) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(Res.string.settings_theme_select))
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(Theme.entries) { theme ->
                    ThemeItem(
                        theme = theme,
                        isSelected = theme == currentTheme,
                        onClick = { onThemeSelected(theme) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.settings_cancel))
            }
        },
        modifier = modifier
    )
}

/**
 * Individual theme item in the selection list.
 *
 * @param theme The theme to display
 * @param isSelected Whether this theme is currently selected
 * @param onClick Callback when this item is clicked
 * @param modifier Optional modifier for the item
 */
@Composable
private fun ThemeItem(
    theme: Theme,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = theme.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
            if (isSelected) {
                RadioButton(
                    selected = true,
                    onClick = null
                )
            }
        }
    }
}
