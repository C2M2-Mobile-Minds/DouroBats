package pt.dourobats.app.features.home.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dourobats.features.home.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.home.ui.HomeAction

@Composable
internal fun ManagementPortalGrid(onAction: (HomeAction) -> Unit) {
    val spacing = LocalSpacing.current
    Column(verticalArrangement = Arrangement.spacedBy(spacing.standard)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.standard)
        ) {
            PortalItem(
                title = stringResource(Res.string.home_portal_new_session),
                icon = Icons.Default.Add,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                iconColor = MaterialTheme.colorScheme.primary,
                onClick = { onAction(HomeAction.OnCreateSessionClick) },
                modifier = Modifier.weight(1f)
            )
            PortalItem(
                title = stringResource(Res.string.home_portal_reports),
                icon = Icons.Default.PieChart,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                iconColor = MaterialTheme.colorScheme.secondary,
                onClick = { onAction(HomeAction.OnViewReportsClick) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.standard)
        ) {
            PortalItem(
                title = stringResource(Res.string.home_portal_members),
                icon = Icons.Default.Groups,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconColor = MaterialTheme.colorScheme.tertiary,
                onClick = { onAction(HomeAction.OnManageMembersClick) },
                modifier = Modifier.weight(1f)
            )
            PortalItem(
                title = stringResource(Res.string.home_portal_post_news),
                icon = Icons.Default.Campaign,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                onClick = { },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
