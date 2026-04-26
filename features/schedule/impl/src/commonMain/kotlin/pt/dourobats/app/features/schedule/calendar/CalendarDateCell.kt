package pt.dourobats.app.features.schedule.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate

/**
 * Shared cell used by both [WeekCalendar] and [MonthCalendar].
 *
 * Renders: optional "Today" label at top · day number in centre · optional session dot at bottom.
 * Pass a [modifier] with an explicit [Modifier.size] to control cell dimensions
 * (week: 44×58dp, month: 44×50dp).
 *
 * @param date            The date this cell represents.
 * @param isSelected      Whether this cell is the currently selected date.
 * @param isToday         Whether this cell represents today.
 * @param hasSession      Whether a training session exists on this date.
 * @param backgroundColor Background colour of the cell.
 * @param textColor       Colour for the day number.
 * @param todayLabel      Localised "Today" label string.
 * @param onClick         Click callback.
 * @param modifier        Size / positioning — default 44×54dp.
 */
@Composable
internal fun CalendarDateCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    hasSession: Boolean,
    backgroundColor: Color,
    textColor: Color,
    todayLabel: String,
    onClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = { onClick(date) },
        modifier = modifier.size(width = 44.dp, height = 54.dp),
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor,
        tonalElevation = if (isToday && !isSelected) 2.dp else 0.dp,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (isToday) {
                Text(
                    text = todayLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
                color = textColor,
                textAlign = TextAlign.Center,
            )
            if (hasSession) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(4.dp)
                        .background(
                            color = if (isSelected) textColor else MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        )
                )
            }
        }
    }
}
