package pt.dourobats.app.core.ui.components.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
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
import kotlinx.datetime.Month
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Year/Month data class for calendar navigation.
 */
data class YearMonth(val year: Int, val month: Month) {
    fun minusMonths(months: Int): YearMonth {
        var newYear = year
        var newMonth = month.ordinal - months

        while (newMonth < 0) {
            newMonth += 12
            newYear -= 1
        }

        return YearMonth(newYear, Month.entries[newMonth])
    }

    fun plusMonths(months: Int): YearMonth {
        var newYear = year
        var newMonth = month.ordinal + months

        while (newMonth >= 12) {
            newMonth -= 12
            newYear += 1
        }

        return YearMonth(newYear, Month.entries[newMonth])
    }
}

/**
 * Modern month calendar component with grid layout.
 * Optimized to match the provided design reference.
 */
@Composable
fun MonthCalendar(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    dayNames: Map<DayOfWeek, String> = defaultDayNames(),
    sessionDates: Set<LocalDate> = emptySet(),
    todayLabel: String = "Today",
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.standard),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DayOfWeekHeader(dayNames = dayNames)

        Spacer(modifier = Modifier.height(spacing.medium))

        MonthGrid(
            yearMonth = yearMonth,
            selectedDate = selectedDate,
            today = today,
            onDateSelected = onDateSelected,
            sessionDates = sessionDates,
            todayLabel = todayLabel
        )
    }
}

@Composable
private fun DayOfWeekHeader(dayNames: Map<DayOfWeek, String>) {
    val spacing = LocalSpacing.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.medium),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        ).forEach { dayOfWeek ->
            Box(
                modifier = Modifier.width(44.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayNames[dayOfWeek] ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun MonthGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    sessionDates: Set<LocalDate> = emptySet(),
    todayLabel: String = "Today"
) {
    val spacing = LocalSpacing.current

    val weeks = remember(yearMonth) {
        generateMonthGridDates(yearMonth).chunked(7)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.medium),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        weeks.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                week.forEach { date ->
                    MonthDateItem(
                        date = date,
                        isSelected = date == selectedDate,
                        isToday = date == today,
                        isPast = date < today,
                        isCurrentMonth = date.month == yearMonth.month,
                        hasSession = date in sessionDates,
                        todayLabel = todayLabel,
                        onDateClick = onDateSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthDateItem(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    isPast: Boolean,
    isCurrentMonth: Boolean,
    hasSession: Boolean,
    todayLabel: String,
    onDateClick: (LocalDate) -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    )
    val textColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.onPrimary
            !isCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            else -> MaterialTheme.colorScheme.onSurface
        }
    )
    val fontWeight = when {
        isSelected -> FontWeight.Bold
        isPast -> FontWeight.Normal
        else -> FontWeight.SemiBold
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CalendarDateCell(
            date = date,
            isSelected = isSelected,
            isToday = isToday,
            hasSession = isCurrentMonth && hasSession,
            enabled = isCurrentMonth,
            fontWeight = fontWeight,
            textColor = textColor,
            backgroundColor = backgroundColor,
            cellHeight = 50.dp,
            dayNumberStyle = MaterialTheme.typography.bodyMedium,
            dotSize = 4.dp,
            dotBottomPadding = 4.dp,
            todayLabel = todayLabel,
            onClick = onDateClick
        )
    }
}

private fun generateMonthGridDates(yearMonth: YearMonth): List<LocalDate> {
    val firstDayOfMonth = LocalDate(yearMonth.year, yearMonth.month, 1)
    val daysFromMonday = firstDayOfMonth.dayOfWeek.ordinal
    val startDate = firstDayOfMonth.minusDays(daysFromMonday)
    return (0 until 42).map { offset ->
        startDate.plusDays(offset)
    }
}
