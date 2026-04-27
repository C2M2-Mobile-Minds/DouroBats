package pt.dourobats.app.features.settings.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dourobats.features.settings.generated.resources.Res
import dourobats.features.settings.generated.resources.settings_language_select
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.core.ui.components.layout.SelectionListItem
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LanguageBottomSheet(
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.standard)
                .navigationBarsPadding()
                .padding(bottom = spacing.standard)
        ) {
            Text(
                text = stringResource(Res.string.settings_language_select),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = spacing.standard)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp,
                ),
            ) {
                Column(modifier = Modifier.padding(vertical = spacing.small)) {
                    Language.entries.forEachIndexed { index, language ->
                        SelectionListItem(
                            label = language.displayName,
                            isSelected = language == currentLanguage,
                            onClick = { onLanguageSelected(language) },
                            leadingContent = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(MaterialTheme.colorScheme.surfaceContainerLow, CircleShape),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(text = getFlagEmoji(language), style = MaterialTheme.typography.titleMedium)
                                }
                            },
                        )
                        if (index < Language.entries.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = spacing.standard),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getFlagEmoji(language: Language): String = when (language) {
    Language.ENGLISH_US -> "🇺🇸"
    Language.ENGLISH_GB -> "🇬🇧"
    Language.PORTUGUESE_PT -> "🇵🇹"
    Language.PORTUGUESE_BR -> "🇧🇷"
    Language.SPANISH -> "🇪🇸"
}

@Preview(showBackground = true)
@Composable
private fun LanguageBottomSheetPreview() {
    AppTheme {
        LanguageBottomSheet(
            currentLanguage = Language.PORTUGUESE_PT,
            onLanguageSelected = {},
            onDismiss = {},
        )
    }
}

