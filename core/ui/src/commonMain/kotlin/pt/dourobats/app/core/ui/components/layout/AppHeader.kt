package pt.dourobats.app.core.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Universal Kinetic Precision header with diagonal athletic gradient.
 *
 * ## Design Decisions
 * - **Diagonal linearGradient** (top-left → bottom-right): Creates kinetic energy vs. the
 *   "muddy" vertical fade that mixes primary and secondary mid-screen.
 * - **24dp bottom corners** (down from 32dp): More editorial, less "mobile-2018."
 * - **Surface shadowElevation = 0dp**: Gradient shape provides visual depth. Zero shadow means
 *   hero cards (elevation = 8dp) always render above the header without z-order fighting.
 *   Content column painted after AppHeader → naturally draws on top (Compose draw order).
 * - **spacing.screenHorizontal**: Title aligns exactly with list content below.
 * - **spacing.extraLarge bottom padding**: Reserves overlap space for the first card.
 * - **Explicit font families**: title → Lexend (actionable), subtitle → Manrope (narrative).
 *
 * @param title The main title text — rendered in Lexend Bold.
 * @param modifier Optional modifier.
 * @param subtitle Optional subtitle — rendered in Manrope at 70% white.
 * @param leading Optional composable at the start of the title row (e.g., back button).
 * @param trailing Optional composable at the end of the title row (e.g., action chips).
 * @param content Optional custom content below the title row (e.g., month picker).
 */
@Composable
fun AppHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leading: @Composable (RowScope.() -> Unit)? = null,
    trailing: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null,
) {
    val spacing = LocalSpacing.current

    val athleticGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,                      // Midnight Pitch
            MaterialTheme.colorScheme.primary.copy(alpha = 0.90f),
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.80f), // Light Blue — stays in Power Blue family
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent, // Let the Column's gradient show through
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(athleticGradient)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = spacing.screenHorizontal)
                .padding(top = spacing.medium, bottom = spacing.extraLarge),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leading != null) {
                    Row { leading() }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Black, // Editorial weight — magazine-cover boldness
                        fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
                    )

                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.70f),
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily, // Manrope
                        )
                    }
                }

                if (trailing != null) {
                    Row { trailing() }
                }
            }

            if (content != null) {
                Spacer(modifier = Modifier.height(spacing.standard))
                content()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppHeaderHomePreview() {
    AppTheme {
        AppHeader(
            title = "Hello, John!",
            subtitle = "Ready to train today?"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppHeaderSchedulePreview() {
    AppTheme {
        AppHeader(
            title = "Schedule",
            trailing = {
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Month",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            },
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ChevronLeft, null, tint = Color.White)
                    }
                    Text(
                        text = "February 2026",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ChevronRight, null, tint = Color.White)
                    }
                }
            }
        )
    }
}

