package pt.dourobats.app.features.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.core.ui.theme.AppTheme

@Composable
internal fun LanguageSelectorRow(
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Language.entries.forEach { language ->
            val isSelected = language == currentLanguage
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface,
                        CircleShape,
                    )
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant,
                        shape = CircleShape,
                    )
                    .clickable(role = Role.Tab) { onLanguageSelected(language) }
                    .semantics {
                        contentDescription = language.displayName
                        selected = isSelected
                        role = Role.Tab
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = language.flagEmoji,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(2.dp),
                )
            }
        }
    }
}

private val Language.flagEmoji: String
    get() = when (this) {
        Language.ENGLISH_US -> "🇺🇸"
        Language.ENGLISH_GB -> "🇬🇧"
        Language.PORTUGUESE_PT -> "🇵🇹"
        Language.PORTUGUESE_BR -> "🇧🇷"
        Language.SPANISH -> "🇪🇸"
    }

@Preview(showBackground = true)
@Composable
private fun LanguageSelectorRowPreview() {
    AppTheme {
        LanguageSelectorRow(
            currentLanguage = Language.PORTUGUESE_PT,
            onLanguageSelected = {},
        )
    }
}
