package pt.dourobats.app.features.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dourobats.features.settings.generated.resources.Res
import dourobats.features.settings.generated.resources.settings_language_select
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.core.domain.model.Language
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Modal bottom sheet for language selection.
 *
 * Provides a more native Android feel compared to dialogs.
 * Better for mobile UX as it's easier to reach with thumb.
 *
 * ## Usage
 *
 * ```kotlin
 * if (showLanguageSheet) {
 *     LanguageBottomSheet(
 *         currentLanguage = uiState.currentLanguage,
 *         onLanguageSelected = { language ->
 *             viewModel.setLanguage(language)
 *             showLanguageSheet = false
 *         },
 *         onDismiss = { showLanguageSheet = false }
 *     )
 * }
 * ```
 *
 * @param currentLanguage Currently selected language
 * @param onLanguageSelected Callback when a language is selected
 * @param onDismiss Callback when sheet is dismissed
 * @param modifier Optional modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacing.large)
        ) {
            // Title
            Text(
                text = stringResource(Res.string.settings_language_select),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    horizontal = spacing.screenHorizontal,
                    vertical = spacing.standard
                )
            )

            HorizontalDivider()

            Spacer(modifier = Modifier.height(spacing.small))

            // Language options
            Language.entries.forEach { language ->
                LanguageOption(
                    language = language,
                    isSelected = language == currentLanguage,
                    onClick = { onLanguageSelected(language) }
                )
            }
        }
    }
}

/**
 * Individual language option with radio button.
 *
 * @param language The language option
 * @param isSelected Whether this language is currently selected
 * @param onClick Callback when this option is clicked
 * @param modifier Optional modifier
 */
@Composable
private fun LanguageOption(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = spacing.screenHorizontal,
                vertical = spacing.standard
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Radio button
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )

        Spacer(modifier = Modifier.width(spacing.standard))

        // Language name
        Text(
            text = language.displayName,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}
