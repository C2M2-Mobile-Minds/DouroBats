package pt.dourobats.app.features.schedule.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dourobats.features.schedule.generated.resources.Res
import dourobats.features.schedule.generated.resources.view_mode_month
import dourobats.features.schedule.generated.resources.view_mode_switch_to_month
import dourobats.features.schedule.generated.resources.view_mode_switch_to_week
import dourobats.features.schedule.generated.resources.view_mode_week
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.features.schedule.ui.CalendarViewMode

@Composable
internal fun ViewModeToggle(
    viewMode: CalendarViewMode,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val toggleLabel = stringResource(
        if (viewMode == CalendarViewMode.WEEK) Res.string.view_mode_switch_to_month
        else Res.string.view_mode_switch_to_week
    )
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
            .clickable(onClick = onToggle)
            .semantics {
                contentDescription = toggleLabel
                role = Role.Button
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (viewMode == CalendarViewMode.WEEK) Icons.Default.CalendarMonth else Icons.Default.ViewWeek,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (viewMode == CalendarViewMode.WEEK)
                    stringResource(Res.string.view_mode_month)
                else
                    stringResource(Res.string.view_mode_week),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
