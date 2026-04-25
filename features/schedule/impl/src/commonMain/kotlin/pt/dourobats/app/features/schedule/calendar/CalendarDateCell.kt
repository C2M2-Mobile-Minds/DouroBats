package pt.dourobats.app.features.schedule.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate

/**
 * Shared cell used by both [WeekCalendar] and [MonthCalendar].
 *
 * Renders: optional "Today" label at top · day number in centre · optional session dot at bottom.
 *
 * @param date           The date this cell represents.
 * @param isSelected     Whether this cell is the currently selected date.
 * @param isToday        Whether this cell represents today.
 * @param hasSession     Whether a training session exists on this date.
 * @param fontWeight     Font weight for the day number.
 * @param textColor      Colour for the day number.
 * @param backgroundColor Background colour of the cell box.
 * @param cellHeight     Height of the cell box (week=58dp, month=50dp).
 * @param dayNumberStyle Text style for the day number.
 * @param dotSize        Diameter of the session dot.
 * @param dotBottomPadding Bottom padding of the session dot.
 * @param todayLabel     Localised "Today" label string.
 * @param onClick        Click callback.
 */
@Composable
fun CalendarDateCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    hasSession: Boolean,
    fontWeight: FontWeight,
    textColor: Color,
    backgroundColor: Color,
    cellHeight: Dp,
    dayNumberStyle: TextStyle,
    dotSize: Dp,
    dotBottomPadding: Dp,
    todayLabel: String,
    onClick: (LocalDate) -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 44.dp, height = cellHeight)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center
    ) {
        if (isToday) {
            Text(
                text = todayLabel,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 8.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 4.dp)
            )
        }
        Text(
            text = date.day.toString(),
            style = dayNumberStyle,
            fontWeight = fontWeight,
            color = textColor,
            textAlign = TextAlign.Center
        )
        if (hasSession) {
            val dotColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
                           else MaterialTheme.colorScheme.primary
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = dotBottomPadding)
                    .size(dotSize)
                    .background(color = dotColor, shape = CircleShape)
            )
        }
    }
}
