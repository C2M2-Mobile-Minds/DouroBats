package pt.dourobats.app.features.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import dourobats.features.home.generated.resources.Res
import dourobats.features.home.generated.resources.home_management_checkin
import dourobats.features.home.generated.resources.home_management_checkin_desc
import dourobats.features.home.generated.resources.home_management_create_session
import dourobats.features.home.generated.resources.home_management_create_session_desc
import dourobats.features.home.generated.resources.home_management_hub_title
import dourobats.features.home.generated.resources.home_management_post_announcement
import dourobats.features.home.generated.resources.home_management_post_announcement_desc
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.core.ui.theme.proIndigo
import pt.dourobats.app.features.home.ui.HomeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ManagementBottomSheet(
    onAction: (HomeAction) -> Unit,
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
                text = stringResource(Res.string.home_management_hub_title),
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
                    ManagementActionItem(
                        title = stringResource(Res.string.home_management_create_session),
                        subtitle = stringResource(Res.string.home_management_create_session_desc),
                        icon = Icons.Default.EditCalendar,
                        iconTint = proIndigo,
                        onClick = {
                            onDismiss()
                            onAction(HomeAction.OnCreateSessionClick)
                        },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    )
                    ManagementActionItem(
                        title = stringResource(Res.string.home_management_post_announcement),
                        subtitle = stringResource(Res.string.home_management_post_announcement_desc),
                        icon = Icons.Default.Campaign,
                        iconTint = MaterialTheme.colorScheme.tertiary,
                        onClick = {
                            onDismiss()
                            onAction(HomeAction.OnPostAnnouncementClick)
                        },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    )
                    ManagementActionItem(
                        title = stringResource(Res.string.home_management_checkin),
                        subtitle = stringResource(Res.string.home_management_checkin_desc),
                        icon = Icons.Default.HowToReg,
                        iconTint = MaterialTheme.colorScheme.primary,
                        onClick = {
                            onDismiss()
                            onAction(HomeAction.OnCheckInMembersClick)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ManagementActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = spacing.standard, vertical = spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.standard),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(iconTint.copy(alpha = 0.10f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ManagementBottomSheetPreview() {
    AppTheme {
        ManagementBottomSheet(
            onAction = {},
            onDismiss = {},
        )
    }
}
