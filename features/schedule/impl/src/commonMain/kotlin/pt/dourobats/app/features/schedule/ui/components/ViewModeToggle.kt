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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val isWeek = viewMode == CalendarViewMode.WEEK
    val toggleLabel = stringResource(
        if (isWeek) Res.string.view_mode_switch_to_month
        else Res.string.view_mode_switch_to_week
    )
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
            .semantics {
                contentDescription = toggleLabel
                role = Role.Button
            }
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onToggle)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isWeek) Icons.Default.CalendarMonth else Icons.Default.ViewWeek,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isWeek)
                    stringResource(Res.string.view_mode_month).uppercase()
                else
                    stringResource(Res.string.view_mode_week).uppercase(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp,
            )
        }
    }
}
