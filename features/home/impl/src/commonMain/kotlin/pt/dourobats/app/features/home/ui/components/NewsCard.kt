package pt.dourobats.app.features.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dourobats.features.home.generated.resources.Res
import dourobats.features.home.generated.resources.home_read_more
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.components.cards.SectionCard
import pt.dourobats.app.core.ui.components.feedback.StatusBadge
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing

@Composable
internal fun NewsCard(
    tag: String,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    SectionCard(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(0.dp), // flush header touches card edges
    ) {
        Column {
            // Editorial header — placeholder for future image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                StatusBadge(
                    text = tag,
                    modifier = Modifier.padding(spacing.standard),
                )
            }

            Column(modifier = Modifier.padding(spacing.standard)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(spacing.medium))
                Text(
                    text = stringResource(Res.string.home_read_more).uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsCardPreview() {
    AppTheme {
        NewsCard(
            tag = "Tournament",
            title = "Summer Championship 2024",
            description = "Registration is now open for the annual summer championship. Secure your spot before it fills up!",
            onClick = {},
        )
    }
}

