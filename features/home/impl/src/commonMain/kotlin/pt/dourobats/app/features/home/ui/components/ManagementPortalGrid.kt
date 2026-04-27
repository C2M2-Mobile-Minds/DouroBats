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
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.features.home.ui.HomeAction

@Composable
internal fun ManagementPortalGrid(onAction: (HomeAction) -> Unit) {
    val spacing = LocalSpacing.current
    // Tonal icon style: all items use primary @ 10% alpha container + solid primary icon.
    // Unified palette prevents the "rainbow" effect from mixing primary/secondary/tertiary.
    val tonalContainer = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
    val tonalIcon = MaterialTheme.colorScheme.primary
    Column(verticalArrangement = Arrangement.spacedBy(spacing.standard)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.standard)
        ) {
            PortalItem(
                title = stringResource(Res.string.home_portal_new_session),
                icon = Icons.Default.Add,
                containerColor = tonalContainer,
                iconColor = tonalIcon,
                onClick = { onAction(HomeAction.OnManageFabClick) },
                modifier = Modifier.weight(1f)
            )
            PortalItem(
                title = stringResource(Res.string.home_portal_reports),
                icon = Icons.Default.PieChart,
                containerColor = tonalContainer,
                iconColor = tonalIcon,
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
                containerColor = tonalContainer,
                iconColor = tonalIcon,
                onClick = { onAction(HomeAction.OnManageMembersClick) },
                modifier = Modifier.weight(1f)
            )
            PortalItem(
                title = stringResource(Res.string.home_portal_post_news),
                icon = Icons.Default.Campaign,
                containerColor = tonalContainer,
                iconColor = tonalIcon,
                onClick = { },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ManagementPortalGridPreview() {
    AppTheme {
        ManagementPortalGrid(onAction = {})
    }
}
