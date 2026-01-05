package pt.dourobats.app.features.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dourobats.features.settings.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.core.domain.model.UserProfile
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Profile header component displaying user avatar and basic info.
 *
 * @param userProfile The user profile to display
 * @param onEditClick Callback when edit button is clicked
 * @param modifier Optional modifier for the component
 */
@Composable
fun ProfileHeader(
    userProfile: UserProfile,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.standard),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            // Profile Image (placeholder for now - shows icon)
            if (userProfile.profileImageUrl != null) {
                // TODO: Load image with Coil/Kamel when image picker implemented
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )
            } else {
                // Placeholder icon
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Edit overlay button
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(32.dp)
                    .clickable(onClick = onEditClick),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                tonalElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier.padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✏️",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(spacing.standard))

        // Display name
        Text(
            text = userProfile.displayName.ifEmpty { stringResource(Res.string.settings_no_name) },
            style = MaterialTheme.typography.headlineSmall
        )

        // Email
        Text(
            text = userProfile.email.ifEmpty { stringResource(Res.string.settings_no_email) },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
