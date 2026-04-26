package pt.dourobats.app.core.ui.components.actions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.proIndigo
import pt.dourobats.app.core.ui.theme.proIndigoContainer

/**
 * Kinetic Precision Primary Button
 *
 * The standard call-to-action button for DouroBats. Designed to feel like
 * professional equipment — not a generic Material button.
 *
 * Design decisions:
 * - **52dp height**: Industry standard "thumb reach" target for athletic apps
 * - **6dp corners**: Consistent with [SectionCard] (rounded-md, serious athletic tone)
 * - **Lexend Bold uppercase**: Forces the actionable/technical font and conveys urgency
 * - **Full width by default**: Anchors CTA at the bottom of forms/sheets
 *
 * @param text Button label — automatically uppercased.
 * @param onClick Called when the button is tapped (ignored when loading or disabled).
 * @param modifier Optional modifier. Override width here if full-width is unwanted.
 * @param isLoading When true disables interaction and shows "PROCESSING..." label.
 * @param enabled When false disables interaction without showing loading state.
 */
@Composable
fun DouroButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled && !isLoading,
        shape = MaterialTheme.shapes.small, // 6dp — rounded-md
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        contentPadding = PaddingValues(horizontal = 24.dp),
    ) {
        Text(
            text = if (isLoading) "PROCESSING…" else text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily, // Lexend
        )
    }
}

/**
 * Kinetic Precision Ghost Button
 *
 * Secondary action button — same geometry as [DouroButton] but uses a transparent
 * background with a ghost border (primary at 40% opacity). Follows the "No-Line Rule"
 * exception for interactive elements that require explicit boundary affordance.
 *
 * Use when a secondary action must share the same visual weight zone as a primary
 * action without competing for dominance (e.g., "Cancel" alongside "Confirm").
 *
 * @param text Button label — automatically uppercased.
 * @param onClick Called when the button is tapped.
 * @param modifier Optional modifier.
 * @param enabled When false the button appears disabled.
 */
@Composable
fun DouroOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = MaterialTheme.shapes.small, // 6dp — rounded-md
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = if (enabled) 0.4f else 0.2f),
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        contentPadding = PaddingValues(horizontal = 24.dp),
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily, // Lexend
        )
    }
}

@Preview
@Composable
private fun DouroButtonPreview() {
    AppTheme {
        DouroButton(text = "Confirm Attendance", onClick = {})
    }
}

@Preview
@Composable
private fun DouroButtonLoadingPreview() {
    AppTheme {
        DouroButton(text = "Confirm Attendance", onClick = {}, isLoading = true)
    }
}

@Preview
@Composable
private fun DouroOutlinedButtonPreview() {
    AppTheme {
        DouroOutlinedButton(text = "Cancel", onClick = {})
    }
}

/**
 * Kinetic Precision Destructive Button
 *
 * Full-width ghost button for irreversible actions (Logout, Delete Account, Cancel Membership).
 * The error color signals danger without being aggressive — faint tint bg + 1dp border.
 *
 * Design decisions:
 * - **1dp border @ 30% opacity**: Integrated, not alarming — "Are you sure?" not "WARNING!"
 * - **error.copy(alpha=0.05f) container**: Barely-there tint reinforces destructive context
 * - **56dp height**: Slightly taller than standard actions — more deliberate tap required
 * - **6dp corners**: Consistent with the rest of the design system
 *
 * @param text Button label.
 * @param onClick Called when the button is tapped.
 * @param modifier Optional modifier.
 * @param icon Optional leading icon.
 */
@Composable
fun DouroDestructiveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.small, // 6dp — rounded-md
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.05f),
            contentColor = MaterialTheme.colorScheme.error,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon?.let {
                Icon(it, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(text = text, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview
@Composable
private fun DouroDestructiveButtonPreview() {
    AppTheme {
        DouroDestructiveButton(text = "Logout", onClick = {})
    }
}

/**
 * High-Contrast Login Button
 *
 * Used exclusively on dark gradient backgrounds (Login/Verify screens).
 * Pure White container + Midnight Pitch text = maximum luminance contrast against
 * the Midnight Pitch → Power Blue gradient. The dark text on white reads as
 * "authority" while the white card reads as "clarity ahead" (light at the end of the tunnel).
 *
 * @param text Button label (auto-uppercased).
 * @param onClick Called when tapped.
 * @param modifier Optional modifier.
 * @param enabled Whether the button accepts interaction.
 */
@Composable
fun DouroLoginButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = Color.White.copy(alpha = 0.30f),
            disabledContentColor = Color.White.copy(alpha = 0.50f),
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Black,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
            letterSpacing = 1.sp,
        )
    }
}

@Preview
@Composable
private fun DouroLoginButtonPreview() {
    AppTheme {
        DouroLoginButton(text = "Entrar", onClick = {})
    }
}

@Preview
@Composable
private fun DouroLoginButtonDisabledPreview() {
    AppTheme {
        DouroLoginButton(text = "Entrar", onClick = {}, enabled = false)
    }
}

@Composable
fun DouroProButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = proIndigo,
            contentColor = Color.White,
            disabledContainerColor = proIndigo.copy(alpha = 0.30f),
            disabledContentColor = Color.White.copy(alpha = 0.50f),
        ),
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
        )
    }
}

@Preview
@Composable
private fun DouroProButtonPreview() {
    AppTheme {
        DouroProButton(text = "Nova Sessão", onClick = {})
    }
}
