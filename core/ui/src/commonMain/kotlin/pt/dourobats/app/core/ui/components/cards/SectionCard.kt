package pt.dourobats.app.core.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.core.ui.theme.ambientShadow

/**
 * Kinetic Precision Section Card
 *
 * Reusable card container following the "No-Line Rule" and tonal layering principles.
 *
 * ## Design Principles
 * - Uses `surfaceContainerLowest` for the "Active Card" layer — hierarchy through luminance,
 *   not borders ("Borders are a failure of hierarchy.")
 * - Rounded corners (rounded-md = 6dp) for serious athletic tone
 * - Tonal elevation through background shifts, not heavy shadows
 * - Responsive padding using design tokens
 *
 * ## Semantic Stripe
 * Pass [accentColor] to render a 4dp left-edge stripe — the industry-standard way for
 * sports apps to convey sport type or booking state at a glance without text.
 * Example: `accentColor = MaterialTheme.colorScheme.secondary` for "Attending" sessions.
 *
 * ## Interactive State
 * Pass [onClick] to make the card tappable. The Material3 Card ripple + your
 * [athleticSpring] easing handle the feedback automatically.
 *
 * @param modifier Optional modifier for the card.
 * @param title Optional section title — rendered in `titleMedium` / `onSurface` Bold.
 * @param accentColor Optional left-edge stripe colour for semantic sport/state encoding.
 * @param onClick Optional click handler. When null the card is non-interactive.
 * @param cornerRadius Corner radius (default 6dp — rounded-md).
 * @param elevation Card elevation (default `spacing.cardElevation`).
 * @param content Card content slot.
 */
@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    accentColor: Color? = null,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = 6.dp,
    elevation: Dp? = null,
    contentPadding: PaddingValues? = null, // null = use spacing.cardPadding
    content: @Composable ColumnScope.() -> Unit,
) {
    val spacing = LocalSpacing.current
    val cardElevation = elevation ?: spacing.cardElevation
    val resolvedPadding = contentPadding ?: PaddingValues(spacing.cardPadding)

    Card(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = cardElevation,
                shape = RoundedCornerShape(cornerRadius),
                clip = false,
                ambientColor = ambientShadow.copy(alpha = 0.08f),
                spotColor = ambientShadow.copy(alpha = 0.18f),
            ),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            if (accentColor != null) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(accentColor),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(resolvedPadding),
            ) {
                title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface, // grounded, not action-primary
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = spacing.medium),
                    )
                }

                content()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SectionCardWithTitlePreview() {
    AppTheme {
        SectionCard(
            title = "Training Info",
            modifier = Modifier.padding(16.dp),
        ) {
            Text("Monday, 18:00 – 19:30")
            Text("Pavilhão Municipal")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SectionCardAccentPreview() {
    AppTheme {
        SectionCard(
            title = "Volleyball — Attending",
            accentColor = MaterialTheme.colorScheme.secondary,
            onClick = {},
            modifier = Modifier.padding(16.dp),
        ) {
            Text("Tuesday, 19:00 – 20:30")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SectionCardNoTitlePreview() {
    AppTheme {
        SectionCard(modifier = Modifier.padding(16.dp)) {
            Text("Bare content block — no section title.")
        }
    }
}

