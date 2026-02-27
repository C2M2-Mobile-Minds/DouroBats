package pt.dourobats.app.core.ui.components.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.ui.theme.LocalSpacing

@Composable
fun WeekCalendar(
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    dayNames: Map<DayOfWeek, String> = defaultDayNames(),
    sessionDates: Set<LocalDate> = emptySet(),
    todayLabel: String = "Today",
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val dates = remember(today) { generateCurrentWeekDates(today) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.standard),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            dates.forEach { date ->
                DateItem(
                    date = date,
                    isSelected = date == selectedDate,
                    isToday = date == today,
                    isPast = date < today,
                    hasSession = date in sessionDates,
                    dayNames = dayNames,
                    todayLabel = todayLabel,
                    onDateClick = onDateSelected
                )
            }
        }
    }
}

@Composable
private fun DateItem(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    isPast: Boolean,
    hasSession: Boolean,
    dayNames: Map<DayOfWeek, String>,
    todayLabel: String,
    onDateClick: (LocalDate) -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    )
    val fontWeight = when {
        isSelected -> FontWeight.Bold
        isPast -> FontWeight.Normal
        else -> FontWeight.SemiBold
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = dayNames[date.dayOfWeek] ?: "",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        CalendarDateCell(
            date = date,
            isSelected = isSelected,
            isToday = isToday,
            hasSession = hasSession,
            fontWeight = fontWeight,
            textColor = textColor,
            backgroundColor = backgroundColor,
            cellHeight = 58.dp,
            dayNumberStyle = MaterialTheme.typography.titleMedium,
            dotSize = 5.dp,
            dotBottomPadding = 6.dp,
            todayLabel = todayLabel,
            onClick = onDateClick
        )
    }
}

internal fun generateCurrentWeekDates(today: LocalDate): List<LocalDate> {
    val daysFromMonday = today.dayOfWeek.ordinal
    val startOfCurrentWeek = today.minusDays(daysFromMonday)
    return (0..6).map { dayOffset ->
        startOfCurrentWeek.plusDays(dayOffset)
    }
}
