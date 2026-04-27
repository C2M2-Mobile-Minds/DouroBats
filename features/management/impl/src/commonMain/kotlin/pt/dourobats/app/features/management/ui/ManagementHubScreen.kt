package pt.dourobats.app.features.management.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dourobats.features.management.generated.resources.Res
import dourobats.features.management.generated.resources.management_hub_checkin
import dourobats.features.management.generated.resources.management_hub_checkin_desc
import dourobats.features.management.generated.resources.management_hub_create_session
import dourobats.features.management.generated.resources.management_hub_create_session_desc
import dourobats.features.management.generated.resources.management_hub_post_announcement
import dourobats.features.management.generated.resources.management_hub_post_announcement_desc
import dourobats.features.management.generated.resources.management_hub_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.core.ui.theme.proIndigo
import pt.dourobats.app.features.management.ManagementAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ManagementHubScreen(
    onAction: (ManagementAction) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 0.dp,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = spacing.standard)
        ) {
            Text(
                text = stringResource(Res.string.management_hub_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(
                    horizontal = spacing.standard,
                    vertical = spacing.standard,
                ),
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.standard),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column {
                    ManagementRow(
                        title = stringResource(Res.string.management_hub_create_session),
                        subtitle = stringResource(Res.string.management_hub_create_session_desc),
                        icon = Icons.Default.EditCalendar,
                        color = proIndigo,
                        onClick = { onAction(ManagementAction.CreateSessionRequested) },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    )
                    ManagementRow(
                        title = stringResource(Res.string.management_hub_post_announcement),
                        subtitle = stringResource(Res.string.management_hub_post_announcement_desc),
                        icon = Icons.Default.Campaign,
                        color = MaterialTheme.colorScheme.tertiary,
                        onClick = { onAction(ManagementAction.PostAnnouncementRequested) },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    )
                    ManagementRow(
                        title = stringResource(Res.string.management_hub_checkin),
                        subtitle = stringResource(Res.string.management_hub_checkin_desc),
                        icon = Icons.Default.HowToReg,
                        color = MaterialTheme.colorScheme.primary,
                        onClick = { onAction(ManagementAction.CheckInRequested) },
                    )
                }
            }
        }
    }
}

@Composable
internal fun ManagementRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(spacing.standard)
                .heightIn(min = 48.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp),
                )
            }

            Spacer(modifier = Modifier.width(spacing.standard))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ManagementHubScreenPreview() {
    AppTheme {
        ManagementHubScreen(
            onAction = {},
            onDismiss = {},
        )
    }
}

